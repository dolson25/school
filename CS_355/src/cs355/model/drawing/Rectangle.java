package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

/**
 * Add your rectangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Rectangle extends Shape {

	// The width of this shape.
	private double width;

	// The height of this shape.
	private double height;
	
	//a point for a corner
	private Point2D.Double point1;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param width the width of the new shape.
	 * @param height the height of the new shape.
	 */
	public Rectangle(Color color, Point2D.Double center, double width, double height) {

		// Initialize the superclass.
		super(color, center);

		// Set fields.
		this.width = width;
		this.height = height;
		this.point1 = center;
	}

	/**
	 * Getter for this shape's width.
	 * @return this shape's width as a double.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Setter for this shape's width.
	 * @param width the new width.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Getter for this shape's height.
	 * @return this shape's height as a double.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setter for this shape's height.
	 * @param height the new height.
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	
	@Override
	public void setRotation(double rotation) {
		this.rotation = rotation + Math.PI / 2 ;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
		
		AffineTransform worldToObj = new AffineTransform();
		Point2D.Double objCoord = new Point2D.Double();
		
		// rotate back from its orientation (last transformation)
		worldToObj.rotate(- rotation);
		
		// translate back from its position in the world (first transformation)
		worldToObj.translate(-center.getX(), -center.getY());
		
		// and transform point from world to object
		worldToObj.transform(pt, objCoord);
		
		
		if((Math.abs(objCoord.getX()) <= (width / 2)) 
				         && (Math.abs(objCoord.getY()) <= (height / 2)))
			return true;
		return false;
	}
	
	public void setPoint2(Point2D.Double point2)
	{	
		if(point2.getX() > point1.getX())
		{
			if(point2.getY() < point1.getY())
			{
				Point2D.Double upperLeft = (new Point2D.Double(point1.getX(),point2.getY()));
				width = point2.getX() - point1.getX();
				height = point1.getY() - point2.getY();
				center = new Point2D.Double(upperLeft.getX() + width / 2,
						                    upperLeft.getY() + height / 2);
			}
			else
			{
				width = point2.getX() - point1.getX();
				height = point2.getY() - point1.getY();
				center = new Point2D.Double(point1.getX() + width / 2,
	                    point1.getY() + height / 2);
			}
		}
		else
		{
			if(point2.getY() < point1.getY())
			{
				width = point1.getX() - point2.getX();
				height = point1.getY() - point2.getY();
				center = new Point2D.Double(point2.getX() + width / 2,
	                    point2.getY() + height / 2);
			}
			else
			{
				Point2D.Double upperLeft = (new Point2D.Double(point2.getX(),point1.getY()));
				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
				center = new Point2D.Double(upperLeft.getX() + width / 2,
	                    upperLeft.getY() + height / 2);
			}
		}
	}


	@Override
	public boolean pointInHandle(Double pt) {
		return handle.pointInShape(pt, 4);	
	}

	@Override
	public void setHandle() {
		handles.clear();
		
		if(rotation == 0)
			handle = new Square(color.RED, new Point2D.Double(
					center.getX(), center.getY() - height / 2 - 10), 10);	
		else
		{
			handle = new Square(color.RED, new Point2D.Double(
					center.getX() + (height / 2 + 10) * Math.sin(rotation),
					center.getY() - (height / 2 + 10) * Math.cos(rotation)), 10);
		
		    handle.setRotation(rotation);
		}	
		
		handles.add(handle);
		
	}

}

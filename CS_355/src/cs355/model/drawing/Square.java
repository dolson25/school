package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

/**
 * Add your square code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Square extends Shape {

	// The size of this Square.
	private double size;
	
	//a point for a corner 
	private Point2D.Double point1;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param size the size of the new shape.
	 */
	public Square(Color color, Point2D.Double center, double size) {

		// Initialize the superclass.
		super(color, center);

		// Set the field.
		this.size = size;
		this.point1 = center;
	}

	/**
	 * Getter for this Square's size.
	 * @return the size as a double.
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Setter for this Square's size.
	 * @param size the new size.
	 */
	public void setSize(double size) {
		this.size = size;
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
		
		
		if((Math.abs(objCoord.getX()) <= (size / 2)) 
				         && (Math.abs(objCoord.getY()) <= (size / 2)))
			return true;
		return false;
	}
	
	public void setPoint2(Point2D.Double point2)
	{		
		double width, height = 0;
		
		if(point2.getX() > point1.getX())
		{
			if(point2.getY() < point1.getY())
			{
				width = point2.getX() - point1.getX();
				height = point1.getY() - point2.getY();
				Point2D.Double upperLeft;
				if(width < height)
				{
					size = width;
					upperLeft = (new Point2D.Double(point1.getX(),point1.getY() - size));
                    center = (new Point2D.Double(upperLeft.getX() + size / 2,
                            upperLeft.getY() + size / 2));
				}
				else
				{
					size = height;
					upperLeft = (new Point2D.Double(point1.getX(),point1.getY() - size));
                    center = (new Point2D.Double(upperLeft.getX() + size / 2,
                            upperLeft.getY() + size / 2));
				}
			}
			else
			{
				width = point2.getX() - point1.getX();
				height = point2.getY() - point1.getY();
				if(width < height)
				{
					size = width;
				}
				else
				{
					size = height;
				}

                center = (new Point2D.Double(point1.getX() + size / 2,
                        point1.getY() + size / 2));
			}
		}
		else
		{
			if(point2.getY() < point1.getY())
			{
				width = point1.getX() - point2.getX();
				height = point1.getY() - point2.getY();
                Point2D.Double upperLeft;
				if(width < height)
				{
					size = width;
					upperLeft = new Point2D.Double(point2.getX(),
									point1.getY() - size);
				}
				else
				{
					size = height;
					upperLeft = new Point2D.Double(point1.getX() - size,
															point2.getY());
				}

                center = (new Point2D.Double(upperLeft.getX() + size / 2,
                        upperLeft.getY() + size / 2));
			}
			else
			{
				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
				if(width < height)
				{
					size = width;
				}
				else
				{
					size = height;
				}
			
			    Point2D.Double upperLeft = (new Point2D.Double(point1.getX() - size,point1.getY()));
                center = (new Point2D.Double(upperLeft.getX() + size / 2,
                        upperLeft.getY() + size / 2));
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
				             center.getX(), center.getY() - size / 2 - 10), 10);
		else
		{
			handle = new Square(color.RED, new Point2D.Double(
					center.getX() + (size / 2 + 10) * Math.sin(rotation + Math.PI / 2 ),
					center.getY() - (size / 2 + 10) * Math.cos(rotation + Math.PI / 2)), 10);
		
		    handle.setRotation(rotation);
		}
		
		    
		handles.add(handle);

		
	}

}

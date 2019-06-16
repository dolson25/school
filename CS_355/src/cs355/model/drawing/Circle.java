package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

/**
 * Add your circle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Circle extends Shape {

	// The radius.
	private double radius;
	
	//a point for a corner of the bounding box
	private Point2D.Double point1;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param radius the radius of the new shape.
	 */
	public Circle(Color color, Point2D.Double center, double radius) {

		// Initialize the superclass.
		super(color, center);

		// Set the field.
		this.radius = radius;
		this.point1 = center;
	}

	/**
	 * Getter for this Circle's radius.
	 * @return the radius of this Circle as a double.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Setter for this Circle's radius.
	 * @param radius the new radius of this Circle.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
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
		
		
		if(Math.sqrt(Math.pow(objCoord.getX(),2) +
				     Math.pow(objCoord.getY(), 2)) <= radius)
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
				if(width < height)
				{
					radius = width / 2;
					Point2D.Double upperLeft = (new Point2D.Double(point1.getX(),point1.getY() - width));
					center = (new Point2D.Double(upperLeft.getX() + radius,
							upperLeft.getY() + radius));
				}
				else
				{
					radius = height / 2;
					Point2D.Double upperLeft = (new Point2D.Double(point1.getX(),point1.getY() - height));
					center = (new Point2D.Double(upperLeft.getX() + radius,
							upperLeft.getY() + radius));
				}
			}
			else
			{
				width = point2.getX() - point1.getX();
				height = point2.getY() - point1.getY();
				if(width < height)
				{
					radius = width / 2;
				}
				else
				{
					radius = height / 2;
				}

				center = (new Point2D.Double(point1.getX() + radius,
				point1.getY() + radius));
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
					radius = width / 2;
					upperLeft = new Point2D.Double(point2.getX(),
									point1.getY() - width);
				}
				else
				{
					radius = height / 2;
					upperLeft = new Point2D.Double(point1.getX() - height,
															point2.getY());
				}
				center = (new Point2D.Double(upperLeft.getX() + radius,
						upperLeft.getY() + radius));
			}
			else
			{
				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
                Point2D.Double upperLeft;
				if(width < height)
				{
					radius = width / 2;
				    upperLeft = (new Point2D.Double(point1.getX() - width ,point1.getY()));
				}
				else
				{
					radius = height / 2;
				    upperLeft = (new Point2D.Double(point1.getX() - height ,point1.getY()));
				}

                center = (new Point2D.Double(upperLeft.getX() + radius,
                        upperLeft.getY() + radius));
			}
		}
	}


	@Override
	public boolean pointInHandle(Double pt) {
		return false;
	}

	@Override
	public ArrayList<Square> getHandle() {
		return null;
	}

	@Override
	public void setHandle() {		
	}

}

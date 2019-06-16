package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

/**
 * Add your triangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Triangle extends Shape {

	// The three points of the triangle.
	private Point2D.Double a;
	private Point2D.Double b;
	private Point2D.Double c;
	
	double maxAB = 0;
	double maxAll =0;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param a the first point, relative to the center.
	 * @param b the second point, relative to the center.
	 * @param c the third point, relative to the center.
	 */
	public Triangle(Color color, Point2D.Double center, Point2D.Double a,
					Point2D.Double b, Point2D.Double c)
	{

		// Initialize the superclass.
		super(color, center);

		// Set fields.
		this.a = a;
		this.b = b;
		this.c = c;
	
	}

	/**
	 * Getter for the first point.
	 * @return the first point as a Java point.
	 */
	public Point2D.Double getA() {
		return a;
	}

	/**
	 * Setter for the first point.
	 * @param a the new first point.
	 */
	public void setA(Point2D.Double a) {
		this.a = a;
	}

	/**
	 * Getter for the second point.
	 * @return the second point as a Java point.
	 */
	public Point2D.Double getB() {
		return b;
	}

	/**
	 * Setter for the second point.
	 * @param b the new second point.
	 */
	public void setB(Point2D.Double b) {
		this.b = b;
	}

	/**
	 * Getter for the third point.
	 * @return the third point as a Java point.
	 */
	public Point2D.Double getC() {
		return c;
	}

	/**
	 * Setter for the third point.
	 * @param c the new third point.
	 */
	public void setC(Point2D.Double c) {
		this.c = c;
		
		setCenter(new Point2D.Double(
				(a.getX() + b.getX() + c.getX()) / 3,
				(a.getY() + b.getY() + c.getY()) / 3));
		
		setA(new Point2D.Double(
				a.getX() - center.getX(),
				center.getY() - a.getY()));
		setB(new Point2D.Double(
				b.getX() - center.getX(),
				center.getY() - b.getY()));
		setCReal(new Point2D.Double(
				c.getX() - center.getX(),
				center.getY() - c.getY()));
	}
	
	public void setCReal(Point2D.Double c)
	{
		this.c = c;
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
		worldToObj.translate(-center.getX(), - center.getY());
		
		// and transform point from world to object
		worldToObj.transform(pt, objCoord);

		
		objCoord.setLocation(objCoord.getX(), - objCoord.getY());

						
		//find out of the point is on the positive or negative side of each line
		double X1 = objCoord.getX() - a.getX();
		double Y1 = objCoord.getY() - a.getY();
		double X2 = b.getX() - a.getX();
		double Y2 = b.getY() - a.getY();
		       Y2 = Y2 * -1;
		double lineOne = X1 * Y2 + Y1 * X2;
		
		X1 = objCoord.getX() - b.getX();
		Y1 = objCoord.getY() - b.getY();
		X2 = c.getX() - b.getX();
		Y2 = c.getY() - b.getY();
		Y2 = Y2 * -1;
		double lineTwo = X1 * Y2 + Y1 * X2;
		
		X1 = objCoord.getX() - c.getX();
		Y1 = objCoord.getY() - c.getY();
		X2 = a.getX() - c.getX();
		Y2 = a.getY() - c.getY();
		Y2 = Y2 * -1;
		double lineThree = X1 * Y2 + Y1 * X2;
		
		if(   ((lineOne < 0) & (lineTwo < 0) & (lineThree < 0)) 
			| ((lineOne > 0) & (lineTwo > 0) & (lineThree > 0)))
			return true;
		return false;
	}


	@Override
	public boolean pointInHandle(Double pt) {
		return handle.pointInShape(pt, 4);		
	}

	@Override
	public ArrayList<Square> getHandle() {
		return handles;
	}

	@Override
	public void setHandle() {
		handles.clear();
		
		if(rotation == 0)
		{
			maxAB = Math.max(a.getY(), b.getY());
			maxAll = Math.max(maxAB, c.getY());
			
			handle = new Square(color.RED, new Point2D.Double(
				             center.getX(), center.getY() - maxAll - 10), 10);	
		}
		else
		{
			handle = new Square(color.RED, new Point2D.Double(
					center.getX() + (maxAll + 10) * Math.sin(rotation),
					center.getY() - (maxAll + 10) * Math.cos(rotation)), 10);
		
		    handle.setRotation(rotation);
		}
		
		    
		handles.add(handle);
	}

}

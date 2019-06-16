package cs355.model.drawing;

import cs355.GUIFunctions;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	private Point2D.Double end; 
	Square handle2;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param start the starting point.
	 * @param end the ending point.
	 */
	public Line(Color color, Point2D.Double start, Point2D.Double end) {

		// Initialize the superclass.
		super(color, start);

		// Set the field.
		this.end = end;
	}

	/**
	 * Getter for this Line's ending point.
	 * @return the ending point as a Java point.
	 */
	public Point2D.Double getEnd() {
		return this.end;
	}

	/**
	 * Setter for this Line's ending point.
	 * @param end the new ending point for the Line.
	 */
	public void setEnd(Point2D.Double end) {
        this.end = end;}

	/**
	 * Add your code to do an intersection test
	 * here. You <i>will</i> need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
		
		//------------------------------------------
		//bounding box so as not to get an infinite line
		//-----------------------------------------
		if(end.getX() > center.getX())
		{
			if(  (pt.getX() - end.getX() > 4) | (center.getX() - (pt.getX()) > 4))
				return false;
		}
		else
		{
            if(  (pt.getX() - center.getX() > 4) | (end.getX() - (pt.getX()) > 4))
				return false;
		}

        if(end.getY() > center.getY())
        {
            if(  (pt.getY() - end.getY() > 4) | (center.getY() - (pt.getY()) > 4))
                return false;
        }
        else
        {
            if(  (pt.getY() - center.getY() > 4) | (end.getY() - (pt.getY()) > 4))
                return false;
        }

		//---------------------------------------------
		// if we got this far we are in the bounding box
		//------------------------------------------

		//get the d vector from the endpoints
		double dX = this.end.getX() - this.center.getX();
		double dY = this.end.getY() - this.center.getY();
		double dDist = Math.sqrt((dX * dX) + (dY * dY));
		dX = dX / dDist;
		dY = dY / dDist;
		
		//get the q' point
		double iDX = pt.getX() - center.getX();
		double iDY = pt.getY() - center.getY();
		double dot = iDX * dX + iDY * dY;
		double midX = dot * dX;
		double midY = dot * dY;
		double qPrimeX = center.getX() + midX;
		double qPrimeY = center.getY() + midY;
		
		//find the q to q' distance
		double distance = Math.sqrt(Math.pow(pt.getX() - qPrimeX, 2) +
				                    Math.pow(pt.getY() - qPrimeY, 2) );
		
		if(distance <= tolerance) {
            return true;
        }
		return false;

	}

	@Override
	public boolean pointInHandle(Double pt) {
		return (handle.pointInShape(pt, 4) | (handle2.pointInShape(pt, 4)));		
	}
	
	public boolean pointInHandleCenter(Double pt) {
		return handle.pointInShape(pt, 4);		
	}

	@Override
	public void setHandle() {
		handles.clear();
		handle = new Square(color.RED, this.center, 8);
		handle2 = new Square(color.RED, this.end, 8);
		handles.add(handle);
		handles.add(handle2);
		
	}
	
	@Override
	public boolean isLine()
	{
		return true;
	}

}

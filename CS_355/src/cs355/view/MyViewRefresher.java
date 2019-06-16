package cs355.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.controller.Controller;
import cs355.model.drawing.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.model.drawing.Line;
import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.WireFrame;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MyViewRefresher implements ViewRefresher {

	Drawing model;
	Controller controller;
	Color curColor;
	Color boxColor;
	
	public MyViewRefresher(Drawing model, Controller controller)
	{
		this.model = model;
		this.controller = controller;
		model.addObserver(this);
		curColor = Color.white;
		boxColor = Color.RED;
	}

	@Override
	public void update(Observable o, Object arg) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) {

        //lab6
        if( controller.get_image() && controller.getImage() != null)
        {
            //zoom and scroll transforms
            AffineTransform worldToViewScale = new AffineTransform(controller.getZoomLevel(), 0, 0, controller.getZoomLevel(), 0, 0);
            AffineTransform worldToViewTranslate = new AffineTransform(1,0,0,1, -controller.getCornerX(), -controller.getCornerY());
            worldToViewTranslate.preConcatenate(worldToViewScale);

            //draw image
            BufferedImage bufferedImage = controller.getImage().getImage();
            g2d.setTransform(worldToViewTranslate);
            g2d.drawImage(bufferedImage, null, 0, 0);

        }

		
		ArrayList<Shape> shapes = (ArrayList)model.getShapes();
        Shape selectedShape = null;
		
		for(int i = 0; i < shapes.size(); i++)
		{
			if(shapes.get(i).IsSelected())
				selectedShape = shapes.get(i);
			
		    String cl = shapes.get(i).getClass().getSimpleName();
		    switch (cl) {
		    case "Line":
				Line l = (Line) model.getShape(i);
				g2d.setColor(l.getColor());
                g2d.setTransform(getTransform(0,new Point2D.Double(0,0)));
				g2d.drawLine(
                        (int)l.getCenter().getX(),
                        (int)l.getCenter().getY(),
						(int)l.getEnd().getX(),
						(int)l.getEnd().getY());
		        break;	    
		    case "Square":
				Square s = (Square) model.getShape(i);
				g2d.setColor(s.getColor());
				g2d.setTransform(getTransform(s.getRotation(),s.getCenter()));
				g2d.fillRect((int)(0 - s.getSize() / 2), 
						(int)(0 - s.getSize() / 2),
						(int)s.getSize(), (int)s.getSize());
		    	break;
		    case "Rectangle":
				Rectangle r = (Rectangle) model.getShape(i);
				g2d.setColor(r.getColor());
                g2d.setTransform(getTransform(r.getRotation(),r.getCenter()));
				g2d.fillRect((int)(0 - r.getWidth() / 2), 
						(int)(0 - r.getHeight() / 2),
						(int)r.getWidth(), (int)r.getHeight());
		        break;	    
		    case "Circle":
				Circle c = (Circle) model.getShape(i);
				g2d.setColor(c.getColor());
                g2d.setTransform(getTransform(c.getRotation(),c.getCenter()));
				g2d.fillOval((int)(0 - c.getRadius()), 
						(int)(0 - c.getRadius()), 
						(int)c.getRadius() * 2, (int)c.getRadius() * 2);
		    	break;
		    case "Ellipse":
				Ellipse ell = (Ellipse) model.getShape(i);
				g2d.setColor(ell.getColor());
                g2d.setTransform(getTransform(ell.getRotation(),ell.getCenter()));
				g2d.fillOval((int)(0 - ell.getWidth() / 2), 
						(int)(0 - ell.getHeight() / 2),
						(int)ell.getWidth(), (int)ell.getHeight());
		        break;	    
		    case "Triangle":
				Triangle t = (Triangle) model.getShape(i);
				g2d.setColor(t.getColor());
                g2d.setTransform(getTransform(t.getRotation(),t.getCenter()));
				int[] xpoints = {(int)t.getA().getX(), 
						         (int)t.getB().getX(), 
						         (int)t.getC().getX()};
				int[] ypoints = {-(int)t.getA().getY(), 
				                 -(int)t.getB().getY(), 
				                 -(int)t.getC().getY()};
				g2d.fillPolygon(xpoints, ypoints,3);
		    	break;
		    }
		}


		

		if(selectedShape != null)
		{
			ArrayList<Square> handle= selectedShape.getHandle();
			if(handle != null)
			{
				for(int i = 0; i < handle.size(); i++)
				{
					Square s = handle.get(i);
					g2d.setColor(s.getColor());
                    g2d.setTransform(getTransform(s.getRotation(),s.getCenter()));
					g2d.drawRect((int)(0 - s.getSize() / 2), (int)(0 - s.getSize() / 2),
							      (int)s.getSize(), (int)s.getSize());
				}
			}
			
			String cl = selectedShape.getClass().getSimpleName();
		    switch (cl) {	    
		    case "Square":
				Square s = (Square) selectedShape;
				g2d.setColor(boxColor);
                g2d.setTransform(getTransform(s.getRotation(),s.getCenter()));
				g2d.drawRect((int)(0 - s.getSize() / 2), 
						(int)(0 - s.getSize() / 2),
						(int)s.getSize(), (int)s.getSize());
		    	break;
		    case "Rectangle":
				Rectangle r = (Rectangle) selectedShape;
				g2d.setColor(boxColor);
                g2d.setTransform(getTransform(r.getRotation(),r.getCenter()));
				g2d.drawRect((int)(0 - r.getWidth() / 2), 
						(int)(0 - r.getHeight() / 2),
						(int)r.getWidth(), (int)r.getHeight());
		        break;	    
		    case "Circle":
				Circle c = (Circle) selectedShape;
				g2d.setColor(boxColor);
                g2d.setTransform(getTransform(c.getRotation(),c.getCenter()));
				g2d.drawRect((int)(0 - c.getRadius()), 
						(int)(0 - c.getRadius()),
						(int)c.getRadius() * 2, (int)c.getRadius() * 2);
		    	break;
		    case "Ellipse":
				Ellipse ell = (Ellipse) selectedShape;
				g2d.setColor(boxColor);
                g2d.setTransform(getTransform(ell.getRotation(),ell.getCenter()));
				g2d.drawRect((int)(0 - ell.getWidth() / 2), 
						(int)(0 - ell.getHeight() / 2),
						(int)ell.getWidth(), (int)ell.getHeight());
		        break;	    
		    case "Triangle":
				Triangle t = (Triangle) selectedShape;
				g2d.setColor(boxColor);
                g2d.setTransform(getTransform(t.getRotation(),t.getCenter()));
				int[] xpoints = {(int)t.getA().getX(), 
						         (int)t.getB().getX(), 
						         (int)t.getC().getX()};
				int[] ypoints = {-(int)t.getA().getY(), 
				                 -(int)t.getB().getY(), 
				                 -(int)t.getC().getY()};
				g2d.drawPolygon(xpoints, ypoints,3);
		    	break;
		   }
        }

        //lab5
        if(controller.get_3D() && controller.getInstances() != null){

            //draw the 3D scene

            //clip matrix
            double farPlane = 75;
            double nearPlane = 5;
            double[][] clipMatrix = new double[4][4];
            clipMatrix[0][0] = controller.getZoomLevel();  //--------------------------------add zoom level
            clipMatrix[1][1] = controller.getZoomLevel();   //     - - -- -  - - - -- --  -- - - - - - same
            clipMatrix[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
            clipMatrix[2][3] = (-2 * farPlane * nearPlane) / (farPlane - nearPlane);
            clipMatrix[3][2] = 1;
            //System.out.println("Clip Matrix:");
            //printMatrix(clipMatrix);


            //-----------------------------
            //viewport matrix
            //-----------------------------
            double width = 512 / controller.getZoomLevel();   //do these change?
            double height = 512 / controller.getZoomLevel();
            double[][] viewMatrix = new double[3][3];
            viewMatrix[0][0] = width / 2;
            viewMatrix[1][1] = -height / 2;
            viewMatrix[0][2] = width / 2;
            viewMatrix[1][2] = height / 2;
            viewMatrix[2][2] = 1;


            //zoom and scroll transforms
            AffineTransform worldToViewScale = new AffineTransform(controller.getZoomLevel(), 0, 0, controller.getZoomLevel(), 0, 0);
            AffineTransform worldToViewTranslate = new AffineTransform(1,0,0,1, -controller.getCornerX(), -controller.getCornerY());
            worldToViewTranslate.preConcatenate(worldToViewScale);


            ArrayList<Instance> instances = controller.getInstances();

            for(int i=0; i <instances.size(); i++){
                WireFrame model = instances.get(i).getModel();

                double[][] objectRotation = new double[4][4];
                for(int j=0; j<objectRotation.length; j++){objectRotation[j][j] = 1;}
                double modelRadians = degreesToRadians(instances.get(i).getRotAngle());
                objectRotation[0][0] = cos(modelRadians);
                objectRotation[0][2] = sin(modelRadians);
                objectRotation[2][0] = cos(-modelRadians + Math.PI / 2);
                objectRotation[2][2] = sin(-modelRadians + Math.PI / 2);


                //world to camera translation
                double[][] cameraTranslation = objectRotation;
                cameraTranslation[0][3] = -controller.getCameraX() - instances.get(i).getPosition().x;  //camera and model position
                cameraTranslation[1][3] = -controller.getCameraY() - instances.get(i).getPosition().y;
                cameraTranslation[2][3] = -controller.getCameraZ() - instances.get(i).getPosition().z;
                //System.out.println("Translation Matrix :");
                //printMatrix(cameraTranslation);


                //world to camera rotation
                double[][] cameraRotation = new double[4][4];
                for(int j=0; j<cameraRotation.length; j++){cameraRotation[j][j] = 1;}

                double cameraRadians = degreesToRadians(controller.getCameraA());

                cameraRotation[0][0] = cos(cameraRadians);
                cameraRotation[0][2] = sin(cameraRadians);
                cameraRotation[2][0] = cos(cameraRadians + Math.PI / 2);
                cameraRotation[2][2] = sin(cameraRadians + Math.PI / 2);
                //System.out.println("Rotation Matrix :");
                //printMatrix(cameraRotation);


                //----------------------------------
                //multiply rotation and translation
                //----------------------------------
                double[][] worldToCamera = multiplyMatrices(cameraRotation,cameraTranslation);
                //System.out.println("World to Camera Matrix :");
                //printMatrix(worldToCamera);


                List<Line3D> lines = model.getLines();

                for(int j=0; j < lines.size(); j++){
                    Line3D line = lines.get(j);

                    double[] startVector = new double[4];
                    double[] endVector = new double[4];

                    startVector[0] = line.start.x;
                    startVector[1] = line.start.y;
                    startVector[2] = - line.start.z;
                    startVector[3] = 1;

                    endVector[0] = line.end.x;
                    endVector[1] = line.end.y;
                    endVector[2] = - line.end.z;
                    endVector[3] = 1;

                    //get a point in camera coordinates
                    double[] cameraPointStart = matrixByVector(worldToCamera, startVector);
                    double[] cameraPointEnd = matrixByVector(worldToCamera, endVector);


                    //clip those points
                    double[] clippedVectorStart = matrixByVector(clipMatrix, cameraPointStart);
                    double[] clippedVectorEnd = matrixByVector(clipMatrix, cameraPointEnd);
                    //System.out.println("Clipped Points:");
                    //printVector(clippedVectorStart);
                    //printVector(clippedVectorEnd);


                    //do clip tests
                    boolean draw = true;

                    if(clippedVectorStart[0] < - clippedVectorStart[3] && clippedVectorEnd[0] < - clippedVectorEnd[3]) {
                        //System.out.println("clipping left");
                        draw = false;
                    } else if(clippedVectorStart[0] > clippedVectorStart[3] && clippedVectorEnd[0] > clippedVectorEnd[3]) {
                        //System.out.println("clipping right");
                        draw = false;
                    } else if(clippedVectorStart[1] < - clippedVectorStart[3] && clippedVectorEnd[1] < - clippedVectorEnd[3]) {
                        //System.out.println("clipping bottom");
                        draw = false;
                    } else if(clippedVectorStart[1] > clippedVectorStart[3] && clippedVectorEnd[1] > clippedVectorEnd[3]) {
                        //System.out.println("clipping top");
                        draw = false;
                    } else if(clippedVectorStart[2] < - clippedVectorStart[3] || clippedVectorEnd[2] < - clippedVectorEnd[3]) {
                       // System.out.println("clipping near");
                        draw = false;
                    }
                    else if(clippedVectorStart[2] > clippedVectorStart[3] || clippedVectorEnd[2] > clippedVectorEnd[3]) {
                        //System.out.println("clipping far");
                        draw = false;
                    }

                    //draw unclipped lines
                    if(draw) {
                        //normalize and drop a dimension
                        double[] pointVectorStart = new double[3];
                        double[] pointVectorEnd = new double[3];


                        for (int k = 0; k < 2; k++) {
                            pointVectorStart[k] = clippedVectorStart[k] / clippedVectorStart[3];
                            pointVectorEnd[k] = clippedVectorEnd[k] / clippedVectorEnd[3];
                        }
                        pointVectorStart[2] = 1;
                        pointVectorEnd[2] = 1;

                        //System.out.println("Point Vectors:");
                        //printVector(pointVectorStart);
                        //printVector(pointVectorEnd);


                        //change to view
                        double[] viewVectorStart = matrixByVector(viewMatrix, pointVectorStart);
                        double[] viewVectorEnd = matrixByVector(viewMatrix, pointVectorEnd);


                        //and then finally draw the line
                        g2d.setColor(instances.get(i).getColor());
                        g2d.setTransform(worldToViewTranslate);
                        g2d.drawLine((int) viewVectorStart[0], (int) viewVectorStart[1],
                                (int) viewVectorEnd[0], (int) viewVectorEnd[1]);
                    }
                }
            }
        }
	}

	private AffineTransform getTransform (double rotation, Point2D.Double center){
        AffineTransform worldToViewScale = new AffineTransform(controller.getZoomLevel(), 0, 0, controller.getZoomLevel(), 0, 0);
        AffineTransform worldToViewTranslate = new AffineTransform(1,0,0,1, -controller.getCornerX(), -controller.getCornerY());
        AffineTransform objectToWorldTranslate = new AffineTransform(1,0,0,1, center.getX(),center.getY());
        AffineTransform objectToWorldRotate = new AffineTransform(cos(rotation),sin(rotation), -sin(rotation), cos(rotation),0,0);
        objectToWorldRotate.preConcatenate(objectToWorldTranslate);
        objectToWorldRotate.preConcatenate(worldToViewTranslate);
        objectToWorldRotate.preConcatenate(worldToViewScale);
        return  objectToWorldRotate;
    }

    private double degreesToRadians(double degrees){
        return degrees * Math.PI / 180;
    }

    private double[][] multiplyMatrices(double[][] leftMatrix, double[][] rightMatrix){

        double[][] result = new double[leftMatrix.length][rightMatrix.length];

        for (int i = 0; i < leftMatrix.length; i++)
            for (int j = 0; j < leftMatrix.length; j++)
                for (int k = 0; k < leftMatrix.length; k++)
                    result[i][j] += leftMatrix[i][k] * rightMatrix[k][j];

        return result;
    }

    private double[] matrixByVector(double[][] matrix, double[] vector){
        double[] result = new double[vector.length];

        for (int i = 0; i < matrix.length; i++)
            for (int k = 0; k < matrix.length; k++)
                result[i] += matrix[i][k] * vector[k];

        return result;
    }

    private void printMatrix(double[][] matrix){
        for(int i=0; i < matrix.length; i++){
            System.out.print("[");
            for(int j=0; j < matrix[0].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("]");
        }
    }

    private void printVector(double[] vector){
        System.out.print("[");
        for(int i=0; i < vector.length; i++){
            System.out.print(vector[i] + " ");
        }
        System.out.println("]");
    }

}

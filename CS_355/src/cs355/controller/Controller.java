package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.model.drawing.*;
import cs355.model.image.CS355Image;
import cs355.model.image.MyImage;
import cs355.model.scene.CS355Scene;
import cs355.model.scene.Instance;

import static java.lang.Math.sin;
import static java.lang.StrictMath.cos;

public class Controller implements CS355Controller {

	Drawing model;
	Shape curShapeDrawing;
	Shape curShapeSelected;
	Color curColor;
	button selectedButton;
	int trianglePoint;
	boolean shapeSelected;
	Point2D.Double pointSelected;
	Point2D.Double centerSelected;
    Point2D.Double endSelected;
	boolean rotating;
    boolean lineCenterR = true;
	double zoomLevel = 1;
    int cornerX = 768;
    int cornerY = 768;
	double viewWidth = 512;
	double NOMRAL_WIDTH = 512;
    boolean scrollFlag = true;

    //lab 5
	double cameraX = 0;
	double cameraY = 0;
	double cameraZ = 0;
	double cameraA = 0;

    boolean _3D = false;

    CS355Scene scene;
    ArrayList<Instance> instances;

	//lab 6
    boolean _image = false;
    MyImage image;

	public enum button {
		LINE, SQUARE, RECTANGLE, CIRCLE, ELLIPSE, TRIANGLE, SELECT
	}

	public Controller(Drawing model) {
		this.model = model;
		curShapeDrawing = null;
		curShapeSelected = null;
		curColor = Color.WHITE;
		selectedButton = button.SELECT;
		trianglePoint = 1;
		shapeSelected = false;
		pointSelected = null;
		rotating = false;
        lineCenterR = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

        Point2D.Double world = getWorldCoordinate(e);

		if (selectedButton == button.TRIANGLE) {
			if (trianglePoint == 1) {
				// almost worthless placeholder triangle
				curShapeDrawing = new Triangle(
						curColor,
						new Point2D.Double((double) world.getX(), (double) world.getY()),
						new Point2D.Double((double) world.getX(), (double) world.getY()),
						new Point2D.Double((double) world.getX(), (double) world.getY()),
						new Point2D.Double((double) world.getX(), (double) world.getY()));

				model.addShape(curShapeDrawing);

				trianglePoint = 2;
			} else if (trianglePoint == 2) {
				Triangle t = (Triangle) curShapeDrawing;
				t.setB(new Point2D.Double((double) world.getX(), (double) world.getY()));

				trianglePoint = 3;
			} else {
				Triangle t = (Triangle) curShapeDrawing;
				t.setC(new Point2D.Double((double) world.getX(), (double) world.getY()));

				curShapeDrawing.setHandle();

                model.update();

				trianglePoint = 1;
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

        Point2D.Double world = getWorldCoordinate(e);

		switch (selectedButton) {
		case LINE:
			curShapeDrawing = new Line(curColor, new Point2D.Double(world.getX(),
					world.getY()), new Point2D.Double(world.getX(), world.getY()));
			model.addShape(curShapeDrawing);
			break;
		case SQUARE:
			curShapeDrawing = new Square(curColor, new Point2D.Double(world.getX(),
					world.getY()), 0);
			model.addShape(curShapeDrawing);
			break;

		case RECTANGLE:
			curShapeDrawing = new Rectangle(curColor, new Point2D.Double(
					world.getX(), world.getY()), 0, 0);
			model.addShape(curShapeDrawing);
			break;
		case CIRCLE:
			curShapeDrawing = new Circle(curColor, new Point2D.Double(world.getX(),
					world.getY()), 0);
			model.addShape(curShapeDrawing);
			break;
		case ELLIPSE:
			curShapeDrawing = new Ellipse(curColor, new Point2D.Double(
					world.getX(), world.getY()), 0, 0);
			model.addShape(curShapeDrawing);
			break;
		case SELECT:
			if (shapeSelected) {
				if (curShapeSelected.pointInHandle(world)) {
					pointSelected = world;
					rotating = true;
                    if(curShapeSelected.isLine()){
                        Line l = (Line)curShapeSelected;
                        if(l.pointInHandleCenter(pointSelected))
                            lineCenterR = true;
                        else
                            lineCenterR = false;
                    }
					break;
				}
			}

			ArrayList<Shape> shapes = (ArrayList) model.getShapes();

			for (int i = shapes.size() - 1; i >= 0; i--) {
				shapeSelected = false;

				if (shapes.get(i).pointInShape(world, 4 )) {
					curShapeSelected = shapes.get(i);
					curShapeSelected.setSelected(true);
					shapeSelected = true;
					pointSelected = world;
					centerSelected = curShapeSelected.getCenter();
                    if(curShapeSelected.isLine())
                        endSelected = ((Line)curShapeSelected).getEnd();
					for(int j = i -1; j >= 0; j--)
					{
						shapes.get(j).setSelected(false);
					}
					i = -1;
					
					
					GUIFunctions.changeSelectedColor(curShapeSelected
							.getColor());
					curColor = curShapeSelected.getColor();
                    model.update();
				} else
					shapes.get(i).setSelected(false);
			}

			if ((!shapeSelected) & (curShapeSelected != null)) {
				curShapeSelected.setSelected(false);
                model.update();
			}
			break;
		default:
			break;
		}
    }

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if ((selectedButton == button.LINE)
				|| (selectedButton == button.SQUARE)
				|| (selectedButton == button.RECTANGLE)
				|| (selectedButton == button.CIRCLE)
				|| (selectedButton == button.ELLIPSE)) {
			curShapeDrawing.setHandle();


            model.update();
		}

		rotating = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

        Point2D.Double world = getWorldCoordinate(e);

		switch (selectedButton) {
		case LINE:
			Line l = (Line) curShapeDrawing;
			l.setEnd(new Point2D.Double(world.getX(), world.getY()));
            model.update();
			break;
		case SQUARE:
			Square s = (Square) curShapeDrawing;
            s.setPoint2(new Point2D.Double(world.getX(),  world.getY()));
            model.update();
			break;
		case RECTANGLE:
			Rectangle r = (Rectangle) curShapeDrawing;
			r.setPoint2(new Point2D.Double( world.getX(), world.getY()));
            model.update();
			break;
		case CIRCLE:
			Circle c = (Circle) curShapeDrawing;
			c.setPoint2(new Point2D.Double(world.getX(), world.getY()));
            model.update();
			break;
		case ELLIPSE:
			Ellipse ell = (Ellipse) curShapeDrawing;
			ell.setPoint2(new Point2D.Double(world.getX(), world.getY()));
            model.update();
			break;
		case SELECT:
			if (shapeSelected) {
				if (rotating) {
					// rotate
					if (curShapeSelected.isLine()) {
                        Line ln = (Line) curShapeSelected;

						if(lineCenterR)
                            ln.setCenter(new Point2D.Double(world.getX(), world.getY()));
                        else
                            ln.setEnd(new Point2D.Double(world.getX(), world.getY()));

                        curShapeSelected.setHandle();
                        model.update();

					} else {
						
						curShapeSelected.setRotation(Math.atan2(world.getY()
								- curShapeSelected.getCenter().getY(), world.getX()
								- curShapeSelected.getCenter().getX()));
						curShapeSelected.setHandle();
                        model.update();
					}
				} else {
					// drag
                    curShapeSelected.setCenter(new Point2D.Double(
                            centerSelected.getX() + world.getX()
                                    - pointSelected.getX(), centerSelected
                            .getY() + world.getY() - pointSelected.getY()));

                    if(curShapeSelected.isLine()){
                        ((Line)curShapeSelected).setEnd(new Point2D.Double(
                                endSelected.getX() + world.getX()
                                        - pointSelected.getX(), endSelected
                                .getY() + world.getY() - pointSelected.getY()));
                    }

                    curShapeSelected.setHandle();
                    model.update();
				}
			}
			break;
		default:
			break;
		}

	}

	private Point2D.Double getWorldCoordinate(MouseEvent e){
        AffineTransform viewToWorldScale = new AffineTransform(1 / zoomLevel, 0, 0, 1/ zoomLevel, 0, 0);
        AffineTransform viewToWorldTranslate = new AffineTransform(1,0,0,1, cornerX, cornerY);
        viewToWorldTranslate.concatenate(viewToWorldScale);
        Point2D.Double world = new Point2D.Double();
        viewToWorldTranslate.transform(new Point2D.Double(e.getX(), e.getY()), world);
        return world;
    }

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void colorButtonHit(Color c) {
		GUIFunctions.changeSelectedColor(c);
		curColor = c;
		if(shapeSelected)
		{
			curShapeSelected.setColor(c);
            model.update();
		}
	}

	@Override
	public void lineButtonHit() {
		selectedButton = button.LINE;

	}

	@Override
	public void squareButtonHit() {
		selectedButton = button.SQUARE;

	}

	@Override
	public void rectangleButtonHit() {
		selectedButton = button.RECTANGLE;

	}

	@Override
	public void circleButtonHit() {
		selectedButton = button.CIRCLE;

	}

	@Override
	public void ellipseButtonHit() {
		selectedButton = button.ELLIPSE;

	}

	@Override
	public void triangleButtonHit() {
		selectedButton = button.TRIANGLE;
		trianglePoint = 1;

	}

	@Override
	public void selectButtonHit() {
		selectedButton = button.SELECT;
		curShapeDrawing = null;
	}

	@Override
	public void zoomInButtonHit() {
        if(zoomLevel < 4) {
            zoomLevel *= 2;

            viewWidth = NOMRAL_WIDTH / zoomLevel;
			cornerX += viewWidth / 2;
			cornerY += viewWidth / 2;

            scrollFlag = false;
            GUIFunctions.setVScrollBarKnob((int)(512 / zoomLevel));
            GUIFunctions.setVScrollBarPosit((int)cornerY);
            GUIFunctions.setHScrollBarKnob((int)(512 / zoomLevel));
            GUIFunctions.setHScrollBarPosit((int)cornerX);
            scrollFlag = true;


            GUIFunctions.refresh();
        }
	}

	@Override
	public void zoomOutButtonHit() {
        if(zoomLevel > .25) {
            zoomLevel *= .5;

			viewWidth = NOMRAL_WIDTH / zoomLevel;
			cornerX -= viewWidth / 4;
			cornerY -= viewWidth / 4;

			//zooming out should not leave the world coordinates
			if(cornerX < 0)
				cornerX = 0;
			if(cornerY < 0)
				cornerY = 0;
			if(cornerX > (2048 - (int)viewWidth))
				cornerX = 2048 - (int)viewWidth;
			if(cornerY > (2048 - (int)viewWidth))
				cornerY = 2048 - (int)viewWidth;

            scrollFlag = false;
            GUIFunctions.setVScrollBarKnob((int)(512 / zoomLevel));
            GUIFunctions.setVScrollBarPosit((int)cornerY);
            GUIFunctions.setHScrollBarKnob((int)(512 / zoomLevel));
            GUIFunctions.setHScrollBarPosit(cornerX);
            if(viewWidth == 2048) {
                GUIFunctions.setHScrollBarPosit(0);
                GUIFunctions.setHScrollBarKnob(2048);
                GUIFunctions.setVScrollBarPosit(0);
                GUIFunctions.setVScrollBarKnob(2048);
            }
            scrollFlag = true;

            GUIFunctions.refresh();
        }
	}

	public double getZoomLevel(){return zoomLevel;}
    public double getCornerX(){return cornerX;}
    public double getCornerY(){return  cornerY;}

    //lab5
	public double getCameraX(){return cameraX;}
	public double getCameraY(){return cameraY;}
	public double getCameraZ(){return cameraZ;}
	public double getCameraA(){return cameraA;}
    public boolean get_3D(){return _3D;}
    public ArrayList<Instance> getInstances(){return instances;}

    //lab6
    public boolean get_image(){return _image;}
    public CS355Image getImage(){return image;}

	@Override
	public void hScrollbarChanged(int value) {
        if(scrollFlag){
            System.out.println(value);
            cornerX = value;
            GUIFunctions.refresh();
        }
	}

	@Override
	public void vScrollbarChanged(int value) {
        if(scrollFlag) {
            cornerY = value;
            System.out.println(value);
            GUIFunctions.refresh();
        }
	}

	@Override
	public void openScene(File file) {
        scene = new CS355Scene();
        scene.open(file);
        cameraX = scene.getCameraPosition().x;
        cameraY = scene.getCameraPosition().y;
        cameraZ = - scene.getCameraPosition().z;
        cameraA = scene.getCameraRotation();

        instances = scene.instances();

        GUIFunctions.refresh();
	}

	@Override
	public void toggle3DModelDisplay() {
        _3D = !_3D;
        GUIFunctions.refresh();
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
        if(_3D){

            double radians = degreesToRadians(cameraA);

            while(iterator.hasNext()){
                switch((char)(int)iterator.next()){
                    case 'W':
                        cameraZ += .5 *cos(radians);
                        cameraX -= .5 * sin(radians);
						GUIFunctions.refresh();
                        break;
                    case 'S':
                        cameraZ -= .5 * cos(radians);
                        cameraX += .5 * sin(radians);
                        GUIFunctions.refresh();
                        break;
                    case 'A':
                        cameraZ -= .5 * cos(radians - Math.PI / 2);
                        cameraX += .5 * sin(radians - Math.PI / 2);
                        GUIFunctions.refresh();
                        break;
                    case 'D':
                        cameraZ -= .5 * cos(radians + Math.PI / 2);
                        cameraX += .5 * sin(radians + Math.PI / 2);
                        GUIFunctions.refresh();
                        break;
                    case 'R':
                        cameraY++;
                        GUIFunctions.refresh();
                        break;
                    case 'F':
                        cameraY--;
                        GUIFunctions.refresh();
                        break;
                    case 'Q':
                        cameraA += 2;
                        GUIFunctions.refresh();
                        break;
                    case 'E':
                        cameraA -= 2;
                        GUIFunctions.refresh();
                        break;
                    case 'H':
                        cameraX = scene.getCameraPosition().x;
                        cameraY = scene.getCameraPosition().y;
                        cameraZ = - scene.getCameraPosition().z;
                        cameraA = scene.getCameraRotation();
                        GUIFunctions.refresh();
                        break;
                }
            }
        }
	}

    private double degreesToRadians(double degrees){
        return degrees * Math.PI / 180;
    }

	@Override
	public void openImage(File file) {
		// call the one in the model
        image = new MyImage(2048, 2048);
        image.open(file);
        image.makeBufferedImage();
        GUIFunctions.refresh();
	}

	@Override
	public void saveImage(File file) {
		// call the one in the model
        if(image != null)
            image.save(file);
	}

	@Override
	public void toggleBackgroundDisplay() {
        _image = !_image;
        GUIFunctions.refresh();
	}

	@Override
	public void saveDrawing(File file) {
		model.save(file);
	}

	@Override
	public void openDrawing(File file) {
		model.open(file);
	}

	@Override
	public void doDeleteShape() {
		if (shapeSelected) {
			ArrayList<Shape> shapes = (ArrayList) model.getShapes();
			model.deleteShape(shapes.indexOf(curShapeSelected));
			curShapeSelected = null;
			shapeSelected = false;
		}
	}

	@Override
	public void doEdgeDetection() {
		image.edgeDetection();
		GUIFunctions.refresh();
	}

	@Override
	public void doSharpen() {
        image.sharpen();
        GUIFunctions.refresh();
	}

	@Override
	public void doMedianBlur() {
        image.medianBlur();
        GUIFunctions.refresh();
	}

	@Override
	public void doUniformBlur() {
        image.uniformBlur();
        GUIFunctions.refresh();
	}

	@Override
	public void doGrayscale() {
        image.grayscale();
        GUIFunctions.refresh();
	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
        image.contrast(contrastAmountNum);
        GUIFunctions.refresh();
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
        image.brightness(brightnessAmountNum);
        GUIFunctions.refresh();
	}

	@Override
	public void doMoveForward() {
		
		if (shapeSelected)
		{		
			ArrayList<Shape> shapes = (ArrayList) model.getShapes();
			model.moveForward(shapes.indexOf(curShapeSelected));
		}
	}

	@Override
	public void doMoveBackward() {
		if (shapeSelected)
		{		
			ArrayList<Shape> shapes = (ArrayList) model.getShapes();
			model.moveBackward(shapes.indexOf(curShapeSelected));
		}
	}

	@Override
	public void doSendToFront() {
		if (shapeSelected) {
			ArrayList<Shape> shapes = (ArrayList) model.getShapes();
			model.moveToFront(shapes.indexOf(curShapeSelected));
		}
	}

	@Override
	public void doSendtoBack() {
		if (shapeSelected) {
			ArrayList<Shape> shapes = (ArrayList) model.getShapes();
			model.movetoBack(shapes.indexOf(curShapeSelected));
		}
	}

}

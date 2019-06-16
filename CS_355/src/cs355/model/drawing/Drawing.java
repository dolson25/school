package cs355.model.drawing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs355.view.MyViewRefresher;

public class Drawing extends CS355Drawing{
	
	List<Shape> shapes;
	
	public Drawing()
	{
		shapes = new ArrayList<Shape>();
	}

	public void update(){
		this.setChanged();
		this.notifyObservers();
	}
	
	@Override
	public Shape getShape(int index) {
		return shapes.get(index);
	}

	@Override
	public int addShape(Shape s) {
		shapes.add(s);
		this.update();
		return 0;
	}

	@Override
	public void deleteShape(int index) {
		shapes.remove(index);
		this.update();
	}

	@Override
	public void moveToFront(int index) {
		Shape temp = shapes.remove(index);
		shapes.add(shapes.size(), temp);
		this.update();
	}

	@Override
	public void movetoBack(int index) {
		Shape temp = shapes.remove(index);
		shapes.add(0, temp);
        this.update();
	}

	@Override
	public void moveForward(int index) {
		
		if(index < shapes.size() -1 )
		{
			Collections.swap(shapes, index, index + 1);
            this.update();
		}		
	}

	@Override
	public void moveBackward(int index) {
		
		if(index > 0)
		{
			Collections.swap(shapes, index, index - 1);
            this.update();
		}	
	}

	@Override
	public List<Shape> getShapes() {
		// this can be sent to drawing or save file
		return shapes;
	}

	@Override
	public List<Shape> getShapesReversed() {
		return null;
	}

	@Override
	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
		this.setChanged();
		this.notifyObservers();
	}

}

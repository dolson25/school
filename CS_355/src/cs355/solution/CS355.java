package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.Controller;
import cs355.model.drawing.Drawing;
import cs355.view.MyViewRefresher;

import java.awt.*;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {

	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) {
		
		Drawing model = new Drawing();
		Controller controller = new Controller(model);

		// Fill in the parameters below with your controller and view.
		GUIFunctions.createCS355Frame(controller, new MyViewRefresher(model, controller));

        //initialize stuff
        GUIFunctions.changeSelectedColor(Color.WHITE);
        GUIFunctions.setVScrollBarMin(0);
        GUIFunctions.setVScrollBarMax(2048);
        GUIFunctions.setVScrollBarKnob(512);
        GUIFunctions.setVScrollBarPosit(0);
        GUIFunctions.setHScrollBarMin(0);
        GUIFunctions.setHScrollBarMax(2048);
        GUIFunctions.setHScrollBarKnob(512);
        GUIFunctions.setHScrollBarPosit(0);

		GUIFunctions.refresh();
	}
}

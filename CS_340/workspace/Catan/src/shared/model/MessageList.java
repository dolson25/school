package shared.model;

import java.util.ArrayList;

public class MessageList {
	
	private ArrayList<MessageLine> _lines;
	
	public MessageList(){
	}

	/*
	Adds a line to the list
	@param source The player doing the action or saying something
	@param message The thing being done or said
	 */
	public void addLine(String source, String message){}

	public ArrayList<MessageLine> getLines() {
		return _lines;
	}

	public void setLines(ArrayList<MessageLine> lines) {
		this._lines = lines;
	}

}

package client.visible.indexingWindow;

import java.awt.Dimension;
import client.notVisible.BatchState;
import client.visible.indexingWindow.bottomLeft.BottomLeft;
import client.visible.indexingWindow.bottomRight.BottomRight;
import javax.swing.JSplitPane;

public class FieldWindows extends JSplitPane {
	
	BatchState bs;
	
	public FieldWindows(BatchState bss)
	{
		bs = bss; 
		
		this.setOrientation(HORIZONTAL_SPLIT);
		
		BottomLeft bottomLeft = new BottomLeft(bs);
		
		this.setTopComponent(bottomLeft);
		
		BottomRight bottomRight = new BottomRight(bs);
		
		this.setBottomComponent(bottomRight);
		
		this.setPreferredSize(new Dimension(700,300));
		this.setMinimumSize(new Dimension(700,150));
		this.setResizeWeight(.5);
	}

}

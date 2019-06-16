package client.visible.indexingWindow;

import java.awt.Dimension;
import client.notVisible.BatchState;
import client.visible.indexingWindow.FieldWindows;
import javax.swing.JSplitPane;

public class ImageWindows extends JSplitPane {
	
	BatchState bs;
	ImageComponent ic;
	FieldWindows fw;
	public ImageWindows(BatchState bss)
	{
		bs = bss; 
		
		this.setOrientation(VERTICAL_SPLIT);
		
		ic = new ImageComponent(bs);

		this.setTopComponent(ic);
		
		fw = new FieldWindows(bs);
		
		this.setBottomComponent(fw);
		
		this.setDividerLocation(.75);
		
		this.setPreferredSize(new Dimension(700,400));
		this.setResizeWeight(.66);
	}
	
	public ImageComponent getImageComponent(){
		
		return ic;
	}
			
}

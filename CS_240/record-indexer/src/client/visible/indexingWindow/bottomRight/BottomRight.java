package client.visible.indexingWindow.bottomRight;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import client.notVisible.BatchState;
import client.notVisible.BatchStateListener;

public class BottomRight extends JTabbedPane implements BatchStateListener{
	
	BatchState bs;
	JPanel container;
	
	public BottomRight(BatchState bss)
	{
		bs = bss;
		bs.addListener(this);
		JEditorPane fieldHelp = new JEditorPane();
		JScrollPane fieldScroll = new JScrollPane(fieldHelp);
		container = new JPanel(); 
		container.setLayout(new BorderLayout());
		container.add(fieldScroll, BorderLayout.CENTER);
		
		this.addTab("Field Help", container);
		
		ImageViewer iViewer = new ImageViewer(bs);
		this.addTab("Image Navigation", iViewer);

		this.setPreferredSize(new Dimension(300,200));
		this.setMinimumSize(new Dimension(200,100));
	}
	
	public void selectedCellChanged(int row, int col)
	{	
		URL url = null;	
		
		try {
			JEditorPane fieldHelp = new JEditorPane();
			fieldHelp.setContentType("text/html");
			fieldHelp.setEditable(false);
			fieldHelp.setPreferredSize(new Dimension(250,145));
			fieldHelp.setVisible(true);
			JScrollPane fieldScroll = new JScrollPane(fieldHelp);

			
			fieldScroll.setVerticalScrollBarPolicy(
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			fieldScroll.setHorizontalScrollBarPolicy(
	        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

			fieldScroll.setPreferredSize(new Dimension(250, 145));
			fieldScroll.setMinimumSize(new Dimension(50, 50));
			url = new URL(bs.getBatchInfo().getFields().get(col).getFieldHelpPath());
			fieldHelp.setPage(url);
			container.removeAll();
			container.add(fieldScroll, BorderLayout.CENTER);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		this.validate();
		this.repaint();
	}
	public void valueChanged(int x, int y, String newValue)
	{
		
	}
	public void highlightsToggled()
	{
		
	}
	public void zoomChanged(double ratio)
	{
		
	}
	public void downloadBatch()
	{
		
	}
	public void submitBatch()
	{
		
	}
	public void imageInvert()
	{
		
	}

}

package client.visible.indexingWindow.bottomLeft;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import client.notVisible.BatchState;
import client.notVisible.BatchStateListener;

public class BottomLeft extends JTabbedPane implements BatchStateListener{
	
	BatchState bs;
	FormEntry formEntry;
	
	public BottomLeft(BatchState bss)
	{
		bs = bss; 
		bs.addListener(this);		
		
		this.addTab("Table Entry", new JPanel());
		this.addTab("Form Entry", new JPanel());	
		this.addChangeListener(stateChanged);
		this.setPreferredSize(new Dimension(300,200));
	}
	
	public void selectedCellChanged(int row, int col)
	{
		
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
		this.removeTabAt(1);
		this.removeTabAt(0);
		
		TablePanel tablePanel = new TablePanel(bs);
		this.addTab("Table Entry", tablePanel);
		
		formEntry = new FormEntry(bs);
		formEntry.addFocusListener(focusListener);
		this.addTab("Form Entry",formEntry);
		
	}
	public void submitBatch()
	{
		
	}
	public void imageInvert()
	{
		
	}
	
	FocusListener focusListener = new FocusListener(){

		@Override
		public void focusGained(FocusEvent e) {
			formEntry.focus();
	    			
	    	 }

		@Override
		public void focusLost(FocusEvent e) {
			
		}
		
		};


        ChangeListener stateChanged = new ChangeListener() {

			
			@Override
			public void stateChanged(ChangeEvent e) {
			        JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			        
			       if(tabbedPane.getSelectedIndex() == 1)
			    	   tabbedPane.getComponentAt(1).requestFocus();
			 }

           };

}

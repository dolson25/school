package client.visible.indexingWindow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import client.notVisible.BatchState;
import client.notVisible.BatchStateListener;
import client.visible.IndexerGUI;

public class ButtonPanel extends JPanel implements BatchStateListener{
	
	JButton zoomInButton;
	JButton zoomOutButton;
	JButton invertImageButton;
	JButton toggleHighlightsButton;
	JButton saveButton;
	JButton submitButton;
	BatchState bs;
	MainFrame mf;
	IndexerGUI mainClass;
	private String userName;
	private String password;
	
	public ButtonPanel(BatchState bss,MainFrame mff, IndexerGUI i, 
												String n, String p) {
		
		userName = n;
		password = p;
		
		mf = mff;
		bs = bss;
		mainClass = i;
		bs.addListener(this);
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setEnabled(false);
		for(Component c : this.getComponents()){
			
			c.setEnabled(false);
		}
		
	zoomInButton = new JButton("Zoom In");
	zoomInButton.addActionListener(actionListener);
	
	zoomOutButton = new JButton("Zoom Out");
	zoomOutButton.addActionListener(actionListener);
	
	invertImageButton = new JButton("Invert Image");
	invertImageButton.addActionListener(actionListener);
	
	toggleHighlightsButton = new JButton("Toggle Highlights");
	toggleHighlightsButton.addActionListener(actionListener);
	
	saveButton = new JButton("Save");
	saveButton.addActionListener(actionListener);
	
	submitButton = new JButton("Submit");
	submitButton.addActionListener(actionListener);

	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	this.add(Box.createRigidArea(new Dimension(5, 0)));
	this.add(zoomInButton);
	this.add(Box.createRigidArea(new Dimension(5, 0)));
	this.add(zoomOutButton);
	this.add(Box.createRigidArea(new Dimension(5, 0)));
	this.add(invertImageButton);
	this.add(Box.createRigidArea(new Dimension(5, 0)));
	this.add(toggleHighlightsButton);
	this.add(Box.createRigidArea(new Dimension(5, 0)));
	this.add(saveButton);
	this.add(Box.createRigidArea(new Dimension(5, 0)));
	this.add(submitButton);
	this.add(Box.createHorizontalGlue());
	}

private ActionListener actionListener = new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == zoomInButton) {
			
			bs.zoomChanged(1.5);
		}
		if (e.getSource() == zoomOutButton) {

			bs.zoomChanged(.75);
		}
		if (e.getSource() == invertImageButton) {

			bs.imageInvert();
		}
		if (e.getSource() == toggleHighlightsButton) {
			
			bs.highlightsToggled();
		}
		if (e.getSource() == saveButton) {
			bs.setFramePosition(getLocation());
			bs.setFrameSize(getSize());
            XMLEncoder encoder = null;
			try {
				encoder = new XMLEncoder(
				   new BufferedOutputStream(
				     new FileOutputStream(bs.getUserName())));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
                 encoder.writeObject(bs);
                 encoder.close();

		}
		if (e.getSource() == submitButton) {
			
			if(bs.getValues() != null)
			{
				bs.submitBatch();
				bs.setBatchAssigned(false);;
				
	            XMLEncoder encoder = null;
				try {
					encoder = new XMLEncoder(
					   new BufferedOutputStream(
					     new FileOutputStream(bs.getUserName())));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	                 encoder.writeObject(bs);
	                 encoder.close();
				mf.dispose();
				mainClass.login(userName, password);
				
			}
			else
			{
        		JOptionPane.showMessageDialog(null,"Download a batch first", "!",
					JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
  };
  
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
		
	}
	public void submitBatch()
	{
		
	}
	public void imageInvert()
	{
		
	}
}
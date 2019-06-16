package client.visible.indexingWindow.bottomLeft;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import client.notVisible.BatchState;
import client.notVisible.BatchStateListener;

public class FormEntry extends JPanel implements BatchStateListener{
	
	ArrayList<JPanel> panels;
	BatchState bs;
	JList recordList;
	ListModel listModel;
	JPanel rightPanel;
	JTextField tempField;

		
	public FormEntry(BatchState bss)
	{
		bs = bss;
		bs.addEmbeddedListener(this);
		
		this.setPreferredSize(new Dimension(300,200));
		this.setMinimumSize(new Dimension(150,150));
		
		panels = new ArrayList<JPanel>();
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setPreferredSize(new Dimension(300,300));
		rightPanel.setMinimumSize(new Dimension(300,100));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		for(int i=0; i<bs.getBatchInfo().getNumFields();i++)
		{
			JPanel tempPanel = new JPanel();
			tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
			tempPanel.setPreferredSize(new Dimension(300,30));
			tempPanel.setMinimumSize(new Dimension(200,30));
			tempPanel.setMaximumSize(new Dimension(500,30));
			
			
			JLabel tempText = new JLabel(bs.getBatchInfo().getFields().get(i).getTitle());
			tempText.setPreferredSize(new Dimension(100,30));
			tempText.setMinimumSize(new Dimension(100,30));
			tempText.setMaximumSize(new Dimension(150,30));
			tempField = new JTextField();
			tempField.setPreferredSize(new Dimension(111,30));
			tempField.setMaximumSize(new Dimension(200,30));
			tempField.setMinimumSize(new Dimension(50,30));
			tempField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			tempField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			tempField.setEditable(true);
			tempField.addFocusListener(focusListener);
			tempField.addKeyListener(keyListener);
			tempField.addKeyListener(keyListener);
			
			
			
			tempPanel.add(tempText);
			tempPanel.add(Box.createRigidArea(new Dimension(5,0)));
			tempPanel.add(tempField);
			
			rightPanel.add(tempPanel);
			panels.add(tempPanel);
			rightPanel.add(Box.createVerticalGlue());
		}
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		listModel = new ListModel();
		recordList = new JList(listModel);
		recordList.setPreferredSize(new Dimension(100,300));
		recordList.setMinimumSize(new Dimension(20,300));
		recordList.setMaximumSize(new Dimension(200,800));
		
		recordList.addMouseListener(mouseListener);
		
		JScrollPane newSP = new JScrollPane(recordList);
		
		newSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		newSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(newSP);

		JScrollPane newSP2 = new JScrollPane(rightPanel);
		newSP2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		newSP2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(newSP2);
		
	}
	
	private class ListModel extends AbstractListModel{

		public ListModel(){
			
		}
		@Override
		public int getSize() {
			
			return bs.getBatchInfo().getNumRecords();
		}

		@Override
		public Object getElementAt(int index) {
			return index + 1;
		}	
	}
	
	FocusListener focusListener = new FocusListener(){

		@Override
		public void focusGained(FocusEvent e) {
			
	    	 int i = 0;
	    	 for(int j = 0; j<panels.size();j++){
	    		 if(panels.get(j).getComponent(2).hasFocus())
	    		 {
	    			 i = j;
	    		 }
	    			
	    	 }

	          bs.selectedCellChanged(bs.getCellSelectedRow(), i);
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			JTextField temp = (JTextField)e.getSource();
			bs.valueChanged(bs.getCellSelectedRow(), bs.getCellSelectedColumn(),temp.getText() );
			
    		 if(!bs.getSpellBools()[bs.getCellSelectedRow()][bs.getCellSelectedColumn()])
    			 temp.setBackground(Color.RED);
    		 else
    			 temp.setBackground(Color.WHITE);
		}
		
	};
	
	
	
	public void focus(){

	           panels.get(bs.getCellSelectedColumn()).getComponent(2).requestFocus();
	           
	             int index = bs.getCellSelectedRow();
	             
	             recordList.setSelectedIndex(index);

	             
	             for(int i = 0;i<bs.getBatchInfo().getNumFields();i++){
	            	 
	            	  JTextField temp = (JTextField)panels.get(i).getComponent(2);
	            	  temp.setText(bs.getValues()[index][i]);
	            	  
	         		 if(!bs.getSpellBools()[index][i])
	        			 temp.setBackground(Color.RED);
	         		 else
	         			 temp.setBackground(Color.WHITE);
	             }
		
	}
	
	 MouseAdapter mouseListener = new MouseAdapter() {
	     public void mouseClicked(MouseEvent e) {
	        
	             int index = recordList.locationToIndex(e.getPoint());
	             
	             
	             bs.selectedCellChanged(index, bs.getCellSelectedColumn());
	             
	             for(int i = 0;i<bs.getBatchInfo().getNumFields();i++){
	            	 
	            	  JTextField temp = (JTextField)panels.get(i).getComponent(2);
	            	  temp.setText(bs.getValues()[index][i]);
	            	  
	            	  if(i == bs.getCellSelectedColumn())
	            		  temp.requestFocus();
	            	  
		         		 if(!bs.getSpellBools()[index][i])
		        			 temp.setBackground(Color.RED);
		         		 else
		         			 temp.setBackground(Color.WHITE);
	             }
	             
	             
	          }
	 };
	
	public void selectedCellChanged(int row, int col)
	{
		recordList.setSelectedIndex(row);
		panels.get(col).getComponent(2).requestFocus();
		
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
	private KeyListener keyListener = new KeyListener(){

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			JTextField temp = (JTextField)e.getSource();
			temp.repaint();
			temp.validate();
			temp.postActionEvent();
			bs.valueChanged(bs.getCellSelectedRow(), bs.getCellSelectedColumn(), temp.getText());
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}
		
	};	

}



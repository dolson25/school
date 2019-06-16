package client.visible.indexingWindow.bottomLeft;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import shared.communication.ValidateUserOutput;
import client.notVisible.BatchState;
import client.notVisible.ClientFacade;

public class SuggestionDialog extends JDialog {
	
	
	JButton cancelButton;
	JButton useSuggestionButton; 
	BatchState bs;
	ListModel listModel;
	
	
	public SuggestionDialog(BatchState bss){
		
		bs = bss;
		this.setTitle("Suggestions");
		this.setResizable(false);
		this.setModal(true);	
		this.setLocation(600,450);
		
		
		listModel = new ListModel();
		JList suggestions = new JList(listModel);
		suggestions.setPreferredSize(new Dimension(100,300));
		suggestions.setMinimumSize(new Dimension(20,300));
		suggestions.setMaximumSize(new Dimension(200,800));
		JScrollPane sp = new JScrollPane(suggestions);
		

		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(actionListener);
		useSuggestionButton = new JButton("useSuggestion");
		useSuggestionButton.addActionListener(actionListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPanel.add(useSuggestionButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		
		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.add(sp);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rootPanel.add(buttonPanel);
		
		this.add(rootPanel);
		this.pack();	
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cancelButton) {
				dispose();
			}
			if (e.getSource() == useSuggestionButton) {
				
			}
		}
	};
	
	private class ListModel extends AbstractListModel{

		public ListModel(){
			
		}
		@Override
		public int getSize() {
			
			return bs.getWords().get(bs.getCellSelectedRow()).
					get(bs.getCellSelectedColumn()).size();
		}

		@Override
		public Object getElementAt(int index) {
			System.out.println("here");
			TreeSet<String> temp = bs.getWords().get(bs.getCellSelectedRow()).
					get(bs.getCellSelectedColumn());
			int i = 0;
			Object o = null;
			for(String s : temp)
			{
				if(index == i)
					o = (Object)s;
			}
			
			return o;
		}	
	}

}

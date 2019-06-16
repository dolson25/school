package client.visible.indexingWindow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import shared.communication.DownloadBatchOutput;
import shared.communication.GetSampleImageOutput;
import client.notVisible.BatchState;
import client.notVisible.ClientFacade;

public class DownloadBatchDialog extends JDialog {
	
	private JComboBox projects;
	private JButton viewSampleButton;
	private JButton cancelButton;
	private JButton downloadButton;
	BatchState bs;
	MainFrame mf;
	ArrayList<JMenuItem> items = null;
	ButtonPanel bp;

	public DownloadBatchDialog(BatchState bss, MainFrame mff, ButtonPanel bpp) {

		bs = bss;
		mf = mff;
		bp = bpp;
		this.setTitle("Download Batch");
		setModal(true);	
		this.setLocation(600,450);
		this.setPreferredSize(new Dimension(600,75));
		this.setMaximumSize(new Dimension(600,75));
		
		

		
		JPanel projectsPanel = new JPanel();
		JLabel projectsLabel = new JLabel("Projects: ");
		
		projects = new JComboBox();
		projects.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		projects.addActionListener(actionListener);
		
		ArrayList<ArrayList<String>> projectInfo = bs.getProjects();
		
		for(int i = 0; i < projectInfo.size(); i++)
		{
			projects.addItem(projectInfo.get(i).get(1));
		}
		
		viewSampleButton = new JButton("View Sample");
		viewSampleButton.addActionListener(actionListener);

		projectsPanel.setLayout(new BoxLayout(projectsPanel, BoxLayout.X_AXIS));
		projectsPanel.add(projectsLabel);
		projectsPanel.add(Box.createRigidArea(new Dimension(0,5)));
		projectsPanel.add(projects);
		projectsPanel.add(Box.createRigidArea(new Dimension(0,5)));
		projectsPanel.add(Box.createRigidArea(new Dimension(0,5)));
		projectsPanel.add(Box.createRigidArea(new Dimension(0,5)));
		projectsPanel.add(viewSampleButton);
	
		
		
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(actionListener);
		downloadButton = new JButton("Download");
		downloadButton.addActionListener(actionListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPanel.add(downloadButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		

		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.add(projectsPanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rootPanel.add(buttonPanel);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rootPanel.setPreferredSize(new Dimension(600,75));
		rootPanel.setMaximumSize(new Dimension(600,75));
		
		
		
		this.add(rootPanel);
		this.setResizable(false);
		this.pack();
	}
	
	public int getProjectID(String title)
	{
		ArrayList<ArrayList<String>> projects = bs.getProjects();

		int i = 0;
		while(true)
		{
			if(projects.get(i).get(1).equals(title))
			{
				return Integer.valueOf(projects.get(i).get(0));
			}
			
			i++;
		}
				
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == viewSampleButton) {
				
	        	String title = (String)projects.getSelectedItem();
	        	
	        	int project_id = getProjectID(title);
	        	
	        	GetSampleImageOutput sampleImage = ClientFacade.getSampleImage(
	        			bs.getUserName(), bs.getPassword() ,project_id);
	        	
	        	SampleImagePopup sample = new SampleImagePopup(sampleImage.getFilepath()
	        															,title);
	        	sample.setVisible(true);
			}
			
			if (e.getSource() == cancelButton) {
					dispose();
			}
			if (e.getSource() == downloadButton) {
				
	        	String title = (String)projects.getSelectedItem();

	        	int project_id = getProjectID(title);
	        	DownloadBatchOutput availableImage = ClientFacade.downloadBatch(
	        			bs.getUserName(), bs.getPassword(), project_id);
	        	
	        	
	        	if(availableImage == null)
	        		JOptionPane.showMessageDialog(null,"This project is fully indexed", 
	        				"Try Again", JOptionPane.INFORMATION_MESSAGE);
	        	else
	        	{
	        		bs.setBatchInfo(availableImage);
	        		mf.downloadBatch(availableImage.getImageURL());
	        		
	        		bs.setBatchInfo(availableImage);
	        		bs.selectedCellChanged(bs.getCellSelectedRow(), bs.getCellSelectedColumn());
	        		bp.setEnabled(true);
	        		mf.disableDownloadBatch();

	        		
	        		String[][] values = new String[bs.getBatchInfo().getNumRecords()]
	        				[bs.getBatchInfo().getNumFields()];
	        		boolean[][] spellBools = new boolean[bs.getBatchInfo().getNumRecords()]
	        				[bs.getBatchInfo().getNumFields()];
	        		ArrayList<ArrayList<TreeSet<String>>> words = 
	        				new ArrayList<ArrayList<TreeSet<String>>>();
	        		
	        		for(int i=0; i<bs.getBatchInfo().getNumRecords();i++)
	        		{
	        			ArrayList<TreeSet<String>> w = new ArrayList<TreeSet<String>>();
	        			for(int j=0; j<bs.getBatchInfo().getFields().size(); j++)
	        			{
	        				
	        				values[i][j] = "";
	        				spellBools[i][j] = true;
	        				w.add(new TreeSet<String>());
	        				
	        			}
	        			
	        			words.add(w);
	        		}
	        		
	        		bs.setValues(values);  
	        		bs.setSpellBools(spellBools);
	        		bs.setWords(words);
	        		bs.setBatchAssigned(true);
	        		bs.downloadBatch();
	        		
	        		dispose();
	        	}

	        }
		}
	};

}

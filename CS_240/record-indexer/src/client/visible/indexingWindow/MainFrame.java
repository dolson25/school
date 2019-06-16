package client.visible.indexingWindow; 

import java.awt.*;
import java.awt.event.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.*;
import shared.communication.GetProjectsOutput;
import client.notVisible.BatchState;
import client.notVisible.ClientFacade;
import client.visible.IndexerGUI;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private JMenuItem downloadBatchMenuItem;
    private JMenuItem logoutMenuItem;
    private JMenuItem exitMenuItem;
    private String userName;
    private String password;
    IndexerGUI mainClass;
    BatchState bs;
    DownloadBatchDialog db;
    ImageWindows imageWindows;
    ButtonPanel buttonPanel;
    MainFrame mf;
    
    
    public MainFrame(String title, IndexerGUI i, String n, String p) {
      
    	super(title);
    	mainClass = i;
    	mf = this;
    	userName = n;
    	password = p;
    	
        XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(
			    new FileInputStream(userName)));
            bs = (BatchState)decoder.readObject();  
             decoder.close();
            
			createComponents();

			if(bs.isBatchAssigned())
			{
				this.downloadBatch(bs.getBatchInfo().getImageURL());
				bs.selectedCellChanged(bs.getCellSelectedRow(), bs.getCellSelectedColumn());
				bs.downloadBatch();
				this.disableDownloadBatch();
			}
			
			mf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mf.pack();
	        mf.setVisible(true);
         
            
		} catch (FileNotFoundException e) {
			bs = new BatchState();
			bs.setUserName(n);
			bs.setPassword(p);
	    	GetProjectsOutput projects = ClientFacade.getProjects(userName,password);
	    	bs.setProjects(projects.getProjects());
			createComponents();
			mf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mf.pack();
	        mf.setVisible(true);
		} 
		finally
		{

		}

    }

    private void createComponents() {
    	this.setLocation(250,150);
        addWindowListener(windowAdapter);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menu.setMnemonic('c');
        menuBar.add(menu);

        downloadBatchMenuItem = new JMenuItem("Download Batch", KeyEvent.VK_B);
        downloadBatchMenuItem.addActionListener(actionListener);
        menu.add(downloadBatchMenuItem);

        logoutMenuItem = new JMenuItem("Logout", KeyEvent.VK_F);
        logoutMenuItem.addActionListener(actionListener);
        menu.add(logoutMenuItem);

        exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_A);
        exitMenuItem.addActionListener(actionListener);
        menu.add(exitMenuItem);
        
        

        
        buttonPanel = new ButtonPanel(bs,this, mainClass, userName, password);
        
        imageWindows = new ImageWindows(bs);

        
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(buttonPanel, BorderLayout.NORTH);
        rootPanel.add(imageWindows , BorderLayout.CENTER);
        rootPanel.setPreferredSize(new Dimension(1200,750));

        this.add(rootPanel);
    }
    
    public void downloadBatch(String url){
    	
    	imageWindows .getImageComponent().downloadBatch(url);
    }
    
    public void disableDownloadBatch(){
    	
    	downloadBatchMenuItem.setEnabled(false);
    }
    
    private WindowAdapter windowAdapter = new WindowAdapter() {
    	
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    };

    private ActionListener actionListener = new ActionListener() {
    	
	    public void actionPerformed(ActionEvent e) {
	    	
	        if (e.getSource() == downloadBatchMenuItem) {

	        	db = new DownloadBatchDialog(bs,MainFrame.this,buttonPanel);
	        	db.setVisible(true);
	        }
	        else if (e.getSource() == logoutMenuItem) {
	        	
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
	            dispose();
	        	mainClass.logout();
	        	
	        }
	        else if (e.getSource() == exitMenuItem) {
	        	mainClass.exitProgram();
	        }
	    }
    };
}

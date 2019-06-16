package client.visible;

import client.notVisible.ClientFacade;
import client.visible.indexingWindow.MainFrame;

public class IndexerGUI {
	 
	
	LoginDialog ld;
	MainFrame mf;
	
	public IndexerGUI(){
			
		logout();
	}


    public static void main(final String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	ClientFacade.setHost(args[0]);
            	ClientFacade.setPortNumber(args[1]);
            	new IndexerGUI();
            }
        });
    }
    
    public void login(String userName, String password)
    {
    	mf = new MainFrame("Indexer", this, userName, password);
    	mf.setVisible(true);
    }
    
    public void logout()
    {
    	ld = new LoginDialog(true,this);
    	ld.setVisible(true);
    }
    
    public void exitProgram(){
    	
    	System.exit(1);
    }
}

package client.visible;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.notVisible.ClientFacade;
import client.visible.IndexerGUI;
import shared.communication.ValidateUserOutput;



@SuppressWarnings("serial")
public class LoginDialog extends JDialog {

	
	private JTextField nameTextField;
	private JPasswordField passwordTextField;
	private JButton loginButton;
	private JButton exitButton;
	private IndexerGUI mainClass;

	public LoginDialog(boolean b, IndexerGUI i) {
		
		mainClass = i;
		this.setTitle("Login to Indexer");
		this.setResizable(false);
		this.setModal(b);	
		this.setLocation(600,450);
		
		
		JPanel usernamePanel = new JPanel();
		JLabel nameLabel = new JLabel("Username: ");
		nameTextField = new JTextField();
		nameTextField.setPreferredSize(new Dimension(400,30));
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		usernamePanel.add(nameLabel);
		usernamePanel.add(Box.createRigidArea(new Dimension(0,5)));
		usernamePanel.add(nameTextField);

		
		
		JPanel passwordPanel = new JPanel();
		JLabel passwordLabel = new JLabel("Password: ");
		passwordTextField = new JPasswordField();
		passwordTextField.setPreferredSize(new Dimension(400,30));
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(Box.createRigidArea(new Dimension(0,5)));
		passwordPanel.add(passwordTextField);

	
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(actionListener);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(actionListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(loginButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		
		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.add(usernamePanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rootPanel.add(passwordPanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rootPanel.add(buttonPanel);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		
		this.add(rootPanel);
		this.pack();
	}
	
	public void clearFields(){
		
		passwordTextField.setText("");
		nameTextField.setText("");
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton) {
				ValidateUserOutput output = ClientFacade.validateUser(
						nameTextField.getText(), passwordTextField.getText());
				if(output == null)
				{
					JOptionPane.showMessageDialog(null,"invalid username or password", 
							"login failed", JOptionPane.ERROR_MESSAGE);
				}
				else if(output.isValidated())
				{
					JOptionPane.showMessageDialog(null,"Welcome, " + output.getFirstname() + 
							" " + output.getLastname() + ".\nYou have indexed " + 
							output.getNumberRecordsIndexed() + " records.", "Welcome to Indexer",
							JOptionPane.PLAIN_MESSAGE);
					
					dispose();
					
				    mainClass.login(nameTextField.getText(),passwordTextField.getText());
				}
				else
				{
					passwordTextField.setText("");
					
					JOptionPane.showMessageDialog(null,"invalid username or password", 
							"login failed", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (e.getSource() == exitButton) {
				mainClass.exitProgram();
			}
		}
	};

}

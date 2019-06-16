package client.visible.indexingWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SampleImagePopup extends JDialog {
	
	JButton closeButton;
	
	public SampleImagePopup(String url, String title){

		this.setTitle("Sample image from " + title);
		this.setResizable(false);
		this.setModal(true);	
		this.setLocation(600,450);	
		
		BufferedImage samplePicture = null;;
		try {
			samplePicture = ImageIO.read(new URL(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int imageHeight = samplePicture.getHeight();
		int imageWidth = samplePicture.getWidth();
		Image scaled = samplePicture.getScaledInstance(imageWidth/2, imageHeight/2, 1);
		this.setPreferredSize(new Dimension(imageWidth/2,imageHeight/2 + 50));
		JLabel pictureLabel = new JLabel(new ImageIcon(scaled));
		pictureLabel.setPreferredSize(new Dimension(imageWidth/2,imageHeight/2));
		
		

		
		closeButton = new JButton("Close");
		closeButton.addActionListener(actionListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(closeButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.setPreferredSize(new Dimension(500,50));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(pictureLabel, BorderLayout.CENTER);
		rootPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		
		this.add(rootPanel);
		this.pack();
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == closeButton) {

				dispose();
			}
		}
	};

}

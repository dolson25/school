package client.visible.indexingWindow.bottomRight;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import client.notVisible.BatchState;

import java.util.*;
import java.io.*;
import java.net.URL;


@SuppressWarnings("serial")
public class ImageViewer extends JComponent {
	
	private int w_translateX;
	private int w_translateY;
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;

	private ArrayList<DrawingShape> shapes;
	private BatchState bs;
	
	public ImageViewer(BatchState bss) {
		
		bs = bss;
		
		w_translateX = 0;
		w_translateY = 0;
		scale = 1.0;
		
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		
		this.setBackground(new Color(111, 111, 111));
		this.setPreferredSize(new Dimension(300,200));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
	}
	
	public void downloadBatch(String url){
		System.out.println(url);
		
		BufferedImage indexImage = null;;
		try {
			indexImage= ImageIO.read(new URL(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int imageHeight = indexImage.getHeight();
		int imageWidth = indexImage.getWidth();
		

		Image scaled = indexImage.getScaledInstance(imageWidth/2, imageHeight/2, 1);
		this.setPreferredSize(new Dimension(imageWidth/2,imageHeight/2 + 50));
		JLabel pictureLabel = new JLabel(new ImageIcon(scaled));
		pictureLabel.setPreferredSize(new Dimension(imageWidth/2,imageHeight/2));
		shapes.add(new DrawingImage(scaled, new Rectangle2D.Double(350, 50, 
										scaled.getWidth(null), scaled.getHeight(null))));
		
		bs.setCellSelectedRow(3);
		bs.setCellSelectedColumn(1);
										
		
		shapes.add(new DrawingRect(new Rectangle2D.Double(
				350 + bs.getBatchInfo().getFields().get(bs.getCellSelectedColumn()).getXCoordinate() / 2,
				50 + bs.getBatchInfo().getFirstYCoordinate() / 2 
						+ bs.getCellSelectedRow() * bs.getBatchInfo().getRecordHeight() / 2,
				bs.getBatchInfo().getFields().get(bs.getCellSelectedColumn()).getWidth() / 2,
				bs.getBatchInfo().getRecordHeight()/2), 
				new Color(210, 180, 140, 192)));
		
		repaint();
	}
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}
	
	public void setScale(double newScale) {
		scale = newScale;
		this.repaint();
	}
	
	public void setTranslation(int w_newTranslateX, int w_newTranslateY) {
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);

		g2.scale(scale, scale);
		g2.translate(w_translateX, w_translateY);

		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			shape.draw(g2);
		}
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.scale(scale, scale);
			transform.translate(w_translateX, w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					hitShape = true;
					break;
				}
			}
			
			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			return;
		}	
	};
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {

		@Override
		public void componentHidden(ComponentEvent e) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			return;
		}

		@Override
		public void componentResized(ComponentEvent e) {
			return;
		}

		@Override
		public void componentShown(ComponentEvent e) {
			return;
		}	
	};

	
	/////////////////
	// Drawing Shape
	/////////////////
	
	
	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}


	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}


	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}
}

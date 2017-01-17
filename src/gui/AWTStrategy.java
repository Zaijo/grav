package gui;

import gameObjects.Position;
import gameObjects.Vektor;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JOptionPane;

import main.Game;


public class AWTStrategy extends WindowStrategy{
	Game game;
	MyCanvas c;
	Frame f;
	
	public AWTStrategy(Game game, int width, int height){
		// uchovaj si instanciu hry aby sa dala neskor zastavit ovladacimi prvkami
		this.game = game;
		
		// kresliaca plocha
		boolean shadeBuffer = false;
		c = new MyCanvas(width, height, shadeBuffer);
		c.addMouseListener(new mouseLauncher());
		c.addMouseWheelListener(new WheelListener());
		Klavesak klavesak = new Klavesak();
		//c.addKeyListener(klavesak);
		
		// komponenty Frameu
		f = new Frame("Window");
		f.add(c, BorderLayout.CENTER);
		
		// EVERY COMPONENT LISTENES FOR KEYS
		f.addKeyListener(klavesak);
		int i = 0;
		while(true){
			try{
				f.getComponent(i++).addKeyListener(klavesak);
			}
			catch(Exception e){
				break;
			}
		};
		
		// zatvorenie Frameu pri kliknuti na X
		f.addWindowListener(new CloseWindowAdapter());
		
		// ked je vsetko pripravene ulozim Grafiku
		g = c.gBuf; // buffer
	}
	
	public void start(){
		f.validate();
		f.pack();
		f.setVisible(true);
	}
	
	class PauseListner implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			game.pause = true;
		}
	}
	
	class ResumeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			game.pause = false;
		}
		
	}
	
	class WheelListener implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		       String message = "";
		       int notches = e.getWheelRotation();
		       if (notches < 0) {
		           //message = "Mouse wheel moved UP "+ -notches + " notch(es)\n";
		    	   game.zoomRelatively(-notches, new Position(e.getPoint()));
		       } else {
		           //message = "Mouse wheel moved DOWN " + notches + " notch(es)\n";
		    	   game.zoomRelatively(-notches, new Position(e.getPoint()));
		       }
		       if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
		           message += "    Scroll type: WHEEL_UNIT_SCROLL\n";
		       } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
		           message += "    Scroll type: WHEEL_BLOCK_SCROLL\n";
		       }
		       //JOptionPane.showMessageDialog(null, message);
		    }
		
	}
	
	class CloseWindowAdapter extends WindowAdapter {
	    @Override
	    public void windowClosing(WindowEvent a){
			boolean blabla = game.stop(); // if not catching the return value, the next clause might be executed before game stops, causing error
			if(blabla)
				f.dispose();
			System.exit(0);
	    } 
	}
	
	class Klavesak implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			final double slowKvocient = 3f/2f; 
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				game.nextFocus();
			else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				game.previousFocus();
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE){
				game.pause = !game.pause;
			}
			else if(e.getKeyCode() == KeyEvent.VK_O){
				game.showHelp = !game.showHelp;
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP){
				//game.pause = !game.pause;
				game.slowRelatively(1/slowKvocient);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				//game.pause = !game.pause;
				game.slowRelatively(slowKvocient);
			}
			else if(e.getKeyCode() == KeyEvent.VK_I){
				//game.pause = !game.pause;
				game.showInfo= !game.showInfo;
			}
			else if(e.getKeyCode() == KeyEvent.VK_T){
				//game.pause = !game.pause;
				game.showTrail = !game.showTrail;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class clickNext extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			game.tick();
			game.draw();
		}
	}
	
	class mouseLauncher extends MouseAdapter{
		Position first;
		
		@Override
		public void mousePressed(MouseEvent e) {
			this.first = new Position(e.getX(), e.getY());
			//game.setStartPosition(first);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(!this.first.equals(new Position(e.getX(), e.getY())))
				game.setVector(Vektor.getVector(new Position(e.getX(), e.getY()), first));
		}
		
	}
	
	@Override
	public void update() {
		c.update();
	}

	public Dimension getDim(){
		return new Dimension(c.getWidth(), c.getHeight());
	}
	
	class MyCanvas extends DoubleBuffered{
		// serializable requires this
		private static final long serialVersionUID = -4024455323912690722L;

		public BufferedImage shadeBuffer = null;
		public Graphics2D shadeGraphics = null;
		boolean useShadeBuffer;
		RescaleOp rop;
		
		public MyCanvas(){
			this(200, 200, false); // default dimensions
		}
		
		public MyCanvas(int width, int height){
			this(width, height, false); // default dimensions
		}
		
		public MyCanvas(int width, int height, boolean shade){
			super(width, height);
			setSize(width, height);
			clear();
			useShadeBuffer = shade;
			if(useShadeBuffer){
				shadeBuffer = new BufferedImage(width, height,	BufferedImage.TYPE_INT_ARGB);
				shadeGraphics = shadeBuffer.createGraphics();
				// antialiasing
				shadeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
				        RenderingHints.VALUE_ANTIALIAS_ON);
//				shadeGraphics.setColor(Color.blue);
//				shadeGraphics.fillRect(10, 10, 20, 20);
				//gBuf.drawImage(shadeBuffer, 0, 0, null);
			}
		}
		
		public void shadeFade(){
			shadeFade(10);
		}
		
		public void shadeFade(float r){
			if(useShadeBuffer){
				
				 // POKUS O POSTUPNY FADE
				float[] scale = {1f, 1f, 1f, 1f};
				float[] offset = {r, r, r, r};
				//offset[3] = - percent;
				rop = new RescaleOp(scale, offset, null);
				rop.filter(shadeBuffer, shadeBuffer);
				//shadeGraphics.drawImage(shadeBuffer, rop, 0, 0);
				
				 
				shadeGraphics.setColor(new Color(255, 255, 255, Math.round((float)(255 / 100) * r)));
				shadeGraphics.fillRect(0, 0, getWidth(), getHeight());
			}
		}
		
		public void shadeUpdate(){
			if(useShadeBuffer){
				shadeGraphics.drawImage(buf, null, 0, 0);
				shadeFade(10);
			}
			
		}
		
		//Override
		public void update(){
			shadeUpdate();
			super.update(); // vykreslenie von
			clear();		// vybielenie buffera
			if(useShadeBuffer){
				gBuf.drawImage(shadeBuffer, rop, 0, 0);
			}
			
		}
		
		public void clear(){
			// vycistenie buffera
//			gBuf.setColor(new Color(0,0,0,0));
			gBuf.setColor(Color.black);
			gBuf.fillRect(0, 0, getWidth(), getHeight());
			gBuf.setColor(Color.white);
		}
	}
	
	public class DoubleBuffered extends Canvas{
		// serializable requires this
		private static final long serialVersionUID = -2220490173047747563L;
		
		public BufferedImage buf;
		public Graphics2D gBuf;
		
		public DoubleBuffered(int width,int height) {
			
			// buffer
			buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			// buffer observer
			gBuf = buf.createGraphics();
			
			// antialiasing
			gBuf.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
			        RenderingHints.VALUE_ANTIALIAS_ON);
			
			gBuf.setColor(Color.white);
		}

		public void update(){
			paint(this.getGraphics());
		}
		
		public void paint(Graphics g){
			g.drawImage(buf, 0, 0, this);
		}
	}


}
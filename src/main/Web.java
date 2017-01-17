package main;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class Web extends Applet {
	
	private static final long serialVersionUID = 1L;
	int i;
	
	Game game;
	public void init(){
		
		game = new Game(getWidth(), getHeight());
		game.start();
		setBackground(Color.white);
		repaint();
	}
	
	public void paint( Graphics g){
		g.setColor(Color.black);
		g.drawLine(0, 0, 10, 10);
		g.drawString("HelloWorld", 10, 10);
	}
	
}

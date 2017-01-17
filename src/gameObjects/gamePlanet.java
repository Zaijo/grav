package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import main.Game;

public class gamePlanet extends gameMovingObject {
	private double diameter; // v km. Normuje sa konstantou
	public double mass; // kolko planet treba aby vyvazili slnko?
	public String name;
	
	public final int recentLocationsBufferSize = 100; // trail length
	Queue<Position> recentLocations = new LinkedList<Position>();
	private int locationStepper;

	public String getName() {
		return name;
	}

	public void setFVector(Vektor v){
		setVector(v.multiply(0.5));
	}

	public void setName(String name) {
		this.name = name;
	}


	public double getDiameter() {
		return diameter;
	}


	public double getMass() {
		return mass;
	}


	public gamePlanet(double x, double y) {
		super(x, y);
		this.diameter = 10; // default
	}


	// v pripade zavadzania polomeru
	public gamePlanet(String name, double x, double y, double diam, double weight) {
		this(x, y);
		setName(name);
		this.diameter = diam;
		this.mass = weight * 1000;
	}
	
	public gamePlanet(Position position) {
		this(position.x, position.y);
	}

	@Override
	public void draw(Graphics g) {
		// drawingPosition
		
		ViewSettings view = new ViewSettings(game.view); // kopia aby boli vsade rovnake vlastnosti
		Position pos = Position.getDrawingPosition(this.getPosition(), view);
		
		// PLANETA
		//pixNorm = game.view.getZoom() / game.AU; // kolko pixelov je jedna AU?
		pixNorm =  view.getZoom() / Game.AU * 10000;
		g.drawArc(pos.igetX() - (int)Math.round(this.diameter * pixNorm / 2), pos.igetY() - (int)Math.round(this.diameter * pixNorm / 2), (int)Math.round(this.diameter * pixNorm), (int)Math.round(this.diameter * pixNorm), 0, 360);
		// BODKA
		//g.drawLine(pos.igetX(), pos.igetY(), pos.igetX(), pos.igetY());
		// NAZOV
		if(this ==  game.getFocusPlanet()){
			g.setColor(Color.GREEN);
		}
		g.drawString(name, pos.igetX() + (int)Math.round(this.diameter * pixNorm / 2), pos.igetY() - (int)Math.round(this.diameter * pixNorm / 2));
		g.setColor(Color.WHITE);
		if(true){
			addRecentLocation(getPosition());
			drawTrail(g, view);
		}
	}
	
	private Color fade(int i, int n){
		int d = 255 - Math.round((n - i) * 255 / n);
		return new Color(d, d, d);
	}
	
	private void drawTrail(Graphics g, ViewSettings view){	
		Position first = null;
		int n = (game.showTrail?recentLocations.size():1), i = 0;
		for(Position secondOrig: recentLocations){
		    	if(++i > n)
		    	    break;
			Position second = (Position) secondOrig.clone();
			if(first != null){
				if(view != null){
					first = Position.getDrawingPosition(first, view);
					second = Position.getDrawingPosition(second, view);
				}
				g.setColor(fade(i, n));
				Game.drawLine(g, first, second);
			}
			first = (Position) secondOrig.clone();
		}
		g.setColor(Color.white);
	}
	
	public void addRecentLocation(Position p){
		//if((locationStepper++ % 200 ) == 0){
			if(recentLocations.size() >= recentLocationsBufferSize){
				recentLocations.remove();
			}
			recentLocations.add((Position)(p.clone()));
		//}
	}

}

package gameObjects;

import java.awt.Graphics;

public abstract class gameObject extends Thing{
	private Position position;

	public gameObject(Position position) {
		super();
		this.position = position;
	}
	
	public gameObject(double x, double y){
		this(new Position(x, y));
	}

	public abstract void draw(Graphics g);
	
	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
	
	// relative position move
	public void setPositionRel(Position position) { 
		this.position.add(position); // vector addition
	}
	
}

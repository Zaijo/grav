package gameObjects;

import java.awt.geom.Point2D;

public class Position extends Point2D.Double{
	private static final long serialVersionUID = -5539571623781008251L;

	public Position(Point2D p){
		super(p.getX(), p.getY());
	}
	
	public Position(double x, double y){
		super(x, y);
	}
	
	public void add(Position p){
		this.x += p.x;
		this.y += p.y;
	}
	
	public int igetX(){
		return (int)Math.round(this.getX());
	}
	
	public int igetY(){
		return (int)Math.round(this.getY());
	}
	
	public static Position getDrawingPosition(final Position pos, double k, double qx, double qy){
		Vektor tmp = new Vektor(pos);
		tmp.add((Position)(new Vektor(qx, qy)).multiply(-1));
		tmp.multiply(k);
		
		return (Position) tmp.clone();
	}
	
	public static Position getDrawingPosition(final Position pos, ViewSettings view){
		return getDrawingPosition(pos, view.getZoom(), view.getOffsetX(), view.getOffsetY());
	}
}

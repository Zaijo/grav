/*	
	This file is part of Erublast.

    Erublast is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Erublast is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package gameObjects;

import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Vektor extends Position{
	private static final long serialVersionUID = 5775954898348560735L;

	public Vektor(Point2D p) {
		super(p);
	}
	
	public Vektor(Position p){
		this(p.x, p.y);
	}

	public Vektor(double x, double y) {
		super(x, y);
	}

	public static Vektor sum(Vektor p1, Vektor p2){
		return new Vektor(p1.x + p2.x, p1.y + p2.y);
	}
	
	public static Vektor sum(Position p1, Vektor p2){
		return sum(new Vektor(p1), p2);
	}
	
	public Vektor multiply(double f){
		this.x *= f;
		this.y *= f;
		return this;
	}
	
	public static double crossProduct(Vektor p1, Vektor p2){
		return p1.x * p2.y - p1.y * p2.x;
	}
	
	public static Vektor getVector(Point2D.Double a, Point2D.Double b){
		return new Vektor(b.x - a.x, b.y - a.y);
	}
	
	public void drawAsVectorFrom(Position p, Graphics g){
		g.drawLine(p.igetX(),p.igetY(), (int)(Math.round(p.getX() + this.getX())), (int)Math.round(p.getY() + this.getY()));
	}
	
	public double getVectorSizeSq(){
		return Math.pow(this.x, 2) + Math.pow(this.y, 2);
	}
	
	public double getVectorSize(){
		return Math.sqrt(this.getVectorSizeSq());
	}
	
	public String getVectorString(){
		return this.toString() + " length: [" + this.getVectorSize() + "]";
	}
	
	public void setVectorSize(double newSize){
		if(!this.equals(new Vektor(0, 0)))
			this.multiply(Math.sqrt(Math.pow(newSize, 2) / (Math.pow(this.x, 2) + Math.pow(this.y, 2))));
	}
	
	public Vektor decompositionInDirection(Vektor dv){
		// get normal vector of direction vector
		Vektor nv = new Vektor(dv.getY(), - dv.getX());

		// fix the direction so that the normal vector points to opposite side than this
		if (Math.signum(Vektor.crossProduct(dv, nv)) == Math.signum(Vektor.crossProduct(dv, this))) {
			nv.multiply(-1f);
		}
		
		nv.setVectorSize(Line2D.Double.ptLineDist(0, 0, dv.getX(), dv.getY(), this.getX(), this.getY()));
		
		return Vektor.sum(nv, this);
	}
	
	public static Vektor createVector(Position p1, Position p2){
		if(p1.equals(p2)){
			return new Vektor(0, 0);
		}
		else
			return new Vektor(p2.x - p1.x , p2.y - p1.y);
	}
	
	public Line2D.Double createLine(Position point){
		return new Line2D.Double(point, this);
	}
}

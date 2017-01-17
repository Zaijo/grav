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

public class movingSquare extends gameMovingObject implements Obstacle{
	public int halfTheSize = 5;
	
	@Override
	public void draw(Graphics g){
		g.drawRect(this.getPosition().igetX() - halfTheSize, this.getPosition().igetY() - halfTheSize, halfTheSize * 2, halfTheSize * 2);
//		Vektor copy = (Vektor)this.getVector().clone();
//		copy.multiply(10).drawAsVectorFrom(this.getPosition(), g);
//		g.setColor(Color.red);
	}

	public movingSquare(){
		this(10, 10, 1, 1);
	}
	
	public movingSquare(double x, double y){
		this(x, y, 0, 0);
	}
	
	public movingSquare(double x, double y, double dx, double dy){
		super(x, y);
		setPosition(new Position(x, y));
		setDX(dx);
		setDY(dy);
	}

	@Override
	public boolean collides(gameMovingObject obj, double dt) {
		// only bubble collision
		return (Vektor.createVector(obj.getPosition(), this.getPosition()).getVectorSizeSq() < Math.pow(10, 2));
	}

	@Override
	public Vektor collisionSolution(gameMovingObject obj, double dt) {
		/*
		Vektor tmpVector = (Vektor)obj.getVector().clone();
		tmpVector.multiply(0.01);
		Position bodOdrazu = Vektor.sum(obj.getPosition(), tmpVector);
			
		Vektor nv = Vektor.createVector(this.getPosition(), bodOdrazu);
		Vektor kolmy = new Vektor(nv.y, -nv.x);
		
		Obstacle odraznica = new LineObstacle(new Line2D.Double(Vektor.sum(bodOdrazu, kolmy), Vektor.sum(bodOdrazu, kolmy.multiply(-1f))));
		
		return odraznica.collisionSolution(obj, dt);
		*/
		this.getVector().add(((Vektor)obj.getVector().clone()).multiply(1f/8f));
		return ((Vektor)obj.getVector().clone()).multiply(-2f/3f);
	}
}

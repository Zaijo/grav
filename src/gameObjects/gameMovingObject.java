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

import java.util.HashMap;
import java.util.Iterator;

import main.Game;


public abstract class gameMovingObject extends gameObject {
	
	public enum Status{
		moving, steady;
	}
	
	private HashMap<Obstacle, Boolean> obstacles = new HashMap<Obstacle, Boolean>();
	private Gravity gravity;
	public Status status = Status.moving;
	Obstacle lockedOne;
	private Vektor vector = new Vektor(0, 0);
	
	public Game game;
	
	public double pixNorm = 50;
	
	public void setGravity(Gravity g){
		this.gravity = g;
	}

	public void removeGravity(){
		this.gravity = null;
	}
	
	public void gravityAffect(Vektor v, double dt){
		if(this.gravity != null){
			if(this.gravity instanceof RadialGravity && this instanceof gamePlanet){
				((RadialGravity) gravity).affect(v, this.getPosition(), dt, 1);
			}
			else
				gravity.affect(v, this.getPosition(), dt);
		}
	}
	
	public void addObstacle(Obstacle o) {
		this.obstacles.put(o, false);
	}

	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}

	public gameMovingObject(double x, double y) {
		super(x, y);
	}

	public gameMovingObject(Position position) {
		super(position);
	}
	
	public void lockObstacle(Obstacle o){
		this.lockedOne = o; 
	}
	
	public void unlockObstacle(){
		this.lockedOne = null;
	}

	public boolean locked(Obstacle o){
		return this.lockedOne == o;
	}

	public void tick(double dt) {
		if(status == Status.moving){
			// necessary to iterate through HashMap
			Iterator<Obstacle> it = obstacles.keySet().iterator();
			
			boolean collided = false;
			while(it.hasNext()) {	
				Obstacle o = it.next();
				
				boolean collision = o.collides(this, dt);
				
				if(collision){
					collided = true;
					this.setVector(o.collisionSolution(this, dt));
				}
				else if(this.locked(o)){
					this.unlockObstacle();
				}
				
				// release the multiple bounce lock
			}
			if(!collided)
				gravityAffect(getVector(), dt);
			// precalculate position using new velocity vector
			this.getPosition().x += dt * getDX();
			this.getPosition().y += dt * getDY();
	//		this.getVector().multiply(0.99); // air|surface fricion

		} // status control
	}

	public void setDY(double dy) {
		this.vector.y = dy;
	}

	public double getDY() {
		return this.vector.getY();
	}

	public void setDX(double dx) {
		this.vector.x = dx;
	}

	public double getDX() {
		return this.vector.getX();
	}

	public Vektor getVector(){
		return this.vector;
	}
	
	public void setVector(Vektor p){
		this.vector = (Vektor)p.clone();
	}
}

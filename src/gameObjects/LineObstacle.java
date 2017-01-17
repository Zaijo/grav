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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineObstacle implements Obstacle {
	private Line2D line;
	@Override
	public boolean collides(gameMovingObject obj, double dt) {
		
		// move presumptions
		Position curPos = (Position)obj.getPosition().clone();
		Vektor tmpVector = (Vektor)obj.getVector().clone();
		obj.gravityAffect(tmpVector, dt);
		
//		System.out.println(tmpVector.getVectorString());

		Position nextPos = Vektor.sum(curPos, new Vektor(dt * tmpVector.getX(), dt
				* tmpVector.getY()));

		// watch out for your obstacles
		Line2D movementLine = new Line2D.Double(curPos, nextPos); // the way that was passed between draws
		
		
		boolean collision = false;
		
		// bubble collision detection
		if (this.getLine().ptSegDistSq(obj.getPosition()) < Math.pow(6, 2)) 
			collision = true;
			
		// precise collision detection
		if (this.getLine().intersectsLine(movementLine)) { // passed-way-line and obstacle-line-seqment intersection
			collision = true;
		}
		
		return collision;
	}

	@Override
	public Vektor collisionSolution(gameMovingObject obj, double dt) {
		if (!obj.locked(this)) {
			// dv - direction vector of the obstacle line
			Vektor dv = Vektor.getVector((Point2D.Double) this.getLine()
					.getP1(), (Point2D.Double) this.getLine().getP2());
			
			// get normal vector of obstacle line
			Vektor nv = new Vektor(dv.getY(), - dv.getX());
//				 JOptionPane.showMessageDialog(null, nv.getVectorString());

			// current position of object
			Position curPos = (Position)obj.getPosition().clone();
			Vektor tmpVector = (Vektor)obj.getVector().clone();
			obj.gravityAffect(tmpVector, dt);

			if (Math.signum(Vektor.crossProduct(dv, nv)) != Math
					.signum(Vektor.crossProduct(dv, Vektor.getVector(
							(Point2D.Double) this.getLine().getP1(), curPos)))) {
				nv.multiply(-1f);
			}

			float friction = 0.0f; // length of vector to be bigger to overcome this
			float energyLoss = 20; // percent
			
			// get v - square of how far behind the obstacle would the thing go in
			// next timestep if it was right on it
			double v = this.getLine().ptLineDistSq(
					Vektor.sum(new Position(this.getLine().getP1()), tmpVector)); // promised distance squared
			
			if(v < Math.pow(1, 2)){
				// stop the small changes when in almost-equilibrium
				// set the length of normal vector to sqrt(v)
				nv.multiply(Math.sqrt(v / (Math.pow(nv.x, 2) + Math.pow(nv.y, 2))));
				tmpVector = Vektor.sum(tmpVector, nv);
				if(tmpVector.decompositionInDirection(dv).getVectorSize() < friction){
					tmpVector.multiply(0);
				}
				else{
					tmpVector.setVectorSize(tmpVector.getVectorSize() - friction * tmpVector.decompositionInDirection(dv).getVectorSize() * dt);
				}
				
			}
			else{
				// precalculate the direction after bounce
				// set the length of normal vector to 2 * sqrt(v)
				nv.multiply(Math.sqrt((4 * v) / (Math.pow(nv.x, 2) + Math.pow(nv.y, 2))));
				
				// lost energy depends on angle of hit
				double lostPercent = energyLoss * ( tmpVector.decompositionInDirection(nv).getVectorSize() 
						/ (tmpVector.getVectorSize()));
				
				tmpVector = Vektor.sum(tmpVector, nv).multiply((80 - lostPercent) / 100); // loss of energy
				
				obj.lockObstacle(this); // unlocks others
			}
			
			return tmpVector;
		}
		else{
			obj.gravityAffect(obj.getVector(), dt);
			return (obj.getVector());
		}
	}

	public LineObstacle(Line2D line) {
		super();
		this.line = line;
	}

	public Line2D getLine() {
		return line;
	}

	public void setLine(Line2D line) {
		this.line = line;
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		Stroke tmpStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 1f, new float[] { 2f }, 0f));
		g2d.drawLine((int) line.getX1(), (int) line.getY1(),
				(int) line.getX2(), (int) line.getY2());
		g2d.setStroke(tmpStroke);
	}


}

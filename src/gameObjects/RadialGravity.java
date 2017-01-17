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

public class RadialGravity extends Gravity{
	private Position center;
	private double power;
	
	
	
	public RadialGravity(Position center, double power) {
		super();
		this.center = center;
		this.power = power;
	}

	public RadialGravity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RadialGravity(Position center) {
		this(center, 10);
	}

	public Position getCenter() {
		return center;
	}

	public void setCenter(Position center) {
		this.center = center;
	}
	
	public void affect(Vektor vector, Position point, double dt, double additionalKoeficient) {
		// any centripetal vector
		Vektor cv = Vektor.createVector(point, center);

		// modify its size depending on distance and addinionalKoeficient
		cv.setVectorSize(power * dt / Vektor.distanceSq(point.x, point.y, center.x, center.y));
		
		vector.add(cv);
	}
	
	@Override
	public void affect(Vektor vector, Position point, double dt) {
		// any centripetal vector
		Vektor cv = Vektor.createVector(point, center);

		// modify its size depending on distance
		cv.setVectorSize(power * dt);
		
		vector.add(cv);
	}
	
}

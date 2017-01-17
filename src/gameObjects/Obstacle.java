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

public interface Obstacle{
	// checks, whether there will be a collision
	public abstract boolean collides(gameMovingObject obj, double dt);
	
	// returns new direction Vektor for the object
	public abstract Vektor collisionSolution(gameMovingObject obj, double dt);
	
	public abstract void draw(Graphics g);

}

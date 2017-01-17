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

public class movingCircle extends gameMovingObject {
	public int halfTheSize = 5;
	
	public movingCircle(Position position) {
		super(position);
	}

	public movingCircle(double x, double y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics g) {
		g.drawArc(this.getPosition().igetX() - halfTheSize, this.getPosition().igetY() - halfTheSize, 2 * halfTheSize, 2 * halfTheSize, 0, 360);
	}

}

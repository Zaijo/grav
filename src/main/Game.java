/*
Erublast is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Erublast is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package main;
import gameObjects.Gravity;
import gameObjects.Position;
import gameObjects.RadialGravity;
import gameObjects.Vektor;
import gameObjects.ViewSettings;
import gameObjects.gameMovingObject;
import gameObjects.gamePlanet;
import gui.AWTStrategy;
import gui.WindowStrategy;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Vector;

public class Game {
	// CONSTANTS
	final public static double AU = 149597887.5;
	
	
	public WindowStrategy gui;
	
	float fps;
	Vector<gameMovingObject> movingObjects = new Vector<gameMovingObject>();
	//Vector<Obstacle> commonObstacles = new Vector<Obstacle>();
	Gravity gravity;
	
	// game status
	public boolean run = true;
	public boolean pause = false;
	public boolean showHelp = true;
	public boolean showInfo = true;
	public boolean showTrail = true;
	public Position sunPosition = new Position(0,0);
	public Rectangle viewZone = new Rectangle(0,0);
	
	// game options
	public double howSlow = 450; // divides timestep
	public ViewSettings view = new ViewSettings(100, 0, 0);
	private int focusPlanet = 0;
	
	// real planets
	private gamePlanet[] realPlanets;
	
	// planets info
	private String[] planetInfo = new String[10];
	
	           
	// temporarily
	Graphics2D g;
	Position koniec;
	gamePlanet square;
	
	public void nextFocus(){
		focusPlanet = (focusPlanet + 1) % realPlanets.length;
	}
	
	public void previousFocus(){
		focusPlanet = (focusPlanet - 1) % realPlanets.length;
		if(focusPlanet < 0)
			focusPlanet = realPlanets.length + focusPlanet;
	}
	
	public void setStartPosition(Position start){
		realPlanets[focusPlanet].setPosition(start);
	}
	
	public void setVector(Vektor vector){
		vector.multiply(10 / view.getZoom());
		realPlanets[focusPlanet].setVector(vector);
	}
	
	public Game(int width, int height){
		gui = new AWTStrategy(this, width, height);
		
		// gamePlanet(x, y, diameterin km, mass in kg)
		// x, y = positions relative to sun
		// diameter in 1000 km
		
		// SLNKO
		gamePlanet sun = new gamePlanet("Slnko", 0, 0, 1391, 2.03e+30); // heliocentricky
		planetInfo[0] = "Slnko je hviezda našej planetárnej sústavy. Planéta Zem obieha okolo Slnka. Je to naša najbližšia hviezda a zároveň najjasnejšia hviezda na oblohe. Gravitačné pôsobenie Slnka udržiava na obežných dráhach okolo Slnka všetky objekty slnečnej sústavy. Jeho energia je nevyhnutná pre život na Zemi.";
		
		// MERKUR
		gamePlanet mercury = new gamePlanet("Merkúr", 0.3226749, -0.239670, 4.880, 3.42e+23);
		mercury.setFVector(new Vektor(19.39046, 41.37893));
		planetInfo[1] = "Merkúr je malá kamenná planéta s povrchom posiatym impaktnými krátermi. Teploty na jeho povrchu kolíšu od +440° C cez deň po -180 °C v noci. Za takéto obrovské rozdiely môže hlavne neprítomnosť hustej atmosféry. Napriek tomu, že je najbližšie k Slnku, nedrží teplotný rekord medzi planétami slnečnej sústavy. Ten patrí Venuši, ktorá je od Slnka síce ďalej, ale panuje na nej silný skleníkový efekt.";
		
		// VENUSA
		gamePlanet venus = new gamePlanet("Venuša", 0.7246052, 0.0298364, 12.104, 4.97e+24);
		venus.setFVector(new Vektor(-1.60499, 34.83093));
		planetInfo[2] = "Venuša je druhá planéta slnečnej sústavy (v poradí od Slnka), po Slnku a po Mesiaci najjasnejší objekt viditeľný zo Zeme. Pomenovaná je po starorímskej bohyni lásky. Jej dráha sa nachádza vo vnútri dráhy Zeme, to znamená, že nikdy sa na oblohe nevzdiali ďaleko od Slnka. Maximálna uhlová vzdialenosť Venuše od Slnka môže byť až 48°. So Slnkom a Mesiacom patrí medzi jediné tri nebeské telesá, ktorých svetlo vrhá na Zem tiene viditeľné voľným okom.";
		
		// ZEM
		gamePlanet earth = new gamePlanet("Zem", -0.5122862, -0.8726855, 12.756, 6.1e+24);
		earth.setFVector(new Vektor(25.19828, -15.18765));
		planetInfo[3] = "Zem je naša materská planéta, v poradí tretia planéta slnečnej sústavy. Je to zároveň jediná planéta, na ktorej je podľa súčasných vedeckých poznatkov voda v kvapalnom skupenstve a život. Zem je predmetom skúmania napríklad kozmogónie, geológie, paleontológie či geografie.";
		
		// MARS
		gamePlanet mars = new gamePlanet("Mars", 1.3184114, 0.5190594, 6.795, 6.52e+23);
		mars.setFVector(new Vektor(-7.959595, 24.61677));
		planetInfo[4] = "Mars je štvrtá planéta slnečnej sústavy v poradí od Slnka. Je to druhá najmenšia planéta (po Merkúre). Pomenovaná je po Marsovi, starorímskom bohovi vojny. Jeho dráha sa nachádza až za dráhou Zeme. Ide o planétu terestrického typu, to znamená, že má pevný horninový povrch pokrytý impaktnými krátermi, vysokými sopkami, hlbokými kaňonmi a ďalšími útvarmi. ";
		
		// JUPITER
		gamePlanet jupiter = new gamePlanet("Jupiter", 4.6340712, 1.7354144, 142.984, 1.93e+27);
		jupiter.setFVector(new Vektor(-4.74797, 12.86814));
		planetInfo[5] = "Jupiter je prvou planétou od Slnka, ktorá nemá pevný povrch. Jeho búrlivá atmosféra plynule prechádza do plášťa a vo väčších hĺbkach do horúceho jadra. Rotáciou planéty sa v jej atmosfére utvorili gigantické, farebne jasne odlíšené štruktúry nazývané pásy a zóny. Jedna otočka Jupitera okolo jeho osi je najrýchlejšia zo všetkých známych planét (netrvá ani 10 hodín).";
		
		// SATURN
		gamePlanet saturn = new gamePlanet("Saturn", -9.2718207, -2.5568423, 120.537, 5.8e+26);
		saturn.setFVector(new Vektor(2.04383, -9.32444));
		planetInfo[6] = "Saturn nemá pevný povrch, ale len hustú atmosféru. Jeden obeh Saturna okolo Slnka trvá takmer 30 pozemských rokov. Je ľahko pozorovateľný voľným okom ako žltý neblikajúci objekt jasnosťou porovnateľný s najjasnejšími hviezdami.";
		
		// URAN
		gamePlanet uranus = new gamePlanet("Urán", 20.0790274, 0.4172934, 51.119, 8.9e+25);
		uranus.setFVector(new Vektor(-0.19446, 6.50097));
		planetInfo[7] = "Urán je siedma planéta od Slnka, tretia najväčšia a štvrtá najhmotnejšia planéta v slnečnej sústave. Patrí medzi plynných obrov a spolu s Neptúnom aj medzi tzv. ľadových obrov. Podobne ako ďalšie plynné planéty má aj Urán prstence, magnetosféru a množstvo mesiacov. Zvláštnosťou Uránu je sklon jeho rotačnej osi: os leží takmer v rovine, v ktorej planéta obieha. Severný a južný pól sa preto nachádzajú v oblastiach, ktoré sú u iných planét charakteristické pre rovník. Pri pohľade zo Zeme sa preto občas stane, že sa prstence Uránu javia ako terč s Uránom v strede.";
		
		// NEPTUN
		gamePlanet neptune = new gamePlanet("Neptún", 25.7069879, -15.4806673, 49.529, 1.04e+26);
		neptune.setFVector(new Vektor(2.76436, 4.69810));
		planetInfo[8] = "Neptún je ôsma a najvzdialenejšia planéta slnečnej sústavy. Rozhodlo o tom hlasovanie na 26. kongrese Medzinárodnej astronomickej únie v Prahe 24. augusta 2006. Dovtedy bolo poslednou planétou slnečnej sústavy Pluto. Planéta bola objavená 23. septembra 1846 Johannom Gallom a študentom astronómie Louisom d'Arrestom. Neptún je prvou planétou objavenou skôr na základe matematických výpočtov ako priamym pozorovaním. Z porúch v dráhe Uránu astronómovia odvodzovali jeho existenciu.";
		
		// PLUTO
		gamePlanet pluto = new gamePlanet("Pluto", 3.3256984, -31.7623693, 2.300, 1.09e+22);
		pluto.setFVector(new Vektor(5.50580, -0.48997));
		planetInfo[9] = "Pluto je trpasličia planéta slnečnej sústavy, v minulosti považované za najmenšiu a najvzdialenejšiu planétu slnečnej sústavy. Keďže za Neptúnom boli objavené ešte ďalšie planétky dokonca aj väčších hmotností, Pluto sa nepovažuje za riadnu planétu.";
		
		square = sun;
		
		gamePlanet[] tmpArray = {sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, pluto};
		//gamePlanet[] tmpArray = {sun, earth};
		realPlanets = tmpArray;
		
		// kazda planeta sa hybe podla gravitacie
		for(gamePlanet planet: realPlanets){
			planet.game = this;
			//planet.setPositionRel(new Position(250, 250));
			movingObjects.add(planet);
		}
		
		// GRAVITY                                               \/ tipnute
		this.gravity = new RadialGravity(new Position(100 ,100), 220); // 6.67300e-11
		
		// apply gravity to all moving objects
		for(gameMovingObject obj : this.movingObjects){
			obj.setGravity(this.gravity);
		}
		
		
		// associate objects and obstacles
		/*
		for(Obstacle obst : this.commonObstacles){
			for(gameMovingObject obj: this.movingObjects){
				obj.addObstacle(obst);
			}
		}
		*/
	}
	
	public Game(){
		this(800,800); // the Game(int, int); constructor
	}
	
	public void start(){
		if(run == true)
			gui.start();
		else
			run = true;
		run();
	}
	
	public void run(){
		Calendar cal = Calendar.getInstance();
		double startTime = cal.getTimeInMillis();
		
		precalculate();
		
		while(run){
			
			double timeNow = Calendar.getInstance().getTimeInMillis();
			double dt = timeNow - startTime;
			startTime = timeNow;   
				if(!pause){
				
				// frames per second information
				fps = (float)(1000 / dt);
				dt = dt / (33 * howSlow);
				
				// number of total logic ticks between frame renders
				int precisionPrecounts = (int)Math.ceil(dt) * 200; // satisfiable logic density
				
				for(int i = 0; i < precisionPrecounts; ++i)	
					tick(dt/precisionPrecounts);
				
				precalculate();
				
				// ZOOM IN
				double howMuch = 1;
				//view.setZoom(view.getZoom() - (howMuch -= 0.3));
				centerView(realPlanets[focusPlanet].getPosition());
				
				draw();
				
				
				// in case of watching steps slowly
				try{
					//Thread.currentThread().sleep(200);
				}
				catch(Exception e){	}
			}
		}
	}
	
	public void zoomRelatively(int notches, Position where){
		view.setZoom(view.getZoom() + 20 * notches * view.getZoom()/100);
	}
	
	public void slowRelatively(double k){
		howSlow = Math.max((howSlow * k), 1);
	}
	
	public gamePlanet getFocusPlanet() {
		return realPlanets[focusPlanet];
	}

	public void setFocusPlanet(int focusPlanet) {
		this.focusPlanet = focusPlanet;
	}

	private void centerView(Position center){
		int x = gui.getDim().width, y = gui.getDim().height;
		double k = view.getZoom();
		view.setOffsetX((center.x - x/(2*k)));
		view.setOffsetY((center.y - y/(2*k)));
	}
	
	private void precalculate(){
		sunPosition = realPlanets[0].getPosition();
		double maxx = 0, maxy = 0, minx = Double.MAX_VALUE, miny = Double.MAX_VALUE;
		for(gamePlanet planet : realPlanets){
			if(planet.getPosition().x > maxx){
				maxx = planet.getPosition().x;
			}
			if(planet.getPosition().x < minx){
				minx = planet.getPosition().x;
			}
			if(planet.getPosition().y > maxy){
				maxy = planet.getPosition().y;
			}
			if(planet.getPosition().y < miny){
				miny = planet.getPosition().y;
			}
		}
		viewZone.setLocation((int)Math.round(minx), (int)Math.round(miny));
		viewZone.setSize((int)Math.round(maxx-minx), (int)Math.round(maxy-miny));
	}
	
	public void pause(){
		run = false;
	}
	
	public boolean stop(){
		run = false;
		return !run;
		// stop game cycle
	}
	
	public void tick(){
		tick(1);
	}
	
	public void tick(double f){
		if(this.gravity instanceof RadialGravity){
			((RadialGravity)gravity).setCenter(square.getPosition());
		}
		
		for(gameMovingObject obj : this.movingObjects){
			obj.tick(f);
			
			if(obj instanceof gamePlanet){
				//((gamePlanet)obj).addRecentLocation(obj.getPosition());
			}
			
		}
		//double vzd = square.getPosition().y - 150; 
		//square.setDY(square.getDY() - Math.signum(vzd) * factor(vzd) );
	}
	
	public void draw(){
		
		//gui.g.drawLine(0, 0, (int)Math.round(koniec.x), (int)Math.round(koniec.y));
		//gui.g.drawLine(0, 0, (int)Math.round(koniec.y), (int)Math.round(koniec.x));
		
		for(gameMovingObject obj : movingObjects){
			obj.draw(gui.g);
		}
		
		gui.g.drawString(Integer.toString((int)Math.round(fps)) + " " + howSlow, 30, 20);
		
		drawHelp();
		drawPlanetInfo(focusPlanet);
		
		gui.update();
	}
	
	private void drawHelp(){
		if(showHelp){
			Graphics2D g = (Graphics2D)gui.g;
			g.setColor(new Color(255, 255, 255, 80));
			g.setPaint(new Color(0,0,0,200));
			g.fillRoundRect(10, 40, 300, 210, 15, 15);
			g.setPaint(new Color(255,255,255,100));
			g.fillRoundRect(10, 40, 300, 210, 15, 15);
			g.setColor(new Color(255, 255, 255));
			String textOvladania[] = {
					"Ovládanie: (okno sa prepína klávesom O)",
					"",
					"Planéty:",
					"šipka vľavo: predchádzajúca planéta", 
					"šípka vpravo: nasledujúca planéta",
					"kláves I: Informácie o planéte",
					"stlačenie a potiahnute tlačítka myši: určenie",
					"    nového smeru vybranej planéte",
					"",
					"Pohľad:",
					"koliesko myši: približovanie a vzďaľovanie",
					"šípka hore, dole: spomalenie a zrýchlenie",
					"",
					""
					};
			drawMultilineText(gui.g, textOvladania, 20, 50);
		}
	}
	
	private void drawPlanetInfo(int planetIndex){
		if(showInfo){
			int vyska = gui.getDim().height, sirka = gui.getDim().width;
			Graphics2D g = (Graphics2D)gui.g;
			g.setColor(new Color(255, 255, 255, 80));
			g.setPaint(new Color(0,0,0,200));
			g.fillRoundRect(10, vyska - 110, sirka - 10, 190, 15, 15);
			g.setPaint(new Color(255,255,255,100));
			g.fillRoundRect(10, vyska - 110, sirka - 20, 100, 15, 15);
			g.setColor(new Color(255, 255, 255));
			drawString(g, planetInfo[planetIndex], 20, vyska - 90, sirka - 40);
		}
	}
	
	public void drawMultilineText(Graphics g, String[] riadky, final int x, final int yy){
		final int rozostup = 15;
		int y = yy;
		for(String riadok: riadky){
			g.drawString(riadok, x, y += rozostup);
		}
	}
	
	public int drawString(Graphics g, String s, int x, int y, int width)
	{
	        // FontMetrics gives us information about the width,
	        // height, etc. of the current Graphics object's Font.
	        FontMetrics fm = g.getFontMetrics();

	        int lineHeight = fm.getHeight();

	        int curX = x;
	        int curY = y;

	        String[] words = s.split(" ");

	        for (String word : words)
	        {
	                // Find out the width of the word.
	                int wordWidth = fm.stringWidth(word + " ");

	                // If text exceeds the width, then move to next line.
	                if (curX + wordWidth >= x + width)
	                {
	                        curY += lineHeight;
	                        curX = x;
	                }

	                g.drawString(word, curX, curY);

	                // Move over to the right for next word.
	                curX += wordWidth;
	        }
	    return curX;
	}
	
	public static void drawLine(Graphics g, Position p1, Position p2){
		g.drawLine(p1.igetX(), p1.igetY(), p2.igetX(), p2.igetY());
	}
}

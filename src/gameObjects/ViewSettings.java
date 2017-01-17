package gameObjects;


public class ViewSettings {
	private double zoom, offsetX, offsetY;

	public ViewSettings(double zoom, double offsetX, double offsetY) {
		super();
		this.zoom = zoom;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public ViewSettings(ViewSettings v){
		this(v.getZoom(), v.getOffsetX(), v.getOffsetY());
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		final double min = 5;
		if(zoom < min)
			this.zoom = min;
		else
			this.zoom = zoom;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}
}

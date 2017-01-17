package gameObjects;

public final class LinearGravity extends Gravity {
	private Vektor forceVector;
	
	public LinearGravity(double x, double y) {
		this(new Vektor(x, y));
	}
	
	public LinearGravity(Vektor forceVector) {
		super();
		this.forceVector = forceVector;
	}

	@Override
	public void affect(Vektor vector, Position point, double dt){
		vector.add(((Vektor)(forceVector.clone())).multiply(dt));
	}

}

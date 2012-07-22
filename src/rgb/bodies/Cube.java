package rgb.bodies;


public class Cube extends Box {
	
	public Cube(float width, float mass, float restitution) {
		this(width, mass, restitution, 0, 0, 0);
	}
	
	public Cube(float width, float mass, float restitution, float x, float y, float z) {
		super(width, width, width, mass, restitution, x, y, z);
	}
}

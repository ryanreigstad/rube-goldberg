package rgb.bodies;

import javax.vecmath.Vector3f;


public class Cube extends Box {
	
	public Cube(Vector3f location, float width, float mass, float restitution) {
		super(location, width, width, width, mass, restitution);
	}
}

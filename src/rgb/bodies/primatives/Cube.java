package rgb.bodies.primatives;

import javax.vecmath.Vector3f;

public class Cube extends Box {
	private Cube() {}
	
	private Cube(float halfWidth) {
		super(new Vector3f(halfWidth, halfWidth, halfWidth));
	}
	
	public static Cube makeCube(Vector3f location, Vector3f orientation, float halfWidth, float mass, float restitution, float friction) {
		Box b = makeBox(location, orientation, new Vector3f(halfWidth, halfWidth, halfWidth), mass, restitution, friction);
		Cube cube = new Cube(halfWidth);
		cube.setRigidBody(b.getRigidBody());
		b.getRigidBody().setUserPointer(cube);
		return cube;
	}
}

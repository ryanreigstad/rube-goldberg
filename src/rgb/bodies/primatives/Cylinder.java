package rgb.bodies.primatives;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class Cylinder extends PrimativeBody {
	
	protected Cylinder() {}
	
	protected Cylinder(float radius, float height) {
		// TODO: all of this (see: Box.ctor(Vector3f))
	}
	
	public static Cylinder makeCylinder(Vector3f location, Vector3f orientation, float height, float radius, float mass, float restitution, float friction) {
		RigidBody cylinderBody;
		
		MotionState motion = getDefaultMotionState(location, orientation);
		CollisionShape shape = new CylinderShape(new Vector3f(radius, height / 2f, radius));
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		cylinderBody = new RigidBody(mass, motion, shape, inertia);
		cylinderBody.setRestitution(restitution);
		cylinderBody.setFriction(friction);
		
		Cylinder cylinder = new Cylinder(radius, height);
		cylinder.setRigidBody(cylinderBody);
		cylinderBody.setUserPointer(cylinder);
		
		return cylinder;
	}
}

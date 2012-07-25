package rgb.bodies;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class Sphere extends PhysicalBody {
	
	public Sphere(Vector3f location, float radius, float mass, float restitution) {
		MotionState motion = getDefaultMotion(location);
		CollisionShape shape = new SphereShape(radius);
		
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.body = new RigidBody(mass, motion, shape, inertia);
		this.body.setRestitution(restitution);
	}
}

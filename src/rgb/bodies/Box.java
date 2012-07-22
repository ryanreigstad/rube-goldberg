package rgb.bodies;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class Box extends PhysicalBody {
	
	protected Box() {}
	
	public Box(float w, float h, float d, float mass, float restitution) {
	}
	
	public Box(float w, float h, float d, float mass, float restitution, float x, float y, float z) {
		MotionState motion = getDefaultMotion(x, y, z);
		CollisionShape shape = new BoxShape(new Vector3f(w / 2f, h / 2f, d / 2f));
		
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.body = new RigidBody(mass, motion, shape, inertia);
		this.body.setRestitution(restitution);
	}
}

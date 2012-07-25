package rgb.bodies;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.CylinderShapeX;
import com.bulletphysics.collision.shapes.CylinderShapeZ;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class Cylinder extends PhysicalBody {
	
	public Cylinder(Vector3f location, float radius, float height, float mass, float restitution) {
		MotionState motion = getDefaultMotion(location);
		CollisionShape shape = new CylinderShapeZ(new Vector3f(radius / 2f, radius / 2f, height / 2f));
		
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.body = new RigidBody(mass, motion, shape, inertia);
		this.body.setRestitution(restitution);
	}
}

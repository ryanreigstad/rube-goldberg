package rgb.bodies;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;

public class Rope extends ComplexBody {
	
	public Rope(Vector3f startLocation, Vector3f endLocation, float radius, int segments, DynamicsWorld world) {
		bodies = new ArrayList<>();
		
		float xd = (endLocation.x - startLocation.x) / segments;
		float yd = (endLocation.y - startLocation.y) / segments;
		float zd = (endLocation.z - startLocation.z) / segments;
		float l = (float) Math.sqrt(xd * xd + yd * yd + zd * zd);
		
		Vector3f last = new Vector3f(startLocation);
		for (int i = 0; i < segments; i++) {
			Vector3f next = new Vector3f(last.x + xd, last.y + yd, last.z + zd);
			Vector3f cur = new Vector3f((next.x + last.x) / 2, (next.y + last.y) / 2, (next.z + last.z) / 2);
			bodies.add(new Cylinder(cur, radius, l, 1, 0.5f));
			if (i > 0) {
				RigidBody lastBody = bodies.get(i - 1).getBody();
				RigidBody currBody = bodies.get(i).getBody();
				
				Point2PointConstraint joint = new Point2PointConstraint(lastBody, currBody, new Vector3f(xd, yd, zd), new Vector3f(-xd, -yd, -zd));
//				lastBody.addConstraintRef(joint);
//				currBody.addConstraintRef(joint);
				world.addConstraint(joint, true);
			}
			last = next;
		}
	}
	
	private ArrayList<PhysicalBody> bodies;

	@Override
	public List<PhysicalBody> getBodies() {
		return this.bodies;
	}
	
}

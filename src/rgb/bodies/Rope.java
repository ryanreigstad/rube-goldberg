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
		
		makeSegments(startLocation, endLocation, radius, segments, world);
	}
	
	private ArrayList<PhysicalBody> bodies;
	
	private void makeSegments(Vector3f startLocation, Vector3f endLocation, float radius, int segments, DynamicsWorld world) {
		float xd = (endLocation.x - startLocation.x) / segments;
		float yd = (endLocation.y - startLocation.y) / segments;
		float zd = (endLocation.z - startLocation.z) / segments;
		float segmentLength = (float) Math.sqrt(xd * xd + yd * yd + zd * zd);
		
		Vector3f lastEnd = new Vector3f(startLocation);
		for (int i = 0; i < segments; i++) {
			Vector3f nextEnd = new Vector3f(lastEnd.x + xd, lastEnd.y + yd, lastEnd.z + zd);
			
			Vector3f midpoint = new Vector3f((nextEnd.x + lastEnd.x) / 2, (nextEnd.y + lastEnd.y) / 2, (nextEnd.z + lastEnd.z) / 2);
			bodies.add(new Cylinder(midpoint, radius, segmentLength, 0.1f, 0.5f));
			
			if (i > 0) {
				// connect the last segment with this segment
				RigidBody lastBody = bodies.get(i - 1).getBody();
				RigidBody currBody = bodies.get(i).getBody();
				
				Point2PointConstraint joint = new Point2PointConstraint(lastBody, currBody, new Vector3f(xd, yd, zd), new Vector3f(-xd, -yd, -zd));
				world.addConstraint(joint, true);
			}
			lastEnd = nextEnd;
		}
	}

	@Override
	public List<PhysicalBody> getBodies() {
		return this.bodies;
	}
	
}

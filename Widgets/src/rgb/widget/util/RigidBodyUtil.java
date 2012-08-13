package rgb.widget.util;

import javax.vecmath.Vector3f;

import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class RigidBodyUtil {
	
	public static MotionState getDefaultMotionState(Vector3f loc, Vector3f rot) {
		Transform location = new Transform();
		location.setIdentity();
		location.origin.set(loc);
		location.basis.rotX(rot.x);
		location.basis.rotY(rot.y);
		location.basis.rotZ(rot.z);
		
		return new DefaultMotionState(location);
	}
}

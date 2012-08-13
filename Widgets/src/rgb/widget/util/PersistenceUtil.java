package rgb.widget.util;

import javax.vecmath.Vector3f;

public class PersistenceUtil {
	
	public static Vector3f parseV3f(String v) {
		Vector3f result = new Vector3f();
		result.x = Float.parseFloat(v.split(",")[0].trim());
		result.y = Float.parseFloat(v.split(",")[1].trim());
		result.z = Float.parseFloat(v.split(",")[2].trim());
		return result;
	}
}

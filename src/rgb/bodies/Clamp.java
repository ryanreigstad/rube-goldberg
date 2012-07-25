package rgb.bodies;

import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3f;

public class Clamp extends ComplexBody {
	
	public Clamp(Vector3f location, float clampWidth) {
		Vector3f l = new Vector3f(location.x - (clampWidth / 2f + 0.95f), location.y, location.z);
		this.left = new Cube(l, 1, 0, 0);
		Vector3f r = new Vector3f(location.x + (clampWidth / 2f + 0.95f), location.y, location.z);
		this.right = new Cube(r, 1, 0, 0);
	}
	
	private Cube left;
	private Cube right;
	
	public List<PhysicalBody> getBodies() {
		return Arrays.asList(new PhysicalBody[] {this.left, this.right});
	}
	
	public void extend(float d) {
		this.left.translate(new Vector3f(-d, 0, 0));
		this.right.translate(new Vector3f(d, 0, 0));
	}
}

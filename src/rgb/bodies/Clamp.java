package rgb.bodies;

import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3f;

public class Clamp extends ComplexBody {
	
	public Clamp(float x, float y, float z, float clampWidth) {
		this.left = new Cube(1, 0, 0, x - (clampWidth / 2f + 0.95f), y, z);
		this.right = new Cube(1, 0, 0, x + (clampWidth / 2f + 0.95f), y, z);
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

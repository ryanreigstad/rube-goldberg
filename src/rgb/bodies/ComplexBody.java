package rgb.bodies;

import java.util.List;

import javax.vecmath.Vector3f;

public abstract class ComplexBody {
	
	public ComplexBody() {}
	
	public abstract List<PhysicalBody> getBodies();
	
	public void translate(Vector3f trans) {
		for (PhysicalBody body : this.getBodies())
			body.translate(trans);
	}
	
	public Vector3f getLocation() {
		Vector3f[] locs = new Vector3f[this.getBodies().size()];
		for (int i = 0; i < this.getBodies().size(); i++)
			locs[i] = this.getBodies().get(i).getLocation();
		Vector3f avg = new Vector3f();
		for (Vector3f v : locs)
			avg.add(v);
		avg.scale(1 / (float) locs.length);
		return avg;
	}
}

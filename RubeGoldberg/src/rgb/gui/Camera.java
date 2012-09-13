package rgb.gui;

import javax.vecmath.Vector3f;

import rgb.widget.Widget;
import rgb.widget.WidgetWithProperties;

@WidgetWithProperties(properties = {"x", "y", "z", "xr", "yr", "zr"})
public class Camera implements Widget {
	public Camera() {
		this(new Vector3f(), new Vector3f());
	}
	
	public Camera(Vector3f location, Vector3f orientation) {
		this.location = location;
		this.orientation = orientation;
	}
	
	private Vector3f location;
	private Vector3f orientation;
	
	public Vector3f getLocation() {
		return this.location;
	}
	
	public Vector3f getOrientation() {
		return this.orientation;
	}
	
	public void setLocation(Vector3f l) {
		this.location = l;
	}
	
	public void setOrientation(Vector3f o) {
		this.orientation = o;
	}

	@Override
	public void update() {
		; // do nothing
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public void setId(int id) {
		; // do nothing
	}

	@Override
	public String getName() {
		return "Camera";
	}

	@Override
	public void setName(String name) {
		; // do nothing
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public Float getX() { return this.location.x; }
	public Float getY() { return this.location.y; }
	public Float getZ() { return this.location.z; }
	public Float getXr() { return this.orientation.x; }
	public Float getYr() { return this.orientation.y; }
	public Float getZr() { return this.orientation.z; }

	public void setX(Float x) { this.location.x = x; }
	public void setY(Float y) { this.location.y = y; }
	public void setZ(Float z) { this.location.z = z; }
	public void setXr(Float xr) { this.orientation.x = xr; }
	public void setYr(Float yr) { this.orientation.y = yr; }
	public void setZr(Float zr) { this.orientation.z = zr; }
}

package rgb.gui;

import javax.vecmath.Vector3f;

public class Camera {
	public Camera() {
		location = new Vector3f();
		orientation = new Vector3f();
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
}

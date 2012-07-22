package rgb.bodies;

import javax.vecmath.Vector3f;

import rgb.CollisionHandler;
import rgb.ICollisionListener;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;


public abstract class PhysicalBody implements ICollisionListener {

	public PhysicalBody() {}
	
//	public PhysicalBody(float mass, float restitution, float x, float y, float z) {
//		;
//	}
	
	protected RigidBody body;
	protected CollisionHandler collisionHandler;
	
	public RigidBody getBody() {
		return body;
	}
	
	public void translate(Vector3f trans) {
		Transform location = this.body.getWorldTransform(new Transform());
		location.origin.add(trans);
		this.body.setWorldTransform(location);
		this.body.activate(true);
	}
	
	public Vector3f getLocation() {
		return this.body.getWorldTransform(new Transform()).origin;
	}
	
	public PhysicalBody getPhysicalBody() {
		return this;
	}
	
	public CollisionHandler getCollisionHandler() {
		return this.collisionHandler;
	}
	
	public void setCollisionHandler(CollisionHandler handler) {
		this.collisionHandler = handler;
	}
	
	protected static MotionState getDefaultMotion(float x, float y, float z) {
		Transform location = new Transform();
		location.setIdentity();
		location.origin.set(x, y, z);
		return new DefaultMotionState(location);
	}
}

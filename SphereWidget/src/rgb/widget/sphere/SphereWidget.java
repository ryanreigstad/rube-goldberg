package rgb.widget.sphere;

import static org.lwjgl.opengl.GL11.*;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import rgb.widget.WidgetWithProperties;
import rgb.widget.physical.PrimitiveShape;
import rgb.widget.renderable.Renderable;
import rgb.widget.util.RigidBodyUtil;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

@WidgetWithProperties(properties = {"name","x","y","z","radius","mass"})
public class SphereWidget implements PrimitiveShape, Renderable {
	
	protected RigidBody rigidBody;
	protected int id;
	protected String name;
	protected float radius;
	
	public SphereWidget(Vector3f location, Vector3f linearVelocity, Vector3f angularVelocity, float radius, float mass, float restitution, float friction) {
		this.radius = radius;
		initRigidBody(location, linearVelocity, angularVelocity, mass, restitution, friction);
	}

	private void initRigidBody(Vector3f location, Vector3f linearVelocity, Vector3f angularVelocity, float mass, float restitution, float friction) {
		MotionState motion = RigidBodyUtil.getDefaultMotionState(location, new Vector3f());
		CollisionShape shape = new SphereShape(this.radius);
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.rigidBody = new RigidBody(mass, motion, shape, inertia);
		this.rigidBody.setLinearVelocity(linearVelocity);
		this.rigidBody.setAngularVelocity(angularVelocity);
		this.rigidBody.setRestitution(restitution);
		this.rigidBody.setFriction(friction);
		this.rigidBody.setSleepingThresholds(0.01f, 0.01f);
		this.rigidBody.setUserPointer(this);
	}
	
	@Override
	public void update() {
		// do nothing
	}
	
	@Override
	public void render() {
		Vector3f location = this.getLocation();
		Vector3f rot = this.getOrientation();

		GL11.glPushMatrix();
		GL11.glTranslatef(location.x, location.y, location.z);
		// FIXME: rotation
//		glRotatef(rot.x * 180f / (float) Math.PI, 1, 0, 0);
		glRotatef(rot.y * 180f / (float) Math.PI, 0, 1, 0);
		glRotatef(rot.z * 180f / (float) Math.PI, 0, 0, 1);
		new Sphere().draw(this.radius, 16, 12);
		GL11.glPopMatrix();
	}
	
	private static Vector3f toAngles(Quat4f quat) {
		double x = Math.atan2(2*(quat.w*quat.x + quat.y*quat.z), 1 - 2*(Math.pow(quat.x, 2) + Math.pow(quat.y, 2)));
		double y = Math.asin(2*(quat.w*quat.y + quat.x*quat.z));
		double z = Math.atan2(2*(quat.w*quat.z + quat.x*quat.y), 1 - 2*(Math.pow(quat.y, 2) + Math.pow(quat.z, 2)));
		return new Vector3f((float) x, (float) y, (float) z);
	}
	
	public Vector3f getLocation() {
		return this.rigidBody.getWorldTransform(new Transform()).origin;
	}
	
	public void setLocation(Vector3f loc) {
		Transform trans = this.rigidBody.getWorldTransform(new Transform());
		trans.origin.set(loc);
		this.rigidBody.setWorldTransform(trans);
	}
	
	public Vector3f getOrientation() {
		return toAngles(this.rigidBody.getWorldTransform(new Transform()).getRotation(new Quat4f()));
	}

	@Override
	public RigidBody getRigidBody() {
		return this.rigidBody;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Float getRadius() {
		return radius;
	}

	public void setRadius(Float radius) {
		this.radius = radius;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public Float getX() { return this.getLocation().x; }
	public Float getY() { return this.getLocation().y; }
	public Float getZ() { return this.getLocation().z; }
	public Float getMass() { return 1f / this.rigidBody.getInvMass(); }
	
	public void setX(Float x) { this.setLocation(new Vector3f(x, this.getY(), this.getZ())); }
	public void setY(Float y) { this.setLocation(new Vector3f(this.getX(), y, this.getZ())); }
	public void setZ(Float z) { this.setLocation(new Vector3f(this.getX(), this.getY(), z)); }
	public void setMass(Float mass) {
		Vector3f inertia = new Vector3f();
		this.rigidBody.getCollisionShape().calculateLocalInertia(mass, inertia);
		this.rigidBody.setMassProps(mass, inertia);
	}
}

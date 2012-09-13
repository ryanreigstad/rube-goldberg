package rgb.widget.box;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import rgb.widget.WidgetWithProperties;
import rgb.widget.physical.PrimitiveShape;
import rgb.widget.renderable.RenderableBase;
import rgb.widget.util.RigidBodyUtil;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

@WidgetWithProperties(properties = {"name","x","y","z","xr","yr","zr","width","height","depth","mass"})
public class BoxWidget extends RenderableBase implements PrimitiveShape {
	
	protected RigidBody rigidBody;
	protected int id;
	protected String name;
	protected Vector3f halfExtents;
	
	public BoxWidget(Vector3f location, Vector3f orientation, Vector3f linearVelocity, Vector3f angularVelocity, Vector3f halfExtents, float mass, float restitution, float friction) {
		this.halfExtents = halfExtents;
		initRigidBody(location, orientation, linearVelocity, angularVelocity, mass, restitution, friction);
		initBufferIds();
	}

	private void initRigidBody(Vector3f location, Vector3f orientation, Vector3f linearVelocity, Vector3f angularVelocity, float mass, float restitution, float friction) {
		MotionState motion = RigidBodyUtil.getDefaultMotionState(location, orientation);
		CollisionShape shape = createCollisionShape(this.halfExtents);
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
	
	private static CollisionShape createCollisionShape(Vector3f halfExtents) {
		return new BoxShape(halfExtents);
	}

	private void initBufferIds() {
		int[] bufferIds = BoxMeshBuilder.build(this.halfExtents);
		this.vertexBufferId = bufferIds[0];
		this.indexBufferId = bufferIds[1];
	}
	
	@Override
	public void update() {
		// do nothing
	}

	@Override
	public RigidBody getRigidBody() {
		return this.rigidBody;
	}

	@Override
	public int getVertexCount() {
		return BoxMeshBuilder.getVertexCount();
	}

	@Override
	public int getIndexCount() {
		return BoxMeshBuilder.getIndexCount();
	}
	
	@Override
	public int getOpenGlDrawMode() {
		return GL11.GL_QUADS;
	}
	
	@Override
	public Transform getWorldTransform() {
		return this.rigidBody.getWorldTransform(new Transform());
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

	public Vector3f getHalfExtents() {
		return halfExtents;
	}

	public void setHalfExtents(Vector3f halfExtents) {
		this.halfExtents = halfExtents;
		this.rigidBody.setCollisionShape(createCollisionShape(halfExtents));
		disposeBuffers(this.vertexBufferId, this.indexBufferId);
		this.initBufferIds();
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
	
	public void setOrientation(Vector3f orient) {
		Transform trans = this.rigidBody.getWorldTransform(new Transform());
		trans.basis.setIdentity();
		trans.basis.rotX(orient.x);
		trans.basis.rotY(orient.y);
		trans.basis.rotZ(orient.z);
		this.rigidBody.setWorldTransform(trans);
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public Float getX() { return this.getLocation().x; }
	public Float getY() { return this.getLocation().y; }
	public Float getZ() { return this.getLocation().z; }
	public Float getXr() { return this.getOrientation().x; }
	public Float getYr() { return this.getOrientation().y; }
	public Float getZr() { return this.getOrientation().z; }
	public Float getWidth() { return this.getHalfExtents().x * 2; }
	public Float getHeight() { return this.getHalfExtents().y * 2; }
	public Float getDepth() { return this.getHalfExtents().z * 2; }
	public Float getMass() { return 1f / this.rigidBody.getInvMass(); }
	
	public void setX(Float x) { this.setLocation(new Vector3f(x, this.getY(), this.getZ())); }
	public void setY(Float y) { this.setLocation(new Vector3f(this.getX(), y, this.getZ())); }
	public void setZ(Float z) { this.setLocation(new Vector3f(this.getX(), this.getY(), z)); }
	public void setXr(Float xr) { this.setOrientation(new Vector3f(xr, this.getYr(), this.getZr())); }
	public void setYr(Float yr) { this.setOrientation(new Vector3f(this.getXr(), yr, this.getZr())); }
	public void setZr(Float zr) { this.setOrientation(new Vector3f(this.getXr(), this.getYr(), zr)); }
	public void setWidth(Float w) { this.setHalfExtents(new Vector3f(w / 2f, this.getHeight() / 2f, this.getDepth() / 2f)); }
	public void setHeight(Float h) { this.setHalfExtents(new Vector3f(this.getWidth() / 2f, h / 2f, this.getDepth() / 2f)); }
	public void setDepth(Float d) { this.setHalfExtents(new Vector3f(this.getWidth() / 2f, this.getHeight() / 2f, d / 2f)); }
	public void setMass(Float mass) {
		Vector3f inertia = new Vector3f();
		this.rigidBody.getCollisionShape().calculateLocalInertia(mass, inertia);
		this.rigidBody.setMassProps(mass, inertia);
	}
}

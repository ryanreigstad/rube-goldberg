package rgb.widget.sphere;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import rgb.widget.physical.PrimitiveShape;
import rgb.widget.renderable.RenderableBase;
import rgb.widget.util.RigidBodyUtil;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class SphereWidget extends RenderableBase implements PrimitiveShape {
	
	protected RigidBody rigidBody;
	protected int id;
	protected String name;
	
	public SphereWidget(Vector3f location, float radius, float mass, float restitution, float friction) {
		initRigidBody(location, radius, mass, restitution, friction);
		initBufferIds(radius);
	}

	private void initRigidBody(Vector3f location, float radius, float mass, float restitution, float friction) {
		MotionState motion = RigidBodyUtil.getDefaultMotionState(location, new Vector3f());
		CollisionShape shape = new SphereShape(radius);
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.rigidBody = new RigidBody(mass, motion, shape, inertia);
		this.rigidBody.setRestitution(restitution);
		this.rigidBody.setFriction(friction);
		this.rigidBody.setSleepingThresholds(0.01f, 0.01f);
		this.rigidBody.setUserPointer(this);
	}

	private void initBufferIds(float radius) {
		int[] bufferIds = SphereMeshBuilder.build(radius);
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
		return SphereMeshBuilder.getVertexCount();
	}

	@Override
	public int getIndexCount() {
		return SphereMeshBuilder.getIndexCount();
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
}

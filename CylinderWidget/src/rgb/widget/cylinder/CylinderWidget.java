package rgb.widget.cylinder;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShapeX;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import rgb.widget.physical.PrimitiveShape;
import rgb.widget.renderable.RenderableBase;
import rgb.widget.util.RigidBodyUtil;

public class CylinderWidget extends RenderableBase implements PrimitiveShape {
	
	protected RigidBody rigidBody;
	
	public CylinderWidget(Vector3f location, Vector3f orientation, float radius, float height, float mass, float restitution, float friction) {
		this.initRigidBody(location, orientation, radius, height, mass, restitution, friction);
		this.initBufferIds(radius, height);
	}

	private void initRigidBody(Vector3f location, Vector3f orientation, float radius, float height, float mass, float restitution, float friction) {
		MotionState motion = RigidBodyUtil.getDefaultMotionState(location, orientation);
		CollisionShape shape = new CylinderShapeX(new Vector3f(height / 2f, radius / 2f, radius / 2f));
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.rigidBody = new RigidBody(mass, motion, shape, inertia);
		this.rigidBody.setRestitution(restitution);
		this.rigidBody.setFriction(friction);
		this.rigidBody.setSleepingThresholds(0.01f, 0.01f);
		this.rigidBody.setUserPointer(this);
	}

	private void initBufferIds(float radius, float height) {
		int[] bufferIds = CylinderMeshBuilder.build(radius, height);
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
		return CylinderMeshBuilder.getVertexCount();
	}

	@Override
	public int getIndexCount() {
		return CylinderMeshBuilder.getIndexCount();
	}

	@Override
	public int getOpenGlDrawMode() {
		return GL11.GL_QUADS;
	}

	@Override
	public Transform getWorldTransform() {
		return this.rigidBody.getWorldTransform(new Transform());
	}

}

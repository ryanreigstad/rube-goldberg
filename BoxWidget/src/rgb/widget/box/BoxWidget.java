package rgb.widget.box;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import rgb.widget.physical.PrimitiveShape;
import rgb.widget.renderable.RenderableBase;
import rgb.widget.util.RigidBodyUtil;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class BoxWidget extends RenderableBase implements PrimitiveShape {
	
	protected RigidBody rigidBody;
	
	public BoxWidget(Vector3f location, Vector3f orientation, Vector3f halfExtents, float mass, float restitution, float friction) {
		initRigidBody(location, orientation, halfExtents, mass, restitution, friction);
		initBufferIds(halfExtents);
	}

	private void initRigidBody(Vector3f location, Vector3f orientation, Vector3f halfExtents, float mass, float restitution, float friction) {
		MotionState motion = RigidBodyUtil.getDefaultMotionState(location, orientation);
		CollisionShape shape = new BoxShape(halfExtents);
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		this.rigidBody = new RigidBody(mass, motion, shape, inertia);
		this.rigidBody.setRestitution(restitution);
		this.rigidBody.setFriction(friction);
		this.rigidBody.setUserPointer(this);
	}

	private void initBufferIds(Vector3f halfExtents) {
		int[] bufferIds = BoxMeshBuilder.build(halfExtents);
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
}

package rgb.bodies.primatives;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import rgb.draw.RenderableHelper;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class Box extends PrimativeBody {
	protected Box() {}
	
	protected Box(Vector3f halfExtents) {
		this.drawMode = GL11.GL_QUADS;
		this.indexCount = 6 * 4; // 6 sides, 4 verts each
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(8 * 3);		// 8 verts, 3 floats each
		IntBuffer indices = BufferUtils.createIntBuffer(indexCount);
		
		for (int x = -1; x < 2; x += 2)
			for (int y = -1; y < 2; y += 2)
				for (int z = -1; z < 2; z += 2)
					vertices.put(new float[] {halfExtents.x * x, halfExtents.y * y, halfExtents.z * z});
		
		vertices.flip();
		
		indices.put(new int[] {0,1,3,2}); // left
		indices.put(new int[] {4,5,7,6}); // right
		indices.put(new int[] {0,1,5,4}); // bottom
		indices.put(new int[] {2,3,7,6}); // top
		indices.put(new int[] {0,2,6,4}); // back
		indices.put(new int[] {1,3,7,5}); // front
		indices.flip();
		
		this.vertBufferId = RenderableHelper.createBufferId();
		RenderableHelper.bufferData(this.vertBufferId, vertices);
		
		this.vertBufferIndexId = RenderableHelper.createBufferId();
		RenderableHelper.bufferElementData(this.vertBufferIndexId, indices);
	}
	
	public static Box makeBox(Vector3f location, Vector3f orientation, Vector3f halfExtents, float mass, float restitution, float friction) {
		RigidBody boxBody;
		
		MotionState motion = getDefaultMotionState(location, orientation);
		CollisionShape shape = new BoxShape(halfExtents);
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		boxBody = new RigidBody(mass, motion, shape, inertia);
		boxBody.setRestitution(restitution);
		boxBody.setFriction(friction);
		
		Box box = new Box(halfExtents);
		box.rigidBody = boxBody;
		boxBody.setUserPointer(box);
		
		return box;
	}
}

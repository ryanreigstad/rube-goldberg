package rgb.bodies.primatives;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import rgb.draw.RenderableHelper;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class Sphere extends PrimativeBody {
	
	private static final int numVertLayers = 8;
	private static final int numVertsPerLayer = 10;
	
	protected Sphere() {}
	
	protected Sphere(float radius) {
		// TODO: all of this (see: Box.ctor(Vector3f))
		this.drawMode = GL11.GL_QUADS;
		this.indexCount = 2 * (2 * numVertsPerLayer) + 4 * ((numVertLayers - 1) * numVertsPerLayer + 1);
		this.vertCount = (numVertLayers - 1) * numVertsPerLayer + 2;
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(this.vertCount * 3);
		IntBuffer indices = BufferUtils.createIntBuffer(this.indexCount);

		vertices.put(new float[] {radius, 0, 0});
		for (float layer = 1; layer < numVertLayers; layer++) {
			float x = radius * (float) Math.cos(Math.PI * (layer / (float) numVertLayers));
			float xr = radius * (float) Math.sin(Math.PI * (layer / (float) numVertLayers));
			
			for (float vert = 1; vert <= numVertsPerLayer; vert++) {
				float y = xr * (float) Math.sin(2 * Math.PI * (vert / (float) numVertsPerLayer));
				float z = xr * (float) Math.cos(2 * Math.PI * (vert / (float) numVertsPerLayer));
				
				vertices.put(new float[] {x, y, z});
			}
		}
		vertices.put(new float[] {-radius, 0, 0});
		vertices.flip();
		
		for (int i = 1; i < numVertsPerLayer + 1; i++) {
			indices.put(new int[] {0, i, i == numVertsPerLayer ? 1 : i + 1, 0});
		}
		for (int layer = 0; layer < numVertLayers - 2; layer++) {
			int i;
			for (i = 1 + (layer * numVertsPerLayer); i < (layer + 1) * numVertsPerLayer; i++) {
				indices.put(new int[] {i, i + numVertsPerLayer, i + numVertsPerLayer + 1, i + 1});
			}
			indices.put(new int[] {i, i + numVertsPerLayer, i + 1, i + 1 - numVertsPerLayer});
		}
		for (int i = 1; i < numVertsPerLayer; i++) {
			int[] t = new int[] {this.vertCount - i - 1, this.vertCount - 1, this.vertCount - 1, this.vertCount - i - 1 - 1};
			indices.put(t);
		}
		int[] t = new int[] {this.vertCount - numVertsPerLayer - 1, this.vertCount - 1, this.vertCount - 1, this.vertCount - 1 - 1};
		indices.put(t);
		indices.flip();
		
		this.vertBufferId = RenderableHelper.createBufferId();
		RenderableHelper.bufferData(this.vertBufferId, vertices);
		
		this.vertBufferIndexId = RenderableHelper.createBufferId();
		RenderableHelper.bufferElementData(this.vertBufferIndexId, indices);
	}
	
	public static Sphere makeSphere(Vector3f location, float radius, float mass, float restitution, float friction) {
		RigidBody sphereBody;
		
		MotionState motion = getDefaultMotionState(location);
		CollisionShape shape = new SphereShape(radius);
		Vector3f inertia = new Vector3f();
		shape.calculateLocalInertia(mass, inertia);
		
		sphereBody = new RigidBody(mass, motion, shape, inertia);
		sphereBody.setRestitution(restitution);
		sphereBody.setFriction(friction);
		
		Sphere sphere = new Sphere(radius);
		sphere.setRigidBody(sphereBody);
		sphereBody.setUserPointer(sphere);
		
		return sphere;
	}
}

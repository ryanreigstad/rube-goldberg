package rgb.widget.box;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;

import rgb.widget.util.RenderableUtil;

class BoxMeshBuilder {
	
	private static final int vertexCount = 8;	// a box has 8 corners
	private static final int indexCount = 24;	// 4 vertices per side, 6 sides
	
	public static int getVertexCount() {
		return vertexCount;
	}
	
	public static int getIndexCount() {
		return indexCount;
	}
	
	public static int[] build(Vector3f halfExtents) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(getVertexCount() * 3);
		IntBuffer indices = BufferUtils.createIntBuffer(getIndexCount());
		
		for (int x = -1; x <= 1; x += 2)
			for (int y = -1; y <= 1; y += 2)
				for (int z = -1; z <= 1; z += 2)
					vertices.put(new float[] {halfExtents.x * x, halfExtents.y * y, halfExtents.z * z});
		vertices.flip();
		
		indices.put(new int[] {0,1,3,2}); // left
		indices.put(new int[] {4,5,7,6}); // right
		indices.put(new int[] {0,1,5,4}); // bottom
		indices.put(new int[] {2,3,7,6}); // top
		indices.put(new int[] {0,2,6,4}); // back
		indices.put(new int[] {1,3,7,5}); // front
		indices.flip();
		
		int vertexBufferId = RenderableUtil.createBufferId();
		RenderableUtil.bufferData(vertexBufferId, vertices);
		
		int indexBufferId = RenderableUtil.createBufferId();
		RenderableUtil.bufferElementData(indexBufferId, indices);
		
		return new int[] {vertexBufferId, indexBufferId};
	}
}

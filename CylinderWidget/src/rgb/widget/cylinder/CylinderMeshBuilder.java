package rgb.widget.cylinder;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import rgb.widget.util.RenderableUtil;

class CylinderMeshBuilder {
	
	private static final int verticesPerSide = 12;
	
	public static int getVertexCount() {
		return verticesPerSide * 2 + 2;
	}
	
	public static int getIndexCount() {
		return verticesPerSide * 2 * 4 + 2 * 2 * verticesPerSide;
	}

	public static int[] build(float radius, float height) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(getVertexCount() * 3);
		IntBuffer indices = BufferUtils.createIntBuffer(getIndexCount());
		
		vertices.put(new float[] {-radius, 0, 0});
		for (float x = -radius; x <= radius; x += 2 * radius) {
			for (int v = 0; v < verticesPerSide; v++) {
				float a = (float) (v * Math.PI * 2 / (float) verticesPerSide);
				float y = (float) Math.sin(a) * radius;
				float z = (float) Math.cos(a) * radius;
				
				vertices.put(new float[] {x, y, z});
			}
		}
		vertices.put(new float[] {radius, 0, 0});
		vertices.flip();
		
		for (int i = 1; i <= verticesPerSide; i++) {
			indices.put(new int[] {0, i, i == verticesPerSide ? 1 : i + 1, 0});
		}
		
		for (int i = 1; i < verticesPerSide; i++) {
			indices.put(new int[] {i, i + verticesPerSide, i + verticesPerSide + 1, i + 1});
		}
		indices.put(new int[] {verticesPerSide, verticesPerSide * 2, verticesPerSide + 1, 1});
		
		for (int i = 1; i < verticesPerSide; i++) {
			indices.put(new int[] {getVertexCount() - i - 1, getVertexCount() - 1, getVertexCount() - 1, getVertexCount() - i - 1 - 1});
		}
		indices.put(new int[] {getVertexCount() - verticesPerSide - 1, getVertexCount() - 1, getVertexCount() - 1, getVertexCount() - 1 - 1});
		indices.flip();
		
		int vertexBufferId = RenderableUtil.createBufferId();
		RenderableUtil.bufferData(vertexBufferId, vertices);
		
		int indexBufferId = RenderableUtil.createBufferId();
		RenderableUtil.bufferElementData(indexBufferId, indices);
		
		return new int[] {vertexBufferId, indexBufferId};
	}

}

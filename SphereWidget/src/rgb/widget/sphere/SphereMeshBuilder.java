package rgb.widget.sphere;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import rgb.widget.util.RenderableUtil;

class SphereMeshBuilder {
	
	private static final int numVertLayers = 8;
	private static final int numVertsPerLayer = 10;
	
	public static int getVertexCount() {
		return (numVertLayers - 1) * numVertsPerLayer + 2;
	}
	
	public static int getIndexCount() {
		return 2 * (2 * numVertsPerLayer) + 4 * ((numVertLayers - 1) * numVertsPerLayer + 1);
	}
	
	public static int[] build(float radius) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(getVertexCount() * 3);
		IntBuffer indices = BufferUtils.createIntBuffer(getIndexCount());

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
			int[] t = new int[] {getVertexCount() - i - 1, getVertexCount() - 1, getVertexCount() - 1, getVertexCount() - i - 1 - 1};
			indices.put(t);
		}
		int[] t = new int[] {getVertexCount() - numVertsPerLayer - 1, getVertexCount() - 1, getVertexCount() - 1, getVertexCount() - 1 - 1};
		indices.put(t);
		indices.flip();
		
		int vertexBufferId = RenderableUtil.createBufferId();
		RenderableUtil.bufferData(vertexBufferId, vertices);
		
		int indexBufferId = RenderableUtil.createBufferId();
		RenderableUtil.bufferElementData(indexBufferId, indices);
		
		return new int[] {vertexBufferId, indexBufferId};
	}
}

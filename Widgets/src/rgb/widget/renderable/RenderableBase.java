package rgb.widget.renderable;

import java.nio.IntBuffer;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;

import com.bulletphysics.linearmath.Transform;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public abstract class RenderableBase implements Renderable {
	
	protected int vertexBufferId;
	protected int indexBufferId;

	public abstract int getVertexCount();
	public abstract int getIndexCount();
	public abstract int getOpenGlDrawMode();
	public abstract Transform getWorldTransform();

	@Override
	protected void finalize() throws Throwable
	{
		IntBuffer buffers = BufferUtils.createIntBuffer(2);
		buffers.put(this.vertexBufferId);
		buffers.put(this.indexBufferId);
		
    	ARBVertexBufferObject.glDeleteBuffersARB(buffers);
	
	    super.finalize();
	}
	
	public void render() {
		if(this.vertexBufferId > 0 && this.indexBufferId > 0)
	    {
	        glEnableClientState(GL_VERTEX_ARRAY);
	        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBufferId);
	        glVertexPointer(3, GL_FLOAT, 0, 0);

	        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, indexBufferId);
	        
	        glPushMatrix();
	        
	        Transform trans = this.getWorldTransform();
	        glTranslatef(trans.origin.x, trans.origin.y, trans.origin.z);
	        
	        Vector3f rot = toAngles(trans.getRotation(new Quat4f()));
	        // FIXME: rotation
//	        glRotatef(rot.x * 180f / (float) Math.PI, 1, 0, 0);
	        glRotatef(rot.y * 180f / (float) Math.PI, 0, 1, 0);
	        glRotatef(rot.z * 180f / (float) Math.PI, 0, 0, 1);
	        
	        glDrawRangeElements(this.getOpenGlDrawMode(), 0, this.getIndexCount(), this.getIndexCount(),
	                            GL_UNSIGNED_INT, 0);
	        
	        glPopMatrix();

	        glDisableClientState(GL_VERTEX_ARRAY);
	    }
	}
	
	private static Vector3f toAngles(Quat4f quat) {
		double x = Math.atan2(2*(quat.w*quat.x + quat.y*quat.z), 1 - 2*(Math.pow(quat.x, 2) + Math.pow(quat.y, 2)));
		double y = Math.asin(2*(quat.w*quat.y + quat.x*quat.z));
		double z = Math.atan2(2*(quat.w*quat.z + quat.x*quat.y), 1 - 2*(Math.pow(quat.y, 2) + Math.pow(quat.z, 2)));
		return new Vector3f((float) x, (float) y, (float) z);
	}
}

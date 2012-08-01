package rgb.bodies.primatives;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.ARBVertexBufferObject;

import rgb.CollisionHandler;
import rgb.ICollisionListener;
import rgb.draw.IRenderable;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public abstract class PrimativeBody implements ICollisionListener, IRenderable {

	protected PrimativeBody() {}
	
	protected CollisionHandler collisionHandler;
	protected RigidBody rigidBody;
	
	protected int vertBufferId;
	protected int vertBufferIndexId;
	
	protected int drawMode;
	protected int vertCount;
	protected int indexCount;
	
	public void render() {
		if(vertBufferId > 0 && vertBufferIndexId > 0)
	    {
	        glEnableClientState(GL_VERTEX_ARRAY);
	        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertBufferId);
	        glVertexPointer(3, GL_FLOAT, 0, 0);

	        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, vertBufferIndexId);
	        
	        glPushMatrix();
	        
	        Transform trans = this.rigidBody.getMotionState().getWorldTransform(new Transform());
	        glTranslatef(trans.origin.x, trans.origin.y, trans.origin.z);
	        
	        Vector3f rot = toAngles(trans.getRotation(new Quat4f()));
//	        glRotatef(rot.x * 180f / (float) Math.PI, 1, 0, 0);
	        glRotatef(rot.y * 180f / (float) Math.PI, 0, 1, 0);
	        glRotatef(rot.z * 180f / (float) Math.PI, 0, 0, 1);
	        
	        glDrawRangeElements(drawMode, 0, indexCount, indexCount,
	                            GL_UNSIGNED_INT, 0);
	        
	        glPopMatrix();

	        glDisableClientState(GL_VERTEX_ARRAY);
	    }
	}
	
	public CollisionHandler getCollisionHandler() {
		return this.collisionHandler;
	}
	
	public void setCollisionHandler(CollisionHandler handler) {
		this.collisionHandler = handler;
	}
	
	public RigidBody getRigidBody() {
		return this.rigidBody;
	}
	
	public void setRigidBody(RigidBody body) {
		this.rigidBody = body;
		body.setUserPointer(this);
	}
	
	protected static MotionState getDefaultMotionState(Vector3f loc) {
		return getDefaultMotionState(loc, new Vector3f());
	}
	
	protected static MotionState getDefaultMotionState(Vector3f loc, Vector3f rot) {
		Transform location = new Transform();
		location.setIdentity();
		location.origin.set(loc);
		location.basis.rotX(rot.x);
		location.basis.rotY(rot.y);
		location.basis.rotZ(rot.z);
		
		return new DefaultMotionState(location);
	}

	@Override
	protected void finalize() throws Throwable
	{
	    if(this.vertBufferId > 0)
	    	ARBVertexBufferObject.glDeleteBuffersARB(this.vertBufferId);
	    if(this.vertBufferIndexId > 0)
	    	ARBVertexBufferObject.glDeleteBuffersARB(this.vertBufferIndexId);
	
	    super.finalize();
	}
	
	private static Vector3f toAngles(Quat4f quat) {
		double x = Math.atan2(2*(quat.w*quat.x + quat.y*quat.z), 1 - 2*(Math.pow(quat.x, 2) + Math.pow(quat.y, 2)));
		double y = Math.asin(2*(quat.w*quat.y + quat.x*quat.z));
		double z = Math.atan2(2*(quat.w*quat.z + quat.x*quat.y), 1 - 2*(Math.pow(quat.y, 2) + Math.pow(quat.z, 2)));
		return new Vector3f((float) x, (float) y, (float) z);
	}
}

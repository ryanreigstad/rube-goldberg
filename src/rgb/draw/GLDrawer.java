package rgb.draw;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.linearmath.Transform;

public class GLDrawer {
	private GLDrawer() {
	}
	
	public static void init(int windowWidth, int windowHeight, float fov) throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
		Display.create();
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, windowWidth / windowHeight, 0.01f, 100f);
		glMatrixMode(GL_MODELVIEW);
	}
	
	private static Camera camera = new Camera();
	
	public static void setCamera(Camera cam) {
		camera = cam;
	}
	
	public static void drawScene(DynamicsWorld world) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		glTranslatef(-camera.getLocation().x, -camera.getLocation().y, -camera.getLocation().z);
		glColor3f(1f, 1f, 1f);
		
		glBegin(GL_POINTS);
		for (CollisionObject obj : world.getCollisionObjectArray()) {
			Transform trans = obj.getWorldTransform(new Transform());
			
			glVertex3f(trans.origin.x, trans.origin.y, trans.origin.z);
		}
		glEnd();
		Display.update();
		try {
			Thread.sleep(1000 / 60);
		} catch (InterruptedException e) {}
	}
	
	public static void shutdown() {
		Display.destroy();
	}
}

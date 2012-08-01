package rgb.draw;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DynamicsWorld;

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
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
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
		
		for (CollisionObject obj : world.getCollisionObjectArray()) {
			IRenderable r = (IRenderable) obj.getUserPointer();
			if (r != null)
				r.render();
		}
		
		Display.update();
	}
	
	public static void shutdown() {
		Display.destroy();
	}
}

package rgb.gui;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rgb.widget.renderable.Renderable;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DynamicsWorld;

public class Renderer {
	public Renderer(int windowWidth, int windowHeight, float fov) throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
		Display.create();
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, windowWidth / windowHeight, 0.01f, 100f);
		glMatrixMode(GL_MODELVIEW);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	
	private Camera camera = new Camera();
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void setCamera(Camera cam) {
		this.camera = cam;
	}
	
	public void drawScene(DynamicsWorld world) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		glTranslatef(-this.camera.getLocation().x, -this.camera.getLocation().y, -this.camera.getLocation().z);
		glColor3f(1f, 1f, 1f);
		
		// TODO: decouple renderer from DynamicsWorld
		for (CollisionObject obj : world.getCollisionObjectArray()) {
			Renderable r = (Renderable) obj.getUserPointer();
			if (r != null)
				r.render();
		}
		
		Display.update();
	}
	
	public void shutdown() {
		Display.destroy();
	}
}

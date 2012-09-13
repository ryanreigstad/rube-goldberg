package rgb.twl;

import static org.lwjgl.opengl.GL11.*;

import javax.vecmath.Vector3f;

import org.lwjgl.util.glu.GLU;

import rgb.Simulation;
import rgb.gui.Camera;
import rgb.widget.renderable.Renderable;

import com.bulletphysics.collision.dispatch.CollisionObject;

import de.matthiasmann.twl.Widget;

public class SimulationView extends Widget {
	public SimulationView(int padding) {
		this(padding, new Camera(new Vector3f(-.5f, 3f, 20f), new Vector3f()));
	}
	
	public SimulationView(int padding, Camera cam) {
		this.padding = padding;
		this.camera = cam;
	}
	
	private int padding;
	private Camera camera;
	private Simulation model;
	
	public void render() {
		if (this.model != null)
			this.render(this.model);
	}
	
	private void render(Simulation sim) {
//		glClear(GL_DEPTH_BUFFER_BIT);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glViewport(this.getX() + padding,
				this.getParent().getInnerHeight() - this.getY() - this.getInnerHeight() + padding,
				this.getInnerWidth() - padding * 2,
				this.getInnerHeight() - padding * 2);
		GLU.gluPerspective(45.0f, this.getInnerWidth() / (float) this.getInnerHeight(), 0.01f, 10000);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glPushAttrib(GL_POLYGON_BIT);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		glTranslatef(-this.camera.getLocation().x, -this.camera.getLocation().y, -this.camera.getLocation().z);
		glRotatef(this.camera.getOrientation().x, 1, 0, 0);
		glRotatef(this.camera.getOrientation().y, 0, 1, 0);
		glRotatef(this.camera.getOrientation().z, 0, 0, 1);
		glColor3f(1, 1, 1);
		
		for (CollisionObject obj : sim.getWorld().getCollisionObjectArray()) {
			Renderable r = (Renderable) obj.getUserPointer();
			if (r != null)
				r.render();
		}
		
		glPopAttrib();
	}
	
	public void setCamera(Camera cam) {
		this.camera = cam;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void setSimulation(Simulation sim) {
		this.model = sim;
	}
	
	public Simulation getSimulation() {
		return this.model;
	}
}

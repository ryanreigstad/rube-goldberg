package rgb;

import java.io.File;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;

import rgb.gui.Camera;
import rgb.gui.Renderer;
import rgb.persistence.Persistence;

public class Main {
	public static void main(String[] args) throws LWJGLException {
		
		Renderer renderer = new Renderer(900, 600, 45f);
		renderer.setCamera(new Camera(new Vector3f(0, 0, 9), new Vector3f()));
		
		WidgetLibrary widgets = new WidgetLibrary(WidgetLoader.loadWidgets());
		
		Simulation sim = Persistence.loadSimulation(new File("saves/test.xml"), widgets);
		sim.setRenderer(renderer);
		
		for (int i = 0; i < 900; i++) {
			sim.drawFrame();
			sim.stepSimulation(1 / 60f);
			try {
				Thread.sleep(25);
			} catch (Exception e) {}
		}
		
		renderer.shutdown();
	}
}

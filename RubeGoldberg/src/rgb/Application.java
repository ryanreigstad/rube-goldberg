package rgb;

import java.io.File;
import java.io.FileNotFoundException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import rgb.persistence.Persistence;
import rgb.twl.ApplicationGUI;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Application implements Runnable {
	public static void main(String[] args) {
		try {
			new Application(900, 500).run();
		} catch (Error ex) {
			ex.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private Application(int width, int height) throws Error {
		this.initLWJGL(width, height, "Rube IDE");
		this.initRube();
		this.initTWL(width, height);
		
		this.newSimulation();
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.appGui.destroy();
		this.gui.destroy();
		Display.destroy();
		
		super.finalize();
	}
	
	private GUI gui;
	private ApplicationGUI appGui;
	private WidgetLibrary widgets;
	
	private void initLWJGL(int width, int height, String title) throws Error {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Display.create();
		} catch (LWJGLException ex) {
			throw new Error("Could not create display", ex);
		}
	}
	
	private void initRube() throws Error {
		try {
			this.widgets = new WidgetLibrary(WidgetLoader.loadWidgets("widgets"));
		} catch (FileNotFoundException e) {
			throw new Error("couldn't load widgets", e);
		}
	}
	
	private void initTWL(int width, int height) throws Error {
		LWJGLRenderer renderer = null;
		ThemeManager theme = null;
		try {
			renderer = new LWJGLRenderer();
			theme = ThemeManager.createThemeManager(new File("twl/main.xml").toURI().toURL(), renderer);
			this.initGUI(renderer, theme, width, height);
		} catch (Exception e) {
			throw new Error("Could not make theme manager", e);
		}
	}
	
	private void initGUI(LWJGLRenderer renderer, ThemeManager theme, int width, int height) {
		this.appGui = new ApplicationGUI(width, height, this);
		this.gui = new GUI(this.appGui, renderer);
		this.gui.applyTheme(theme);
	}
	
	@Override
	public void run() {
		final int mspf = 1000 / 50;
		long last = System.currentTimeMillis();
		while (!Display.isCloseRequested()) {
			this.render();
			this.update(mspf);
			try {
				long sleep = mspf - (System.currentTimeMillis() - last);
				Thread.sleep(sleep < 0 ? 0 : sleep);
			} catch (Exception e) {}
			last = System.currentTimeMillis();
		}
	}
	
	private void update(long time) {
		this.appGui.getSimulationView().getSimulation().stepSimulation(time / 1000f);
		// TODO: Application.update().. update everything that needs updating that isn't in the simulation
	}
	
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, this.gui.getInnerWidth(), this.gui.getInnerHeight());
		GLU.gluPerspective(45.0f, this.gui.getInnerWidth() / (float) this.gui.getInnerHeight(), 0.01f, 10000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		this.gui.update();
		this.appGui.getSimulationView().render();
		
		Display.update();
	}
	
	public void newSimulation() {
		Simulation sim = new Simulation();
		this.appGui.setSimulation(sim);
	}
	
	public void loadSimulation(File simFile) {
		Simulation sim = Persistence.loadSimulation(simFile, this.widgets);
		this.appGui.setSimulation(sim);
	}
	
	public void saveSimulation(File sim) {
		// TODO: save simulation
		System.out.println("TODO: save simulation");
	}
	
	public WidgetLibrary getWidgetLibrary() {
		return this.widgets;
	}
	
	private static class Error extends Exception {
		public Error(String cause, Throwable inner) {
			super(cause, inner);
		}

		private static final long serialVersionUID = -7960105850901234068L;
	}
}

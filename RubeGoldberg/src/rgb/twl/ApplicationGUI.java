package rgb.twl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rgb.Application;
import rgb.Simulation;
import rgb.Simulation.SimulationState;
import rgb.widget.WidgetInfo;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.Menu;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class ApplicationGUI extends Widget {
	public ApplicationGUI(int width, int height, Application app) {
		this.setSize(width, height);
		this.app = app;
		this.callbacks = new AppGuiCallbacks(this);
		
		this.initWidgetList();
		this.initMenu();
		this.initWidgetSelector();
		this.initPropertyEditor();
		this.initControls();
		this.initSimulationView();
		this.add(this.callbacks.fileChooser);
	}
	
	private Application app;
	private ListBox<WidgetInfo<? extends rgb.widget.Widget>> widgetTypes;
	private Widget menu;
	private ComboBox<String> widgetSelector;
	private PropertyEditor properties;
	private Button[] controls;
	private SimulationView simulation;
	private AppGuiCallbacks callbacks;
	
	private void initWidgetList() {
		this.widgetTypes = new ListBox<>(new SimpleChangableListModel<>(this.app.getWidgetLibrary().getWidgets()));
		this.callbacks.createCallback(this.widgetTypes);
		this.add(this.widgetTypes);
	}
	
	private void initMenu() {
		Menu bar = new Menu("MainMenu");
		
		bar.add(this.createFileMenu());
		
		this.menu = bar.createMenuBar();
		this.add(this.menu);
	}
	
	private Menu createFileMenu() {
		Menu file =  new Menu("File");
		
		file.add("New", new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.newMenuOptionCallback();
			}
		});
		file.add("Save", new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.saveMenuOptionCallback();
			}
		});
		file.add("Load", new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.loadMenuOptionCallback();
			}
		});
		file.addSpacer();
		file.add("Exit", new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.exitMenuOptionCallback();
			}
		});
		
		return file;
	}
	
	private void initWidgetSelector() {
		this.widgetSelector = new ComboBox<>();
		this.callbacks.createCallback(this.widgetSelector);
		this.add(this.widgetSelector);
	}
	
	private void initPropertyEditor() {
		this.properties = new PropertyEditor();
		this.add(this.properties);
	}
	
	private void initControls() {
		this.controls = new Button[3];
		this.controls[0] = initPlayControl();
		this.controls[1] = initStopControl();
		this.controls[2] = initResetControl();
		for (int i = 0; i < this.controls.length; i++)
			this.add(this.controls[i]);
	}

	private Button initPlayControl() {
		Button result = new Button("Play");
		result.addCallback(new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.playControlCallback();
			}
		});
		return result;
	}
	
	private Button initStopControl() {
		Button result = new Button("Stop");
		result.addCallback(new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.stopControlCallback();
			}
		});
		return result;
	}
	
	private Button initResetControl() {
		Button result = new Button("Reset");
		result.addCallback(new Runnable() {
			@Override
			public void run() {
				ApplicationGUI.this.callbacks.resetControlCallback();
			}
		});
		return result;
	}
	
	private void initSimulationView() {
		this.simulation = new SimulationView(4);
		this.add(this.simulation);
	}
	
	@Override
	protected void layout() {
		this.menu.setPosition(10, 0);
		this.menu.setSize(this.getInnerWidth(), 20);
		
		final int sideW = 150;
		
		this.widgetTypes.setPosition(20, 30);
		this.widgetTypes.setSize(sideW, 200);
		
		this.widgetSelector.setPosition(20, 240);
		this.widgetSelector.setSize(sideW, 20);
		
		this.properties.setPosition(20, 270);
		this.properties.setSize(sideW, 210);
		
		this.simulation.setPosition(sideW + 40, 30);
		this.simulation.setSize(this.getInnerWidth() - 210, this.getInnerHeight() - 80);
		
		this.layoutControls();
	}
	
	private void layoutControls() {
		int y = this.getInnerHeight() - 40;
		int x = (this.getInnerWidth() - 190) / 2 + 190;
		int w = 50, h = 20;
		int border = 10;
		int off = this.controls.length * (w + border) / 2;
		for (int i = 0; i < this.controls.length; i++) {
			this.controls[i].setPosition(x + (i * (w + border)) - off, y);
			this.controls[i].setSize(w, h);
		}
	}
	
	public void updateWidgetSelector() {
		List<String> names = new ArrayList<>();
		for (rgb.widget.Widget w : this.simulation.getSimulation().getWidgets())
			names.add(w.getName());
		this.widgetSelector.setModel(new SimpleChangableListModel<>(names));
	}

	public void setSimulation(Simulation sim) {
		this.simulation.setSimulation(sim);
		this.updateWidgetSelector();
	}
	
	public SimulationView getSimulationView() {
		return this.simulation;
	}
	
	public void pauseSimulation() {
		this.simulation.getSimulation().setState(SimulationState.PAUSED);
	}
	
	public void playSimulation() {
		this.simulation.getSimulation().setState(SimulationState.RUNNING);
	}
	
	public void loadSimulation(File sim) {
		if (sim == null)
			this.app.newSimulation();
		else
			this.app.loadSimulation(sim);
	}
	
	public void saveSimulation(File sim) {
		this.app.saveSimulation(sim);
	}
}

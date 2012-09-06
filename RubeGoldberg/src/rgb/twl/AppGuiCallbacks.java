package rgb.twl;

import java.io.File;

import rgb.widget.WidgetInfo;

import de.matthiasmann.twl.CallbackWithReason;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.FileSelector;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.FileSelector.Callback;
import de.matthiasmann.twl.ListBox.CallbackReason;
import de.matthiasmann.twl.model.JavaFileSystemModel;

class AppGuiCallbacks {
	public AppGuiCallbacks(ApplicationGUI appGui) {
		this.gui = appGui;
		
		this.initFileChooser();
	}
	
	private ApplicationGUI gui;
	public ResizableFrame fileChooser;
	private FileChooserState fcState;
	private File lastFile;
	
	private void initFileChooser() {
		fileChooser = new ResizableFrame();
		fileChooser.setSize(600, 400);
		fileChooser.setTitle("Choose File");
		fileChooser.setVisible(false);
		fileChooser.add(initSelector());
	}
	
	private Widget initSelector() {
		FileSelector fs = new FileSelector();
		fs.setFileSystemModel(new JavaFileSystemModel());
		fs.setCurrentFolder(new File("C:/Users/Ryan/Documents/Neumont/Summer 2012-Q9/Capstone/rubegoldberg-ws/RubeGoldberg/saves"));
		fs.setSize(600, 400);
		fs.addCallback(new Callback() {
			@Override
			public void filesSelected(Object[] files) {
				AppGuiCallbacks.this.filesSelected(files);
				this.close();
			}
			@Override
			public void canceled() {
				this.close();
			}
			private void close() {
				fileChooser.setVisible(false);
				AppGuiCallbacks.this.fcState = FileChooserState.NONE;
			}
		});
		return fs;
	}
	
	private void filesSelected(Object[] files) {
		switch (this.fcState) {
		case LOADING:
			if (files.length != 1) {
				// TODO: popup instead of sysout
				System.out.println("Choose 1 file");
				return;
			}
			this.lastFile = (File) files[0];
			this.gui.loadSimulation(this.lastFile);
			break;
		case SAVING:
			if (files.length != 1) {
				// TODO: popup instead of sysout
				System.out.println("Choose 1 file");
				return;
			}
			this.lastFile = (File) files[0];
			this.gui.saveSimulation(this.lastFile);
			break;
		default:
			break;
		}
	}
	
	public void playControlCallback() {
		this.gui.playSimulation();
	}

	public void stopControlCallback() {
		this.gui.pauseSimulation();
	}

	public void resetControlCallback() {
		this.gui.pauseSimulation();
		this.gui.loadSimulation(this.lastFile);
	}

	public void newMenuOptionCallback() {
		this.gui.pauseSimulation();
		this.gui.loadSimulation(null);
	}

	public void saveMenuOptionCallback() {
		this.gui.pauseSimulation();
		this.fcState = FileChooserState.SAVING;
		this.fileChooser.setVisible(true);
	}

	public void loadMenuOptionCallback() {
		this.gui.pauseSimulation();
		this.fcState = FileChooserState.LOADING;
		this.fileChooser.setVisible(true);
	}

	public void exitMenuOptionCallback() {
		System.out.println("TODO: exitMenuOptionCallback");
	}
	
	private static enum FileChooserState {
		NONE, SAVING, LOADING
	}

	public void createCallback(final ListBox<WidgetInfo<? extends rgb.widget.Widget>> widgets) {
		widgets.addCallback(new CallbackWithReason<ListBox.CallbackReason>() {
			@Override
			public void callback(CallbackReason cbr) {
				switch (cbr) {
				case MOUSE_DOUBLE_CLICK:
				case KEYBOARD_RETURN:
					try {
						AppGuiCallbacks.this.createNew(widgets.getModel().getEntry(widgets.getSelected()));
					} catch (ReflectiveOperationException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void createNew(WidgetInfo<? extends rgb.widget.Widget> info) throws ReflectiveOperationException {
		rgb.widget.Widget widget = info.getWidgetPersistenceClass().newInstance().create();
		if (widget.getName() == null || widget.getName().isEmpty()) {
			widget.setName("new widget " + (this.gui.getSimulationView().getSimulation().getWidgets().size() + 1));
		}
		if (widget.getId() == 0) {
			widget.setId(this.gui.getSimulationView().getSimulation().getWidgets().size() + 1);
		}
		
		this.gui.getSimulationView().getSimulation().addWidget(widget);
		this.gui.updateWidgetSelector();
	}
	
	public void createCallback(final ComboBox<String> widgets) {
		widgets.addCallback(new Runnable() {
			@Override
			public void run() {
				if (widgets.getSelected() >= 0)
					System.out.println(widgets.getModel().getEntry(widgets.getSelected()));
			}
		});
	}
}
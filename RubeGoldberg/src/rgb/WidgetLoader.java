package rgb;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import rgb.widget.Widget;
import rgb.widget.WidgetInfo;

public class WidgetLoader {
	
	private static URLClassLoader getLoader(URL[] urls) {
		try {
			return URLClassLoader.newInstance(urls, WidgetLoader.class.getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<WidgetInfo<? extends Widget>> loadWidgets(String location) throws FileNotFoundException {
		File widgetFolder = new File(location);
		if (!widgetFolder.exists()) {
			throw new FileNotFoundException("That folder doesn't exist");
		}
		
		File[] widgets = widgetFolder.listFiles();
		ArrayList<WidgetInfo<? extends Widget>> widgetInfos = new ArrayList<>();
		for (File widget : widgets) {
			try {
				widgetInfos.add(loadWidgetInfo(widget));
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Unable to load widget located in " + widget.getAbsolutePath());
			}
		}
		return widgetInfos;
	}
	
	@SuppressWarnings("unchecked")
	private static WidgetInfo<? extends Widget> loadWidgetInfo(File widget) throws Exception {
		URLClassLoader loader = getLoader(new URL[] {widget.toURI().toURL()});
		Class<? extends WidgetInfo<? extends Widget>> infoClass = (Class<? extends WidgetInfo<? extends Widget>>) Class.forName("info.Info", true, loader);
		return infoClass.newInstance();
	}
}

package rgb;

import java.util.List;

import rgb.widget.Widget;
import rgb.widget.WidgetInfo;

public class WidgetLibrary {
	public WidgetLibrary(List<WidgetInfo<? extends Widget>> widgets) {
		this.widgets = widgets;
	}
	
	private List<WidgetInfo<? extends Widget>> widgets;
	
	public List<WidgetInfo<? extends Widget>> getWidgets() {
		return this.widgets;
	}
	
	public WidgetInfo<? extends Widget> getWidgetInfo(String clsName) {
		for (WidgetInfo<? extends Widget> wi : this.widgets) {
			if (wi.getWidgetClass().getName().equals(clsName))
				return wi;
		}
		return null;
	}
}

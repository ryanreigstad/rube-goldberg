package info;

import rgb.widget.WidgetInfo;
import rgb.widget.box.BoxWidget;
import rgb.widget.box.BoxWidgetPersistence;

public class Info implements WidgetInfo<BoxWidget> {

	@Override
	public String getName() {
		return "Box";
	}

	@Override
	public Class<BoxWidget> getWidgetClass() {
		return BoxWidget.class;
	}

	@Override
	public Class<BoxWidgetPersistence> getWidgetPersistenceClass() {
		return BoxWidgetPersistence.class;
	}
	
	@Override
	public String toString() {
		return this.getName() + " Shape";
	}
}

package info;

import rgb.widget.WidgetInfo;
import rgb.widget.box.BoxWidget;
import rgb.widget.box.BoxWidgetFactory;

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
	public Class<BoxWidgetFactory> getWidgetFactoryClass() {
		return BoxWidgetFactory.class;
	}
}

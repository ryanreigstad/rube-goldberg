package info;

import rgb.widget.WidgetInfo;
import rgb.widget.sphere.SphereWidget;
import rgb.widget.sphere.SphereWidgetFactory;

public class Info implements WidgetInfo<SphereWidget> {

	@Override
	public String getName() {
		return "Sphere";
	}

	@Override
	public Class<SphereWidget> getWidgetClass() {
		return SphereWidget.class;
	}

	@Override
	public Class<SphereWidgetFactory> getWidgetFactoryClass() {
		return SphereWidgetFactory.class;
	}

}

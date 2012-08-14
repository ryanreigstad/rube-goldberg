package info;

import rgb.widget.WidgetInfo;
import rgb.widget.cylinder.CylinderWidget;
import rgb.widget.cylinder.CylinderWidgetPersistence;

public class Info implements WidgetInfo<CylinderWidget> {

	@Override
	public String getName() {
		return "Cylinder";
	}

	@Override
	public Class<CylinderWidget> getWidgetClass() {
		return CylinderWidget.class;
	}

	@Override
	public Class<CylinderWidgetPersistence> getWidgetPersistenceClass() {
		return CylinderWidgetPersistence.class;
	}

}

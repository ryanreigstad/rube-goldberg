package rgb.widget;

public interface WidgetInfo<T extends Widget> {
	public String getName();
	// public Image(?) getIcon();
	
	public Class<T> getWidgetClass();
	public Class<? extends WidgetFactory<T>> getWidgetFactoryClass();
}

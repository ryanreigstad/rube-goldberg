package rgb.widget;

import org.w3c.dom.Node;

public interface WidgetFactory<T extends Widget> {
	public T create(Node node);
	public Node persist(T widget);
}

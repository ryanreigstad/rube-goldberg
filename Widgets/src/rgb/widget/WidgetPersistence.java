package rgb.widget;

import org.w3c.dom.Node;

public interface WidgetPersistence<T extends Widget> {
	public T create();
	public T create(Node node);
	public Node persist(T widget);
}

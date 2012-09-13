package rgb.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface WidgetPersistence<T extends Widget> {
	public T create();
	public T create(Node node);
	public Element persist(Document doc, Element node, Widget widget);
}

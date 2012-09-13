package rgb.persistence;

import java.io.File;
import java.util.List;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rgb.Simulation;
import rgb.WidgetLibrary;
import rgb.widget.Widget;
import rgb.widget.WidgetInfo;
import rgb.widget.WidgetPersistence;

public class Persistence {
	public static Simulation loadSimulation(File file, WidgetLibrary widgets) {
		try {
			Document doc = getDoc(file);
			return buildSimulation(doc, widgets);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Document getDoc(File file) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(file);
	}
	
	private static Simulation buildSimulation(Node root, WidgetLibrary widgetLibrary) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression worldSizeExpr = xpath.compile("/simulation/world/@size");
		XPathExpression worldGravExpr = xpath.compile("/simulation/world/@gravity");
		
		Vector3f size = parseV3f((String) worldSizeExpr.evaluate(root, XPathConstants.STRING));
		Vector3f grav = parseV3f((String) worldGravExpr.evaluate(root, XPathConstants.STRING));
		Simulation simulation = new Simulation(size, grav);
		
		XPathExpression widgetExpr = xpath.compile("/simulation/widgets/*");
		NodeList widgetNodes = (NodeList) widgetExpr.evaluate(root, XPathConstants.NODESET);
		for (int i = 0; i < widgetNodes.getLength(); i++) {
			Node widgetNode = widgetNodes.item(i);
			Widget widget = buildWidget(widgetNode, widgetLibrary, xpath);
			simulation.addWidget(widget);
		}
		
		return simulation;
	}
	
	private static Widget buildWidget(Node widgetNode, WidgetLibrary widgetLibrary, XPath xpath) throws Exception {
		XPathExpression widgetIdExpr = xpath.compile("./@id");
		XPathExpression widgetNameExpr = xpath.compile("./@name");
		XPathExpression widgetTypeExpr = xpath.compile("./@type");

		int widgetId = Integer.parseInt((String) widgetIdExpr.evaluate(widgetNode, XPathConstants.STRING));
		String widgetName = (String) widgetNameExpr.evaluate(widgetNode, XPathConstants.STRING);
		String widgetClass = (String) widgetTypeExpr.evaluate(widgetNode, XPathConstants.STRING);
		
		WidgetInfo<? extends Widget> wi = widgetLibrary.getWidgetInfo(widgetClass);
		Widget w = wi.getWidgetPersistenceClass().newInstance().create(widgetNode);
		w.setId(widgetId);
		w.setName(widgetName);
		return w;
	}
	
	public static void saveSimulation(File file, Simulation sim, WidgetLibrary widgets) {
		Document doc = createNewDocument();
		if (doc != null)
			doc.appendChild(createXml(doc, doc.createElement("simulation"), sim, widgets));
		else {
			System.out.println("couldn't make document");
			return;
		}
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			System.out.println("couldn't save to file");
			e.printStackTrace();
		}
	}
	
	private static Document createNewDocument() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			return null;
		}
	}
	
	private static Element createXml(Document doc, Element root, Simulation sim, WidgetLibrary widgets) {
		root.appendChild(createWorldNode(doc, sim));
		root.appendChild(createWidgetsNode(doc, sim, widgets));
		return root;
	}
	
	private static Element createWorldNode(Document doc, Simulation sim) {
		Element result = doc.createElement("world");
		
		result.setAttributeNode(createAttr(doc, "size", "2048,2048,2048"));
		result.setAttributeNode(createAttr(doc, "gravity", v3fToString(sim.getWorld().getGravity(new Vector3f()))));
		
		return result;
	}
	
	private static Element createWidgetsNode(Document doc, Simulation sim, WidgetLibrary widgetLib) {
		Element node = doc.createElement("widgets");
		
		List<Widget> widgets = sim.getWidgets();
		for (Widget w : widgets) {
			Class<? extends WidgetPersistence<? extends Widget>> persistenceClass = widgetLib.getWidgetPersistenceClass(w.getClass());
			try {
				WidgetPersistence<? extends Widget> persistence = persistenceClass.newInstance();
				Element widgetNode = persistence.persist(doc, prepWidgetNode(doc, w), w);
				node.appendChild(widgetNode);
			} catch (Exception e) {
				System.err.println("couldn't save widget with name\"" + w.getName() + "\"");
				e.printStackTrace();
			}
		}
		
		return node;
	}
	
	private static Element prepWidgetNode(Document doc, Widget w) {
		Element n = doc.createElement("widget");

		n.setAttributeNode(createAttr(doc, "id", "" + w.getId()));
		n.setAttributeNode(createAttr(doc, "name", w.getName()));
		n.setAttributeNode(createAttr(doc, "type", w.getClass().getName()));
		
		return n;
	}
	
	private static Attr createAttr(Document doc, String name, String value) {
		Attr attr = doc.createAttribute(name);
		attr.setValue(value);
		return attr;
	}
	
	private static Vector3f parseV3f(String v) {
		Vector3f result = new Vector3f();
		result.x = Float.parseFloat(v.split(",")[0].trim());
		result.y = Float.parseFloat(v.split(",")[1].trim());
		result.z = Float.parseFloat(v.split(",")[2].trim());
		return result;
	}
	
	private static String v3fToString(Vector3f v) {
		return "" + v.x + "," + v.y + "," + v.z;
	}
}

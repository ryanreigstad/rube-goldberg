package rgb.persistence;

import java.io.File;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rgb.Simulation;
import rgb.WidgetLibrary;
import rgb.widget.Widget;
import rgb.widget.WidgetInfo;

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
	
	private static Vector3f parseV3f(String v) {
		Vector3f result = new Vector3f();
		result.x = Float.parseFloat(v.split(",")[0].trim());
		result.y = Float.parseFloat(v.split(",")[1].trim());
		result.z = Float.parseFloat(v.split(",")[2].trim());
		return result;
	}
}

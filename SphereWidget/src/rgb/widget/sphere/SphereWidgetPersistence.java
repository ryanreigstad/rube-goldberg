package rgb.widget.sphere;

import javax.vecmath.Vector3f;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rgb.widget.Widget;
import rgb.widget.WidgetPersistence;
import rgb.widget.util.PersistenceUtil;

import com.bulletphysics.dynamics.RigidBody;

public class SphereWidgetPersistence implements WidgetPersistence<SphereWidget> {
	
	@Override
	public SphereWidget create() {
		return new SphereWidget(new Vector3f(), new Vector3f(), new Vector3f(), 0.5f, 1, 0.5f, 0.5f);
	}

	@Override
	public SphereWidget create(Node node) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression locationExpr = xpath.compile("./param[@name='location']/text()");
			XPathExpression linearVeloctiyExpr = xpath.compile("./param[@name='linearVeloctiy']/text()");
			XPathExpression angularVelocityExpr = xpath.compile("./param[@name='angularVelocity']/text()");
			XPathExpression radiusExpr = xpath.compile("./param[@name='radius']/text()");
			XPathExpression massExpr = xpath.compile("./param[@name='mass']/text()");
			XPathExpression restitutionExpr = xpath.compile("./param[@name='restitution']/text()");
			XPathExpression frictionExpr = xpath.compile("./param[@name='friction']/text()");

			Vector3f location = PersistenceUtil.parseV3f((String) locationExpr.evaluate(node, XPathConstants.STRING));
			Vector3f linearVelocity = PersistenceUtil.parseV3f((String) linearVeloctiyExpr.evaluate(node, XPathConstants.STRING));
			Vector3f angularVelocity = PersistenceUtil.parseV3f((String) angularVelocityExpr.evaluate(node, XPathConstants.STRING));
			float radius = Float.parseFloat((String) radiusExpr.evaluate(node, XPathConstants.STRING));
			float mass = Float.parseFloat((String) massExpr.evaluate(node, XPathConstants.STRING));
			float restitution = Float.parseFloat((String) restitutionExpr.evaluate(node, XPathConstants.STRING));
			float friction = Float.parseFloat((String) frictionExpr.evaluate(node, XPathConstants.STRING));
			
			return new SphereWidget(location, linearVelocity, angularVelocity, radius, mass, restitution, friction);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Element persist(Document doc, Element widgetNode, Widget widget) {
		SphereWidget sphere = (SphereWidget) widget;
		RigidBody sphereBody = sphere.getRigidBody();

		widgetNode.appendChild(persistParam(doc, "location", v3fToString(sphereBody.getCenterOfMassPosition(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "linearVeloctiy", v3fToString(sphereBody.getLinearVelocity(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "angularVelocity", v3fToString(sphereBody.getAngularVelocity(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "radius", "" + sphere.getRadius()));
		widgetNode.appendChild(persistParam(doc, "mass", "" + (1f / sphereBody.getInvMass())));
		widgetNode.appendChild(persistParam(doc, "restitution", "" + sphereBody.getRestitution()));
		widgetNode.appendChild(persistParam(doc, "friction", "" + sphereBody.getFriction()));
		
		return widgetNode;
	}
	
	private static Element persistParam(Document doc, String name, String textContent) {
		Element param = doc.createElement("param");
		
		Attr attr = doc.createAttribute("name");
		attr.setValue(name);
		param.setAttributeNode(attr);
		
		param.setTextContent(textContent);
		return param;
	}
	
	private static String v3fToString(Vector3f v) {
		return "" + v.x + "," + v.y + "," + v.z;
	}
}

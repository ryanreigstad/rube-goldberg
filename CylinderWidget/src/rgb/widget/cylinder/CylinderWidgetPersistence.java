package rgb.widget.cylinder;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import rgb.widget.Widget;
import rgb.widget.WidgetPersistence;
import rgb.widget.renderable.RenderableBase;
import rgb.widget.util.PersistenceUtil;

public class CylinderWidgetPersistence implements WidgetPersistence<CylinderWidget> {
	
	@Override
	public CylinderWidget create() {
		return new CylinderWidget(new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(), 0.5f, 0.5f, 1, 0.5f, 0.5f);
	}

	@Override
	public CylinderWidget create(Node node) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression locationExpr = xpath.compile("./param[@name='location']/text()");
			XPathExpression orientationExpr = xpath.compile("./param[@name='orientation']/text()");
			XPathExpression linearVeloctiyExpr = xpath.compile("./param[@name='linearVeloctiy']/text()");
			XPathExpression angularVelocityExpr = xpath.compile("./param[@name='angularVelocity']/text()");
			XPathExpression radiusExpr = xpath.compile("./param[@name='radius']/text()");
			XPathExpression heightExpr = xpath.compile("./param[@name='height']/text()");
			XPathExpression massExpr = xpath.compile("./param[@name='mass']/text()");
			XPathExpression restitutionExpr = xpath.compile("./param[@name='restitution']/text()");
			XPathExpression frictionExpr = xpath.compile("./param[@name='friction']/text()");

			Vector3f location = PersistenceUtil.parseV3f((String) locationExpr.evaluate(node, XPathConstants.STRING));
			Vector3f orientation = PersistenceUtil.parseV3f((String) orientationExpr.evaluate(node, XPathConstants.STRING));
			Vector3f linearVelocity = PersistenceUtil.parseV3f((String) linearVeloctiyExpr.evaluate(node, XPathConstants.STRING));
			Vector3f angularVelocity = PersistenceUtil.parseV3f((String) angularVelocityExpr.evaluate(node, XPathConstants.STRING));
			float radius = Float.parseFloat((String) radiusExpr.evaluate(node, XPathConstants.STRING));
			float height = Float.parseFloat((String) heightExpr.evaluate(node, XPathConstants.STRING));
			float mass = Float.parseFloat((String) massExpr.evaluate(node, XPathConstants.STRING));
			float restitution = Float.parseFloat((String) restitutionExpr.evaluate(node, XPathConstants.STRING));
			float friction = Float.parseFloat((String) frictionExpr.evaluate(node, XPathConstants.STRING));
			
			return new CylinderWidget(location, orientation, linearVelocity, angularVelocity, radius, height, mass, restitution, friction);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Element persist(Document doc, Element widgetNode, Widget widget) {
		CylinderWidget cylinder = (CylinderWidget) widget;
		RigidBody cylinderBody = cylinder.getRigidBody();

		widgetNode.appendChild(persistParam(doc, "location", v3fToString(cylinderBody.getCenterOfMassPosition(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "orientation", v3fToString(RenderableBase.toAngles(cylinderBody.getWorldTransform(new Transform()).getRotation(new Quat4f())))));
		widgetNode.appendChild(persistParam(doc, "linearVeloctiy", v3fToString(cylinderBody.getLinearVelocity(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "angularVelocity", v3fToString(cylinderBody.getAngularVelocity(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "radius", "" + cylinder.getRadius()));
		widgetNode.appendChild(persistParam(doc, "height", "" + cylinder.getHeight()));
		widgetNode.appendChild(persistParam(doc, "mass", "" + (1f / cylinderBody.getInvMass())));
		widgetNode.appendChild(persistParam(doc, "restitution", "" + cylinderBody.getRestitution()));
		widgetNode.appendChild(persistParam(doc, "friction", "" + cylinderBody.getFriction()));
		
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

package rgb.widget.box;

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

public class BoxWidgetPersistence implements WidgetPersistence<BoxWidget> {
	
	@Override
	public BoxWidget create() {
		return new BoxWidget(new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(0.5f, 0.5f, 0.5f), 1, 0.5f, 0.5f);
	}

	@Override
	public BoxWidget create(Node node) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression locationExpr = xpath.compile("./param[@name='location']/text()");
			XPathExpression orientationExpr = xpath.compile("./param[@name='orientation']/text()");
			XPathExpression linearVeloctiyExpr = xpath.compile("./param[@name='linearVeloctiy']/text()");
			XPathExpression angularVelocityExpr = xpath.compile("./param[@name='angularVelocity']/text()");
			XPathExpression halfExtentsExpr = xpath.compile("./param[@name='halfExtents']/text()");
			XPathExpression massExpr = xpath.compile("./param[@name='mass']/text()");
			XPathExpression restitutionExpr = xpath.compile("./param[@name='restitution']/text()");
			XPathExpression frictionExpr = xpath.compile("./param[@name='friction']/text()");

			Vector3f location = PersistenceUtil.parseV3f((String) locationExpr.evaluate(node, XPathConstants.STRING));
			Vector3f orientation = PersistenceUtil.parseV3f((String) orientationExpr.evaluate(node, XPathConstants.STRING));
			Vector3f linearVelocity = PersistenceUtil.parseV3f((String) linearVeloctiyExpr.evaluate(node, XPathConstants.STRING));
			Vector3f angularVelocity = PersistenceUtil.parseV3f((String) angularVelocityExpr.evaluate(node, XPathConstants.STRING));
			Vector3f halfExtents = PersistenceUtil.parseV3f((String) halfExtentsExpr.evaluate(node, XPathConstants.STRING));
			float mass = Float.parseFloat((String) massExpr.evaluate(node, XPathConstants.STRING));
			float restitution = Float.parseFloat((String) restitutionExpr.evaluate(node, XPathConstants.STRING));
			float friction = Float.parseFloat((String) frictionExpr.evaluate(node, XPathConstants.STRING));
			
			return new BoxWidget(location, orientation, linearVelocity, angularVelocity, halfExtents, mass, restitution, friction);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Element persist(Document doc, Element widgetNode, Widget widget) {
		BoxWidget box = (BoxWidget) widget;
		RigidBody boxBody = box.getRigidBody();

		widgetNode.appendChild(persistParam(doc, "location", v3fToString(boxBody.getCenterOfMassPosition(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "orientation", v3fToString(RenderableBase.toAngles(boxBody.getWorldTransform(new Transform()).getRotation(new Quat4f())))));
		widgetNode.appendChild(persistParam(doc, "linearVeloctiy", v3fToString(boxBody.getLinearVelocity(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "angularVelocity", v3fToString(boxBody.getAngularVelocity(new Vector3f()))));
		widgetNode.appendChild(persistParam(doc, "halfExtents", v3fToString(box.getHalfExtents())));
		widgetNode.appendChild(persistParam(doc, "mass", "" + (1f / boxBody.getInvMass())));
		widgetNode.appendChild(persistParam(doc, "restitution", "" + boxBody.getRestitution()));
		widgetNode.appendChild(persistParam(doc, "friction", "" + boxBody.getFriction()));
		
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

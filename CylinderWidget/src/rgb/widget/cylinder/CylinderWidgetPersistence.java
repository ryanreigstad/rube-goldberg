package rgb.widget.cylinder;

import javax.vecmath.Vector3f;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

import rgb.widget.WidgetPersistence;
import rgb.widget.util.PersistenceUtil;

public class CylinderWidgetPersistence implements WidgetPersistence<CylinderWidget> {
	
	@Override
	public CylinderWidget create() {
		return new CylinderWidget(new Vector3f(), new Vector3f(), 0.5f, 0.5f, 1, 0.5f, 0.5f);
	}

	@Override
	public CylinderWidget create(Node node) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression locationExpr = xpath.compile("./param[@name='location']/text()");
			XPathExpression orientationExpr = xpath.compile("./param[@name='orientation']/text()");
			XPathExpression radiusExpr = xpath.compile("./param[@name='radius']/text()");
			XPathExpression heightExpr = xpath.compile("./param[@name='height']/text()");
			XPathExpression massExpr = xpath.compile("./param[@name='mass']/text()");
			XPathExpression restitutionExpr = xpath.compile("./param[@name='restitution']/text()");
			XPathExpression frictionExpr = xpath.compile("./param[@name='friction']/text()");

			Vector3f location = PersistenceUtil.parseV3f((String) locationExpr.evaluate(node, XPathConstants.STRING));
			Vector3f orientation = PersistenceUtil.parseV3f((String) orientationExpr.evaluate(node, XPathConstants.STRING));
			float radius = Float.parseFloat((String) radiusExpr.evaluate(node, XPathConstants.STRING));
			float height = Float.parseFloat((String) heightExpr.evaluate(node, XPathConstants.STRING));
			float mass = Float.parseFloat((String) massExpr.evaluate(node, XPathConstants.STRING));
			float restitution = Float.parseFloat((String) restitutionExpr.evaluate(node, XPathConstants.STRING));
			float friction = Float.parseFloat((String) frictionExpr.evaluate(node, XPathConstants.STRING));
			
			return new CylinderWidget(location, orientation, radius, height, mass, restitution, friction);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Node persist(CylinderWidget widget) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not Implemented");
	}

}

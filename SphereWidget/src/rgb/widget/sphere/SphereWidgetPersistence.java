package rgb.widget.sphere;

import javax.vecmath.Vector3f;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

import rgb.widget.WidgetPersistence;
import rgb.widget.util.PersistenceUtil;

public class SphereWidgetPersistence implements WidgetPersistence<SphereWidget> {

	@Override
	public SphereWidget create(Node node) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression locationExpr = xpath.compile("./param[@name='location']/text()");
			XPathExpression radiusExpr = xpath.compile("./param[@name='radius']/text()");
			XPathExpression massExpr = xpath.compile("./param[@name='mass']/text()");
			XPathExpression restitutionExpr = xpath.compile("./param[@name='restitution']/text()");
			XPathExpression frictionExpr = xpath.compile("./param[@name='friction']/text()");

			Vector3f location = PersistenceUtil.parseV3f((String) locationExpr.evaluate(node, XPathConstants.STRING));
			float radius = Float.parseFloat((String) radiusExpr.evaluate(node, XPathConstants.STRING));
			float mass = Float.parseFloat((String) massExpr.evaluate(node, XPathConstants.STRING));
			float restitution = Float.parseFloat((String) restitutionExpr.evaluate(node, XPathConstants.STRING));
			float friction = Float.parseFloat((String) frictionExpr.evaluate(node, XPathConstants.STRING));
			
			return new SphereWidget(location, radius, mass, restitution, friction);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Node persist(SphereWidget widget) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not Implemented");
	}
}

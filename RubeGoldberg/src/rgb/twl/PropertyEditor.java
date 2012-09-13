package rgb.twl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rgb.widget.Widget;
import rgb.widget.WidgetWithProperties;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

public class PropertyEditor extends de.matthiasmann.twl.Widget {
	public PropertyEditor() {
		this.properties = new ArrayList<>();
	}
	
	private Widget currentWidget;
	private List<PropertyLine> properties;

	public void init(Widget widget) {
		if (this.currentWidget != null && this.currentWidget.equals(widget)) {
			update();
		} else {
			this.properties.clear();
			this.removeAllChildren();
			this.currentWidget = widget;
			
			List<Property> props = PropertyEditorReflectionHelper.getProperties(widget.getClass());
			for (Property prop : props) {
				PropertyLine line = new PropertyLine(widget, prop);
				this.properties.add(line);
				this.add(line);
			}
		}
	}
	
	public void update() {
		for (PropertyLine line : this.properties)
			line.update();
	}
	
	@Override
	protected void layout() {
		final int h = 30;
		this.setSize(this.getInnerWidth(), this.properties.size() * h);
		for (int i = 0; i < this.properties.size(); i++) {
			this.properties.get(i).setPosition(0 + this.getX(), i * h + this.getY());
			this.properties.get(i).setSize(this.getInnerWidth() - 10, h);
		}
	}
	
	public static class PropertyLine extends de.matthiasmann.twl.Widget {
		public PropertyLine(Widget widget, Property prop) {
			this.widget = widget;
			this.property = prop;
			this.initTWL(this.property.name);
			
			this.update();
		}
		
		private Widget widget;
		private Property property;
		private Label name;
		private EditField editField;
		
		private void initTWL(String name) {
			this.name = new Label(name);
			this.add(this.name);
			this.editField = new EditField();
			this.editField.addCallback(new Callback() {
				@Override
				public void callback(int key) {
					if (key == Event.KEY_RETURN) {
						String value = PropertyLine.this.editField.getText();
						Object obj = castTo(value, PropertyLine.this.property.type);
						PropertyEditorReflectionHelper.setPropertyValue(PropertyLine.this.widget, PropertyLine.this.property.name, obj);
					}
				}
			});
			this.add(this.editField);
		}
		
		public void update() {
			this.editField.setText(PropertyEditorReflectionHelper.getPropertyValue(this.widget, this.property.name));
		}
		
		@Override
		protected void layout() {
			this.name.setPosition(5 + this.getX(), 5 + this.getY());
			this.name.setSize(this.getInnerWidth() / 2 - 5, this.getInnerHeight());
			
			this.editField.setPosition(this.getInnerWidth() / 2 + 10 + this.getX(), 5 + this.getY());
			this.editField.setSize(this.getInnerWidth() / 2 - 15, this.getInnerHeight());
		}
		
		@SuppressWarnings("unchecked")
		private static Object castTo(String str, Class<?> clz) {
			// TODO: this needs to convert to the correct type
			if (clz.equals(String.class))
				return str;
			if (Number.class.isAssignableFrom(clz))
				return castToNumber(str, (Class<? extends Number>) clz);
			return null;
		}
		
		private static Number castToNumber(String str, Class<? extends Number> clz) {
			if (clz.equals(Float.class))
				return Float.parseFloat(str);
			if (clz.equals(Integer.class))
				return Integer.parseInt(str);
			// TODO: double, long, etc
			return null;
		}
	}
	
	public static class Property {
		public Property(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}
		
		public String name;
		public Class<?> type;
	}
	
	private static class PropertyEditorReflectionHelper {
		public static List<Property> getProperties(Class<? extends Widget> clz) {
			WidgetWithProperties props = clz.getAnnotation(WidgetWithProperties.class);
			List<Property> propList = new ArrayList<>();
			for (String p : props.properties())
				propList.add(new Property(p, getTypeOfProperty(clz, p)));
			return propList;
		}
		
		private static Class<?> getTypeOfProperty(Class<?> clz, String propName) {
			try {
				if (propName.length() > 1) {
					return clz.getMethod("get" + propName.substring(0, 1).toUpperCase() + propName.substring(1)).getReturnType();
				} else {
					return clz.getMethod("get" + propName.toUpperCase()).getReturnType();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static String getPropertyValue(Widget w, String name) {
			try {
				if (name.length() > 1) {
					return w.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(w).toString();
				} else {
					return w.getClass().getMethod("get" + name.toUpperCase()).invoke(w).toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "null";
			}
		}
		
		public static void setPropertyValue(Widget w, String name, Object value) {
			try {
				if (name.length() > 1) {
					Method m = w.getClass().getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), value.getClass());
					m.invoke(w, value);
				} else {
					Method m = w.getClass().getMethod("set" + name.toUpperCase(), value.getClass());
					m.invoke(w, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

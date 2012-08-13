package rgb.widget.physical;

import java.util.List;

public interface CompoundShape extends PhysicalWidget {
	public List<SimpleShape> getParts();
}

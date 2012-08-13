package rgb.widget.physical;

import rgb.widget.renderable.Renderable;

import com.bulletphysics.dynamics.RigidBody;

public interface SimpleShape extends PhysicalWidget, Renderable {
	public RigidBody getRigidBody();
	
	// TODO: get and set for physical properties
}

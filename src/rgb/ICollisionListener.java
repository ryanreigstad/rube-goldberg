package rgb;

import java.util.EventListener;

import rgb.bodies.PhysicalBody;

public interface ICollisionListener extends EventListener {
	public PhysicalBody getPhysicalBody();
	public CollisionHandler getCollisionHandler();
	public void setCollisionHandler(CollisionHandler handler);
}

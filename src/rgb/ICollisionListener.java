package rgb;

import java.util.EventListener;

public interface ICollisionListener extends EventListener {
	public CollisionHandler getCollisionHandler();
	public void setCollisionHandler(CollisionHandler handler);
}

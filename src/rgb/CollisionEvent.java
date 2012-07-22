package rgb;
import java.util.EventObject;

import com.bulletphysics.collision.dispatch.CollisionObject;


public class CollisionEvent extends EventObject {
	private static final long serialVersionUID = 3966443286293777242L;
	
	public CollisionEvent(Object source) {
		super(source);
	}
	
	public CollisionObject bodyA;
	public CollisionObject bodyB;
}

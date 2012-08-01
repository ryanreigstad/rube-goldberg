package rgb;

import java.util.ArrayList;
import java.util.List;

import rgb.util.DataPair;

import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;

public class CollisionEventDispatcher {
	
	public CollisionEventDispatcher() {
		listeners = new ArrayList<>();
	}
	
	private List<CollisionObject> listeners;
	
	public void addListener(CollisionObject object) {
		ICollisionListener listener;
		if (object.getUserPointer() instanceof ICollisionListener)
			listener = (ICollisionListener) object.getUserPointer();
		else
			throw new IllegalArgumentException("Cannot add CollisionObject as a listener unless the user pointer is a ICollisionListener");
		
		if (listener.getCollisionHandler() == null)
			throw new NullPointerException("ICollisionListener.getCollisionHandler() must not return null");
		this.listeners.add(object);
	}
	
	public void removeListenerFor(CollisionObject object) {
		this.listeners.remove(object);
	}
	
	private List<DataPair<Object, Object>> prevContacts = new ArrayList<>();
	public void checkCollisions(Dispatcher dispatch) {
		List<DataPair<Object, Object>> newContacts = new ArrayList<>();
		for (int i = 0; i < dispatch.getNumManifolds(); i++) {
			PersistentManifold manifold = dispatch.getManifoldByIndexInternal(i);
			
			DataPair<Object, Object> contact = checkManifoldPoints(manifold);
			if (contact != null) {
				newContacts.add(contact);
			}
		}
		prevContacts = newContacts;
	}
	
	private DataPair<Object, Object> checkManifoldPoints(PersistentManifold manifold) {
		for (int j = manifold.getNumContacts() - 1; j >= 0; j--) {
			ManifoldPoint point = manifold.getContactPoint(j);
			if (point.getDistance() < 0) {
				DataPair<Object, Object> pair = new DataPair<>(manifold.getBody0(), manifold.getBody1());
				if (prevContacts.contains(pair))
					this.fireContact(makeEvent(manifold, point));
				else
					this.fireCollide(makeEvent(manifold, point));
				return pair;
			}
		}
		return null;
	}
	
	private CollisionEvent makeEvent(PersistentManifold manifold, ManifoldPoint point) {
		CollisionEvent event = new CollisionEvent(this);

		event.bodyA = (CollisionObject) manifold.getBody0();
		event.bodyB = (CollisionObject) manifold.getBody1();
		
		return event;
	}
	
	private void fireCollide(CollisionEvent e) {
		if (this.listeners.contains(e.bodyA))
			((ICollisionListener) e.bodyA).getCollisionHandler().onCollide(e);
		if (this.listeners.contains(e.bodyB))
			((ICollisionListener) e.bodyB).getCollisionHandler().onCollide(e);
	}
	
	private void fireContact(CollisionEvent e) {
		if (this.listeners.contains(e.bodyA))
			((ICollisionListener) e.bodyA).getCollisionHandler().onContact(e);
		if (this.listeners.contains(e.bodyB))
			((ICollisionListener) e.bodyB).getCollisionHandler().onContact(e);
	}
}

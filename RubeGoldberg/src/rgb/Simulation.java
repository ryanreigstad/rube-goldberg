package rgb;

import javax.vecmath.Vector3f;

import rgb.gui.Renderer;
import rgb.widget.Widget;
import rgb.widget.physical.SimpleShape;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class Simulation {
	public Simulation() {
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.world.destroy();
		
		super.finalize();
	}
	
	public void initWorld(Vector3f size, Vector3f grav) {
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.world = new DiscreteDynamicsWorld(
				new CollisionDispatcher(collisionConfig),
				new AxisSweep3(new Vector3f(-size.x / 2f, -size.y / 2f, -size.z / 2f),
					new Vector3f(size.x / 2f, size.y / 2f, size.z / 2f), 1024),
				new SequentialImpulseConstraintSolver(),
				collisionConfig);
		this.world.setGravity(grav);
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	private Renderer renderer;
	private DiscreteDynamicsWorld world;
	
	public void addWidget(Widget widget) {
		if (widget instanceof SimpleShape) {
			SimpleShape ws = (SimpleShape) widget;
			this.world.addRigidBody(ws.getRigidBody());
		}
		// NOTE: different widgets need to be added differently
//		else if (widget instanceof CompoundShape) {
//			CompoundShape cw = (CompoundShape) widget;
//			for (SimpleShape simple : cw.getParts())
//				this.world.addRigidBody(simple.getRigidBody());
//		}
	}
	
	public void stepSimulation(float seconds) {
		this.world.stepSimulation(seconds);
	}
	
	public void drawFrame() {
		this.renderer.drawScene(this.world);
	}
}

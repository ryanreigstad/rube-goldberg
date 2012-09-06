package rgb;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import rgb.widget.Widget;
import rgb.widget.physical.SimpleShape;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class Simulation {
	public Simulation() {
		this(new Vector3f(2048, 2048, 2048), new Vector3f(0, -9.8f, 0));
	}
	
	public Simulation(Vector3f size, Vector3f grav) {
		this.state = SimulationState.PAUSED;
		this.widgets = new ArrayList<>();
		this.initWorld(size, grav);
	}
	
	private DiscreteDynamicsWorld world;
	private List<Widget> widgets;
	private SimulationState state;
	
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
					new Vector3f(size.x / 2f, size.y / 2f, size.z / 2f), 4096),
				new SequentialImpulseConstraintSolver(),
				collisionConfig);
		this.world.setGravity(grav);
	}
	
	public void addWidget(Widget widget) {
		this.widgets.add(widget);
		if (widget instanceof SimpleShape) {
			SimpleShape ws = (SimpleShape) widget;
			this.world.addRigidBody(ws.getRigidBody());
		}
		// TODO: different widgets need to be added differently
//		else if (widget instanceof CompoundShape) {
//			CompoundShape cw = (CompoundShape) widget;
//			for (PhysicalWidget widget : cw.getParts())
//				this.addWidget(widget);
//		}
	}
	
	public void stepSimulation(float seconds) {
		switch (this.state) {
		case PAUSED:
			break;
		case RUNNING:
			this.updateWidgets();
			this.world.stepSimulation(seconds);
			break;
		default:
			break;
		}
	}
	
	private void updateWidgets() {
		for (Widget w : this.widgets)
			w.update();
	}
	
	public DynamicsWorld getWorld() {
		return this.world;
	}
	
	public void setState(SimulationState state) {
		this.state = state;
	}
	
	public SimulationState getState() {
		return this.state;
	}
	
	public List<Widget> getWidgets() {
		return this.widgets;
	}
	
	public static enum SimulationState {
		RUNNING, PAUSED, 
	}
}

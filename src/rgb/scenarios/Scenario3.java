package rgb.scenarios;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;

import rgb.bodies.primatives.Box;
import rgb.bodies.primatives.Cube;
import rgb.bodies.primatives.Sphere;
import rgb.draw.Camera;
import rgb.draw.GLDrawer;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Scenario3 {
	public static void run(int steps) {
		try {
			GLDrawer.init(900, 600, 45f);
			GLDrawer.setCamera(new Camera(new Vector3f(-3, 3, 15), null));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		new Scenario3().simulate(steps);
		GLDrawer.shutdown();
	}

	private Scenario3() {
		this.initWorld();
		this.world.addRigidBody(Cube.makeCube(new Vector3f(0, 5, 0), new Vector3f(30, 0, 0), 1, 1, 0.5f, 0.5f).getRigidBody());
		this.world.addRigidBody(Cube.makeCube(new Vector3f(0.5f, 1, 0), new Vector3f(), 0.5f, 1, 0.5f, 0.5f).getRigidBody());
		this.world.addRigidBody(Cube.makeCube(new Vector3f(0.75f, 1.75f, 0), new Vector3f(), 0.25f, 1, 0.5f, 0.5f).getRigidBody());
		this.world.addRigidBody(Sphere.makeSphere(new Vector3f(-1.5f, 10, 0), 1, 1, 0.6f, 0.5f).getRigidBody());
		this.world.addRigidBody(Box.makeBox(new Vector3f(-7, 1, 0), new Vector3f(), new Vector3f(2, 0.5f, 2), 1, 0.5f, 0.5f).getRigidBody());
	}

	private DiscreteDynamicsWorld world;

	private void initWorld() {
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.world = new DiscreteDynamicsWorld(
				new CollisionDispatcher(collisionConfig),
				new AxisSweep3(new Vector3f(-1024, -1024, -1024),
						new Vector3f(1024, 1024, 1024), 1024),
				new SequentialImpulseConstraintSolver(),
				collisionConfig);
		this.world.setGravity(new Vector3f(0, -9.8f, 0));
		this.initGround();
	}
	
	private void initGround() {
		Transform location = new Transform() {{this.setIdentity(); this.origin.set(0, -1, 0);}};
		DefaultMotionState motion = new DefaultMotionState(location);
		this.world.addRigidBody(new RigidBody(0, motion, new StaticPlaneShape(new Vector3f(0, 1, 0), 1)));
	}

	public void simulate(int steps) {
		for (int i = 0; i < steps; i++) {
			this.displayWorld();
			this.world.stepSimulation(10);
			try {
				Thread.sleep(25);
			} catch (Exception e) {}
		}
	}

	public void displayWorld() {
		GLDrawer.drawScene(this.world);
	}
}

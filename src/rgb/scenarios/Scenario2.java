package rgb.scenarios;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;

import rgb.CollisionEventDispatcher;
import rgb.bodies.Cube;
import rgb.bodies.Cylinder;
import rgb.bodies.PhysicalBody;
import rgb.bodies.Rope;
import rgb.bodies.Sphere;
import rgb.draw.Camera;
import rgb.draw.GLDrawer;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Scenario2 {
	public static void run(int steps) {
		try {
			GLDrawer.init(900, 600, 45f);
			GLDrawer.setCamera(new Camera(new Vector3f(5, 5, 40), null));
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Scenario2().simulate(steps);
		GLDrawer.shutdown();
	}

	private Scenario2() {
		this.initWorld();
		this.collisionDispatcher = new CollisionEventDispatcher();
		
		Cube anchor = new Cube(new Vector3f(0, 20, 0), 1, 0, 0);
		Rope rope = new Rope(new Vector3f(0, 20, 0), new Vector3f(10, 20, 0), 0.5f, 50, this.world);
		Point2PointConstraint anchrope = new Point2PointConstraint(anchor.getBody(), rope.getBodies().get(0).getBody(), new Vector3f(1f, 0, 0), new Vector3f(-0.1f, 0, 0));
		this.world.addConstraint(anchrope, true);
		
		this.sphere = new Sphere(new Vector3f(10, 20, 0), 1, 1, 0.5f);
		Point2PointConstraint ropesphe = new Point2PointConstraint(this.sphere.getBody(), rope.getBodies().get(49).getBody(), new Vector3f(-1f, 0, 0), new Vector3f(0.1f, 0, 0));
		this.world.addConstraint(ropesphe, true);
		
		this.world.addRigidBody(new Cylinder(new Vector3f(1, 8, 0), 2, 100, 0, 1).getBody());
		
		this.world.addRigidBody(anchor.getBody());
		this.world.addRigidBody(this.sphere.getBody());
		for (PhysicalBody pb : rope.getBodies())
			this.world.addRigidBody(pb.getBody());
	}

	private DiscreteDynamicsWorld world;
	private Sphere sphere;
	private CollisionEventDispatcher collisionDispatcher;

	private void initWorld() {
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.world = new DiscreteDynamicsWorld(new CollisionDispatcher(
				collisionConfig), new AxisSweep3(new Vector3f(-1024, -1024,
				-1024), new Vector3f(1024, 1024, 1024), 1024),
				new SequentialImpulseConstraintSolver(), collisionConfig);
		this.world.setGravity(new Vector3f(0, -9.8f, 0));
//		this.world.addRigidBody(this.makeGround());
	}

	private RigidBody makeGround() {
		Transform location = new Transform() {{
				this.setIdentity();
				this.origin.set(0, -1, 0);
			}};
		DefaultMotionState motion = new DefaultMotionState(location);
		return new RigidBody(0, motion, new StaticPlaneShape(new Vector3f(0, 1, 0), 1));
	}

	public void simulate(int steps) {
		this.displayWorld();
		for (int i = 0; i < steps; i++) {
			this.world.stepSimulation(1 / 60f, 1);
			this.collisionDispatcher.checkCollisions(this.world.getDispatcher());
			this.displayWorld();
		}
	}

	public void displayWorld() {
		GLDrawer.drawScene(this.world);
	}
}

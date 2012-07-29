package rgb.scenarios;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;

import rgb.CollisionEventDispatcher;
import rgb.bodies.Cube;
import rgb.bodies.Cylinder;
import rgb.bodies.PhysicalBody;
import rgb.bodies.Rope;
import rgb.draw.Camera;
import rgb.draw.GLDrawer;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Scenario2 {
	public static void run(int steps) {
		try {
			GLDrawer.init(900, 600, 45f);
			GLDrawer.setCamera(new Camera(new Vector3f(5, 0, 40), null));
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		new Scenario2().simulate(steps);
		GLDrawer.shutdown();
	}

	private Scenario2() {
		this.initWorld();
		this.collisionDispatcher = new CollisionEventDispatcher();
	}

	private DiscreteDynamicsWorld world;
	private Cylinder rotor;
	private HingeConstraint motor;
	private Cube weight;
	private Cylinder[] pulleys;
	private Rope rope;
	private CollisionEventDispatcher collisionDispatcher;

	private void initWorld() {
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.world = new DiscreteDynamicsWorld(new CollisionDispatcher(
				collisionConfig), new AxisSweep3(new Vector3f(-1024, -1024,
				-1024), new Vector3f(1024, 1024, 1024), 1024),
				new SequentialImpulseConstraintSolver(), collisionConfig);
		this.world.setGravity(new Vector3f(0, -9.8f, 0));
		
		initGround();
		initRotor();
		initWeight();
		initRope();
		initPulleys();
	}
	
	private void initGround() {
		Transform location = new Transform() {{this.setIdentity(); this.origin.set(0, -20, 0);}};
		DefaultMotionState motion = new DefaultMotionState(location);
		this.world.addRigidBody(new RigidBody(0, motion, new StaticPlaneShape(new Vector3f(0, 1, 0), 1)));
	}
	
	private void initRotor() {
		this.rotor = new Cylinder(new Vector3f(-5, 0, 0), 2, 20, 1, 0.9f);
		this.motor = new HingeConstraint(this.rotor.getBody(), new Vector3f(), new Vector3f(0, 0, 1));
		
		this.world.addRigidBody(this.rotor.getBody());
		this.world.addConstraint(this.motor);
	}
	
	private void initWeight() {
		this.weight = new Cube(new Vector3f(15, 0, 0), 2, 1, 1);
		this.world.addRigidBody(this.weight.getBody());
	}
	
	private void initRope() {
		int ropeSegments = 100;
		this.rope = new Rope(
				new Vector3f(-5, 2, 0),
				new Vector3f(20, 2, 0),
				0.5f, ropeSegments, this.world);
		
		Point2PointConstraint begin = new Point2PointConstraint(
				this.rotor.getBody(),
				this.rope.getBodies().get(0).getBody(),
				new Vector3f(0, 2, 0),
				new Vector3f(0, -0.25f, 0));
		
		Point2PointConstraint end = new Point2PointConstraint(
				this.weight.getBody(),
				this.rope.getBodies().get(ropeSegments - 1).getBody(),
				new Vector3f(0, 2, 0),
				new Vector3f(0, 0.25f, 0));
		
		for (PhysicalBody pb : this.rope.getBodies()) {
			this.world.addRigidBody(pb.getBody());
		}
		this.world.addConstraint(begin);
		this.world.addConstraint(end);
	}
	
	private void initPulleys() {
		this.pulleys = new Cylinder[3];
		
		float[] xs = new float[] {2, 6, 10};
		float[] ys = new float[] {-5, 5, -5};
		
		for (int i = 0; i < 3; i++) {
			this.pulleys[i] = new Cylinder(new Vector3f(xs[i], ys[i], 0), 3, 20, 0, 0.5f);
			this.pulleys[i].getBody().setFriction(0);
			
			this.world.addRigidBody(this.pulleys[i].getBody());
		}
	}

	public void simulate(int steps) {
		this.displayWorld();
		for (int i = 0; i < steps; i++) {
			this.updatePulleys(i);
			if (i == 1500) {
				// turn on the motor
				this.motor.enableAngularMotor(true, 1.0f, 1.0f);
			}
			
			this.world.stepSimulation(1 / 60f, 5);
			this.collisionDispatcher.checkCollisions(this.world.getDispatcher());
			this.displayWorld();
		}
	}
	
	private void updatePulleys(int i) {
		if (i >= 500 && this.pulleys[0].getLocation().y < 2) {
			this.pulleys[0].translate(new Vector3f(0, 0.01f, 0));
			this.pulleys[1].translate(new Vector3f(0, -0.01f, 0));
			this.pulleys[2].translate(new Vector3f(0, 0.01f, 0));
		}
	}
	
	public void displayWorld() {
		GLDrawer.drawScene(this.world);
	}
}

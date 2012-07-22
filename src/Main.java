import javax.vecmath.Vector3f;

import rgb.CollisionEvent;
import rgb.CollisionEventDispatcher;
import rgb.CollisionHandler;
import rgb.bodies.Box;
import rgb.bodies.Clamp;
import rgb.bodies.PhysicalBody;
import rgb.bodies.Sphere;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Main {
	public static void main(String[] args) {
		new Main().simulate(300);
	}

	public Main() {
		this.initWorld();
		this.collisionDispatcher = new CollisionEventDispatcher();
		this.world.addRigidBody((this.ball = new Sphere(1, 1, 0.7f, 0, 10, 0)).getBody());
		this.world.addRigidBody(makeSlope().getBody());
		this.world.addRigidBody((this.box = new Box(2, 1, 2, 1, 0.7f, 5, 0.5f, 0)).getBody());
		this.box.setCollisionHandler(new CollisionHandler() {
			@Override
			public void onContact(CollisionEvent e) {
				CollisionObject ball = Main.this.ball.getBody();
				if (ball.equals(e.bodyA) || ball.equals(e.bodyB))
					System.out.printf("\tBall maintains contact\n");
			}
			
			@Override
			public void onCollide(CollisionEvent e) {
				CollisionObject ball = Main.this.ball.getBody();
				if (ball.equals(e.bodyA) || ball.equals(e.bodyB))
					System.out.printf("\tBall collided with the platform\n");
			}
		});
		this.collisionDispatcher.addListenerTo(this.box.getBody(), this.box);
		
		this.clamp = new Clamp(0, 10, 0, 1);
		for (PhysicalBody b : this.clamp.getBodies())
			this.world.addRigidBody(b.getBody());
	}

	private DiscreteDynamicsWorld world;
	private Sphere ball;
	private Clamp clamp;
	private Box box;
	private CollisionEventDispatcher collisionDispatcher;
	
	private void initWorld() {
		CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		this.world = new DiscreteDynamicsWorld(
				new CollisionDispatcher(collisionConfig),
				new AxisSweep3(new Vector3f(-1024, -1024, -1024), new Vector3f(1024, 1024, 1024), 1024),
				new SequentialImpulseConstraintSolver(),
				collisionConfig);
		this.world.setGravity(new Vector3f(0, -9.8f, 0));
		this.world.addRigidBody(this.makeGround());
	}
	
	private RigidBody makeGround() {
		Transform location = new Transform() {{this.setIdentity(); this.origin.set(0, -1, 0);}};
		DefaultMotionState motion = new DefaultMotionState(location);
		return new RigidBody(0, motion, new StaticPlaneShape(new Vector3f(0, 1, 0), 1));
	}
	
	private Box makeSlope() {
		Box result = new Box(3, 0.5f, 1, 0, 0.4f, 1, 3.5f, 0);
		Transform trans = result.getBody().getWorldTransform(new Transform());
		trans.basis.rotZ((float) -Math.PI / 6f);
		result.getBody().setWorldTransform(trans);
		return result;
	}

	public void simulate(int steps) {
		this.displayWorld();
		for (int i = 0; i < steps; i++) {
			if (i == 10) {
				this.clamp.extend(.2f);
				System.out.printf("\tClamp released\n");
			}
			this.world.stepSimulation(1 / 60f, 1);
			this.collisionDispatcher.checkCollisions(this.world.getDispatcher());
			this.displayWorld();
		}
	}

	public void displayWorld() {
		Vector3f loc = this.ball.getLocation();
		System.out.printf("ball's (x,y): (%f, %f)", loc.x, loc.y);
		loc = this.box.getLocation();
		System.out.printf("\tbox's (x,y):  (%f, %f)\n", loc.x, loc.y);
	}
}

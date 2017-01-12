package sma.agents;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;

import env.jme.Environment;
import sma.AbstractAgent;
import sma.actionsBehaviours.LogicBehaviour;

public class LogicAgent extends AbstractAgent {
	
	private static final long serialVersionUID = -6559224244597633941L;

	/**
	 * True to create a friend, false otherwise 
	 */
	public boolean friendorFoe;
	
	//public RandomWalkBehaviour randomWalk;
	public LogicBehaviour followWalk;

	
	// Variables pour Prolog
	public static boolean highGround = false;
	public static boolean lowHealth = false;
	public static boolean onHighestPoint = false;
	public static boolean enemyInSight = false;
	public static boolean enemyObserved = false;
	
	public List<Vector3f> highPoints = new ArrayList<Vector3f>();
	
	public float highestAlt = -250.0f;
	
	protected void setup(){
		super.setup();
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null && args[1]!=null){
			
			this.friendorFoe = ((boolean)args[1]);
			
			if (friendorFoe) {
				deployAgent((Environment) args[0]);
			} else {
				deployEnemy((Environment) args[0]);
			}
			
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		//randomWalk = new RandomWalkBehaviour(this);
		followWalk = new LogicBehaviour(this);
		//addBehaviour(randomWalk);
		addBehaviour(followWalk);
		
		System.out.println("the player "+this.getLocalName()+ " is started. Tag (0==enemy): " + friendorFoe);
		
	}
	
	public boolean isInHighPoints(Vector3f point){
		boolean res = false;
		for(Vector3f hp : this.highPoints){
			res = approximativeEqualsCoordinates(point, hp);
		}
		return res;
	}
	
	private boolean approximativeEqualsCoordinates(Vector3f a, Vector3f b) {
		return approximativeEquals(a.x, b.x) && approximativeEquals(a.z, b.z);
	}
	
	private boolean approximativeEquals(float a, float b) {
		return b-2.5 <= a && a <= b+2.5;
	}

}

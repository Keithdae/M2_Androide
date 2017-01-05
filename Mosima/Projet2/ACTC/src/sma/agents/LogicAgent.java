package sma.agents;

import env.jme.Environment;
import sma.AbstractAgent;
import sma.actionsBehaviours.RandomWalkBehaviour;

public class LogicAgent extends AbstractAgent {
	
	private static final long serialVersionUID = -6559224244597633941L;

	/**
	 * True to create a friend, false otherwise 
	 */
	public boolean friendorFoe;
	
	public RandomWalkBehaviour randomWalk;
	
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

		randomWalk = new RandomWalkBehaviour(this);
		addBehaviour(randomWalk);
		
		System.out.println("the player "+this.getLocalName()+ " is started. Tag (0==enemy): " + friendorFoe);
		
	}

}
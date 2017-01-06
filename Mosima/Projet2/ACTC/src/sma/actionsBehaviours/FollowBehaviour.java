package sma.actionsBehaviours;

import java.util.List;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import sma.AbstractAgent;
import sma.actionsBehaviours.LegalActions.LegalAction;
import jade.core.behaviours.TickerBehaviour;

public class FollowBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	private String enemy = null;
	private boolean inSight = false;
	
	public FollowBehaviour(final AbstractAgent myagent) {
		// TODO Auto-generated constructor stub
		super(myagent, 100);
	}

	@Override
	protected void onTick() {	
		Vector3f currentpos  = ((AbstractAgent)this.myAgent).getCurrentPosition();
		Vector3f dest = ((AbstractAgent)this.myAgent).getDestination();
		Vector3f enemypos = null;
		
		
		Situation sit = ((AbstractAgent)this.myAgent).observeAgents();
		
		// System.out.println(sit);
		
		List<Tuple2<Vector3f, String>> targets = sit.agents;
		
		if(!targets.isEmpty())
		{
			inSight = true;
			enemy = targets.get(0).getSecond();
			enemypos = targets.get(0).getFirst();
			System.out.println("Target in sight : " + enemy);
			try{
				((AbstractAgent)this.myAgent).shoot(enemy);
			}
			catch(Exception e)
			{
				System.out.println("Shoot Exception triggered.");
			}
		}
		else
		{
			inSight = false;
		}
		
		if(!inSight)
		{
			if (dest==null || approximativeEqualsCoordinates(currentpos, dest))
			{
				((AbstractAgent)this.myAgent).randomMove();
			}
		}
		else
		{
			if(enemypos != null)
			{
				if (dest == null || approximativeEqualsCoordinates(enemypos, dest))
				{
					((AbstractAgent)this.myAgent).moveTo(enemypos);
				}
			}			
		}
	}
	
	private boolean approximativeEqualsCoordinates(Vector3f a, Vector3f b) {
		return approximativeEquals(a.x, b.x) && approximativeEquals(a.z, b.z);
	}
	
	private boolean approximativeEquals(float a, float b) {
		return b-2.5 <= a && a <= b+2.5;
	}
}

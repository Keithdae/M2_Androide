package sma.actionsBehaviours;

import java.util.List;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import sma.AbstractAgent;
import sma.agents.NotBasicAgent;
import jade.core.behaviours.TickerBehaviour;

public class FollowBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	private String enemy = null;
	private boolean inSight = false;
	
	private NotBasicAgent ag = (NotBasicAgent)this.myAgent;
	
	// The agent will be stronger the closer both probabilities are to 1
	private final float followProba = 0.2f;
	private final float shootProba = 0.3f;
	
	private boolean start = true;
	
	public FollowBehaviour(final AbstractAgent myagent) {
		// TODO Auto-generated constructor stub
		super(myagent, 100);
	}

	@Override
	protected void onTick() {
		if(start)
		{
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			start = false;
		}
		
		Vector3f currentpos  = ag.getCurrentPosition();
		Vector3f dest = ag.getDestination();
		Vector3f enemypos = null;
		
		
		Situation sit = ag.observeAgents();
		
		// System.out.println(sit);
		
		List<Tuple2<Vector3f, String>> targets = sit.agents;
		
		if(!targets.isEmpty())
		{
			inSight = true;
			enemy = targets.get(0).getSecond();
			enemypos = targets.get(0).getFirst();
		}
		else
		{
			inSight = false;
		}
		
		if(!inSight)
		{
			if (dest==null || approximativeEqualsCoordinates(currentpos, dest))
			{
				ag.randomMove();
			}
		}
		else
		{
			if(enemypos != null)
			{
				if (Math.random() < this.followProba)
				{
					((AbstractAgent)this.myAgent).moveTo(enemypos);
				}
				if (Math.random() < this.shootProba)
				{
					try{
						ag.shoot(enemy);
					}
					catch(Exception e)
					{
						System.out.println("Shoot Exception triggered.");
					}
				}
				
				// If we didn't follow and reached our former destination
				if(dest == null)
				{
					ag.randomMove();
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

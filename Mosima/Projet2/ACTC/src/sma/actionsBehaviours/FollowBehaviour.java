package sma.actionsBehaviours;

import java.util.List;

import org.jpl7.Query;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import sma.AbstractAgent;
import sma.actionsBehaviours.LegalActions.LegalAction;
import sma.agents.LogicAgent;
import jade.core.behaviours.TickerBehaviour;

public class FollowBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	private String enemy = null;
	
	private LogicAgent ag;
	private String query = "";
	
	public FollowBehaviour(final AbstractAgent myagent) {
		// TODO Auto-generated constructor stub
		super(myagent, 100);
		ag = (LogicAgent) myagent;
		query = "consult('./ressources/prolog/test/situation.pl')";
		System.out.println(query+" ?: "+Query.hasSolution(query));
	}

	@Override
	protected void onTick() {	
		Vector3f currentpos  = ag.getCurrentPosition();
		Vector3f dest = ag.getDestination();
		Vector3f enemypos = null;
		
		
		Situation sit = ag.observeAgents();
		
		// System.out.println(sit);
		
		List<Tuple2<Vector3f, String>> targets = sit.agents;
		
		if(!targets.isEmpty())
		{
			ag.enemyInSight = true;
			enemy = targets.get(0).getSecond();
			enemypos = targets.get(0).getFirst();
			System.out.println("Target in sight : " + enemy);
			try{
				ag.shoot(enemy);
			}
			catch(Exception e)
			{
				System.out.println("Shoot Exception triggered.");
			}
		}
		else
		{
			ag.enemyInSight = false;
		}
		
		if(!ag.enemyInSight)
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
				if (dest == null || !approximativeEqualsCoordinates(enemypos, dest))
				{
					ag.moveTo(enemypos);
				}
			}			
		}
		
		System.out.println("**Test Prolog**");
		query = "goodSituation(agent)";
		boolean gSol = Query.hasSolution(query);
		System.out.println(query+" ?: "+gSol);
	}
	
	private boolean approximativeEqualsCoordinates(Vector3f a, Vector3f b) {
		return approximativeEquals(a.x, b.x) && approximativeEquals(a.z, b.z);
	}
	
	private boolean approximativeEquals(float a, float b) {
		return b-2.5 <= a && a <= b+2.5;
	}
}

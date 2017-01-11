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

public class LogicBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	private String enemy = null;
	private Vector3f enemyPos = null;
	
	private LogicAgent ag;
	private String query = "";
	
	public LogicBehaviour(final AbstractAgent myagent) {
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
		enemyPos = null;
		
		// Observation
		Situation sit = ag.observeAgents();
		
		// System.out.println(sit);
		
		List<Tuple2<Vector3f, String>> targets = sit.agents;
		
		if(!targets.isEmpty())
		{
			LogicAgent.enemyInSight = true;
			enemy = targets.get(0).getSecond();
			enemyPos = targets.get(0).getFirst();
		}
		else
		{
			LogicAgent.enemyInSight = false;
		}
		
		
		// Action
		if(!LogicAgent.enemyInSight)
		{
			if (dest==null || approximativeEqualsCoordinates(currentpos, dest))
			{
				ag.randomMove();
			}
		}
		else
		{
			System.out.println("Target in sight : " + enemy);
			try{
				ag.shoot(enemy);
			}
			catch(Exception e)
			{
				System.out.println("Shoot Exception triggered.");
			}
			
			if(enemyPos != null)
			{
				if (dest == null || !approximativeEqualsCoordinates(enemyPos, dest))
				{
					ag.moveTo(enemyPos);
				}
			}			
		}
		
		// Prolog
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
	
	// Pas necessaire avec moveTo() ?
	private int lookAtPoint(Vector3f p)
	{
		int res = -1;
		Vector3f agPos = ag.getCurrentPosition();
		Vector3f agPosGround = new Vector3f(agPos.x,0,agPos.z);
		if(p != null)
		{
			Vector3f posGround = new Vector3f(p.x,0,p.z);
			Vector3f dir = posGround.subtract(agPosGround).normalize();
			Vector3f n = new Vector3f(0,0,1);
			float a = n.angleBetween(dir);
			System.out.println(agPosGround);
			System.out.println(posGround);
			System.out.println(dir);
		}
		
		return res;
	}
}

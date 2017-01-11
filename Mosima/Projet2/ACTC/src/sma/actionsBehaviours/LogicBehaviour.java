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

	private static final float pi = (float) Math.PI;
	
	public enum CardinalAngle {
		N(0.f, LegalAction.MOVE_NORTH), NE(pi / 4, LegalAction.MOVE_NORTHEAST)
		, E(pi / 2, LegalAction.MOVE_EAST), SE((3*pi)/4, LegalAction.MOVE_SOUTHEAST)
		, S(pi, LegalAction.MOVE_SOUTH), SW((5*pi)/4, LegalAction.MOVE_SOUTHWEST)
		, W((3*pi)/2, LegalAction.MOVE_WEST), NW((7*pi)/4, LegalAction.MOVE_NORTHWEST);
		
		private final float angle;
		private final LegalAction action;
		private CardinalAngle(float ang, LegalAction la) 
		{
			this.angle = ang;
			this.action = la;
		}
		
		public float getAngle(){return this.angle;}
		public LegalAction getAction(){return this.action;}
		
		public String toString(){return "Angle : " + this.angle + ", Action : " + this.action +".\n";}
	}
	
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
		
		System.out.println(sit);
		
		List<Tuple2<Vector3f, String>> targets = sit.agents;
		LogicAgent.enemyInSight = false;

		// mise à jour des variables enemyObserved, lowHealth, heightOverAverage et enemyInSight
		if(!targets.isEmpty())
		{
			LogicAgent.enemyObserved = true;
			enemy = targets.get(0).getSecond();
			enemyPos = targets.get(0).getFirst();
			if(ag.enemyInSight(enemy)){
				LogicAgent.enemyInSight = true;
			}
		}
		else
		{
			LogicAgent.enemyObserved = false;
		}

		
		LogicAgent.lowHealth = ag.getHealth() < (ag.getMaxLife()/2);
		
		LogicAgent.heightOverAverage = currentpos.y > sit.avgAltitude;
		
		//mise a jour de la l'altitude maximum rencontrée et highGround
		if(sit.maxAltitude.y > ag.highestAlt){
			ag.highestAlt = sit.maxAltitude.y;
		}
			
		LogicAgent.highGround = sit.agentAltitude.y > 0.8f * ag.highestAlt;
		
		// Action
		if(!LogicAgent.enemyObserved)
		{
			if (dest==null || approximativeEqualsCoordinates(currentpos, dest))
			{
				ag.randomMove();
			}
		}
		else
		{
			//System.out.println("Target in sight : " + enemy);
			
			LegalAction dir = lookAtPoint(enemyPos);
			ag.moveTo(enemyPos);
			if(dir != LegalAction.SHOOT)
			{
				if (dest != null || !approximativeEqualsCoordinates(currentpos, enemyPos))
				{
					//ag.cardinalMove(dir);
					//ag.randomMove();
				}	
			}
			
			try{
				//ag.shoot(enemy);
			}
			catch(Exception e)
			{
				System.out.println("Shoot Exception triggered.");
			}	
		}
		
		// Prolog
		/*System.out.println("**Test Prolog**");
		query = "goodSituation(agent)";
		boolean gSol = Query.hasSolution(query);
		System.out.println(query+" ?: "+gSol);*/
		
	}
	
	private boolean approximativeEqualsCoordinates(Vector3f a, Vector3f b) {
		return approximativeEquals(a.x, b.x) && approximativeEquals(a.z, b.z);
	}
	
	private boolean approximativeEquals(float a, float b) {
		return b-2.5 <= a && a <= b+2.5;
	}
	
	/* Calcule le point cardinal le plus proche de l'angle forme entre la position de l'agent et le point fourni en parametre
	*  Rend la LegalAction correspondant a l'angle trouve
	*/
	private LegalAction lookAtPoint(Vector3f p)
	{
		LegalAction res = LegalAction.SHOOT;
		Vector3f agPos = ag.getCurrentPosition();
		Vector3f agPosGround = new Vector3f(agPos.x,0,agPos.z);
		if(p != null)
		{
			Vector3f posGround = new Vector3f(p.x,0,p.z);
			Vector3f dir = posGround.subtract(agPosGround).normalize();
			Vector3f north = new Vector3f(0,0,-1);
			Vector3f south = new Vector3f(0,0,1);
			float a = north.angleBetween(dir) + ((p.x < agPos.x)?south.angleBetween(dir)*2:0);
			CardinalAngle best = null;
			float min = 100.f;
			for(CardinalAngle ca : CardinalAngle.values())
			{
				if(Math.abs((ca.getAngle()-a)) < min)
				{
					best = ca;
					min = Math.abs((ca.getAngle()-a));
				}
			}
			if(best != null)
			{
				res = best.getAction();
			}
		}
		
		return res;
	}
}

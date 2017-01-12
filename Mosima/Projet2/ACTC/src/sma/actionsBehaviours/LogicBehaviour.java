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

	private static final long serialVersionUID = -6213008344104265259L;
	
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
	
	// NOP correspond a une absence d'action (valeur par defaut)
	public static enum Decision {
		ATTACK, EXPLORE, CLIMB, CAMPING, LOOKAT, NOP;
	}
	
	public static void setDecision(String s){
		dec = Decision.valueOf(s.toUpperCase());
	}
	
	private static Decision dec;
	
	private String enemy = null;
	private Vector3f enemyPos = null;
	
	private LogicAgent ag;
	private String query = "";
	
	private boolean start = true;
	
	private int nMove = 0;
	private Vector3f lastPos = null;
	
	public LogicBehaviour(final AbstractAgent myagent) {
		// TODO Auto-generated constructor stub
		super(myagent, 100);
		ag = (LogicAgent) myagent;
		dec = Decision.NOP;
		query = "consult('./ressources/prolog/test/situation.pl')";
		System.out.println(query+" ?: "+Query.hasSolution(query));
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
		enemyPos = null;
		
		// Observation
		Situation sit = ag.observeAgents();
		
		//System.out.println(sit);
		
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

		//mise a jour de la l'altitude maximum rencontrée et highGround
		if(sit.maxAltitude.y > ag.highestAlt){
			ag.highestAlt = sit.maxAltitude.y;
		}
		
		LogicAgent.lowHealth = ag.getHealth() < (ag.getMaxLife()/2);
		
		LogicAgent.onHighestPoint = this.approximativeEqualsCoordinates(currentpos, sit.maxAltitude);
		
		LogicAgent.highGround = sit.agentAltitude.y > 0.8f * ag.highestAlt;
		
		if(LogicAgent.onHighestPoint && LogicAgent.highGround && !ag.isInHighPoints(sit.maxAltitude)){
			ag.highPoints.add(sit.maxAltitude);
		}
		
		boolean stopExplo = (LogicBehaviour.dec.equals(Decision.EXPLORE) && sit.maxAltitude.y > 0.8f * ag.highestAlt && !ag.isInHighPoints(sit.maxAltitude));
			
		
		query = "perfectSituation(agent)";
		boolean gSol = Query.hasSolution(query);
		/*System.out.println(query+" ?: "+gSol);
		System.out.println("Decision taken : " + dec);*/
		if(!gSol && (dest==null || stopExplo || dec == Decision.NOP))
		{
			query = "goodSituation(agent)";
			gSol = Query.hasSolution(query);
			/*System.out.println(query+" ?: "+gSol);
			System.out.println("Decision taken : " + dec);*/
		}
		
		if(!gSol && isStuck())
		{
			ag.randomMove();
		}
		
		switch(dec)
		{
		case ATTACK: // L'ennemi est en vue, on tente de tirer dessus et on le suit
			try{
				ag.shoot(enemy);
			}
			catch(Exception e)
			{
				System.out.println("Shoot Exception triggered.");
			}
			
			// Si on ne se deplace pas vers l'ennemi, on le fait
			if (dest == null ||!approximativeEqualsCoordinates(dest, enemyPos))
			{
				ag.moveTo(enemyPos);
			}
			
			break;
		case CAMPING: // On reste sur place et on tourne aleatoirement en attendant l'adversaire
			Vector3f test = currentpos.clone();
			test.add( new Vector3f( ((float)Math.random() * 2) - 1,0,((float)Math.random() * 2) - 1 ) );
			ag.lookAt(lookAtPoint(test));
			break;
		case CLIMB:
			if(dest == null)
				ag.moveTo(sit.maxAltitude);
			break;
		case EXPLORE:
			if(dest == null)
				ag.randomMove();
			break;
		case LOOKAT: // L'agent a ete detecte mais n'est pas dans le champ de vision, on essaye de le voir
			System.out.println("J'avais tort");
			ag.lookAt(lookAtPoint(enemyPos));
			break;
		case NOP:
			System.out.println("Attention, aucune decision prise !!!");
			break;
		default:
			System.err.println("INCORRECT VALUE FOR DECISION : " + dec);;
			break;
		}
		
		lastPos = currentpos.clone();
	}
	
	private boolean approximativeEqualsCoordinates(Vector3f a, Vector3f b) {
		return approximativeEquals(a.x, b.x) && approximativeEquals(a.z, b.z);
	}
	
	private boolean approximativeEquals(float a, float b) {
		return b-2.5 <= a && a <= b+2.5;
	}
	
	
	private boolean isStuck(){
		boolean res = false;
		if(nMove > 30)
		{
			res = true;
			nMove = 0;
		}
		else
		{
			if(approximativeEqualsCoordinatesStricter(ag.getCurrentPosition(), lastPos))
			{
				nMove++;
			}
			else
			{
				nMove = 0;
			}
		}
		return res;
	}
	private boolean approximativeEqualsCoordinatesStricter(Vector3f a, Vector3f b) {
		return approximativeEqualsStricter(a.x, b.x) && approximativeEqualsStricter(a.z, b.z);
	}
	private boolean approximativeEqualsStricter(float a, float b) {
		return b-0.2 <= a && a <= b+0.2;
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

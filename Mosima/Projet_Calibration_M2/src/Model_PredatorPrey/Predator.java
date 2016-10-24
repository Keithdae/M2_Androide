package Model_PredatorPrey;




import java.awt.Color;

import uchicago.src.sim.gui.SimGraphics;

public class Predator extends PredatorPreyAgent
{
    /**
     * @param minLifeSpan
     * @param maxLifeSpan
     */
    public Predator(int minLifeSpan, int maxLifeSpan, int maximumEnergy, int energyGain, int energyUse, double mateProbability, int energyPredatorUseForMate)
    {
        super(minLifeSpan, maxLifeSpan, maximumEnergy, energyGain, energyUse, mateProbability, energyPredatorUseForMate);
        setS("predator");
        //setStarving(0);
    }
    
public void report()
{
    System.out.println(getMale()+ getID() + " at " + getX() + ", " + getY() + " has "
            + getFood() + " prey" + " and " + getStepsToLive()
            + " steps to live.");
}
public void draw(SimGraphics G)
{
  //  if (getStepsToLive() > 10)
        G.drawFastRoundRect(Color.red);
  //  else
  //      G.drawFastRoundRect(Color.yellow);
}


/**
 * @param newX
 * @param newY
 */
public void compatibleMatch(int newX, int newY)
{
    Predator sa =  getPredatorPreySpace().getPredatorAgentAt(newX, newY);
    if (sa != null)
    {   
        if ((sa.getMale()!=getMale())&&(sa.getStepsToLive() < 0.8*sa.getInitialLifeTime())
                &&(getStepsToLive() < 0.8*getInitialLifeTime())&& Math.random() < getMateProbability() && getEnergy()>getEnergyLimit()/2)
        {
            mate(); 
            setEnergy((int)(getEnergy()- getEnergyUseForMate()));
        }
    }
}

public void hunt()
{
  /*Note no = new Note();
  no.toString();*/
  
}
}

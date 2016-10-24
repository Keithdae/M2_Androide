package Model_PredatorPrey;




import java.awt.Color;

import uchicago.src.sim.gui.SimGraphics;

public class Prey extends PredatorPreyAgent
{
    /**
     * @param minLifeSpan
     * @param maxLifeSpan
     */
    public Prey(int minLifeSpan, int maxLifeSpan, int maximumEnergy, int energyGain, int energyUse, double mateProbability, int energyPreyUseForMate)
    {
        super(minLifeSpan, maxLifeSpan, maximumEnergy, energyGain, energyUse, mateProbability, energyPreyUseForMate);
        setS("prey");
        //setEnergyUse(getEnergyPreyUseEachTick()); 
    }

    public void report()
    {
        System.out.println(getMale()+ getID() + " at " + getX() + ", " + getY() + " has "
                + getFood() + " grass" + " and " + getStepsToLive()
                + " steps to live.");
    }

    public void draw(SimGraphics G)
    {
   //     if (getStepsToLive() > 10)
            G.drawFastRoundRect(Color.black);
    //    else
    //        G.drawFastRoundRect(Color.blue);
    }

    /**
     * @param newX
     * @param newY
     */
    public void compatibleMatch(int newX, int newY)
    {
        Prey sa =  getPredatorPreySpace().getPreyAgentAt(newX, newY);
        if (sa != null)
        { 
            if (sa.getMale()!=getMale()&&(sa.getStepsToLive() < 0.8*sa.getInitialLifeTime())
                    &&(getStepsToLive() < 0.8*getInitialLifeTime())&& Math.random() < getMateProbability()&& getEnergy()>getEnergyLimit()/2)
            {
                mate(); 
                setEnergy((int)(getEnergy()- getEnergyUseForMate()));
            }
        }
    }

    public void escape()
{
    
}
}

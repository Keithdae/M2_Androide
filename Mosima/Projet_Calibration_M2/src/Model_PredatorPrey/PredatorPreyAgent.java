package Model_PredatorPrey;



//import java.util.Hashtable;
//import uchicago.src.reflector.DescriptorContainer;
//import uchicago.src.reflector.RangePropertyDescriptor;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

public class PredatorPreyAgent implements Drawable//, DescriptorContainer
{
    public int x;
    public int y;
    public int vX;
    public int vY;
    public int food;
    public int energyGain;
    public int energyUse;
    public int stepsToLive;
    public int InitialLifeTime;
    public int energy;
    public int energyLimit;
    public static int IDNumber = 0;
    public int ID;
    public PredatorPreySpace sSpace;
    public static int Kids = 0;
    public boolean male;
    public String s;
    public double mateProbability;
    
    public int energyUseForMate;
    
    //public int heading;
   // protected Hashtable descriptors = new Hashtable();
   /* RangePropertyDescriptor d = new RangePropertyDescriptor("food", 
            10, 100, 20);*/


	public PredatorPreyAgent(int minLifeSpan, int maxLifeSpan, int maximumEnergy, int energyGain, int energyUse, double mateProbability, int energyUseForMate)
    {
        x = -1;
        y = -1;
        food = 0;
        setEnergyLimit(maximumEnergy);
        energy = (int) ((Math.random() * getEnergyLimit()));//2 * energyGain
        setVxVy();
        stepsToLive = (int) ((Math.random() * (maxLifeSpan - minLifeSpan)) + minLifeSpan);
        InitialLifeTime = stepsToLive;
        IDNumber++;
        ID = IDNumber;
        male = ((int) Math.floor(Math.random() * 2) == 1);
        setEnergyGain(energyGain);
        setEnergyUse(energyUse);
        setMateProbability(mateProbability);
        
        setEnergyUseForMate(energyUseForMate);
        //heading = (int) Math.floor(Math.random() * 8) + 1;
     
    //    descriptors.put("foods", d);
    }
    public String toString() { return s; }
    // unesi sada tostring u report funkciju...
    
    public void setPredatorPreySpace(PredatorPreySpace ss)
    {
        sSpace = ss;
    }
    public PredatorPreySpace getPredatorPreySpace()
    {
        return sSpace;
    }

    public void mate()
    {
        Kids++; //modifikovati da se dete radja pored majke i da nasledjuje izvesne osobine roditelja...
    }

    protected boolean tryMove(int newX, int newY)
    {
        return sSpace.moveAgentAt(x, y, newX, newY, s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.gui.Drawable#draw(uchicago.src.sim.gui.SimGraphics)
     */
    public void draw(SimGraphics arg0)
    {
    }

    public void report()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.gui.Drawable#getX()
     */
    public int getX()
    {
        return x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.gui.Drawable#getY()
     */
    public int getY()
    {
        return y;
    }

    public void setXY(int newX, int newY)
    {
        x = newX;
        y = newY;
    }

    protected void setVxVy()
    {
        
        vX = 0;
        vY = 0;
        while ((vX == 0) && (vY == 0))
        {
            vX = (int) Math.floor(Math.random() * 3) - 1;
            vY = (int) Math.floor(Math.random() * 3) - 1;
        }
        
        
/*        int location = (int) Math.floor(Math.random() * 8) + 1;
        
        heading = (location + heading) % 8;
        if (heading == 0) heading = 8;
        
        switch (heading) 
        {
        
        456
        3 7
        218
        
        case 1:
            vX = 0;
            vY = -1;
            break;
        case 2:
            vX = -1;
            vY = -1;
            break;
        case 3:
            vX = -1;
            vY = 0;
            break;
        case 4:
            vX = -1;
            vY = 1;
            break;
        case 5:
            vX = 0;
            vY = 1;
            break;
        case 6:
            vX = 1;
            vY = 1;
            break;
        case 7:
            vX = 1;
            vY = 0;
            break;
        case 8:
            vX = 1;
            vY = -1;
        default:
            
            break;
        }*/
    }
    /* public void step() { // modifikuj kod za druzenje i koperaciju...
     * love se oblasti sa vise hrane...
    long heatHere = (long)space.getValueAt(x, y);

   if (heatHere < idealTemp) {
     unhappiness = (double) (idealTemp - heatHere) / Diffuse2D.MAX;
   } else {
     unhappiness = (double) (heatHere - idealTemp) / Diffuse2D.MAX;
   }

   int type = (heatHere < idealTemp) ? HeatSpace.HOT : HeatSpace.COLD;
   Point p = space.findExtreme(type, x, y);

   if (Uniform.staticNextFloatFromTo(0.0f, 1.0f) < randomMoveProb) {
     p.x = x + Uniform.staticNextIntFromTo(-1, 1);
     p.y = y + Uniform.staticNextIntFromTo(-1, 1);
   }

   if (unhappiness == 0) {
     space.addHeat(x, y, outputHeat);
   } else {
     int tries = 0;

     if (p.x != x || p.y != y) {

       // get the neighbors
       int prevX = SimUtilities.norm(x - 1, xSize);
       int nextX = SimUtilities.norm(x + 1,  xSize);
       int prevY = SimUtilities.norm(y - 1, ySize);
       int nextY = SimUtilities.norm(y + 1, ySize);

       while ((world.getObjectAt(p.x, p.y) != null) && tries < 10) {

         int location = Uniform.staticNextIntFromTo(1, 8);

         switch (location) {
           case 1:
             p.x = prevX;
             p.y = prevY;
             break;
           case 2:
             p.x = x;
             p.y = prevY;
             break;
           case 3:
             p.x = nextX;
             p.y = prevY;
             break;
           case 4:
             p.x = nextX;
             p.y = y;
             break;
           case 5:
             p.x = prevX;
             p.y = y;
             break;
           case 6:
             p.x = prevX;
             p.y = nextY;
             break;
           case 7:
             p.x = x;
             p.y = nextY;
             break;
           case 8:
             p.x = nextX;
             p.y = nextY;
           default:
             break;
         }
         tries++;
       }
       if (tries == 10) {
         p.x = x;
         p.y = y;
       }

     }

     space.addHeat(x, y, outputHeat);
     world.putObjectAt(x, y, null);
     x = p.x;
     y = p.y;
     world.putObjectAt(x, y, this);
   }
 }*/
    public String getID()
    {
        return "A-" + ID;
    }

    public String getMale()
    {
        if (male)
        {
            return "Male ";
        } else
        {
            return "Female ";
        }
    }

    public int getVx()
    {
        return vX;
    }

    public int getVy()
    {
        return vY;
    }

    public String getS()
    {
        return s;
    }

    public void setS(String st)
    {
        s = st;
    }

    public int getStepsToLive()
    {
        /*
         * if (grass == 0) stepsToLive-=1;
         */
        return stepsToLive;
    }

    public void setStepsToLive(int s)
    {
        stepsToLive = s;
    }

    public int getFood()
    {
        return food;
    }

    public void setFood(int s)
    {
        food = s;
    }

    public int getInitialLifeTime()
    {
        return InitialLifeTime;
    }

    public int getEnergy()
    {
        return energy;
    }

    public void setEnergy(int s)
    {
        energy = s;
    }

    /**
     * 
     */
    public void step()
    {
        int newX = getX() + getVx();
        int newY = getY() + getVy();
        
        Object2DGrid grid = getPredatorPreySpace().getCurrentPreyAgentSpace();
        newX = (newX + grid.getSizeX()) % grid.getSizeX();
        newY = (newY + grid.getSizeY()) % grid.getSizeY();
        
        if (tryMove(newX, newY))
        {
            
                setFood(getFood()-1);
                if(getFood()<0)setFood(0);
                    int test = getFood();
            if(getEnergy()<getEnergyLimit())
                {
                setFood(getFood()- getPredatorPreySpace().takeFoodAt(getX(), getY(), getS()));
                }
            //if((int) Math.floor(Math.random() * 3) - 1==1) 
            if (test==getFood())
            {
                setEnergy(getEnergy()- getEnergyUse());
            }
            else
            {
                setEnergy(getEnergy()- getEnergyUse() + getEnergyGain());
            }
            compatibleMatch(newX, newY);// izbaci  - ovo je samo poredjenje sa netlogo
        }
        else
        {
            compatibleMatch(newX, newY);
        }
        setVxVy();
    }

    /**
     * @param newX
     * @param newY
     */
    // this method is overriden in inhereted classes
    public void compatibleMatch(int newX, int newY)
    {
        if (Math.random() < getMateProbability())
        {
            mate(); 
            setEnergy((int)((getEnergy()-1)/2));
        }
    }

    /* (non-Javadoc)
     * @see uchicago.src.reflector.DescriptorContainer#getParameterDescriptors()
     */
  /*  public Hashtable getParameterDescriptors()
    {
        return descriptors;
    }*/
  /*  public void setD(RangePropertyDescriptor val) {
        d = val;
      }

      public RangePropertyDescriptor getD() {
        return d;
      }*/
    public int getEnergyGain()
    {
        return energyGain;
    }
    public void setEnergyGain(int energyGain)
    {
        this.energyGain = energyGain;
    }
    public int getEnergyUse()
    {
        return energyUse;
    }
    public void setEnergyUse(int energyUse)
    {
        this.energyUse = energyUse;
    }
    public double getMateProbability()
    {
        return mateProbability;
    }
    public void setMateProbability(double mateProbability)
    {
        this.mateProbability = mateProbability;
    }
    public int getEnergyLimit()
    {
        return energyLimit;
    }
    public void setEnergyLimit(int energyLimit)
    {
        this.energyLimit = energyLimit;
    }
    
    public int getEnergyUseForMate() {
		return energyUseForMate;
	}
	public void setEnergyUseForMate(int energyUseForMate) {
		this.energyUseForMate = energyUseForMate;
	}
}

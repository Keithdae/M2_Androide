package Model_PredatorPrey;




import uchicago.src.sim.space.Object2DGrid;

public class PredatorPreySpace
{
    public Object2DGrid grassSpace;
    public Object2DGrid preySpace;
    public Object2DGrid predatorSpace;
    public int xs;
    public int ys;
    public PredatorPreyModel predatorPreyModel;
    
    public PredatorPreySpace(int xSize, int ySize,PredatorPreyModel predatorPreyModel )
    {
        grassSpace = new Object2DGrid(xSize, ySize);
        preySpace = new Object2DGrid(xSize, ySize);
        predatorSpace = new Object2DGrid(xSize, ySize);
        xs = xSize;
        ys = ySize;
        // Initialize grassSpace with zeros
        for (int i = 0; i < xSize; i++)
        {
            for (int j = 0; j < ySize; j++)
            {
                grassSpace.putObjectAt(i, j, new Integer(0));
            }
        }
        
        this.predatorPreyModel = predatorPreyModel;
    }

    public void spreadGrass(int grass)
    {
        /*set countdown random grass-regrowth-time ;; initialize grass grow clocks randomly
        ;; countdown on brown patches, if reach 0, grow some grass
              set countdown grass-regrowth-time ] 
            [ set countdown (countdown - 1) ] */
        int t = 0;
        // Randomly place grass in grassSpace
        int sgrs = 1;
        if(grass<0){grass=-grass;sgrs = -1;}
        for (int i = 0; i < grass; i++)
        {
            // Choose coordinates
            int x = (int) (Math.random() * (grassSpace.getSizeX()));
            int y = (int) (Math.random() * (grassSpace.getSizeY()));

            // Get the value of the object at those coordinates
            int currentValue = getGrassAt(x, y);
            if(currentValue > (int)(Math.random()*2)+1)
            {
                i--;
                t++;
                //modifikovano prema netlogo
                if(t > grass*10)i = grass;
            }
            /*else if (currentValue < 1 && Math.random()>0.8)
            {    
            // Replace the Integer object with another one with the new value
            grassSpace.putObjectAt(x, y, new Integer(currentValue + 1));
            }*/
            else
            {    
            // Replace the Integer object with another one with the new value
            grassSpace.putObjectAt(x, y, new Integer(currentValue + sgrs));
            }
            if (getGrassAt(x, y)<0) grassSpace.putObjectAt(x, y, new Integer(0));
        }
    }

    public int getGrassAt(int x, int y)
    {
        int i;
        if (grassSpace.getObjectAt(x, y) != null)
        {
            i = ((Integer) grassSpace.getObjectAt(x, y)).intValue();
        } else
        {
            i = 0;
        }
        return i;
    }

    public Object2DGrid getCurrentGrassSpace()
    {
        return grassSpace;
    }

    public Object2DGrid getCurrentPreyAgentSpace()
    {
        return preySpace;
    }
   
    public Object2DGrid getCurrentPredatorAgentSpace()
    {
        return predatorSpace;
    }
    
    public boolean IsCellOccupied(int x, int y, String s)
    {
        boolean retVal = false;
        if ((s=="prey" && preySpace.getObjectAt(x, y) != null) || 
                (s=="predator" && predatorSpace.getObjectAt(x, y) != null))
            retVal = true;
        return retVal;
    }
    
    public boolean addAgent(PredatorPreyAgent agent)
    {
        boolean retVal = false;
        int count = 0;
        int countLimit = 10 * xs * ys;
        
        while ((retVal == false) && (count < countLimit))
        {
            int x = (int) (Math.random() * xs);
            int y = (int) (Math.random() * ys);
            if (IsCellOccupied(x, y, agent.getS()) == false)
            {
                if (agent.getS()=="prey") 
                {
                    preySpace.putObjectAt(x, y, (Prey)agent);
                }
                else if
                (agent.getS()=="predator")
                {
                    predatorSpace.putObjectAt(x, y, (Predator)agent);
                }
                agent.setXY(x, y);
                agent.setPredatorPreySpace(this);
                retVal = true;
            }
            count++;
        }
        return retVal;
    }
    
    public void removeAgentAt(int x, int y, String s)
    {
        if (s=="prey") 
        {
            preySpace.putObjectAt(x, y, null);
        }
        else if
        (s=="predator")
        {
            predatorSpace.putObjectAt(x, y, null);
        }
    }
    
    public int takeFoodAt(int x, int y, String s)
    {
        int food=0;
        if (s=="prey")
        {
            food = getGrassAt(x, y);
            if(food>0)
            //{grassSpace.putObjectAt(x, y, new Integer(0));
            {grassSpace.putObjectAt(x, y, new Integer(food-1));
            food=1;}
        } 
        else if (s=="predator")
        {
            if (IsCellOccupied(x, y,"prey")) 
            {  
                Prey sa =  (Prey) preySpace.getObjectAt(x, y);
               if (sa.getStepsToLive() > 0&&sa.getStepsToLive()>20)
               {
                   sa.setStepsToLive(0);
                   predatorPreyModel.nbrPreyEaten ++;
                food =1;
               }
            }
        } 
        return food;
    }

    public boolean moveAgentAt(int x, int y, int newX, int newY, String s)
    {
        boolean retVal = false;
        // String s = getClass().getName();
        if (!IsCellOccupied(newX, newY,s))
        {
            if (s=="prey") 
            {
                 Prey sa =  (Prey) preySpace.getObjectAt(x, y);
                 removeAgentAt(x, y, s);
                 sa.setXY(newX, newY);
                 preySpace.putObjectAt(newX, newY, sa);
                 retVal = true;
            }
            else if
            (s=="predator")
            {
                Predator  sa =  (Predator) predatorSpace.getObjectAt(x, y);
                removeAgentAt(x, y, s);
                sa.setXY(newX, newY);
                predatorSpace.putObjectAt(newX, newY, sa);
                retVal = true;
            }
        }
        return retVal;
    }

    public Prey getPreyAgentAt(int x, int y)
    {
        Prey retVal = null; 
        if( preySpace.getObjectAt(x, y) != null)
            retVal =  (Prey) preySpace.getObjectAt(x, y);
        return retVal;
    }
    
    public Predator getPredatorAgentAt(int x, int y)
    {
        Predator retVal = null; 
        if( predatorSpace.getObjectAt(x, y) != null)
            retVal = (Predator) predatorSpace.getObjectAt(x, y);
        return retVal;
    }
    
    public int getTotalGrass()
    {
        int totalGrass = 0;
        for (int i = 0; i < xs; i++)
        {
            for (int j = 0; j < ys; j++)
            {
                totalGrass += getGrassAt(i, j);
            }
        }
        return totalGrass;
    }
}

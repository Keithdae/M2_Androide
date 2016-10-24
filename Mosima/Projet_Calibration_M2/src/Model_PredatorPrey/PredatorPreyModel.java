package Model_PredatorPrey;




import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import uchicago.src.sim.analysis.BinDataSource;
import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.Histogram;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Controller;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.event.SliderListener;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;
//TODO modify histogram & negative food logic!
/*cak i da pocnu sa raznom, trebali bi da se vremenom izjednace! - mozda i ne!(pareto vukovi)
ovde razlicite sposobnosti ne uticu, samo slucajnosti. kasnije uvedi i stepen sposobnosti!*/
public class PredatorPreyModel extends SimModelImpl
{
    public  final long serialVersionUID = 1L;
    // Default Values correspond to NETLOGO  values. 
    public  final int WORLDXSIZE = 51;
    public  final int WORLDYSIZE = 54;
    public  final int NUMAGENTS = 1000;
    public  final int TOTALINITIALGRASS = 324*4;// in NETLOGO only brown/green, here more values for grass
    public  final int AGENT_MIN_LIFESPAN = 100;//high = absent in NETLOGO!
    public  final int AGENT_MAX_LIFESPAN = 1000;//high = absent in NETLOGO!
    public  final int MAX_ENERGY_THAT_CAN_BE_CONSUMED = 1000;//high = absent in NETLOGO!
    public  final int ENERGY_TAKEN_FROM_GRASS = 20;//modify
    public  final int ENERGY_TAKEN_FROM_PREY = 40;//modify
    public  final int ENERGY_PREY_USE_EACH_TICK = 1;//modify
    public  final int ENERGY_PREDATOR_USE_EACH_TICK = 1;//modify
    public  final int ENERGY_PREDATOR_USE_FOR_MATE = 10;//set energy (energy / 2 )  ;; divide energy between parent and offspring
    public  final int ENERGY_PREY_USE_FOR_MATE = 8;//[ rt random-float 360 fd 1 ]  ;; hatch an offspring and move it forward 1 step

    //~later introduce randomized values entered as comments
    public int worldXSize = WORLDXSIZE;//(int) Math.floor(Math.random() * 513);
    public int worldYSize = WORLDYSIZE;//(int) Math.floor(Math.random() * 513);
    public int totaInitialGrass = TOTALINITIALGRASS;//WORLDXSIZE*WORLDYSIZE/2;
    public int newGrassGrowthRate = 12;// 1% of area of world grows grass each tick - to be changed
    public int numPredatorAgents = 100;//(int) Math.floor(Math.random() * 513);
    public int numPreyAgents = NUMAGENTS;//(int) Math.floor(Math.random() * 513);
    public int predatorAgentMinLifespan = AGENT_MIN_LIFESPAN;//numPredatorAgents/10;
    public int predatorAgentMaxLifespan = AGENT_MAX_LIFESPAN;//numPredatorAgents/5;
    public int preyAgentMinLifespan = AGENT_MIN_LIFESPAN;//numPreyAgents/10;
    public int preyAgentMaxLifespan = AGENT_MAX_LIFESPAN;//numPreyAgents/5;
    public int preyAgentMinAgeToEat = 5;//numPreyAgents/5;
    public int maximumEnergyThatPredatorCanTake = MAX_ENERGY_THAT_CAN_BE_CONSUMED/2;//10;
    public int maximumEnergyThatPreyCanTake = MAX_ENERGY_THAT_CAN_BE_CONSUMED/4;//10;
    public int energyTakenFromPrey = ENERGY_TAKEN_FROM_PREY;
    public int energyTakenFromGrass = ENERGY_TAKEN_FROM_GRASS;
    public int energyPredatorUseEachTick = ENERGY_PREDATOR_USE_EACH_TICK;
    public int energyPreyUseEachTick = ENERGY_PREY_USE_EACH_TICK;
    public int energyPredatorUseForMate = ENERGY_PREDATOR_USE_FOR_MATE;//jos nije uneto parametre agenata
    public int energyPreyUseForMate = ENERGY_PREY_USE_FOR_MATE;//jos nije uneto parametre agenata
    public double probabilityForPredatorMate = 0.5;
    public double probabilityForPreyMate = 0.8;
    public int predatorStep = 1; // how many times is predator faster than prey
    public int currentLivingPreys = 0;
    public int currentLivingPredators = 0;
    public int speed = 3;
    public int multigrass = 0;
    public double seazon = 1;
    
    public Schedule schedule;
    public PredatorPreySpace sSpace;
    public ArrayList<Prey> preyList;
    public ArrayList<Predator> predatorList;
    public DisplaySurface displaySurf;
    public OpenSequenceGraph amountOfSpeciesInSpace;
    public Histogram preyAgentFoodDistribution;
    public Histogram predatorAgentFoodDistribution;
    // public Parameters parameters;

    public  int nbrPreyEaten = 0;
    
    
    class GrassInSpace implements DataSource, Sequence
    {
        public Object execute()
        {
            return new Double(getSValue());
        }

        public double getSValue()
        {
            return (double) sSpace.getTotalGrass()/4;
        }
    }

    class PreyInSpace implements DataSource, Sequence
    {       
        public double getSValue()
        {
            return (double)getCurrentLivingPreys();
        }

        public Object execute()
        {
            return new Double(getSValue());
        }   
    }

    class PredatorInSpace implements DataSource, Sequence
    {       
        public double getSValue()
        {
            return (double)getCurrentLivingPredators();
        }

        public Object execute()
        {
            return new Double(getSValue());
        }   
    }

    class PreyAgentFood implements BinDataSource
    {
        public double getBinValue(Object o)
        {
            Prey sa = (Prey) o;
            return (double) sa.getEnergy();
        }
    }

    class PredatorAgentFood implements BinDataSource
    {
        public double getBinValue(Object o)
        {
            Predator sa = (Predator) o;
            return (double) sa.getEnergy();
        }
    }
    
    /*class GrassAgentInSpace implements BinDataSource
    {
        public double getBinValue(Object o)
        {
            PredatorPreySpace sa = (PredatorPreySpace) o;
            return (double) sa.getTotalGrass();
        }
    }*/
    
    public static void main(String[] args) {
    
        //System.out.println("Predator-Prey Simulation.");
        SimInit init = new SimInit();
        PredatorPreyModel model = new PredatorPreyModel();
        init.loadModel(model, "", false); 
    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.engine.SimModel#getInitParam()
     */
    public String[] getInitParam()
    {
        String[] initParams =
        {  "WorldXSize", "WorldYSize", "TotaInitialGrass", "NewGrassGrowthRate", "NumPredatorAgents", "NumPreyAgents",
           "PredatorAgentMinLifespan", "PredatorAgentMaxLifespan", "PreyAgentMinLifespan", "PreyAgentMaxLifespan",
           "MaximumEnergyThatPredatorCanTake", "MaximumEnergyThatPreyCanTake", 
           "EnergyTakenFromPrey", "EnergyTakenFromGrass", "EnergyPredatorUseEachTick" , "EnergyPreyUseEachTick",  
           "EnergyPredatorUseForMate", "EnergyPreyUseForMate", "ProbabilityForPredatorMate", "ProbabilityForPreyMate", "PredatorStep", "Multigrass", "preyAgentMinAgeToEat"
        };
        return initParams;
    }
//    public String[] getInitParam()
//    {
//        String[] initParams =
//        {  "Seazon", "NumPredatorAgents", "NewGrassGrowthRate", "MaximumEnergyThatPredatorCanTake", "MaximumEnergyThatPreyCanTake", 
//           "EnergyTakenFromPrey", "EnergyTakenFromGrass", "ProbabilityForPredatorMate", 
//           "ProbabilityForPreyMate", "PredatorStep", "preyAgentMinAgeToEat"
//        };
//        return initParams;
//    }
    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.engine.SimModel#setup()
     */
    public void setup()
    {
        Controller.ALPHA_ORDER = false;
        //System.out.println("Running setup");
        sSpace = null;
        preyList = new ArrayList<Prey>();
        predatorList = new ArrayList<Predator>();
        schedule = new Schedule(1);
        // System.gc ();
        // Tear down Displays
        if (displaySurf != null)
        {
            displaySurf.dispose();
        }
        displaySurf = null;
        if (amountOfSpeciesInSpace != null)
        {
            amountOfSpeciesInSpace.dispose();
        }
        amountOfSpeciesInSpace = null;
        if (preyAgentFoodDistribution != null)
        {
            preyAgentFoodDistribution.dispose();
        }
        preyAgentFoodDistribution = null;
        if (predatorAgentFoodDistribution != null)
        {
            predatorAgentFoodDistribution.dispose();
        }
        predatorAgentFoodDistribution = null;
        // Create Displays
        displaySurf = new DisplaySurface(this, "Predator-Prey Model Window 1");
        amountOfSpeciesInSpace = new OpenSequenceGraph(
                "Number of Agents In Space", this);
        preyAgentFoodDistribution = new Histogram("Prey Agent Wealth - food in belly - energy", 50, 0, getMaximumEnergyThatPreyCanTake()+10);
        predatorAgentFoodDistribution = new Histogram("Predator Agent Wealth - food in belly - energy", 50, 0, getMaximumEnergyThatPredatorCanTake()+10);
        // Register Displays
     registerDisplaySurface("Predator-Prey Model Window 1", displaySurf);
        this.registerMediaProducer("Plot", amountOfSpeciesInSpace);
        /* if (parameters != null){parameters.dispose();}parameters = null; */
       // setupCustomAction();
    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.engine.SimModel#getSchedule()
     */
    public Schedule getSchedule()
    {
        return schedule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.engine.SimModel#begin()
     */
    public void begin()
    {
        if(numPreyAgents > worldXSize*worldYSize || numPredatorAgents > worldXSize*worldYSize)
            {
            System.out.println("World is too small under present configuration for so many agents! Each coordinate can have only one agent of the same species.");
            System.out.println("Under current world size, there can be worldXSize*worldYSize = " + worldXSize*worldYSize + " agents of the same species.");
            System.out.println("I may modify this latter with building more abstract models, and 3D models.");
            System.out.println("It can also be changed now, but there should be a limit of agents per coordinate cooresponding to the level of the cultural/technological development! Not applicable to animal simulations, only human.");
            System.out.println("Also, predators may be dinosaurus, occuping the 'whole coordinate, while preys may be rabbits, and 100 of them may fit in one coordinate.");
            System.out.println("This is the reason why wolf-sheep simulation is the simplest one - they are of the same size! This is in fact probably wolf-sheep simulation, and not any predator-prey simulation!");
            System.out.println("Will think about this more. There should also be correspondence of agents' energies with their sizes.");
            
            // either cell corresponds to maximum size animal, and then many smaller ones can fit in one cell
            // or cell corresponds to minimum size animal, and then many cells are needed for drawing bigger animals.
            // first case is way simpler!
            // at this moment, 1 animal on 1 cell regardless of size
            System.out.println("More agents than space!");
            stop();
            }
        else
            {
            try
            {
                buildModel();
                buildSchedule();
                buildDisplay();
                displaySurf.display();
                amountOfSpeciesInSpace.display();
                preyAgentFoodDistribution.display();
                predatorAgentFoodDistribution.display();
            } catch (RuntimeException e)
            {
                System.out.print("exception in begin");
                stop();
                e.printStackTrace();
            }
            }
        System.out.print("PredatorStep : " + predatorStep + ";  ");
        System.out.print("NumPreyAgents: " + numPreyAgents + ";  ");
        System.out.print("NumPredatorAgents : " + numPredatorAgents + ";  ");
        System.out.print("EnergyPreyUseEachTick: " + energyPreyUseEachTick + ";  ");
        System.out.print("EnergyPredatorUseEachTick : " + energyPredatorUseEachTick + ";  ");
        System.out.print("PreyAgentMaxLifespan : " + preyAgentMaxLifespan + ";  ");
        System.out.print("PredatorAgentMaxLifespan : " + predatorAgentMaxLifespan + ";  ");
        System.out.print("MaximumEnergyThatPredatorCanTake : " + maximumEnergyThatPredatorCanTake + ";  ");
        System.out.print("MaximumEnergyThatPreyCanTake : " + maximumEnergyThatPreyCanTake + ";  ");
        System.out.print("EnergyTakenFromGrass : " + energyTakenFromGrass + ";  ");
        System.out.print("EnergyTakenFromPrey : " + energyTakenFromPrey + ";  ");
        System.out.print("ProbabilityForPreyMate : " + probabilityForPreyMate + ";  ");
        System.out.print("ProbabilityForPredatorMate : " + probabilityForPredatorMate + ";  ");
        System.out.println("NewGrassGrowthRate : " + newGrassGrowthRate + ";  ");
    }

    public void buildModel()
    {
        // /System.out.println("Running BuildModel");
        sSpace = new PredatorPreySpace(worldXSize, worldYSize, this);
        sSpace.spreadGrass(totaInitialGrass);
        
  
        
        for (int i = 0; i < numPreyAgents; i++)
        {
            addNewPreyAgent(preyList);
        }
        for (int i = 0; i < numPredatorAgents; i++)
        {
            addNewPredatorAgent(predatorList);
        }
        // Report call slows down application significantly! Why? I will certainly
        // have to report agents statistics, both individual and collective. How
        // to do this?
        /*
         * for (int i = 0; i < preyList.size(); i++) 
         * { Prey sa = preyList.get(i); 
         * sa.report(); } 
         * for (int i = 0; i < predatorList.size(); i++) 
         * { Predator sa = predatorList.get(i);
         * sa.report(); }
         */
        /*
         * parameters = new Parameters(); parameters.setVisible(true);
         */
        // // addSimEventListener (displaySurf);
    }

    public void buildSchedule()
    {
        //System.out.println("Running BuildSchedule");
        class SocialStep extends BasicAction
        {
            public void execute()
            {
                try
                {//TODO uprosti ovo da se generise po svim predatorprey vrstama a ne posebno za svaku
                	
                		nbrPreyEaten = 0;
                        SimUtilities.shuffle(preyList);
                        for (int i = 0; i < preyList.size(); i++)
                        { //TODO if num> space, stop()
                            Prey sa = preyList.get(i);
                            sa.step();
                            sa.setStepsToLive(sa.getStepsToLive() - 1);
                        }
                        int deadAgents = reapDeadAgents(preyList);
                        
                        double nbrPreyKids = Prey.Kids;
                        System.out.println("Prey.Kids " + Prey.Kids);
                        
                        if(((numPreyAgents + Prey.Kids) < (worldXSize-1)*(worldYSize-1)))
                        {
                            for (int i = 0; i < Prey.Kids; i++) // deadAgents +
                        
                        {
                            addNewPreyAgent(preyList);
                        }
                        }
                        else
                        {
                            System.out.println("overpopulatedPrey");
                            stop(); 
                        }
                        Prey.Kids = 0;
                        for (int s = 0; s < predatorStep; s++)
                        { // redosled ove 2 petlje treba zameniti - to ce usporiti simulaciju.
                            SimUtilities.shuffle(predatorList);
                            for (int i = 0; i < predatorList.size(); i++)
                            {
                                Predator sa = predatorList.get(i);
                                sa.step();
                                //SimUtilities.shuffle(predatorList); moved few lines up.
                            } 
                            //System.out.println("s: " + s);
                        }
                        for (int i = 0; i < predatorList.size(); i++)
                        {
                            Predator sa = predatorList.get(i);
                            sa.setStepsToLive(sa.getStepsToLive() - 1);
                        }
                        
                        int deadAgents2 = reapDeadAgents(predatorList);
                        
                        if(((numPredatorAgents + Predator.Kids) < (worldXSize-1)*(worldYSize-1)))
                        {
                            for (int i = 0; i < Predator.Kids; i++) // deadAgents +
                        
                        {
                            addNewPredatorAgent(predatorList);
                        }
                        }
                        else
                        {
                            System.out.println("overpopulatedPredator");
                            stop(); 
                        }
                        
                        //System.out.println(nbrPreyKids );
                        
                        System.out.println("nbrPreyEaten " + nbrPreyEaten);
                        
                        Predator.Kids = 0;
                        displaySurf.updateDisplay();
             //           sSpace.spreadGrass(((int) (currentLivingPredators*(newGrassGrowthRate * worldXSize * worldYSize / (100*10))))); // Scaled to 1/10th of percent of area!
                } catch (RuntimeException e)
                {
                	  System.out.println("RunTimeException"+e);
                	stop();
                }
            }
        }
        schedule.scheduleActionBeginning(0, new SocialStep());

        class SocialCountLiving extends BasicAction
        {
            public void execute()
            {
                currentLivingPreys = countLivingAgents(preyList);
                currentLivingPredators = countLivingAgents(predatorList);
                // System.out.println(agentList.size());
                System.out.print("Cows: " + currentLivingPreys + " ");
                System.out.print("Cowboys: " + currentLivingPredators + " ");
                System.out.println("Grass: " + sSpace.getTotalGrass());
                sSpace.spreadGrass(((int) ((newGrassGrowthRate * (1)* worldXSize * worldYSize / (100*10))))); // Scaled to 1/10th of percent of area! //+0.3*Math.sin(seazon*getTickCount()*Math.PI/180)
            }
        }
        schedule.scheduleActionAtInterval(1, new SocialCountLiving());

        class SocialUpdateGrassInSpace extends BasicAction
        {
            public void execute()
            {
                amountOfSpeciesInSpace.step();
            }
        }
        schedule.scheduleActionAtInterval(1, new SocialUpdateGrassInSpace());

        class SocialUpdateAgentWealth extends BasicAction
        {
            public void execute()
            {
                  if (currentLivingPreys>0)
                   {preyAgentFoodDistribution.step();}
                   if (currentLivingPredators>0)
                   {predatorAgentFoodDistribution.step();}
                   if (currentLivingPreys==0 )//|| currentLivingPredators==0
                  {
                       if(getTickCount() >1)
                           { 

                    //ZeroAgentNotice p = new ZeroAgentNotice();
                    System.out.println("Simulation has been STOPED! All preys are dead! Change parameters, and restart simulation.");
                           System.out.print("PREYS DEAD");
                           System.out.print("PredatorStep: " + predatorStep);
                           System.out.print("PredatorStep : " + predatorStep + ";  ");
                           System.out.print("NumPreyAgents: " + numPreyAgents + ";  ");
                           System.out.print("NumPredatorAgents : " + numPredatorAgents + ";  ");
                           System.out.print("EnergyPreyUseEachTick: " + energyPreyUseEachTick + ";  ");
                           System.out.print("EnergyPredatorUseEachTick : " + energyPredatorUseEachTick + ";  ");
                           System.out.print("PreyAgentMaxLifespan : " + preyAgentMaxLifespan + ";  ");
                           System.out.print("PredatorAgentMaxLifespan : " + predatorAgentMaxLifespan + ";  ");
                           System.out.print("MaximumEnergyThatPredatorCanTake : " + maximumEnergyThatPredatorCanTake + ";  ");
                           System.out.print("MaximumEnergyThatPreyCanTake : " + maximumEnergyThatPreyCanTake + ";  ");
                           System.out.print("EnergyTakenFromGrass : " + energyTakenFromGrass + ";  ");
                           System.out.print("EnergyTakenFromPrey : " + energyTakenFromPrey + ";  ");
                           System.out.print("ProbabilityForPreyMate : " + probabilityForPreyMate + ";  ");
                           System.out.print("ProbabilityForPredatorMate : " + probabilityForPredatorMate + ";  ");
                           System.out.print("NewGrassGrowthRate : " + newGrassGrowthRate + ";  ");
                           stop();
                           }
                  }
                   if(getTickCount() == 5000)
                   {
                       System.out.print("PredatorStep : " + predatorStep + ";  ");
                       System.out.print("NumPreyAgents: " + numPreyAgents + ";  ");
                       System.out.print("NumPredatorAgents : " + numPredatorAgents + ";  ");
                       System.out.print("EnergyPreyUseEachTick: " + energyPreyUseEachTick + ";  ");
                       System.out.print("EnergyPredatorUseEachTick : " + energyPredatorUseEachTick + ";  ");
                       System.out.print("PreyAgentMaxLifespan : " + preyAgentMaxLifespan + ";  ");
                       System.out.print("PredatorAgentMaxLifespan : " + predatorAgentMaxLifespan + ";  ");
                       System.out.print("MaximumEnergyThatPredatorCanTake : " + maximumEnergyThatPredatorCanTake + ";  ");
                       System.out.print("MaximumEnergyThatPreyCanTake : " + maximumEnergyThatPreyCanTake + ";  ");
                       System.out.print("EnergyTakenFromGrass : " + energyTakenFromGrass + ";  ");
                       System.out.print("EnergyTakenFromPrey : " + energyTakenFromPrey + ";  ");
                       System.out.print("ProbabilityForPreyMate : " + probabilityForPreyMate + ";  ");
                       System.out.print("ProbabilityForPredatorMate : " + probabilityForPredatorMate + ";  ");
                       System.out.println("NewGrassGrowthRate : " + newGrassGrowthRate + ";  ");
                       stop(); 
                   }
                   if(numPreyAgents > (worldXSize-1)*(worldYSize-1) || numPredatorAgents > (worldXSize-1)*(worldYSize-1))
                   {
                       System.out.print("OVERPOPULATION: ");
                       stop();
                   }
            }
        }
        schedule.scheduleActionAtInterval(1, new SocialUpdateAgentWealth());
    }

    public void buildDisplay()
    {
        //System.out.println("Running BuildDisplay");
        ColorMap map = new ColorMap();

        for (int i = 1; i < 64; i++)
        {
            map.mapColor(i, new Color((int) 0, (i * 2 + 127), 0));
        }
        map.mapColor(0, Color.white);
        Value2DDisplay displayGrass = new Value2DDisplay(sSpace
                .getCurrentGrassSpace(), map);
        Object2DDisplay displayAgents = new Object2DDisplay(sSpace
                .getCurrentPreyAgentSpace());
        Object2DDisplay displayAgents2 = new Object2DDisplay(sSpace
                .getCurrentPredatorAgentSpace());
        displayAgents.setObjectList(preyList);
        displayAgents2.setObjectList(predatorList);
        displaySurf.addDisplayableProbeable(displayGrass, "Grass");
        displaySurf.addDisplayableProbeable(displayAgents, "Preys");
        displaySurf.addDisplayableProbeable(displayAgents2, "Predators");
        amountOfSpeciesInSpace.addSequence("Grass In Space", new GrassInSpace(), java.awt.Color.green, 0);
        amountOfSpeciesInSpace.addSequence("Prey In Space", new PreyInSpace(), java.awt.Color.black, 0);
        amountOfSpeciesInSpace.addSequence("Predator In Space", new PredatorInSpace(), java.awt.Color.red, 0);
        preyAgentFoodDistribution.createHistogramItem("PreyAgent Wealth", preyList,
                new PreyAgentFood()); 
        predatorAgentFoodDistribution.createHistogramItem("PredatorAgent Wealth", predatorList,
                new PredatorAgentFood()); 
    }

    /*
     * (non-Javadoc)
     * 
     * @see uchicago.src.sim.engine.SimModel#getName()
     */

    public void addNewPreyAgent(ArrayList<Prey> agentlist)
    {
        Prey a = new Prey(preyAgentMinLifespan, preyAgentMaxLifespan, maximumEnergyThatPreyCanTake, energyTakenFromGrass, energyPreyUseEachTick, probabilityForPreyMate, energyPreyUseForMate);
        
        if (sSpace.addAgent(a))
        {
            agentlist.add(a);
        }
         else 
        {
        	 System.out.println("addNewPrey Stopped");
            stop();
        }         
    }

    public void addNewPredatorAgent(ArrayList<Predator> agentlist)
    {
        Predator a = new Predator(predatorAgentMinLifespan, predatorAgentMaxLifespan, maximumEnergyThatPredatorCanTake, energyTakenFromPrey, energyPredatorUseEachTick, probabilityForPredatorMate, energyPredatorUseForMate);
        if (sSpace.addAgent(a))
        {
            agentlist.add(a);
        }
         else 
        {  System.out.println("addNewPredator Stopped");
        	 	
            stop();
        }
    }

    public int reapDeadAgents(ArrayList agentlist)
    {
        int count = 0;
        for (int i = (agentlist.size() - 1); i >= 0; i--)
        {
            PredatorPreyAgent sa = (PredatorPreyAgent) agentlist.get(i);
            if ((sa.getStepsToLive() < 1)
                    || sa.getEnergy() < 0)
            {
                sSpace.removeAgentAt(sa.getX(), sa.getY(), sa.getS());
                //sSpace.spreadGrass(sa.getFood());
                // pojedeni travari vracaju travu, a pojedeni mesari vracaju
                // isto travu!!! sto da ne!
                agentlist.remove(i);
                count++;
            }
        }
        return count;
    }

    public int countLivingAgents(ArrayList agentlist)
    {
        int livingAgents = 0;
        String str = "agent";
        if (agentlist.size() > 0)
        {
            PredatorPreyAgent sstr = (PredatorPreyAgent) agentlist.get(0);
            str = sstr.getS();
        }
        for (int i = 0; i < agentlist.size(); i++)
        {
            PredatorPreyAgent sa = (PredatorPreyAgent) agentlist.get(i);
            if (sa.getStepsToLive() > 0)
                livingAgents++;
        }
        //System.out.println("Number of living " + str + "s is: " + livingAgents);
        return livingAgents;
    }

/*    public void setupCustomAction()
    {
        modelManipulator.init();

        modelManipulator.addButton("Add NumPreyAgents and NumPredatorAgents",
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        for (int i = 0; i < numPredatorAgents; i++)
                        {
                            addNewPredatorAgent(predatorList);
                        }
                        for (int i = 0; i < numPreyAgents; i++)
                        {
                            addNewPreyAgent(preyList);
                        }
                    }
                });

        modelManipulator.addSlider("How much is predator faster than prey?", 1,
                10, 1, new SliderListener()
                {
                    public void execute()
                    {
                        if (isAdjusting)
                        {
                            setPredatorStep(value);
                        }
                    }
                });

        modelManipulator.addSlider("Maximum Energy That Prey Can Take. GLOBAL - influences immidiately all agents.",
                1, 31, 3, new SliderListener()
                {
                    public void execute()
                    {
                        if (isAdjusting)
                        {
                            setMaximumEnergyThatPreyCanTake(value);
                        }
                    }
                });

        modelManipulator.addSlider("Maximum Energy That Predator Can Take. GLOBAL - influences immidiately all agents.",
                1, 31, 3, new SliderListener()
                {
                    public void execute()
                    {
                        if (isAdjusting)
                        {
                            setMaximumEnergyThatPredatorCanTake(value);
                        }
                    }
                });

        modelManipulator.addSlider("How much percent of area grows grass each tick? (10 is 1%, 100 is 10%)",
                0, 100, 10, new SliderListener()
                {
                    public void execute()
                    {
                        if (isAdjusting)
                        {
                            setNewGrassGrowthRate(value);
                            //System.out.println(value);
                        }
                    }
                });
    }*/

    public String getName()
    {
        return "Predator-Prey";
    }
    public int getNumPreyAgents()
    {
        return numPreyAgents;
    }
    public void setNumPreyAgents(int na)
    {
        numPreyAgents = na;
    }
    public int getNumPredatorAgents()
    {
        return numPredatorAgents;
    }
    public void setNumPredatorAgents(int na)
    {
        numPredatorAgents = na;
    }
    public int getWorldXSize()
    {
        return worldXSize;
    }
    public void setWorldXSize(int wxs)
    {
        worldXSize = wxs;
    }
    public int getWorldYSize()
    {
        return worldYSize;
    }
    public void setWorldYSize(int wys)
    {
        worldYSize = wys;
    }
    public int getTotaInitialGrass()
    {
        return totaInitialGrass;
    }
    public void setTotaInitialGrass(int i)
    {
        totaInitialGrass = i;
    }
    public int getPredatorAgentMaxLifespan()
    {
        return predatorAgentMaxLifespan;
    }
    public void setPredatorAgentMaxLifespan(int i)
    {
        predatorAgentMaxLifespan = i;
    }
    public int getPredatorAgentMinLifespan()
    {
        return predatorAgentMinLifespan;
    }
    public void setPredatorAgentMinLifespan(int i)
    {
        predatorAgentMinLifespan = i;
    }
    public int getPreyAgentMinLifespan()
    {
        return preyAgentMinLifespan;
    }
    public void setPreyAgentMinLifespan(int i)
    {
        preyAgentMinLifespan = i;
    }
    public int getPreyAgentMaxLifespan()
    {
        return preyAgentMaxLifespan;
    }
    public void setPreyAgentMaxLifespan(int i)
    {
        this.preyAgentMaxLifespan = i;
    }
    public int getPredatorStep()
    {
        return predatorStep;
    }
    public void setPredatorStep(int i)
    {
        predatorStep = i;
    }
    /*public int getPredatorsLiveLongerXTimes()
    {
        return predatorsLiveLongerXTimes;
    }
    public void setPredatorsLiveLongerXTimes(int livelonger)
    {
        this.predatorsLiveLongerXTimes = livelonger;
    }*/
    public int getSpeed()
    {
        return speed;
    }
    public void setSpeed(int i)
    {
        this.speed = i;
    }
    public int getCurrentLivingPredators()
    {
        return currentLivingPredators;
    }
    public void setCurrentLivingPredators(int i)
    {
        this.currentLivingPredators = i;
    }
    public int getCurrentLivingPreys()
    {
        return currentLivingPreys;
    }
    public void setCurrentLivingPreys(int i)
    {
        this.currentLivingPreys = i;
    }
    public int getNewGrassGrowthRate()
    {
        return newGrassGrowthRate;
    }
    public void setNewGrassGrowthRate(int i)
    {
        this.newGrassGrowthRate = i;
    }
    public int getMaximumEnergyThatPreyCanTake()
    {
        return maximumEnergyThatPreyCanTake;
    }
    public void setMaximumEnergyThatPreyCanTake(int i)
    {
        this.maximumEnergyThatPreyCanTake = i;
        for (int j = 0; j < preyList.size(); j++)
        {
            Prey sa = preyList.get(j);
            sa.setEnergyLimit(i);
        }
    }
    public int getMaximumEnergyThatPredatorCanTake()
    {
        return maximumEnergyThatPredatorCanTake;
    }
    public void setMaximumEnergyThatPredatorCanTake(int i)
    {
        this.maximumEnergyThatPredatorCanTake = i;
        for (int j = 0; j < predatorList.size(); j++)
        {
            Predator sa = predatorList.get(j);
            sa.setEnergyLimit(i);
        }
    }
    public int getEnergyTakenFromGrass()
    {
        return energyTakenFromGrass;
    }
    public void setEnergyTakenFromGrass(int i)
    {
        this.energyTakenFromGrass = i;
    }
    public int getEnergyTakenFromPrey()
    {
        return energyTakenFromPrey;
    }
    public void setEnergyTakenFromPrey(int i)
    {
        this.energyTakenFromPrey = i;
    }
    public int getEnergyPredatorUseEachTick()
    {
        return energyPredatorUseEachTick;
    }
    public void setEnergyPredatorUseEachTick(int i)
    {
        this.energyPredatorUseEachTick = i;
    }
    public int getEnergyPreyUseEachTick()
    {
        return energyPreyUseEachTick;
    }
    public void setEnergyPreyUseEachTick(int i)
    {
        this.energyPreyUseEachTick = i;
    }
    public int getEnergyPredatorUseForMate()
    {
        return energyPredatorUseForMate;
    }
    public void setEnergyPredatorUseForMate(int i)
    {
        this.energyPredatorUseForMate = i;
    }
    public int getEnergyPreyUseForMate()
    {
        return energyPreyUseForMate;
    }
    public void setEnergyPreyUseForMate(int i)
    {
        this.energyPreyUseForMate = i;
    }
    public double getProbabilityForPredatorMate()
    {
        return probabilityForPredatorMate;
    }
    public void setProbabilityForPredatorMate(double i)
    {
        this.probabilityForPredatorMate = i;
    }
    public double getProbabilityForPreyMate()
    {
        return probabilityForPreyMate;
    }
    public void setProbabilityForPreyMate(double i)
    {
        this.probabilityForPreyMate = i;
    }
    public int getMultigrass()
    {
        return multigrass;
    }
    public void setMultigrass(int multigrass)
    {
        this.multigrass = multigrass;
    }
    public int getPreyAgentMinAgeToEat()
    {
        return preyAgentMinAgeToEat;
    }
    public void setPreyAgentMinAgeToEat(int preyAgentMinAgeToEat)
    {
        this.preyAgentMinAgeToEat = preyAgentMinAgeToEat;
    }
    public double getSeazon()
    {
        return seazon;
    }
    public void setSeazon(double seazon)
    {
        this.seazon = seazon;
    }
}

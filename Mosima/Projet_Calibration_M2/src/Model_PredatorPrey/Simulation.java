package Model_PredatorPrey;


import uchicago.src.sim.util.SimUtilities;


public class Simulation extends Thread {

	public int horizon; 
	public int rangeAverageFitness; 
	
	public double energyPredatorUseEachTick;
	public double energyPredatorUseForMate;
	public double energyPreyUseEachTick;
	public double energyPreyUseForMate;
	public double energyTakenFromGrass;
	public double energyTakenFromPrey;
	public double probabilityForPredatorMate;
	public double probabilityForPreyMate;


		
	public double meanRatePreysOverPredators;
	public double meanRatePreyEaten;
	
	
	public void run() {
		
			

		PredatorPreyModel predatorPreyModel = new PredatorPreyModel();
		
		
		predatorPreyModel.numPreyAgents = 1000;   
		predatorPreyModel.numPredatorAgents = 100;

		predatorPreyModel.energyPredatorUseEachTick = (int)energyPredatorUseEachTick; 
		predatorPreyModel.energyPredatorUseForMate = (int)energyPredatorUseForMate;
		predatorPreyModel.energyPreyUseEachTick = (int)energyPreyUseEachTick;
		predatorPreyModel.energyPreyUseForMate = (int)energyPreyUseForMate;
		predatorPreyModel.energyTakenFromGrass = (int)energyTakenFromGrass;
		predatorPreyModel.energyTakenFromPrey = (int)energyTakenFromPrey;
		predatorPreyModel.probabilityForPredatorMate = (int)probabilityForPredatorMate;
		predatorPreyModel.probabilityForPreyMate = (int)probabilityForPreyMate;
		
		
		
		predatorPreyModel.setup();
		predatorPreyModel.buildModel();
		predatorPreyModel.buildSchedule();
	
		meanRatePreysOverPredators = 0;
		meanRatePreyEaten =0;
		
		

		
		
		for(int k = 0; k < horizon; k++){
		  
		 predatorPreyModel.nbrPreyEaten = 0;
		  double nbrPreyKids = 0;
		  
		  //SimUtilities.shuffle(predatorPreyModel.preyList);
	      for (int i = 0; i < predatorPreyModel.preyList.size(); i++)
	      { //TODO if num> space, stop()
	          Prey sa = predatorPreyModel.preyList.get(i);
	          sa.step();
	          sa.setStepsToLive(sa.getStepsToLive() - 1);
	      }
	      int deadAgents = predatorPreyModel.reapDeadAgents(predatorPreyModel.preyList);
	      
	      nbrPreyKids = Prey.Kids;
	      
	      if(((predatorPreyModel.numPreyAgents + Prey.Kids) < (predatorPreyModel.worldXSize-1)*(predatorPreyModel.worldYSize-1)))
	      {
	          for (int i = 0; i < Prey.Kids; i++) // deadAgents +
	      
	      {
	        	  predatorPreyModel.addNewPreyAgent(predatorPreyModel.preyList);
	      }
	      }
	      else
	      {
	          System.out.println("overpopulatedPrey");
	          predatorPreyModel.stop(); 
	      }
	      Prey.Kids = 0;
	      for (int s = 0; s < predatorPreyModel.predatorStep; s++)
	      { // redosled ove 2 petlje treba zameniti - to ce usporiti simulaciju.
	          //SimUtilities.shuffle(predatorPreyModel.predatorList);
	          for (int i = 0; i < predatorPreyModel.predatorList.size(); i++)
	          {
	              Predator sa = predatorPreyModel.predatorList.get(i);
	              sa.step();
	              //SimUtilities.shuffle(predatorList); moved few lines up.
	          } 
	          //System.out.println("s: " + s);
	      }
	      for (int i = 0; i < predatorPreyModel.predatorList.size(); i++)
	      {
	          Predator sa = predatorPreyModel.predatorList.get(i);
	          sa.setStepsToLive(sa.getStepsToLive() - 1);
	      }
	      
	      int deadAgents2 = predatorPreyModel.reapDeadAgents(predatorPreyModel.predatorList);
	      
	      if(((predatorPreyModel.numPredatorAgents + Predator.Kids) < (predatorPreyModel.worldXSize-1)*(predatorPreyModel.worldYSize-1)))
	      {
	          for (int i = 0; i < Predator.Kids; i++) // deadAgents +
	      
	      {
	        	  predatorPreyModel.addNewPredatorAgent(predatorPreyModel.predatorList);
	      }
	      }
	      else
	      {
	          System.out.println("overpopulatedPredator");
	          predatorPreyModel.stop(); 
	      }
	      
	      Predator.Kids = 0;

	      
	      double currentLivingPreys = predatorPreyModel.countLivingAgents(predatorPreyModel.preyList);
	      double currentLivingPredators = predatorPreyModel.countLivingAgents(predatorPreyModel.predatorList);
	      

	      predatorPreyModel.sSpace.spreadGrass(((int) ((predatorPreyModel.newGrassGrowthRate * (1)* predatorPreyModel.worldXSize * predatorPreyModel.worldYSize / (100*10))))); // Scaled to 1/10th of percent of area! //+0.3*Math.sin(seazon*getTickCount()*Math.PI/180)
		
	      if( k >= horizon - rangeAverageFitness){
	    	  meanRatePreysOverPredators += currentLivingPreys / currentLivingPredators;
	      }
	      
	      if( k >= horizon - rangeAverageFitness){
	    	 
	    	  meanRatePreyEaten += (double) predatorPreyModel.nbrPreyEaten/ (double)predatorPreyModel.numPreyAgents;
	    	  
	      }

		}
		
		meanRatePreysOverPredators = meanRatePreysOverPredators / (double)(rangeAverageFitness);
		meanRatePreyEaten = meanRatePreyEaten / (double)(rangeAverageFitness);
		

		 

	    
	    

	}
}

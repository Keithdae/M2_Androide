package Model_PredatorPrey;

import Model_Calibration.FitnessTargets;

public class ParallelSimuLauncher {


	
	
	
	public double[] getFitnessSimulation(int NbRunSimu, int horizonCalib, int rangeAverageFitness, double[] params ){
		
		double fitness = 0;
		double meanRatePreysOverPredators = 0;	
		double meanRatePreyEaten = 0;
		
	
		
    		 
	         Simulation[] threads = new Simulation[NbRunSimu];


	         
	         for (int t = 0; t < NbRunSimu; t++) {

	    				
	             threads[t] = new Simulation();
	        	
	             threads[t].horizon = horizonCalib;
	             
	             threads[t].rangeAverageFitness = rangeAverageFitness;
	        	
	             threads[t].energyPredatorUseEachTick =params[0];
	             threads[t].energyPredatorUseForMate = params[1];
	             threads[t].energyPreyUseEachTick = params[2];
	             threads[t].energyPreyUseForMate= params[3];
	             threads[t].energyTakenFromGrass= params[4];
	             threads[t].energyTakenFromPrey= params[5];
	             threads[t].probabilityForPredatorMate= params[6];
	        	 threads[t].probabilityForPreyMate= params[7];
	        	 
	             threads[t].start();
	             
	           
	         }
	         
	         

	         try {

	             for (int t = 0; t < NbRunSimu; t++) {
	                 threads[t].join();
	             }
	          

		        
	         

	         } catch (InterruptedException e) {
	             e.printStackTrace();
	         }
	         
	        
	         for (int t = 0; t < NbRunSimu; t++) {
					
					
					meanRatePreysOverPredators+= threads[t].meanRatePreysOverPredators;
					meanRatePreyEaten +=  threads[t].meanRatePreyEaten;	 
					
	         }
	         
	         

		meanRatePreysOverPredators = meanRatePreysOverPredators /NbRunSimu;
		meanRatePreyEaten = meanRatePreyEaten / NbRunSimu;
		
		
		
	    fitness = Math.pow((meanRatePreysOverPredators - FitnessTargets.ratePreysOverPredatorsTarget)/FitnessTargets.ratePreysOverPredatorsTarget, 2)+
	    		Math.pow((meanRatePreyEaten - FitnessTargets.ratePreyEatenTarget)/FitnessTargets.ratePreyEatenTarget, 2);
	    
	    
		double[] result = new double[3];
		
		result[0] = fitness;
		result[1] = meanRatePreysOverPredators;
		result[2] = meanRatePreyEaten;
		
		
		return result;
	}
}

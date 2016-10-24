package Model_Calibration.SimulatedAnnealing;


import java.util.ArrayList;



import AlgoGDE.Pair;
import AlgoGDE.Teacher.Result;
import AlgoRecuit.AnnealingScheme;
import AlgoRecuit.ObjectiveFunction;
import Model_Calibration.OptionResults;
import Model_Calibration.Parameters;
import Model_PredatorPrey.ParallelSimuLauncher;
import Model_PredatorPrey.Simulation;

import uchicago.src.sim.util.SimUtilities;


public class TeacherSimulatedAnnealing {

	public static int itr = 0; 
	public static boolean stopannealingSchemme = false;
	
	/* 
	 * horizonCalib : horizon de calibration du modèle
	 * NbRunSimu : Nb de simu lancées pour le calcul de la fitness moyenne
	 * rangeAverageFitness : Plage de temps sur laquelle sont moyennées les sorties du modèle
	 * coolingRate : taux de refroidissement
	 * temperature : température initiale
	 * iteration : nombre d'itération avec refroidissement
	 * fitnessTarget : objectif de fitness
	 * maxIter : nombre d'iterations max autorisées
	 */
	
	public double teachSimulatedAnnealing(final int horizonCalib, final int NbRunSimu,final int rangeAverageFitness, double coolingRate,  double temperature, int iteration, final double fitnessTarget, final int maxIter ) {
		
		stopannealingSchemme = false;
		itr = 0; 
		
		AnnealingScheme scheme = new AnnealingScheme();
		
        final Parameters parameters = new Parameters();
        
        final double[] initParams = parameters.getInitParams();
		
        /* 
         * Paramètres de l'algorithme de recuit simulé
         */
		scheme.setCoolingRate(coolingRate);
		scheme.setTemperature(temperature);
		scheme.setIterations((int) iteration);
		
		
        
        /* 
         * Fonction fitness
         */
		scheme.setFunction(new ObjectiveFunction()
		{
			double bestFitness = 1000000;
			
			public int getNdim()
			{
				return parameters.getInitParams().length;
			}

			public double distance(double[] params)
			{
			
				
				double[] finalParams = new double[8];
				
				for(int i = 0; i < 8; i++){
					finalParams[i] = initParams[i] + params[i];
				}
				
				
				Parameters parameters = new Parameters();
				
                double penalty  = parameters.BoundaryPenaltyParameters(params);
                if( penalty > 0){
                	return 100 + penalty *10;
                }
                
                itr++;
                
	            ParallelSimuLauncher parallelSimuLauncher = new ParallelSimuLauncher();
	            double[] results = parallelSimuLauncher.getFitnessSimulation(NbRunSimu, horizonCalib, rangeAverageFitness, finalParams);
				         
	            double fitness = results[0];
	            
				if(fitness < bestFitness){
					
					bestFitness = fitness;
				
					if(OptionResults.showFitness){
						System.out.println("bestFitness " + fitness );
					}

					
				}	
				
				if (fitness < fitnessTarget ){
					
			    	if(OptionResults.showCalibratedResults){
			    		
						System.out.println("energyPredatorUseEachTick " + finalParams[0]);
						System.out.println("energyPredatorUseForMate " + finalParams[1]);
						System.out.println("energyPreyUseEachTick " + finalParams[2]);
						System.out.println("energyPreyUseForMate " + finalParams[3]);
						System.out.println("energyTakenFromGrass " + finalParams[4]);
						System.out.println("energyTakenFromPrey " + finalParams[5]);
						System.out.println("probabilityForPredatorMate " + finalParams[6]);
						System.out.println("probabilityForPreyMate " + finalParams[7]);
						System.out.println(" ");
						System.out.println("meanRatePreysOverPredators " + results[1]);
						System.out.println("meanRatePreyEaten " + results[2]);
			    	}
			    	
					stopannealingSchemme = true;
				}
				
				if(itr == maxIter ){
					
					stopannealingSchemme = true;
				}
				
				return fitness;
			}
		});
		

		
		/*
		 * Lancement de l'algorithme
		 */
		scheme.anneal();
		

		if(itr > maxIter ){
			return -1;
		}
		
		
		return itr;
	}
}

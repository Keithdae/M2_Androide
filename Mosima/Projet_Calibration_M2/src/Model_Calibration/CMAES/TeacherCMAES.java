package Model_Calibration.CMAES;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;



import apache.commons.math3.analysis.MultivariateFunction;
import apache.commons.math3.optim.InitialGuess;
import apache.commons.math3.optim.PointValuePair;
import apache.commons.math3.optim.SimpleBounds;
import apache.commons.math3.optim.SimpleValueChecker;
import apache.commons.math3.optim.nonlinear.scalar.GoalType;
import apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import apache.commons.math3.random.JDKRandomGenerator;





import Model_Calibration.OptionResults;
import Model_Calibration.Parameters;
import Model_PredatorPrey.ParallelSimuLauncher;
import Model_PredatorPrey.Simulation;



import uchicago.src.sim.util.SimUtilities;



public class TeacherCMAES {

	
	public static int itr;
	
	/* 
	 * horizonCalib : horizon de calibration du modèle
	 * NbRunSimu : Nb de simu lancées pour le calcul de la fitness moyenne
	 * rangeAverageFitness : Plage de temps sur laquelle sont moyennées les sorties du modèle
	 * sigmaCMAES : sigma init de l'algorithme CMA-ES
	 * fitnessTarget : objectif de fitness
	 * maxIter : nombre d'iterations max autorisées
	 */
	
	public double teachCMAES(final int horizonCalib,final int  NbRunSimu, final int rangeAverageFitness, double sigmaCMAES, final double fitnessTarget ,final  int maxIter) {
		
		
		
		
		final Parameters parameters = new Parameters();
		
		/*
		 * Tirage d'un point d'initialisation aléatoire
		 */
		final double[] initParams = parameters.getInitParams();
		
		
		
		int popSize = parameters.getInitParams().length;
		
		double[] startingPoint = new double[parameters.getInitParams().length];
		for(int i = 0; i < parameters.getInitParams().length; i++){
			startingPoint[i] = 1;
	    }
		
		JDKRandomGenerator rand = new JDKRandomGenerator();
		rand.setSeed(System.nanoTime());
		
		double[] sigmaVector = new double[popSize]; 
	   	
		for(int i = 0; i < parameters.getInitParams().length; i++){
			  sigmaVector[i] = sigmaCMAES;
		}
		
		itr = 0;
		
		
		
		/*
		 * Fonction fitness
		 */
		MultivariateFunction f = new MultivariateFunction() {
			

			
			double bestFitness = 10000;
			
			
			
			public double value(double[] arg0) {
			    
				

				
            	double[] params = new double[8];
            	for (int j = 0; j < 8; ++j) {
            		params[j] =  arg0[j]  * initParams[j];
            	}
				
                ParallelSimuLauncher parallelSimuLauncher = new ParallelSimuLauncher();
                
                /*
                 * Pénalité si le point tiré sort des bornes de la calibration
                 */
                double penalty  = parameters.BoundaryPenaltyParameters(params);
                if( penalty > 0){
                	return 100 + penalty *100;
                }
                
                itr++;
                
                /*
                 *  Calcul de la fitness moyenne : lancement de NbRunSimu en multi-thread
                 */
                double[] results = parallelSimuLauncher.getFitnessSimulation(NbRunSimu, horizonCalib, rangeAverageFitness, params);
            	
                double fitness = results[0];
                
                if(fitness < bestFitness){
                	
                	if(OptionResults.showFitness){
                		System.out.println("fitness " + fitness);

                	}
                	
                	bestFitness = fitness;
                }
                
                
                if(fitness < fitnessTarget ){	
                	
			   
			    	if(OptionResults.showCalibratedResults){
			    		
						System.out.println("energyPredatorUseEachTick " + params[0]);
						System.out.println("energyPredatorUseForMate " + params[1]);
						System.out.println("energyPreyUseEachTick " + params[2]);
						System.out.println("energyPreyUseForMate " + params[3]);
						System.out.println("energyTakenFromGrass " + params[4]);
						System.out.println("energyTakenFromPrey " + params[5]);
						System.out.println("probabilityForPredatorMate " + params[6]);
						System.out.println("probabilityForPreyMate " + params[7]);
						System.out.println(" ");
						System.out.println("meanRatePreysOverPredators " + results[1]);
						System.out.println("meanRatePreyEaten " + results[2]);
					
						
			    	}
			    	
			    	return 0;
                }
                
                if(itr == maxIter){
                	return 0;
                }
                
                
            	return fitness;
			}
			
	
			
		};
				

		InitialGuess initialGuess = new InitialGuess(startingPoint);
		
		
		double[] lb = new double[8];
		for(int i = 0; i < 8; i++){
			  lb[i] = -1000;
			  }
		  
		double[] ub = new double[8];
		for(int i = 0; i < 8; i++){
			  ub[i] = +1000;
	    }


		SimpleBounds simpleBounds = new SimpleBounds(lb,ub);  
		SimpleValueChecker checker = new SimpleValueChecker(0.00000000000001,0.000000000001);
		  

		CMAESOptimizer opt = new CMAESOptimizer(999999, 0.000000001, true, 1, 1, rand, false, checker);
		
		PointValuePair p = opt.optimize(new ObjectiveFunction(f),	GoalType.MINIMIZE, new CMAESOptimizer.Sigma(sigmaVector), new	CMAESOptimizer.PopulationSize(popSize),initialGuess,simpleBounds);
	    
		if(itr >= maxIter){
			return -1;
		}
		
		return itr;
	
	}
	
	
	
	
}

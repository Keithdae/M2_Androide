package Model_Calibration.GDE;


import java.util.ArrayList;
import java.util.LinkedList;

import AlgoGDE.GroupedDifferentialEvolution;
import AlgoGDE.Pair;
import AlgoGDE.Teacher.Result;
import Model_Calibration.OptionResults;
import Model_Calibration.Parameters;
import Model_PredatorPrey.ParallelSimuLauncher;
import Model_PredatorPrey.Simulation;



import uchicago.src.sim.util.SimUtilities;


public class TeacherGDE {

	
	/* 
	 * horizonCalib : horizon de calibration du modèle
	 * NbRunSimu : Nb de simu lancées pour le calcul de la fitness moyenne
	 * rangeAverageFitness : Plage de temps sur laquelle sont moyennées les sorties du modèle
	 * F : mutation parameter pour GDE (See Piotrowski, A.P., Napiórkowski, J.J., 2010. The grouping differential evolution algorithm for multi-dimensional optimization problems)
	 * Cr : crossover parameter pour GDE (See Piotrowski, A.P., Napiórkowski, J.J., 2010.)
	 * PNI_ : nombre d'iteration pendant lequel un groupe est autorisé à recevoir à échanger des informations avec les autres groupes quand il est bloqué dans un optimum local (See Piotrowski, A.P., Napiórkowski, J.J., 2010.)
	 * fitnessTarget : objectif de fitness
	 * maxIter : maxIter : nombre d'iterations max autorisées
	 */
	
	
	public double teachGDE(int horizonCalib, int NbRunSimu, int rangeAverageFitness, double F,  double Cr, int PNI_, double fitnessTarget , int maxIter) {
		
		
		
	    int weightNb = 8;
        double bestFitness = 999999;

        int PNI = (int) PNI_;

        double[] GFBEST_PNI = new double[4];

        int counter_frozen_individual = 0;
        LinkedList<Integer> frozList = new LinkedList<Integer>();

        double[][] fitness;
        double[][] fitness_nextgeneration;

        GroupedDifferentialEvolution GDE = new GroupedDifferentialEvolution();

        GDE.setDimension(weightNb);
        GDE.setF(F);
        GDE.setCr(Cr);
        int pop_size = weightNb * 8;
        int group_pop_size = weightNb * 2;

        GDE.setPop_size(pop_size);
        GDE.setGroupPop_size(group_pop_size);

        

        
        double[][][] group_pop = GDE.init();

        fitness = new double[4][group_pop_size];
        fitness_nextgeneration = new double[4][group_pop_size];

        int itr = 0;
        Parameters parameters = new Parameters();
        
        for (int n = 0; n < 4; ++n) {
            for (int i = 0; i < group_pop_size; ++i) {
            	
            	
            	
            	
            	
            	
                double penalty  = parameters.BoundaryPenaltyParameters(group_pop[n][i]);
                if( penalty > 0){
                	fitness[n][i] =  100 + penalty *10;
                } else {
                
	                itr++;
	            	
	              
	                ParallelSimuLauncher parallelSimuLauncher = new ParallelSimuLauncher();
	                double[] results = parallelSimuLauncher.getFitnessSimulation(NbRunSimu, horizonCalib, rangeAverageFitness, group_pop[n][i]);
	                double fit = results[0];
	                
	                fitness[n][i] = fit;
	                
					if(fit  < bestFitness){
						
						bestFitness = fit;

						if(OptionResults.showFitness){
							System.out.println("bestFitness " + fit );
						}
						

					}
					
					if(fit  < fitnessTarget){
						
				    	if(OptionResults.showCalibratedResults){
				    		
								System.out.println("energyPredatorUseEachTick " + group_pop[n][i][0]);
								System.out.println("energyPredatorUseForMate " + group_pop[n][i][1]);
								System.out.println("energyPreyUseEachTick " + group_pop[n][i][2]);
								System.out.println("energyPreyUseForMate " + group_pop[n][i][3]);
								System.out.println("energyTakenFromGrass " + group_pop[n][i][4]);
								System.out.println("energyTakenFromPrey " + group_pop[n][i][5]);
								System.out.println("probabilityForPredatorMate " + group_pop[n][i][6]);
								System.out.println("probabilityForPreyMate " + group_pop[n][i][7]);
								System.out.println(" ");
								System.out.println("meanRatePreysOverPredators " + results[1]);
								System.out.println("meanRatePreyEaten " + results[2]);
					    }
				    	
						return itr;
					}
					
	                if(itr == maxIter){
	                	return -1;
	                }
                }
               
            }
        }

        for (int counter = 0; counter < 50000; counter++) {

            // --- core iteration step ---
            double[][][] pop_nextgeneration = GDE.samplePopulation();
            double[] GFBEST_current = new double[4];
            int[] MinPop = new int[4];

            double GF = 0;

            for (int n = 0; n < 4; ++n) {
                GFBEST_current[n] = 1;
            }

            for (int n = 0; n < 4; ++n) {
                for (int i = 0; i < group_pop_size; ++i) {

                    if (pop_nextgeneration[n][i] != group_pop[n][i]) {
                    	
                    	double fit = 0;
                    	

                    	
                        double penalty  = parameters.BoundaryPenaltyParameters(group_pop[n][i]);
                        if( penalty > 0){
                        	fit =  100 + penalty *10;
                        } else {
                        
        	                itr++;
        	            	
        	                if(itr == maxIter){
        	                	return -1;
        	                }

        	                
                        	ParallelSimuLauncher parallelSimuLauncher = new ParallelSimuLauncher();
                            double[]  results = parallelSimuLauncher.getFitnessSimulation(NbRunSimu, horizonCalib, rangeAverageFitness, group_pop[n][i]);
        	                fit = results[0];
                      
	
    					
	    					if(fit  < bestFitness){
	    						
	    						bestFitness = fit;
	
	    						if(OptionResults.showFitness){
	    							System.out.println("bestFitness " + fit );
	    						}
	    					}
	    					
	    					
	    					if (fit < fitnessTarget ){
	    						
	    						
	    				    	if(OptionResults.showCalibratedResults){
	    				    		
	    								System.out.println("energyPredatorUseEachTick " + group_pop[n][i][0]);
	    								System.out.println("energyPredatorUseForMate " + group_pop[n][i][1]);
	    								System.out.println("energyPreyUseEachTick " + group_pop[n][i][2]);
	    								System.out.println("energyPreyUseForMate " + group_pop[n][i][3]);
	    								System.out.println("energyTakenFromGrass " + group_pop[n][i][4]);
	    								System.out.println("energyTakenFromPrey " + group_pop[n][i][5]);
	    								System.out.println("probabilityForPredatorMate " + group_pop[n][i][6]);
	    								System.out.println("probabilityForPreyMate " + group_pop[n][i][7]);
	    								System.out.println(" ");
	    								System.out.println("meanRatePreysOverPredators " + results[1]);
	    								System.out.println("meanRatePreyEaten " + results[2]);
	    					    }
	    				    	
	    						return itr;
	    					}
                        }
                        
                        fitness_nextgeneration[n][i] = fit;

               

                        if (fitness_nextgeneration[n][i] < fitness[n][i]) {

                            group_pop[n][i] = pop_nextgeneration[n][i];
                            fitness[n][i] = fitness_nextgeneration[n][i];
                        }

                        if (fitness[n][i] < GFBEST_current[n]) {
                            GFBEST_current[n] = fitness[n][i];
                            MinPop[n] = i;
                        }
                    }

                

                }


            }

            if (counter % PNI == 0) {

                if (counter == 0) {
                    GFBEST_PNI = GFBEST_current;
                } else {

                    GF = 0;
                    for (int n = 0; n < 4; ++n) {
                        GF = GF + GFBEST_PNI[n] - GFBEST_current[n];
                    }
                    GF = GF / 100;

                    int[] LG = GDE.getLG();
                    int[] Lx = GDE.getLx();
                    int counter_group_trapped = 0;

                    for (int n = 0; n < 3; ++n) {
                        if (GFBEST_PNI[n] - GFBEST_current[n] < GF && LG[n] == 0 && counter_group_trapped == 0) {
                            LG[n] = 1;
                            counter_group_trapped = 1;

                            Lx[n * group_pop_size + MinPop[n]] = 1;
                            frozList.addLast(n * group_pop_size + MinPop[n]);

                            if (counter_frozen_individual < pop_size / 10) {
                                counter_frozen_individual = counter_frozen_individual + 1;
                            } else {
                                Lx[frozList.getFirst()] = 0;
                                frozList.removeFirst();
                            }

                        } else {
                            LG[n] = 0;
                        }

                    }

                    GDE.setLG(LG);
                    GDE.setLx(Lx);
                    GFBEST_PNI = GFBEST_current;
                }

            }

            GDE.setPop(group_pop);

        }


        return itr;		
	}
	
	 public static double getMinFitness(double[][] fitness) {

	        double min = 1;

	        for (int n = 0; n < 4; ++n) {
	            for (int i = 0; i < fitness[n].length; ++i) {
	                if (fitness[n][i] < min) {

	                    min = fitness[n][i];

	                }
	            }
	        }

	        return min;

	    }
	 
	 public static int getMinPopFitness(double[][] fitness) {

	        double min = 1;
	        int minPop = 0;

	        for (int n = 0; n < 4; ++n) {
	            for (int i = 0; i < fitness[n].length; ++i) {
	                if (fitness[n][i] < min) {

	                    min = fitness[n][i];
	                    minPop = n * fitness[n].length + 1;
	                }
	            }
	        }

	        return minPop;

	    }
}

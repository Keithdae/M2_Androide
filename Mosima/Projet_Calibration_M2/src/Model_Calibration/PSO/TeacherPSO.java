package Model_Calibration.PSO;


import Model_Calibration.OptionResults;
import Model_Calibration.Parameters;
import AlgoPSO.Swarm;

public class TeacherPSO {
	public static int NbSimu;
	
	/* 
	 * horizonCalib : horizon de calibration du modèle
	 * NbRunSimu : Nb de simu lancées pour le calcul de la fitness moyenne
	 * rangeAverageFitness : Plage de temps sur laquelle sont moyennées les sorties du modèle
	 * fitnessTarget : objectif de fitness
	 * maxIter : nombre d'iterations max autorisées
	 */
	public double teachPSO(int horizonCalib, int NbRunSimu, int rangeAverageFitness, double fitnessTarget , int maxIter) {
		
		NbSimu = NbRunSimu;
		int sizeSwarm = 25;
		
		Swarm swarm = new Swarm(sizeSwarm, new MyParticle(), new MyFitnessFunction(false));

		Parameters parameters = new Parameters();
		
		/*
		 * Paramètres du PSO : bornes sur les positions et vitesses des particules
		 */
		double[] maxPosition=parameters.getMaxParam();
		double[] minPosition=parameters.getMinParam();
		double[] maxVelocity=parameters.getMaxVelocity();
		double[] minVelocity=parameters.getMinVelocity();
		
		swarm.setMaxPosition(maxPosition);
		swarm.setMinPosition(minPosition);
		swarm.setMaxVelocity(maxVelocity);
		swarm.setMinVelocity(minVelocity);
		
		
		double bestFitness = 999999999;
		
		/*
		 * initialisation de l'essaim
		 */
		swarm.init();
		
		for(int i = 0; i < maxIter/sizeSwarm; i++){
			
			
			double[] results = swarm.evolve(fitnessTarget);
			
			
			double bestFitnessCourante = results[0];
			
			
			if(bestFitnessCourante < bestFitness){
				
				if(OptionResults.showFitness){
					System.out.println("bestFitness " + bestFitnessCourante );
				}
				bestFitness = bestFitnessCourante;
			}
			
			
			if(bestFitnessCourante < fitnessTarget){
				
				double[] params = swarm.getBestParticle().getPosition();
				
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
		    	
				return swarm.getNumberOfEvaliations();
			}
		}
		return -1;
	}
}

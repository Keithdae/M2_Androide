package Model_Calibration;

import apache.commons.math3.random.JDKRandomGenerator;

public class Parameters {
	// Espace des paramètres (bornes min et max)
	// dépenses 
    double minEnergyPredatorUseEachTick= 2; // survie prédateur
    double minEnergyPredatorUseForMate =5;  // reproduction prédateur
    double minEnergyPreyUseEachTick = 2;	// survie proie
    double minEnergyPreyUseForMate = 10;	// reproduction proie
   
    // gains 
    double minEnergyTakenFromGrass = 30;	// consommation proie	
    double minEnergyTakenFromPrey = 50;		// consommation  prédateur
    
    // taux reproduction
    double minProbabilityForPredatorMate = 0.5;
    double minProbabilityForPreyMate = 0.1;
    

    double maxEnergyPredatorUseEachTick = 5;
    double maxEnergyPredatorUseForMate = 15;
    double maxEnergyPreyUseEachTick = 10;
    double maxEnergyPreyUseForMate = 30;
    double maxEnergyTakenFromGrass = 70; 
    double maxEnergyTakenFromPrey = 100;
    double maxProbabilityForPredatorMate = 1;
    double maxProbabilityForPreyMate = 0.5;
     
    
    double[] max = new double[8];
    
  
   
    public double[] getInitParams(){

    	
    	double[] initParams= new double[8];
    	
		JDKRandomGenerator rand = new JDKRandomGenerator();
		rand.setSeed(System.nanoTime());
    	
    	initParams[0] = minEnergyPredatorUseEachTick + rand.nextDouble()* (maxEnergyPredatorUseEachTick - minEnergyPredatorUseEachTick ); 
    	initParams[1] = minEnergyPredatorUseForMate + rand.nextDouble()* (maxEnergyPredatorUseEachTick - minEnergyPredatorUseForMate ); 
    	initParams[2] = minEnergyPreyUseEachTick + rand.nextDouble()* (maxEnergyPreyUseEachTick - minEnergyPreyUseEachTick ); 
    	initParams[3] = minEnergyPreyUseForMate + rand.nextDouble()* (maxEnergyPreyUseForMate - minEnergyPreyUseForMate ); 
    	initParams[4] = minEnergyTakenFromGrass + rand.nextDouble()* (maxEnergyTakenFromGrass - minEnergyTakenFromGrass ); 
    	initParams[5] = minEnergyTakenFromPrey + rand.nextDouble()* (maxEnergyTakenFromPrey - minEnergyTakenFromPrey ); 
    	initParams[6] = minProbabilityForPredatorMate + rand.nextDouble()* (maxProbabilityForPredatorMate - minProbabilityForPredatorMate ); 
    	initParams[7] = minProbabilityForPreyMate + rand.nextDouble()* (maxProbabilityForPreyMate - minProbabilityForPreyMate ); 
    	

    	return initParams;
    }
    
  
    
   	public double[] getMaxParam(){
   		double[] maxPosition = new double[8];
   		maxPosition[0] = maxEnergyPredatorUseEachTick;
		maxPosition[1] = maxEnergyPredatorUseForMate;
		maxPosition[2] = maxEnergyPreyUseEachTick;
		maxPosition[3] = maxEnergyPreyUseForMate;
		maxPosition[4] = maxEnergyTakenFromGrass; 
		maxPosition[5] = maxEnergyTakenFromPrey;
		maxPosition[6] = maxProbabilityForPredatorMate;
		maxPosition[7] = maxProbabilityForPreyMate;
		
		return maxPosition;
   	}
   	
   	
   	public  double[] getMinParam(){
   		double[] minPosition = new double[8];
   		minPosition[0] = minEnergyPredatorUseEachTick;
		minPosition[1] = minEnergyPredatorUseForMate;
		minPosition[2] = minEnergyPreyUseEachTick;
		minPosition[3] = minEnergyPreyUseForMate;
		minPosition[4] = minEnergyTakenFromGrass; 
		minPosition[5] = minEnergyTakenFromPrey;
		minPosition[6] = minProbabilityForPredatorMate;
		minPosition[7] = minProbabilityForPreyMate;

		return minPosition;
   	}
   	
   	
   	public double BoundaryPenaltyParameters(double[] parameters){
   		
   		double[] maxParam = getMaxParam();
   		double[] minParam = getMinParam();
   		
   		double penalty = 0;
   		
   		for(int i = 0; i < 8 ; i++){
   			
   			if(parameters[i] > maxParam[i]){
   				
   				penalty += parameters[i] -  maxParam[i];
   				
   			}
   			
			if(parameters[i] < minParam[i]){
	   				
	   				penalty +=    minParam[i] - parameters[i];
	   				
	   		}
 		
   				
   		}
		return penalty;
   	}
   	
   	public  double[] getMaxVelocity(){
   		double[] maxVelocity = new double[8];
   		maxVelocity[0] = 0.1;
		maxVelocity[1] = 0.2;
		maxVelocity[2] = 0.1;
		maxVelocity[3] = 0.2;
		maxVelocity[4] = 0.5; 
		maxVelocity[5] = 0.5;
		maxVelocity[6] = 0.02;
		maxVelocity[7] = 0.02;
		
		return maxVelocity;
   	}
   	
   	public  double[] getMinVelocity(){
   		double[] minVelocity = getMaxVelocity();
   		
   		for(int i=0; i<8; i++){
   			minVelocity[i] = -minVelocity[i];
   		}
   		
		return minVelocity;
   	}
}



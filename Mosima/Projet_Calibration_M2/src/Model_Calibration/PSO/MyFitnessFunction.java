package Model_Calibration.PSO;

import Model_PredatorPrey.ParallelSimuLauncher;
import AlgoPSO.FitnessFunction;

public class MyFitnessFunction extends FitnessFunction{
	
	public MyFitnessFunction(boolean maximize) {
		super( maximize);
	}
	
	public double[] evaluate(double params[]) { 
		ParallelSimuLauncher parallelSimuLauncher = new ParallelSimuLauncher();
		
		
		
        double[] results = parallelSimuLauncher.getFitnessSimulation(TeacherPSO.NbSimu, 100, 10, params);
		return results; 
	}
}

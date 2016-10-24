package Model_Calibration.SimulatedAnnealing;



public class Main_SimulatedAnnealing {

	public static void main(String[] args) {
		
		
		int N = 20;
		
		TeacherSimulatedAnnealing teacherSimulatedAnnealing = new TeacherSimulatedAnnealing();	
		
		/*
		 * Nb de simu lanc��es pour le calcul de la fitness moyenne
		 */
		int NbRun = 24;
 	
		
		double nbIterMoy=0;
		
		int nbEchecs = 0;
		
		double cpt = 0;
		
		
		for(int i = 0; i < N ; i++){
			 
			 /*
			  * Lancement d'une calibration avec le CMA-ES et r��cup��ration du nombre de jeux de param��tres test��s
			  */
			double nbrIter = teacherSimulatedAnnealing.teachSimulatedAnnealing(100, NbRun, 10,10, 10, 3, 0.01, 1000);
			   
			 System.out.println("nbrIter " + nbrIter);  
			 
			 if(nbrIter == -1){
				 nbEchecs++;
			 } else {
				 nbIterMoy += nbrIter;
				 cpt++;
			 }
			 
			 
		}
		
		/*
		 * Moyenne du nombre de jeux de param��tres test��s pour calibrer
		 */
		if(cpt !=0){
			nbIterMoy=nbIterMoy/cpt;
		}
		
		double tauxEchecs = nbEchecs/(double)N;
		
		System.out.println("nbIterMoy " + nbIterMoy);
		System.out.println("tauxEchecs calibration" + tauxEchecs);
		
		
		
		
	
	}
}
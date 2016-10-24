package Model_Calibration.CMAES;


public class Main_CMAES {

	public static void main(String[] args) {
		
		int NbCalib=5; // Nombre de calibrations par run 
	
		TeacherCMAES teacherCMAES = new TeacherCMAES();
		
		/*
		 * Nb de simu lancées pour le calcul de la fitness moyenne
		 */
		int NbSimus = 1;
 	
		
		double nbIterMoy=0;
		
		int nbEchecs = 0;
		
		double cpt = 0;
		
		
		for(int i = 0; i < NbCalib ; i++){
			 
			 /*
			  * Lancement d'une calibration avec le CMA-ES et récupération du nombre d'itération - 1 itération = 1 jeu de paramètres testés
			  */
			 double nbrIter =  teacherCMAES.teachCMAES(100, NbSimus, 30, 0.5 , 0.01, 1000);
			   
			 System.out.println("[Calibration #" + (i+1) +"] - nbrIter =" + nbrIter +"\n-------------------------------------------\n");  
			 
			 if(nbrIter == -1){
				 nbEchecs++;
			 } else {
				 nbIterMoy += nbrIter;
				 cpt++;
			 }
			 
			 
		}
		
	// Moyenne du nombre d'itérations
		if(cpt !=0){
			nbIterMoy=nbIterMoy/cpt;
		}
		
		double tauxEchecs = nbEchecs/(double)NbCalib;
		  
		System.out.println("\n nbIterMoy : " + nbIterMoy);
		System.out.println("tauxEchecs : " + tauxEchecs);
		
	}
}

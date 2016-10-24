package Model_Calibration.PSO;



public class Main_PSO {

	public static void main(String[] args) {
		int N=20;
		
		
		TeacherPSO teacherPSO = new TeacherPSO();

		/*
		 * Nb de simu lancées pour le calcul de la fitness moyenne
		 */
		int NbRun = 48;
 	
		
		double nbIterMoy=0;
		
		int nbEchecs = 0;
		
		double cpt = 0;
		
		
		for(int i = 0; i < N ; i++){
			 
			 /*
			  * Lancement d'une calibration avec le CMA-ES et récupération du nombre de jeux de paramètres testés
			  */
			 double nbrIter =  teacherPSO.teachPSO(100, NbRun, 30, 0.01, 1000);
			   
			 System.out.println("nbrIter " + nbrIter);  
			 
			 if(nbrIter == -1){
				 nbEchecs++;
			 } else {
				 nbIterMoy += nbrIter;
				 cpt++;
			 }
			 
			 
		}
		
		/*
		 * Moyenne du nombre de jeux de paramètres testés pour calibrer
		 */
		if(cpt !=0){
			nbIterMoy=nbIterMoy/cpt;
		}
		
		double tauxEchecs = nbEchecs/(double)N;
		
		System.out.println("nbIterMoy : " + nbIterMoy);
		System.out.println("taux Echecs calibration : " + tauxEchecs);

	}
}
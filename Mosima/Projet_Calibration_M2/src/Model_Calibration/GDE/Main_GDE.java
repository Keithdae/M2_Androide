package Model_Calibration.GDE;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main_GDE {

	public static void main(String[] args) {
		int N = 20;
		
		TeacherGDE teacherGDE = new TeacherGDE();

		/*
		 * Nb de simu lancées pour le calcul de la fitness moyenne
		 */
		int NbRun = 48;
		
		
		double nbIterMoy=0;
		
		int nbEchecs = 0;
		
		double cpt = 0;

		   
		for(int i = 0; i < N ; i++){
			
			// Lancement d'une calibration avec le CMA-ES et récupération du nombre de jeux de paramètres testés
			 
                // public double teachGDE(int horizonCalib, int NbRunSimu, int rangeAverageFitness, double F,  double Cr, int PNI_, double fitnessTarget , int maxIter) {

			double nbrIter = teacherGDE.teachGDE(100, NbRun, 10, 0.5, 0.5, 10, 0.01, 1000);
			 
			 System.out.println("nbrIter " + nbrIter);  
			 
			 if(nbrIter == -1){
				 nbEchecs++;
			 } else {
				 nbIterMoy += nbrIter;
				 cpt++;
			 }
		}
		
		/*
		 * Moyenne du nombre de jeu de paramètres testés pour calibrer
		 */
		if(cpt !=0){
			nbIterMoy=nbIterMoy/cpt;
		}
		
		double tauxEchecs = nbEchecs/(double)N;
		  
		System.out.println("nbIterMoy : " + nbIterMoy);
		System.out.println("tauxEchecs : " + tauxEchecs);
	 
	}
}
package Model_Calibration;

import Model_Calibration.CMAES.TeacherCMAES;
import Model_Calibration.GDE.*;

public class Calibration {

	public static void main(String[] args) {
		int NbCalib= 5; // Nombre de calibrations par run 
		
		TeacherCMAES teacherCMAES = new TeacherCMAES();
		TeacherGDE teacherGDE = new TeacherGDE();
		
		/*
		 * Nb de simu lancées pour le calcul de la fitness moyenne
		 */
		int NbSimus = 2;
 	
		
		double nbIterMoy1=0;
		double nbIterMoy2=0;
		
		double[] nbIter1=new double[NbCalib];
		double[] nbIter2=new double[NbCalib];
		
		int nbEchecs1 = 0;
		int nbEchecs2 = 0;
		
		double cpt1 = 0;
		double cpt2 = 0;
		
		
		for(int i = 0; i < NbCalib ; i++){
			 
			 /*
			  * Lancement d'une calibration avec le CMA-ES et récupération du nombre d'itération - 1 itération = 1 jeu de paramètres testés
			  */
			 nbIter1[i] = teacherCMAES.teachCMAES(100, NbSimus, 30, 0.5 , 0.01, 1000);
			 nbIter2[i] = teacherGDE.teachGDE(    100, NbSimus, 30, 0.5, 0.5, 10, 0.01, 1000);
			  
			 
			 
			 System.out.println("[Calibration #" + (i+1) +"] - nbrIter =" + nbIter1[i] +"\n-------------------------------------------\n");  
			 
			 if(nbIter1[i] == -1){
				 nbEchecs1++;
			 } else {
				 nbIterMoy1 += nbIter1[i];
				 cpt1++;
			 }
			 
			 if(nbIter2[i] == -1){
				 nbEchecs2++;
			 } else {
				 nbIterMoy2 += nbIter2[i];
				 cpt2++;
			 }
			 
			 
		}
		
		// Moyenne du nombre d'itérations
		if(cpt1 !=0){
			nbIterMoy1=nbIterMoy1/cpt1;
		}
		
		double tauxEchecs1 = nbEchecs1/(double)NbCalib;
		
		if(cpt2 !=0){
			nbIterMoy2=nbIterMoy2/cpt2;
		}
		
		double tauxEchecs2 = nbEchecs2/(double)NbCalib;
		  
		System.out.println("\nCMAES nbIterMoy : " + nbIterMoy1);
		System.out.println("tauxEchecs : " + tauxEchecs1);
		
		System.out.println("\nGDE nbIterMoy : " + nbIterMoy2);
		System.out.println("tauxEchecs : " + tauxEchecs2);
		
	}
	

}

package prologTest;

import java.util.Random;

public class PrologCalls {

	public PrologCalls() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean hooked(){
		Random rng = new Random();
		float rand = rng.nextFloat();
		if(rand < 0.2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}

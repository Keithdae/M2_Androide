package prologTest;

import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Compound;
import org.jpl7.JPLException;
import org.jpl7.PrologException;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Util;
import org.jpl7.Variable;

public class fishingtest {
	public static void main(String[] args) {
		boolean open = openFishing();
		if(open)
		{
			testMauriceTom();
		}
	}
	
	static boolean openFishing() {
		System.out.print("Open fishing...");
		String t0 = "consult('fishing.pl')";

		if (!Query.hasSolution(t0)) {
			System.out.println(t0 + " failed");
			return false;
		}
		
		System.out.println("passed");	
		return true;
	}
	
	static void testMauriceTom(){
		String t1 = "fish(maurice)";
		System.out.print("maurice is...");
		if(!Query.hasSolution(t1)){
			System.out.println("not a fish !");
		}
		else {
			System.out.println("a fish !");
		}
		String t2 = "fish(tom)";
		System.out.print("tom is...");
		if(!Query.hasSolution(t2)){
			System.out.println("not a fish !");
		}
		else {
			System.out.println("a fish !");
		}
	}
	
	static void fish() {
		
	}
}

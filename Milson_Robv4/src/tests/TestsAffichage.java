package tests;

import affichage.InterfacePrincipale;
import carte.Carte;
import carte.CarteImagee;
import carte.Elements;
import carte.TypeElement;
import comm.Commande;
import interfaceCarte.InterfaceAffichage;
import lejos.nxt.addon.NXTLineLeader.Command;

public class TestsAffichage {

	public static void main(String[] args) {
		TestsAffichage ta = new TestsAffichage();
		ta.test2();
		
	}
	
	public void test1() {
		// TODO Auto-generated method stub
		int absc =  2 ;
		int ord = 2 ;
		Carte myMap = new Carte(absc ,ord) ;
		Elements virageAGauche = new Elements(TypeElement.VirageBG) ;
		Elements virageADroite = new Elements(TypeElement.VirageBD) ; 
		Elements virageBasADroite = new Elements(TypeElement.VirageHD) ;
		Elements virageBasAGauche = new Elements(TypeElement.VirageHG) ;
		myMap.SetAt(0, 0, virageADroite);
		myMap.SetAt(0, 1, virageAGauche);
		myMap.SetAt(1, 0, virageBasADroite);
		myMap.SetAt(1, 1, virageBasAGauche);
		//CarteImagee CarteIm = new CarteImagee(myMap);
		
		Carte myMap2 = new Carte (7,7) ;
		for(int i=0 ; i<7 ; ++i) {
			for(int j=0 ; j<7 ; ++j) {
				myMap2.SetAt(i, j, virageBasAGauche);
			}
		}
		CarteImagee CarteIm2 = new CarteImagee(myMap2) ;
		InterfaceAffichage intA = new InterfaceAffichage();
		intA.affichage(CarteIm2);
		
		int i = 0 ;
		do {
		try
		
		{
		    Thread.sleep(1000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		
		i+=1 ;
		
		if (i % 2 == 0) {
			myMap2.SetAt(2, 2, virageBasADroite);
			CarteIm2 = new CarteImagee(myMap2) ;
		}
		else {
			myMap2.SetAt(2, 2, virageBasAGauche);
			CarteIm2 = new CarteImagee(myMap2) ;
		}
		
		
		intA.refresh(CarteIm2);
		} while(true);
	}
	
	public void test2() {
		// TODO Auto-generated method stub
		int absc =  2 ;
		int ord = 2 ;
		Carte myMap = new Carte(absc ,ord) ;
		Elements virageAGauche = new Elements(TypeElement.VirageBG) ;
		Elements virageADroite = new Elements(TypeElement.VirageBD) ; 
		Elements virageBasADroite = new Elements(TypeElement.VirageHD) ;
		Elements virageBasAGauche = new Elements(TypeElement.VirageHG) ;
		myMap.SetAt(0, 0, virageADroite);
		myMap.SetAt(0, 1, virageAGauche);
		myMap.SetAt(1, 0, virageBasADroite);
		myMap.SetAt(1, 1, virageBasAGauche);
		//CarteImagee CarteIm = new CarteImagee(myMap);
		
		Carte myMap2 = new Carte (7,7) ;
		for(int i=0 ; i<7 ; ++i) {
			for(int j=0 ; j<7 ; ++j) {
				myMap2.SetAt(i, j, virageBasAGauche);
			}
		}
		CarteImagee CarteIm2 = new CarteImagee(myMap2) ;
		Commande com = new Commande();
		
		InterfacePrincipale intA = new InterfacePrincipale();
		intA.affichage(myMap2,com);
		
		int i = 0 ;
		do {
		try
		
		{
		    Thread.sleep(1000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		
		i+=1 ;
		
		if (i % 2 == 0) {
			myMap2.SetAt(2, 2, virageBasADroite);
			CarteIm2 = new CarteImagee(myMap2) ;
		}
		else {
			myMap2.SetAt(2, 2, virageBasAGauche);
			CarteIm2 = new CarteImagee(myMap2) ;
		}
		
		
		intA.refresh();
		} while(true);
	}
	

}

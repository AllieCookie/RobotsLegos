package tests;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import affichage.InterfacePrincipale;
import carte.*;
import interfaceCarte.InterfaceAffichage;

public class TestsCarte {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int absc =  2 ;
		int ord = 2 ;
		Carte myMap = new Carte(absc ,ord) ;
		Elements virageAGauche = new Elements(TypeElement.VirageBG) ;
		Elements virageADroite = new Elements(TypeElement.VirageBD) ; 
		Elements virageBasADroite = new Elements(TypeElement.VirageHD) ;
		Elements vide = new Elements(TypeElement.Vide) ;
		Elements test = null;
		myMap.SetAt(0, 0, virageADroite);
		myMap.SetAt(0, 1, virageAGauche);
		myMap.SetAt(1, 0, virageBasADroite);
		//myMap.SetAt(1, 1, test);

		myMap.SetAt(1, 1, vide);
		
		//InterfaceAffichage monInterface = new InterfaceAffichage(); 
		CarteImagee CarteIm = new CarteImagee(myMap);
		CarteIm.getAbscisses() ;
		
		System.out.println("Ta race");
		
		//InterfaceAffichage ia = new InterfaceAffichage();
		//ia.affichage(CarteIm);
		
		Elements e = myMap.GetAt(1, 1);
		System.out.println("t"+e+myMap.GetAt(0, 0)+myMap.GetAt(0, 1)+myMap.GetAt(1, 0));
		
		GrapheMap gm =  new GrapheMap(myMap);
		
		TestsCarte ts = new TestsCarte();
		
		//ts.test3x3();
		//ts.testBoucleWithPred();
		//ts.testBoucleWithOrient();
		//ts.testIA();
		
		//ts.testBoucleWithPos();
		ts.testGetLigCol();
		
		
	}

	public void test3x3() {
		Carte myMap = carteType1();

		
		//InterfaceAffichage monInterface = new InterfaceAffichage(); 
		CarteImagee CarteIm = new CarteImagee(myMap);
		CarteIm.getAbscisses() ;
		
		
		InterfaceAffichage ia = new InterfaceAffichage();
	
		ia.affichage(CarteIm);
	
		
		Elements e = myMap.GetAt(1, 1);
		Elements j;
		System.out.println(e);
		
		GrapheMap gm =  new GrapheMap(myMap);
		

		
		
	}
	
	public void testBoucleWithPred(){
		
		System.out.println("\n\n-- DEBUT TEST BOUCLE PRED --\n");

		Carte map = carteType1();
		GrapheMap gm = new GrapheMap(map);
		CarteImagee CarteIm = new CarteImagee(map);		
		InterfaceAffichage ia = new InterfaceAffichage();
		ia.affichage(CarteIm);
		
		Elements pred = null;
		Elements e = map.GetAt(0, 0);
		Elements olde;
		System.out.println("a"+gm.getVoisins(e));
		System.out.println("b"+gm.getVoisinsArray(e));
		for(int i=0;i<10;i++) {
			System.out.println("\nETAPE "+i+": Courant: "+e+" Pred:"+pred);
			
			if(gm.deplacementPossibleWithPred(e, "r", pred)) {
				olde = e;
				e = gm.deplacementPred(e, "r", pred);
				pred = olde;

			}
			else {
				System.out.println("ERREUR");
			}

		}
		e = map.GetAt(0, 0);

		System.out.println("-- FIN TEST BOUCLE PRED--\n");
		System.out.println("a"+gm.getVoisins(e));
		System.out.println("b"+gm.getVoisinsArray(e));
		
		
	}
	
	public void testBoucleWithOrient(){
		
		System.out.println("\n\n-- DEBUT TEST BOUCLE ORIENT --");

		Carte map = carteType1();
		GrapheMap gm = new GrapheMap(map);
		CarteImagee CarteIm = new CarteImagee(map);		
		InterfaceAffichage ia = new InterfaceAffichage();
		ia.affichage(CarteIm);
		
		Elements pred = null;
		Elements e = map.GetAt(0, 0);
		Elements olde;
		String s = "s";
		System.out.println("a"+gm.getVoisins(e));
		System.out.println("b"+gm.getVoisinsArray(e));
		
		for(int i=0;i<10;i++) {
			System.out.println("\nETAPE "+i+": Courant: "+e+" Orient:"+s+" Pred:"+pred);
			if(gm.deplacementPossibleWithOrient(e, "r", s)) {
				olde = e;
				e = gm.deplacementOrient(e, "r", s);
				pred = olde;
				s = gm.getOrientFromPred(e, pred);
			}

			else {
				System.out.println("ERREUR");
			}

		}
		
		System.out.println("-- FIN TEST BOUCLE ORIENT --\n");
		System.out.println(gm.getAllElemments());
		e = map.GetAt(0, 0);
		System.out.println("a"+gm.getVoisins(e));
		System.out.println("b"+gm.getVoisinsArray(e));


		
	}
	
	public void testBoucleWithPos(){
	
		System.out.println("\n\n-- DEBUT TEST BOUCLE POS --");

		Carte map = carteType1();
		GrapheMap gm = new GrapheMap(map);
		CarteImagee CarteIm = new CarteImagee(map);		
		InterfaceAffichage ia = new InterfaceAffichage();
		ia.affichage(CarteIm);
		
		Elements pred = null;
		Elements e = map.GetAt(0, 0);
		Elements olde;
		String s = "s";
		System.out.println("a"+gm.getVoisins(e));
		System.out.println("b"+gm.getVoisinsArray(e));
		
	
		olde = e;
		e = gm.deplacementPos(e, "s", s);
		pred = olde;
		System.out.println(e);
		e = gm.deplacementPos(e, "r", "l");
		System.out.println(e);
		e = gm.deplacementPos(e, "s", "s");
		System.out.println(e);
		
		System.out.println("-- FIN TEST BOUCLE Pos --\n");

		
	}
	
	public void testDep() {
		System.out.println("\n\n-- DEBUT TEST DEP --");

		Carte map = carteType3();
		GrapheMap gm = new GrapheMap(map);
		CarteImagee CarteIm = new CarteImagee(map);		
		InterfaceAffichage ia = new InterfaceAffichage();
		InterfacePrincipale ip = new InterfacePrincipale();
		ia.affichage(CarteIm);
		ip.affichage(map, null);
		
		Elements pred = null;
		Elements e1 = map.GetAt(2, 1);
		Elements e2 = map.GetAt(2, 2);
		Elements olde;
		
		String s = gm.deplacement(e1, e2, "u");
		String u = gm.newPosAfterDep(e1, e2);
		System.out.println(s+":"+u);
		
		
	}
	
	public void testGetLigCol() {
		System.out.println("\n\n-- DEBUT TEST GetLigCol--");

		Carte map = carteType4();
		GrapheMap gm = new GrapheMap(map);
		CarteImagee CarteIm = new CarteImagee(map);		
		InterfaceAffichage ia = new InterfaceAffichage();
		ia.affichage(CarteIm);
		
		Elements e1 = map.GetAt(4, 2);
		System.out.println(e1);
		System.out.println(map.findLigneElem(e1)+":"+map.findColElem(e1));
	}
	//Cate 3x3
	public Carte carteType1() {
		int absc =  3;
		int ord = 3;
		Carte myMap = new Carte(absc ,ord) ;
		Elements vide1 = new Elements(TypeElement.Vide);
		Elements vide2 = new Elements(TypeElement.Vide);
		Elements vide3 = new Elements(TypeElement.Vide);		
		Elements zerozero = new Elements(TypeElement.VirageBD);

		myMap.SetAt(0, 0, zerozero);
		myMap.SetAt(0, 1, new Elements(TypeElement.CroisementGDB));
		myMap.SetAt(1, 0, new Elements(TypeElement.CroisementDHB));
		myMap.SetAt(2, 0, new Elements(TypeElement.RouteHB));
		myMap.SetAt(1, 1, new Elements(TypeElement.VirageHG));
		myMap.SetAt(0, 2, new Elements(TypeElement.RouteHB));
		myMap.SetAt(2, 1, vide1);
		myMap.SetAt(2, 2, vide2);
		myMap.SetAt(1, 2, vide3);
		
		return myMap;
		
	}
	
	//Carte 4x4
	public Carte carteType2() {
		int absc =  4;
		int ord = 4;
		int taille = absc * ord;
		
		Carte myMap = new Carte(absc ,ord) ;
		Elements[] al = new Elements[taille];
		
		//Init
		for(int i=0;i<taille;i++) {
			al[i] = new Elements(TypeElement.Vide);
		}
		
		//Modification
		al[0].setType(TypeElement.VirageBD);
		al[1].setType(TypeElement.CroisementGDB);
		al[2].setType(TypeElement.VirageBG);
		al[3].setType(TypeElement.RouteHB);

		
		al[4].setType(TypeElement.VirageHD);
		al[5].setType(TypeElement.CroisementGHB);
		al[6].setType(TypeElement.CroisementDHB);
		al[7].setType(TypeElement.VirageHG);
		
		al[9].setType(TypeElement.CroisementGHB);
		al[10].setType(TypeElement.RouteHB);
		
		al[13].setType(TypeElement.VirageHD);
		al[14].setType(TypeElement.VirageHG);


		//Integration dans carte
		for(int i=0;i<absc;i++) {
			for(int j=0;j<ord;j++) {
				myMap.SetAt(i, j, al[j+i*absc]);
			}
			
		}
		/*
		System.out.println("\n\n--Test Fiabilite");
        int finalabs = 0;
        int finalord = 0;
		for(Elements ele : al) {
			
			for(int i=0;i<4;i++) {
				for(int j=0;j<4;j++) {
					
					if(myMap.GetAt(i, j) == ele) {
						System.out.println("\t"+ele+": egalite trouver "+i+":"+j);
						finalabs = i;
						finalord = j;
					}						
				}
			}
			System.out.println(finalabs+":"+finalord+" | "+ele);

			
		}
		System.out.println("--Fin test--\n\n");*/
		
		return myMap;
		
	}
	
	//3x4
	public Carte carteType3() {
		int absc = 3;
		int ord = 4;
		int taille = absc * ord;
		
		Carte myMap = new Carte(absc ,ord) ;
		Elements[][] al = new Elements[absc][ord];
		
		//Init
		for(int i=0;i<absc;i++) {
			for(int j=0;j<ord;j++) {
				al[i][j] = new Elements(TypeElement.Vide);
			}
		}
		
		//Modification
		al[0][0].setType(TypeElement.CroisementGDB);
		al[0][1].setType(TypeElement.CroisementGDB);
		al[0][2].setType(TypeElement.RouteDG);
		al[0][3].setType(TypeElement.CroisementGHB);

		
		al[1][0].setType(TypeElement.CroisementDHB);
		al[1][1].setType(TypeElement.CroisementGHB);
		al[1][2].setType(TypeElement.Vide);
		al[1][3].setType(TypeElement.RouteHB);
		
		al[2][0].setType(TypeElement.VirageHD);
		al[2][1].setType(TypeElement.CroisementGDH);
		al[2][2].setType(TypeElement.RouteDG);
		al[2][3].setType(TypeElement.VirageHG);
		

		//Integration dans carte
		for(int i=0;i<absc;i++) {
			for(int j=0;j<ord;j++) {
				myMap.SetAt(i, j, al[i][j]);
			}
			
		}

		
		return myMap;
		
	}
	
	//5x5
	public Carte carteType4() {
		int absc = 5;
		int ord = 5;
		
		Carte myMap = new Carte(absc ,ord) ;
		Elements[][] al = new Elements[absc][ord];
		
		//Init
		for(int i=0;i<absc;i++) {
			for(int j=0;j<ord;j++) {
				al[i][j] = new Elements(TypeElement.Vide);
			}
		}
		
		//Modification
		
		al[0][1].setType(TypeElement.VirageBD);
		al[0][2].setType(TypeElement.CroisementGDB);
		al[0][3].setType(TypeElement.RouteDG);
		al[0][4].setType(TypeElement.VirageBG);
		
		al[1][0].setType(TypeElement.VirageBD);
		al[1][1].setType(TypeElement.CroisementGDH);
		al[1][2].setType(TypeElement.VirageHG);
		
		al[1][4].setType(TypeElement.RouteHB);
		
		al[2][0].setType(TypeElement.VirageHD);
		al[2][1].setType(TypeElement.CroisementGDB);
		al[2][2].setType(TypeElement.CroisementGDB);
		al[2][3].setType(TypeElement.RouteDG);
		al[2][4].setType(TypeElement.CroisementGHB);

		
		al[3][1].setType(TypeElement.CroisementDHB);
		al[3][2].setType(TypeElement.CroisementGHB);
		al[3][3].setType(TypeElement.Vide);
		al[3][4].setType(TypeElement.RouteHB);
		
		al[4][1].setType(TypeElement.VirageHD);
		al[4][2].setType(TypeElement.CroisementGDH);
		al[4][3].setType(TypeElement.RouteDG);
		al[4][4].setType(TypeElement.VirageHG);
		
		//al[4][4].setVictimInThere(true);
	

		//Integration dans carte
		for(int i=0;i<absc;i++) {
			for(int j=0;j<ord;j++) {
				myMap.SetAt(i, j, al[i][j]);
			}
			
		}

		
		return myMap;
		
	}

}

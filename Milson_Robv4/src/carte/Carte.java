package carte;
import java.util.ArrayList;
import java.util.List;

import protagonistes.*;

public class Carte {
	
	private int abscisses ;
	private int ordonnees ;
	private Elements coordonees[][] ;
	private List<Robot> listeRobots  ;
	private List<Victime> listeVictimes ;
	private List<Hopital> listeHopitaux ;
	
	public Carte(int absc , int ord ) {
		abscisses = absc ;
		ordonnees = ord ;
		coordonees =  new Elements[abscisses][ordonnees] ;
		listeRobots = new ArrayList<Robot>();
		listeVictimes = new ArrayList<Victime>() ;
		listeHopitaux = new ArrayList<Hopital>() ;
		
	}
	
	
	public int getAbscisses() {
		return abscisses;
	}

	public int getOrdonnees() {
		return ordonnees;
	}
	
	public void SetAt (int absc , int ord , Elements e) {
		coordonees[absc][ord] = e ;
	}
	
	public Elements GetAt (int absc , int ord ) {
		return coordonees[absc][ord] ;
	}


	public List<Robot> getListeRobots() {
		return listeRobots;
	}


	//Modification du 01/05 Alex
	public void setListeRobots(List<Robot> listeRobots) {
		//System.out.println("nouveau set robots");
		Elements e;
		int x,y;

		//On supprime les anciens robots et on mets les nouveaux
		for( x=0; x < abscisses; x++) {
			for( y=0; y < ordonnees; y++) {		
				e = coordonees[x][y];
				e.setRobotInThere(false);
				coordonees[x][y] = e;
			}
		}
		
		 
		this.listeRobots = listeRobots;

		
		//On ajoute les nouvelles victimes dans les cases de la carte
		for(Robot r : listeRobots) {
			x = r.getPosAbscisses();
			y = r.getPosOrdonnes();
			
			e = coordonees[x][y];
			e.setRobotInThere(true);
			coordonees[x][y] = e;
		}
	}


	public List<Victime> getListeVictimes() {
		return listeVictimes;
	}


	//Modification 30/04 Alex
	public void setListeVictimes(List<Victime> listeVictimes) {
		//System.out.println("nouveau set victime");
		int x,y;
		Elements e;
		
		//On supprime les anciennes victimes des cases de la carte
		for( x=0; x < abscisses; x++) {
			for( y=0; y < ordonnees; y++) {		
				e = coordonees[x][y];
				e.setVictimInThere(false);
				coordonees[x][y] = e;
			}
		}
		
		 
		this.listeVictimes = listeVictimes;

		
		//On ajoute les nouvelles victimes dans les cases de la carte
		for(Victime v : listeVictimes) {
			x = v.getPosAbscisses();
			y = v.getPosOrdonnes();
			
			e = coordonees[x][y];
			e.setVictimInThere(true);
			coordonees[x][y] = e;
			
			//System.out.println(v);
		}
	}


	public List<Hopital> getListeHopitaux() {
		return listeHopitaux;
	}

	//Modification 01/05 Alex
	public void setListeHopitaux(List<Hopital> listeHopitaux) {
		//System.out.println("nouveau set hopitaux");
		int x,y;
		Elements e;
		
		//On supprime les anciens hopitaux des cases de la carte
		for( x=0; x < abscisses; x++) {
			for( y=0; y < ordonnees; y++) {		
				e = coordonees[x][y];
				e.setHospitalInThere(false);
				coordonees[x][y] = e;
			}
		}
		
		 
		this.listeHopitaux = listeHopitaux;

		
		//On ajoute les nouveaux hopitaux dans les cases de la carte
		for(Hopital h : listeHopitaux) {
			x = h.getPosAbscisses();
			y = h.getPosOrdonnes();
			
			e = coordonees[x][y];
			e.setHospitalInThere(true);
			coordonees[x][y] = e;
			
			//System.out.println(v);
		}
		
		
	}
	//Rajout du 1/05
	public boolean estVide(int x,int y) {
		return coordonees[x][y].getType() == TypeElement.Vide;
	}
	
	// Rajout du 26 avril
	public int findLigneElem(Elements e) {
		int finalI = -1;
		int i=-1,j=-1;
		while(i<abscisses-1 && finalI == -1) {
			i++;
			j = -1;
			while(j<ordonnees-1 && finalI == -1) {
				j++;
				if(coordonees[i][j] == e) {
					finalI = i;
				}
				
			}
			
		}
		if(finalI == -1) {
			System.err.println("Carte/getLigneele: Impossible de trouver l'elements!");
		}
		return finalI;
	}
	
	public int findColElem(Elements e) {
		int finalJ = -1;
		int i=-1,j=-1;
		while(i<abscisses-1 && finalJ == -1) {
			i++;
			j = -1;
			while(j<ordonnees-1 && finalJ == -1) {
				j++;
				if(coordonees[i][j] == e) {
					finalJ = j;
				}
				
			}
			
		}
		if(finalJ == -1) {
			System.err.println("Carte/getLigneele: Impossible de trouver l'elements!");
		}
		return finalJ;
	}
}

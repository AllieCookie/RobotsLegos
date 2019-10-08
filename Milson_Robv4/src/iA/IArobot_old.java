package iA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* derniere version en cours*/

import carte.Carte;
import carte.Elements;
import carte.GrapheMap;
import comm.BTcommConnectException;
import comm.Commande;
import log.Log;
import protagonistes.Robot;
import protagonistes.TypeProtagoniste;

public class IArobot_old {
	Carte c;	//carte
	GrapheMap gm;	//graphe par rapport à la carte
	Commande robot=new Commande();
	ArrayList<String> solution = new ArrayList<>(); //solution à envoyer au robot
	String pos=new String(); //position par rapport à la case
	Elements position; 		//la case où est positionné le robot
	Log log;
	Robot robotIa;

	static final int infini=Integer.MAX_VALUE;


	//Constructeur
	public IArobot_old(Carte c,String nom,String pos) throws BTcommConnectException {
		this.pos=pos;
		this.c=c;
		gm=new GrapheMap(c);
		//robot.on(nom);
	}

	public Elements trouveMin(List<Elements> Q,HashMap<Elements,Integer> d) {
		int mini=infini;
		Elements sommet=null;
		for ( Elements s : Q ) {
			if ( d.get(s) < mini ) {
				mini=d.get(s);
				sommet = s;
			}
		}
		return sommet;
	}

	public void majDistance(Elements s1,Elements s2,HashMap<Elements,Integer> d,HashMap<Elements,Elements> predecesseur) {
		if (d.get(s2) > d.get(s1) + 1 ) {
			d.put(s2,d.get(s1)+1);
			predecesseur.put(s2,s1);
		}
	}


	private HashMap<Elements,Elements> Dijkstra() {
		HashMap<Elements,Integer> d= new HashMap<>();
		Elements sdebut = position;

		//initialisation
		for ( Elements s : gm.getAllElemments() ) {
			d.put(s,infini);
		}
		d.put(sdebut,0);

		List<Elements> Q=gm.getAllElemments();
		Elements s1;
		HashMap<Elements,Elements> predecesseur=new HashMap<>();
		while ( !Q.isEmpty() ) {
			s1=trouveMin(Q,d);
			Q.remove(s1);
			for ( Elements s2 : gm.getVoisinsArray(s1) ) {
				majDistance(s1,s2,d,predecesseur);
			}
		}

		return predecesseur;
	}

	//recupere le chemin a prendre pour aller jusqu'à l'élement B
	private ArrayList<Elements> chemin(HashMap<Elements,Elements> tabPred,Elements B){
		ArrayList<Elements> resultat=new ArrayList<>();
		Elements s = B;

		while ( s != position ) {
			resultat.add(0,s);
			s=tabPred.get(s);
		}

		return resultat;
	}

	//deplacement du robot jusqu'à la case suivante
	private void deplacement(Elements suivant) throws BTcommConnectException {
		
		robotIa.setPosAbscisses(c.findLigneElem(position));
		robotIa.setPosOrdonnes(c.findColElem(position));
		log.ecrireDeplacement(robotIa, gm.deplacement(position, suivant, pos));
		
		solution.add(gm.deplacement(position, suivant, pos));
		pos=gm.newPosAfterDep(position,suivant);
		position=suivant;
		

	}

	//deplacement par défaut sur la case
	private void deplacementCase(Elements s) throws BTcommConnectException {
		robotIa.setPosAbscisses(c.findLigneElem(position));
		robotIa.setPosOrdonnes(c.findColElem(position));
		log.ecrireDeplacement(robotIa, gm.deplacementDefault(s));
		
		//ajout du déplacement de la case à la solution
		solution.add(gm.deplacementDefault(s));

		//mies à jour position et pos
		position=gm.deplacementPos(s, gm.deplacementDefault(s), pos);
		pos=gm.newPosAfterDep(s, position); 


	}


	//recuperation du chemin pour aller de A à B à partir de Dijkstra
	private void pointAaB(Elements B) throws BTcommConnectException {
		HashMap<Elements,Elements> tabPred = Dijkstra(); //tableau des predecesseur
		ArrayList<Elements> resultat=chemin(tabPred,B);	//liste d'élements dans l'ordre croissant jusqu'au résultat
		Elements suivant;
		
		//initialisation
		suivant=resultat.get(0);
		resultat.remove(0);

		deplacement(suivant);

		//parcourir tout le tableau des résultats
		while ( suivant != B ) {
			suivant=resultat.get(0);
			resultat.remove(0);
			deplacement(suivant);

		}

		//parcourir la case
		deplacementCase(suivant);

	}


	// chercher victime
	public void chercherVictime(Elements victime) throws BTcommConnectException {
		System.out.println("\tDirection victime !");
		pointAaB(victime);
		solution.add("t");
		
		log.prendreVictime(robotIa);
		
		System.out.println("\tVictime recupérée !");
	}


	// deposer a l hopital
	public void deposerHopital(Elements hopital) throws BTcommConnectException {
		System.out.println("\tDirection l'hopital ! ");
		pointAaB(hopital);
		solution.add("d");
		
		log.deposerVictime(robotIa);

		System.out.println("\tVictime sauvée !");
	}


	public void envoie() throws BTcommConnectException {
		for ( String commande : solution) {
			switch(commande) {
			case "r":
				robot.droite();
				break;
			case "l":	
				robot.gauche(); 
				break;				
			case "s":	
				robot.avancer();
				break;				
			case "u":
				robot.demitour(); 
				break;	
			case "d":
				robot.deposer();
				break;
			case "t":
				robot.ramasser();
				break;
			default :
				break;
			}


			try{
				Thread.sleep(3500);
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}

		}
		solution.clear();

	}


	public Elements plusProche(ArrayList<Elements> victimes) {
		int pluscourt=infini;
		Elements res=null;

		for (Elements e : victimes) {
			HashMap<Elements,Elements> tabPred = Dijkstra(); //tableau des predecesseur
			ArrayList<Elements> resultat=chemin(tabPred,e);	//liste d'élements dans l'ordre croissant jusqu'au résultat
			if (resultat.size() < pluscourt) {
				pluscourt=resultat.size();
				res=e;
			}
		}
		return res;
	}



	// applique l'algorithme de recuperation des victime
	public void run(Elements debut,ArrayList<Elements> victimes,Elements hopital) throws BTcommConnectException {
		position=debut;
		Elements v;
		log = new Log("Ia_rapport");
		System.out.println("\tDébut de la mission.");
		robotIa = new Robot(c.findLigneElem(debut), c.findColElem(debut),TypeProtagoniste.robotIA , "Test"); 


		while ( !victimes.isEmpty() ) { //tant qu'il y a des victimes à sauver
			//victime la plus proche
			v=plusProche(victimes);
			chercherVictime(v);

			System.out.println("solution jusqu'à la victime :" + solution );
			solution.clear();
			//envoie();

			deposerHopital(hopital);

			System.out.println("solution jusqu'à l'hopital :" + solution );
			solution.clear(); //TODO a supp
			//envoie();

			victimes.remove(v);//enlever victime de la liste des victime
		}

		try{
			Thread.sleep(5000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}

		//robot.off();
		log.closeWriter();
		System.out.println("\tFin de la mission.");
	}

}

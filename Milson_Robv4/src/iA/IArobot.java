package iA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import carte.*;
import comm.*;
import lejos.pc.comm.NXTCommException;
import log.Log;
import protagonistes.*;

public class IArobot extends Thread{
	/* ----- Attributs ------ */
	private Carte c;	//carte
	private GrapheMap gm;	//graphe par rapport à la carte
	private Commande comRobot=new Commande();
	private String pos=new String(); //position par rapport à la case
	private Elements position; 		//la case où est positionné le robot
	private Log log;
	private ArrayList<Elements> resultat=new ArrayList<>();
	private AssistantCalcul ac;


	/* ------ Constructeur ------ */
	public IArobot(Carte c, Elements depart, String pos,String nomRobot,String adr,String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		setParametreBase(c,depart,pos);
		comRobot.on(nomRobot,adr,nomC);
		setRobIA();
	}

	public IArobot(Carte c, Elements depart, String pos, BTcommunication bt) {
		setParametreBase(c,depart,pos);
		comRobot.on(bt);
		setRobIA();
	}

	public IArobot(Carte c, String pos,String nomRobot,String adr,String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		this.c=c;
		gm=new GrapheMap(c);
		this.pos=pos;
		ac = new AssistantCalcul(c);
		comRobot.on(nomRobot,adr,nomC);
	}

	public IArobot(Carte c, Elements depart, String pos,String nomRobot,String adr,String nomC,Robot robot) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		setParametreBase(c,depart,pos);
		comRobot.on(nomRobot,adr,nomC);
		setRobIA(robot);
	}

	public IArobot(Carte c, Elements depart, String pos, BTcommunication bt,Robot robot) {
		setParametreBase(c,depart,pos);
		comRobot.on(bt);
		setRobIA(robot);
	}

	/* ----- Setter privee utile pour l initialisation du constructeur ------ */
	private void setParametreBase(Carte c, Elements depart, String pos) {
		this.c=c;
		gm=new GrapheMap(c);
		this.position=depart;
		this.pos=pos;
		ac = new AssistantCalcul(c);
	}

	private void setRobIA() {
		comRobot.setRobot(new Robot(c.findLigneElem(position), c.findColElem(position),TypeProtagoniste.robotIA , "IARobot"));
		comRobot.setCarte(c);
		comRobot.setPositionCarte(position);
		comRobot.setPos(pos);
	}

	private void setRobIA(Robot robot) {
		comRobot.setRobot(robot);
		comRobot.setCarte(c);
		comRobot.setPositionCarte(position);
		comRobot.setPos(pos);
	}

	public void setDebut(Elements debut) {
		this.position=debut;
		setRobIA();
	}

	public void setRobotIA(Robot robot) {
		System.out.println("New rob");
		comRobot.setRobot(robot);
	}
	public void setRobotIA(Robot robot,Elements newPosition,String newPos ) {
		System.out.println("New rob");
		comRobot.setRobot(robot);
		position = newPosition;
		pos = newPos;
		comRobot.setCarte(c);
		comRobot.setPositionCarte(position);
		comRobot.setPos(pos);
	}

	/* ------ Methodes ------ */
	//ArrayList d'hopital a ArrayList pour Elements
	public ArrayList<Elements> hopitalToElem(ArrayList<Hopital> hopitaux){
		ArrayList<Elements> resultat=new ArrayList<>();
		for (Hopital hop : hopitaux) {
			resultat.add(c.GetAt(hop.getPosAbscisses(),hop.getPosOrdonnes()));
		}

		return resultat;
	}

	//ArrayList de victimes a ArrayList pour Elements
	public ArrayList<Elements> victimeToElem(ArrayList<Victime> victimes){
		ArrayList<Elements> resultat=new ArrayList<>();
		for (Victime vic : victimes) {
			resultat.add(c.GetAt(vic.getPosAbscisses(),vic.getPosOrdonnes()));
		}

		return resultat;
	}

	private void attendre() {
		try{
			Thread.sleep(3500);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}


	//deplacement du robot
	private void deplacementRobot(String solution) throws BTcommConnectException {
		switch(solution) {
		case "r":
			comRobot.droite();
			System.out.println(comRobot.getRobot().getIdentifier()+": droite");
			attendre();
			attendre();
			break;
		case "l":	
			comRobot.gauche(); 
			System.out.println(comRobot.getRobot().getIdentifier()+": gauche");
			attendre();
			attendre();
			break;				
		case "s":	
			comRobot.avancer();
			System.out.println(comRobot.getRobot().getIdentifier()+": avancer");
			attendre();
			break;				
		case "u":
			comRobot.demitour(); 
			System.out.println(comRobot.getRobot().getIdentifier()+": demi tour");
			attendre();
			break;
		default :
			break;
		}
	}



	//deplacement du robot jusqu'à la case suivante
	private void deplacement(Elements suivant) throws BTcommConnectException {
		String solution = gm.deplacement(position, suivant, pos);
		//deplacement par le robot
		deplacementRobot(solution);

		// mises a jour des positions
		position=suivant;
		pos=comRobot.getPosRobot();

		// ecriture sur le log
		log.ecrireDeplacement(comRobot.getRobot(), solution);
	}


	//deplacement par défaut sur la case
	private void deplacementCase(Elements s) throws BTcommConnectException {
		String solution = gm.deplacementDefault(s);

		// deplacement par le robot
		deplacementRobot(solution);

		// mises a jour des positions 
		position=comRobot.getPosElemRobot();
		pos=comRobot.getPosRobot();

		// ecriture sur le log
		log.ecrireDeplacement(comRobot.getRobot(), solution);
	}

	// recuperation du chemin pour aller de A à B à partir de Dijkstra
	private void pointAaH(Elements B) throws BTcommConnectException {
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


	// depot des victimes a l hopital
	public void deposerHopital(Elements hopital) throws BTcommConnectException {
		System.out.println(comRobot.getRobot().getIdentifier()+": Direction l'hopital ! ");
		// aller a l hopital
		pointAaH(hopital);

		// depot des victimes
		comRobot.deposer();
		System.out.println(comRobot.getRobot().getIdentifier()+": deposer");
		attendre();

		// ecriture sur le log
		log.deposerVictime(comRobot.getRobot());

		System.out.println(comRobot.getRobot().getIdentifier()+": Victime sauvée !");
	}


	//vérifie si la victime est toujours sur la carte
	public boolean victimeSurCarte(Victime vic) {
		for ( Victime v : c.getListeVictimes()) {
			if (v==vic) {
				return true;
			}
		}
		return false;
	}

	// recuperation du chemin pour aller de A à B à partir de Dijkstra
	private void pointAaV(Elements B,Victime v) throws BTcommConnectException {
		Elements suivant;

		boolean continuer=true;
		//initialisation
		suivant=resultat.get(0);
		resultat.remove(0);
		deplacement(suivant);
		//tant qu’on est pas arrivée à la victime ou continue {
		//vérification victime toujours là

		while ( ( suivant != B ) && continuer) {
			if ( victimeSurCarte(v) ) {
				suivant=resultat.get(0);
				resultat.remove(0);
				deplacement(suivant);
			}else{
				continuer=false;
				System.out.println(comRobot.getRobot().getIdentifier()+": La victime n'est plus sur la case :c");
			}
		}

		//il y a toujours la victime sur la case ?
		if ( victimeSurCarte(v) && continuer ) {
			//parcourir la case
			deplacementCase(suivant);
			//on est arrive a la victime
			if ( victimeSurCarte(v) && continuer ) {
				//Récupérer victime
				comRobot.ramasser(v);
				c.setListeVictimes((ArrayList<Victime>) comRobot.getCarte().getListeVictimes());
				System.out.println(comRobot.getRobot().getIdentifier()+": prendre");
				attendre();
				System.out.println(comRobot.getRobot().getIdentifier()+": Victime recupérée !");
				// ecriture sur le log
				log.prendreVictime(comRobot.getRobot());
			}else {
				System.out.println(comRobot.getRobot().getIdentifier()+": La victime n'est plus sur la case :c");
			}
		}else {
			System.out.println(comRobot.getRobot().getIdentifier()+": La victime n'est plus sur la case :c");
		}

	}

	// chercher victime
	public void chercherVictime(Elements victime, Victime v) throws BTcommConnectException {
		System.out.println(comRobot.getRobot().getIdentifier()+": Direction victime ! " + victime);
		// aller chercher la victime
		pointAaV(victime,v);
	}

	//obtention d'un HashMap qui relient une victime à sa case
	public HashMap<Elements,Victime> HMvictime(ArrayList<Victime> victimes){
		HashMap<Elements,Victime> victimesCase=new HashMap<>();
		for (Victime vic : victimes) {
			victimesCase.put(c.GetAt(vic.getPosAbscisses(),vic.getPosOrdonnes()),vic);
		}
		return victimesCase;
	}


	/* ----- Programme principal ------ */
	public void run(ArrayList<Victime> victimes,ArrayList<Hopital> hopitaux) throws BTcommConnectException {
		c.setListeVictimes(victimes);
		c.setListeHopitaux(hopitaux);
		go();

	}

	public void run(Elements debut,ArrayList<Victime> victimes,ArrayList<Hopital> hopitaux) throws BTcommConnectException {
		setDebut(debut);
		c.setListeVictimes(victimes);
		c.setListeHopitaux(hopitaux);
		go();
	}

	public void run() {
		try {
			go();
		} catch (BTcommConnectException e) {
			e.printStackTrace();
		}
	}


	private void go() throws BTcommConnectException {	
		ArrayList<Victime> victimes = (ArrayList<Victime>)c.getListeVictimes();
		ArrayList<Hopital> hopitaux = (ArrayList<Hopital>)c.getListeHopitaux();
		// ouverture du log
		log = new Log("Ia_rapport_" + comRobot.getRobot().getIdentifier());

		// mise a jour des victimes de la carte
		comRobot.setCarte(c);

		// tableau d'elements d'hopitaux
		ArrayList<Elements> hopital = hopitalToElem(hopitaux);
		// element le plus proche
		Elements p;
		// victimes de la carte
		victimes=(ArrayList<Victime>) comRobot.getCarte().getListeVictimes();
		ArrayList<Elements> victime = victimeToElem(victimes);
		HashMap<Elements,Victime> victimesCase=HMvictime(victimes);

		System.out.println("\t\tDébut de la mission : "  + comRobot.getRobot().getIdentifier());

		//tant qu'il y a des victimes à sauver et qu'il n'y en a plus dans l'ambulance
		while ( !victimes.isEmpty() || comRobot.getRobot().isTransportingVictim() ) {

			//si le robot transporte au moins une victime
			if ( comRobot.getRobot().isTransportingVictim() ) {

				//s'il y a de la place dans l'ambulance du robot
				if ( comRobot.getRobot().getNbVictimesTransportees() < comRobot.getRobot().getNbVictimesMax() ) {


					//si l'hopital est le plus proche
					if (ac.HplusProche(victime, hopital, position)){
						p=ac.plusProche(hopital, position); //recuperer le plus proche
						resultat=ac.getResultat();
						deposerHopital(p);

					}else{//sinon la victime est la plus proche
						p=ac.plusProche(victime,position);
						resultat=ac.getResultat();
						chercherVictime(p,victimesCase.get(p));
						victimes=(ArrayList<Victime>) comRobot.getCarte().getListeVictimes();
						victime = victimeToElem(victimes);
					}

					//sinon il n'y a plus de place dans l'ambulance, déposer les victimes
				}else {
					p=ac.plusProche(hopital, position);
					resultat=ac.getResultat();
					deposerHopital(p);
				}
				//sinon le robot ne transporte pas de victimes, aller en chercher
			}else{
				p=ac.plusProche(victime,position);
				resultat=ac.getResultat();
				chercherVictime(p,victimesCase.get(p));
				victimes=(ArrayList<Victime>) comRobot.getCarte().getListeVictimes();
				victime = victimeToElem(victimes);

			}
			System.out.println("Il y a " + comRobot.getRobot().getNbVictimesTransportees() + " victimes dans l'ambulances. "
					+ "Il reste " + victime.size() + " victimes à sauver.");
		}//fin while toutes les victimes sont sauvees

		// fermeture du log
		log.closeWriter();

		/*while (comRobot.reception() != "d") {
			System.out.println("Pas déposé.");
			//System.out.println(comRobot.reception());

		}
		System.out.println("Déposé.");*/
		
		
		// a decommenter
		/*
		try{
			Thread.sleep(30000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}*/

		System.out.println("\t\tFin de la mission : " + comRobot.getRobot().getIdentifier());
	} //fin run
}

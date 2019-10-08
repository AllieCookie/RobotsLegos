package iA;

import java.io.IOException;
import java.util.ArrayList;

import carte.*;
import comm.*;
import lejos.pc.comm.NXTCommException;
import log.Log;
import protagonistes.*;

public class IAbyControl extends Thread{
	/* ----- Attributs ------ */
	private Carte c;	//carte
	private GrapheMap gm;	//graphe par rapport à la carte
	private Commande comRobot=new Commande();
	private String pos=new String(); //position par rapport à la case
	private Elements position; 		//la case où est positionné le robot
	private Log log;
	private ArrayList<Elements> resultat=new ArrayList<>();
	private Controleur control;


	/* ------ Constructeur ------ */
	public IAbyControl(Carte c, Elements depart, String pos,String nomRobot,String adr,String nomC,Robot robot) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		setParametreBase(c,depart,pos);
		comRobot.on(nomRobot,adr,nomC);
		setRobIA(robot);
	}

	public IAbyControl(Carte c, Elements depart, String pos, BTcommunication bt,Robot robot) {
		setParametreBase(c,depart,pos);
		comRobot.on(bt);
		setRobIA(robot);
	}

	public IAbyControl(Carte c, Elements depart, String pos, Commande com,Robot robot) {
		setParametreBase(c,depart,pos);
		comRobot = com;
		setRobIA(robot);
	}

	/* ----- Setter privee utile pour l initialisation du constructeur ------ */
	private void setParametreBase(Carte c, Elements depart, String pos) {
		this.c=c;
		gm=new GrapheMap(c);
		this.position=depart;
		this.pos=pos;
	}

	private void setRobIA(Robot robot) {
		comRobot.setRobot(robot);
		comRobot.setCarte(c);
		comRobot.setPositionCarte(position);
		comRobot.setPos(pos);
		System.out.println("SET "+robot+" "+position+" "+pos);

	}

	
	/* ---- Setter ---- */
	public void setControleur(Controleur control) {
		this.control=control;
	}

	
	/* ---- Getter ---- */
	public Robot getRobot() {
		return comRobot.getRobot();
	}
	
	/* ------ Methodes ------ */
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
			attendre();attendre();
			break;
		case "l":	
			comRobot.gauche(); 
			System.out.println(comRobot.getRobot().getIdentifier()+": gauche");
			attendre();attendre();
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
		default :break;
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
	private void pointAaH() throws BTcommConnectException {
		Elements suivant;

		//initialisation
		suivant=resultat.get(0);
		resultat.remove(0);
		deplacement(suivant);

		//parcourir tout le tableau des résultats
		while ( !resultat.isEmpty() ) {
			suivant=resultat.get(0);
			resultat.remove(0);
			deplacement(suivant);
		}

		//parcourir la case
		deplacementCase(suivant);
	}

	// depot des victimes a l hopital
	public void deposerHopital() throws BTcommConnectException {
		System.out.println(comRobot.getRobot().getIdentifier()+": Direction l'hopital ! ");
		// aller a l hopital
		pointAaH();

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
		Elements suivant = position;//TODO ca marche ça ?!

		boolean continuer=true;
		//initialisation
		while ( (!resultat.isEmpty()) && continuer ) {
			if ( victimeSurCarte(v) ) {
				suivant=resultat.get(0);
				resultat.remove(0);
				deplacement(suivant);
			}else{
				continuer=false;
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
				System.out.println(comRobot.getRobot().getIdentifier()+": Victime dans l'ambulance !");
				// ecriture sur le log
				log.prendreVictime(comRobot.getRobot());
			}else {
				System.out.println(comRobot.getRobot().getIdentifier()+": La victime n'est plus sur la case :(");
			}
		}else {
			System.out.println(comRobot.getRobot().getIdentifier()+": La victime n'est plus sur la case :(");
		}

	}

	// chercher victime
	public void chercherVictime(Elements victime, Victime v) throws BTcommConnectException {
		System.out.println(comRobot.getRobot().getIdentifier()+": Direction victime ! " + victime);
		// aller chercher la victime
		pointAaV(victime,v);
	}	


	/* ----- Programme principal ------ */
	public void run() {
		Robot r = comRobot.getRobot();
		System.out.println("\t\tDébut de la mission : " + r.getIdentifier());
		//ouverture du log
		log = new Log("IA_rapport_"+r.getIdentifier());

		//initialisation
		resultat=control.getChemin(r);

		//tant qu'on obtient un chemin du controleur
		while ( resultat != null ) {

			//si on a une victime en cible
			if ( control.getVictimeIsTarget(r) ) {
				try {
					chercherVictime(control.getVictimeE(r),control.getVictime(r));
				} catch (BTcommConnectException e) {
					e.printStackTrace();
				}

				//sinon deposer les victimes à l'hopital
			}else {
				try {
					deposerHopital();
				} catch (BTcommConnectException e) {
					e.printStackTrace();
				}

			}
			control.majRobot(r, position);
			resultat=control.getChemin(r);

			System.out.println(r.getIdentifier() + " - Il y a " + r.getNbVictimesTransportees() + " victimes dans l'ambulances. "
					+ "Il reste " + c.getListeVictimes().size() + " victimes à sauver.");
		}
		System.out.println("\t\tFin de la mission : " + comRobot.getRobot().getIdentifier());

		try{
			Thread.sleep(30000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
		
		//comRobot.off();

	} //fin run
}

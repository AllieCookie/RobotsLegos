package iA;

import java.util.ArrayList;
import java.util.HashMap;

import carte.Carte;
import carte.Elements;
import protagonistes.*;

public class Controleur {
	/* ---- Attributs ---- */
	private Carte carte;
	private AssistantCalcul jarvis;

	private ArrayList<IAbyControl> robots = new ArrayList<>();

	private ArrayList<Elements> hopitauxElements = new ArrayList<>();
	private ArrayList<Elements> robotsElements = new ArrayList<>();;
	private ArrayList<Elements> victimesElements = new ArrayList<>();;

	private HashMap<Robot,ArrayList<Elements>> robotChemin = new HashMap<>(); //associe un robot et le chemin qu'il doit faire
	private HashMap<Robot,Victime> robotVictime = new HashMap<>(); //associe une victime a un robot
	private HashMap<Robot,Boolean> robotGoVictime = new HashMap<>();//vrai si un robot va chercher une victime (est donc à faux lorsqu'il va a l'hopital)
	private HashMap<Victime,Elements> hmVE = new HashMap<>(); //associe un element a une victime
	private HashMap<Elements,Victime> hmEV = new HashMap<>(); //associe une victime a un element


	/* ----- Constructeur ----- */
	//on prend en paramètre la liste des robots qui lui sont attribués
	public Controleur(Carte c, ArrayList<IAbyControl> rob) {
		carte=c;
		jarvis = new AssistantCalcul(carte);
		this.robots=rob;

		for ( Hopital h : carte.getListeHopitaux() ) {
			hopitauxElements.add(carte.GetAt(h.getPosAbscisses(), h.getPosOrdonnes()));
		}

		Elements vCase;
		for ( Victime v : carte.getListeVictimes() ) {
			vCase=carte.GetAt(v.getPosAbscisses(),v.getPosOrdonnes());
			victimesElements.add(vCase);
			hmVE.put(v,vCase);
			hmEV.put(vCase,v);
		}

		ArrayList<Robot> robots = new ArrayList<>();
		for ( IAbyControl r : rob ) {
			robots.add(r.getRobot());
			r.setControleur(this);
		}

		//Pour chaque robot, on lui associe la victime la plus proche
		for (Robot r : robots) {
			robotsElements.add(carte.GetAt(r.getPosAbscisses(),r.getPosOrdonnes()));
			majRobot(r,carte.GetAt(r.getPosAbscisses(), r.getPosOrdonnes()));
		}
	}


	/* ----- Methodes ----- */
	private void chercherVictime(Robot r,Elements position) {
		//s'il n'y a pas de victime
		if ( victimesElements.isEmpty() )
			robotChemin.put(r,null);
		//s'il reste des victimes
		else {
			Elements v;
			//lui associer la victime la plus proche
			v=jarvis.plusProche(victimesElements,position);
			robotVictime.put(r,hmEV.get(v));
			robotGoVictime.put(r, true);

			//lui donner le chemin
			robotChemin.put(r,jarvis.getResultat());

			//on supprime la victime du tableau
			victimesElements.remove(v);
		}
	}

	private void chercherHopital(Robot r,Elements position) {
		jarvis.plusProche(hopitauxElements,position);
		robotChemin.put(r,jarvis.getResultat());
		robotGoVictime.put(r,false);
	}

	//met à jour le chemin que le robot doit prendre
	public void majRobot(Robot r, Elements position) {
		// s'il a des victimes dans l'ambulance
		if ( r.isTransportingVictim() ) {
			//s'il y a de la place dans l'ambulance
			if ( r.getNbVictimesTransportees() < r.getNbVictimesMax() ) {
				//hopital le plus proche ?
				if ( jarvis.HplusProche( hopitauxElements , victimesElements, position) ) {
					chercherHopital(r,position);
				//sinon c'est la victime le plus proche
				}else{
					chercherVictime(r,position);
				}
			//Sinon il n'y a plus de place dans l'ambulance, donc aller à l'hopital
			}else{
				chercherHopital(r,position);
			}
			// sinon aller chercher victime
		}else {
			chercherVictime(r,position);
		}
	}

	//retourne victime associé au robot en parametre
	public Victime getVictime(Robot r) {
		return robotVictime.get(r);
	}

	public Elements getVictimeE(Robot r) {
		return hmVE.get(robotVictime.get(r));
	}

	//retourne le chemin que le robot doit parcourir
	public ArrayList<Elements> getChemin(Robot r) {
		return robotChemin.get(r);
	}

	//retourne si une victime est prise pour cible
	public boolean getVictimeIsTarget(Robot r) {
		return robotGoVictime.get(r);
	}
	
	//retourne vrai si un robot à finit son job
	public boolean aFini(IAbyControl r) {
		return (!r.isAlive());
	}

	/* ----- programme principal ---- */
	public void go() {
		//pour tous les robots, lancer leurs IA
		for ( IAbyControl r : robots ) {
			r.start();
		}
		
	}
}

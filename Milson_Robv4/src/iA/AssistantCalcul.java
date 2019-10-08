package iA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import carte.Elements;
import carte.*;

public class AssistantCalcul {
	/* ----- Attributs ----- */
	static final int infini=Integer.MAX_VALUE;
	Carte c;
	GrapheMap gm;
	ArrayList<Elements> resultat=new ArrayList<>();

	/* ----- Constructeur ------ */
	public AssistantCalcul(Carte c) {
		this.c=c;
		gm=new GrapheMap(c);
	}
	
	/* ----- Getter ----- */
	public ArrayList<Elements> getResultat(){
		return resultat;
	}


	/* ------ Methode ------ */
	private Elements trouveMin(List<Elements> Q,HashMap<Elements,Integer> d) {
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

	private void majDistance(Elements s1,Elements s2,HashMap<Elements,Integer> d,HashMap<Elements,Elements> predecesseur) {
		if (d.get(s2) > d.get(s1) + 1 ) {
			d.put(s2,d.get(s1)+1);
			predecesseur.put(s2,s1);
		}
	}


	private HashMap<Elements,Elements> Dijkstra(Elements position) {
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
	private ArrayList<Elements> chemin(HashMap<Elements,Elements> tabPred,Elements B,Elements position){
		ArrayList<Elements> resultat=new ArrayList<>();
		Elements s = B;

		while ( s != position ) {
			resultat.add(0,s);
			s=tabPred.get(s);
		}

		return resultat;
	}




	//retourne vrai si un hopital est le plus proche de la position
	public boolean HplusProche(ArrayList<Elements> h,ArrayList<Elements> v,Elements position) {
		int pluscourt=infini;
		boolean cestHopital=true;

		HashMap<Elements,Elements> tabPred=new HashMap<>();
		ArrayList<Elements> resultat=new ArrayList<>();

		//test avec les hopitaux
		for (Elements e : h) {
			tabPred= Dijkstra(position); //tableau des predecesseur
			resultat=chemin(tabPred,e,position);	//liste d'élements dans l'ordre croissant jusqu'au résultat
			if (resultat.size() < pluscourt) {
				this.resultat=resultat;
				pluscourt=resultat.size();
			}
		}

		//test avec les victimes
		for (Elements e : v) {
			tabPred = Dijkstra(position); //tableau des predecesseur
			resultat=chemin(tabPred,e,position);	//liste d'élements dans l'ordre croissant jusqu'au résultat
			if (resultat.size() < pluscourt) {
				this.resultat=resultat;
				pluscourt=resultat.size();
				cestHopital=false;
			}
		}

		return cestHopital;
	}
	
	
	public Elements plusProche(ArrayList<Elements> tab,Elements position) {
		int pluscourt=infini;
		Elements res=null;

		for (Elements e : tab) {
			HashMap<Elements,Elements> tabPred = Dijkstra(position); //tableau des predecesseur
			ArrayList<Elements> resultat=chemin(tabPred,e,position);	//liste d'élements dans l'ordre croissant jusqu'au résultat
			if (resultat.size() < pluscourt) {
				pluscourt=resultat.size();
				res=e;
				this.resultat=resultat;
			}
		}
		return res;
	}


}

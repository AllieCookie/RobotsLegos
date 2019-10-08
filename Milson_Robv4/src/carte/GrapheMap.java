package carte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class GrapheMap {
	
	private Carte carte;
	
	//Pour chaque sommet on associe un chemin pour acceder a l'element voisin
	private HashMap<Elements, HashMap<String, Elements>> graphe;
	
	public GrapheMap(Carte cr) {
		carte =  cr;
		initGraphe();
	}
	
	/* Creation du graphe qui associe pour chaque element de la carte un ensemble de voisin */
	private void initGraphe() {
		
		graphe = new HashMap<Elements, HashMap<String, Elements>>();
		int ordMax = carte.getOrdonnees();
		int absMax = carte.getAbscisses();
		HashMap<String, Elements> he = new HashMap<String, Elements>();
		Elements e;
		
		for(int i=0;i<absMax;i++) {
			for(int j=0;j<ordMax;j++) {
				
				e = carte.GetAt(i, j);
				if(e.getType() != TypeElement.Vide){
					he = createVoisin(i, j);
					graphe.put(e, he);
				}	
			}
		}
		
	}
	
	
	public Carte getCarte() {
		return carte;
	}
	
	//Recupere une liste de tout les elements de la carte
	public List<Elements> getAllElemments(){
		
		ArrayList<Elements> al = new ArrayList<Elements>();
		int ordMax = carte.getOrdonnees();
		int absMax = carte.getAbscisses();
		Elements e;
		
		for(int i=0;i<absMax;i++) {
			for(int j=0;j<ordMax;j++) {
				
				e = carte.GetAt(i, j);
				if(e.getType() != TypeElement.Vide){
					al.add(e);
				}	
			}
		}
		
		return al;
	}

	//Maj le 25 avril
	//Creer les differents voisins accessible depuis la case en abs ord
	private HashMap<String, Elements> createVoisin(int abs,int ord) {
		HashMap<String, Elements> he = new HashMap<String, Elements>();
		Elements e;
		
		int absMax = carte.getAbscisses() - 1;
		int ordMax = carte.getOrdonnees() - 1;
		Elements ele = carte.GetAt(abs, ord);
		TypeElement tele = ele.getType();
		
		if (abs != 0 && (tele == TypeElement.VirageHD || tele == TypeElement.VirageHG || tele == TypeElement.CroisementDHB 
				 || tele == TypeElement.CroisementGDH || tele == TypeElement.CroisementGHB || tele == TypeElement.RouteHB)  ) {
			e = carte.GetAt(abs - 1, ord);
			if(e.getType() != TypeElement.Vide){
				he.put("s", e);
				//le.add(e);	
			}
		}
		
		if (abs != absMax && (tele == TypeElement.VirageBD || tele == TypeElement.VirageBG || tele == TypeElement.CroisementGDB 
				 || tele == TypeElement.CroisementDHB || tele == TypeElement.CroisementGHB || tele == TypeElement.RouteHB)) {
			e = carte.GetAt(abs + 1, ord);
			if(e.getType() != TypeElement.Vide){
				he.put("u", e);
				//le.add(e);	
			}

		}
		
		if(ord != 0 && (tele == TypeElement.VirageBG || tele == TypeElement.VirageHG || tele == TypeElement.CroisementGDB 
				 || tele == TypeElement.CroisementGDH || tele == TypeElement.CroisementGHB || tele == TypeElement.RouteDG)) {
			e = carte.GetAt(abs, ord - 1);
			if(e.getType() != TypeElement.Vide){
				he.put("l", e);
				//le.add(e);	
			}

		}
		if(ord != ordMax && (tele == TypeElement.VirageBD || tele == TypeElement.VirageHD || tele == TypeElement.CroisementGDB 
				 || tele == TypeElement.CroisementDHB || tele == TypeElement.CroisementGDH || tele == TypeElement.RouteDG)) {
			e = carte.GetAt(abs, ord + 1);
			if(e.getType() != TypeElement.Vide){
				he.put("r", e);
				//le.add(e);	
			}

		}
		
		//System.out.println(abs+","+ord+":"+he);
		return he;
		
	}
	
	//Renvoie une hashmap qui associe pour chaque direction un voisin de e
	public HashMap<String, Elements> getVoisins(Elements e) {
		return graphe.get(e);
	}
	
	//Renvoie une list des voisins de e
	public ArrayList<Elements> getVoisinsArray(Elements e) {
		HashMap<String, Elements> he = getVoisins(e);
		ArrayList<Elements> al = new ArrayList<Elements>();
			
		Set<String> keyList = he.keySet();
		for(String s : keyList) {
			al.add(he.get(s));

		}
		return al;
	}
	
	//Renvoie une hashmap qui associe pour chaque direction un voisin de e orienter par rapport a la case ou se trouvait le robot avant (voisin de e)
	//Renvoie null en cas de pb
	public  HashMap<String, Elements> getVoisinsWithPred(Elements e,Elements pred){
		HashMap<String, Elements> he = graphe.get(e);
		if (pred == null) {
			return he;
		}
		
		//Recherche de l'emplacement de l'etat precedent
		Set<String> keyList = he.keySet();
		String instruPred = "";
		for(String s : keyList) {
			if(he.get(s)==pred) {
				instruPred = s;
			}
		}

		HashMap<String, Elements> newhe = new HashMap<String, Elements>();
		
		for(String s : keyList) {
			
			switch (instruPred) {
			
			case "r":
				switch (s) {
				case "r":	newhe.put("u", he.get("r")); break;
				case "l":	newhe.put("s", he.get("l")); break;				
				case "s":	newhe.put("r", he.get("s")); break;				
				case "u":	newhe.put("l", he.get("u")); break;			
				}
				break;
				
			case "l":
				switch (s) {
				case "r":	newhe.put("s", he.get("r")); break;
				case "l":	newhe.put("u", he.get("l")); break;				
				case "s":	newhe.put("l", he.get("s")); break;				
				case "u":	newhe.put("r", he.get("u")); break;			
				}	
				break;
			case "s":
				switch (s) {
				case "r":	newhe.put("l", he.get("r")); break;
				case "l":	newhe.put("r", he.get("l")); break;				
				case "s":	newhe.put("u", he.get("s")); break;				
				case "u":	newhe.put("s", he.get("u")); break;			
				}	
				break;
			case "u":
				newhe.put(s, he.get(s)); 
				break;
					
			}
		
		}
		//System.out.println("E: "+e+" Pred: "+pred+"He:"+he+" newHe: "+newhe);
		return newhe;
		
	}
	
	//Dit si le deplacement est possible, c'est a dire si aller dans tel direction est permis par le grahe
	//Utilisation pour le depart quand on ne connait pas de pred
	public boolean deplacementPossible(Elements depart,String direction) {
		return graphe.get(depart).containsKey(direction);
	}
	
	/**
	Dit si le deplacement est possible, c'est a dire si aller dans tel direction est permis par le grahe en prenant
	Un predeceseur afin d'orienter les choix possible, si pred = null -> deplacement possible
	Utilisation qui marche pour tout les cas, utilisation par défaut 
	**/
	public boolean deplacementPossibleWithPred(Elements depart,String direction,Elements pred) {
		if (pred == null) {
			return deplacementPossible(depart,direction);
		}
		return getVoisinsWithPred(depart, pred).containsKey(direction);
	}
	
	//Donne le nouvel element issue du deplacement de depart à tel direction
	public Elements deplacement(Elements depart, String direction) {
		return getVoisins(depart).get(direction);
	}
	
	public Elements deplacementPred(Elements depart, String direction, Elements pred) {
		return getVoisinsWithPred(depart, pred).get(direction);
	}
	
	//Renvoie une hashmap qui associe pour chaque direction un voisin de e orienter par rapport a l'orientation du robot actuel (voisin de e)
	public  HashMap<String, Elements> getVoisinsWithOrient(Elements e,String st){
		HashMap<String, Elements> he = graphe.get(e);
		
		//Recherche de l'emplacement de l'etat precedent
		Set<String> keyList = he.keySet();
		String instruPred = st;


		HashMap<String, Elements> newhe = new HashMap<String, Elements>();
		
		for(String s : keyList) {
			
			switch (instruPred) {
			
			case "l":
				switch (s) {
				case "r":	newhe.put("u", he.get("r")); break;
				case "l":	newhe.put("s", he.get("l")); break;				
				case "s":	newhe.put("r", he.get("s")); break;				
				case "u":	newhe.put("l", he.get("u")); break;			
				}
				break;
				
			case "r":
				switch (s) {
				case "r":	newhe.put("s", he.get("r")); break;
				case "l":	newhe.put("u", he.get("l")); break;				
				case "s":	newhe.put("l", he.get("s")); break;				
				case "u":	newhe.put("r", he.get("u")); break;			
				}	
				break;
			case "u":
				switch (s) {
				case "r":	newhe.put("l", he.get("r")); break;
				case "l":	newhe.put("r", he.get("l")); break;				
				case "s":	newhe.put("u", he.get("s")); break;				
				case "u":	newhe.put("s", he.get("u")); break;			
				}	
				break;
			case "s":
				newhe.put(s, he.get(s)); 
				break;
					
			}
		
		}
		//System.out.println("E: "+e+" Orient: "+st+" He:"+he+" newHe: "+newhe);
		return newhe;
		
	}
	
	/**
	Dit si le deplacement est possible, c'est a dire si aller dans tel direction est permis par le grahe en prenant
	Une orientation du robot afin d'orienter les choix possible,
	Utilisation qui marche pour tout les cas, utilisation par défaut quand on connait pas la case precedente ou si on souhaite parcourir dans un chemin 
	**/
	public boolean deplacementPossibleWithOrient(Elements depart,String direction,String orientation) {
		return getVoisinsWithOrient(depart, orientation).containsKey(direction);
	}
	
	public Elements deplacementOrient(Elements depart, String direction, String orientation) {
		return getVoisinsWithOrient(depart, orientation).get(direction);
	}
	
	public String getOrientFromPred(Elements actuel, Elements pred) {
		HashMap<String, Elements> gv = getVoisins(actuel);
		Set<String> ensemble = gv.keySet();
		String st ="";
		String r = "";
		for(String s: ensemble) {
			if(gv.get(s) == pred) {
				st = s;
			}
		}
		switch (st) {
		
		case "s":
			r = "u";
			break;
		case "u":
			r = "s";
			break;
		case "r":
			r = "l";
			break;
		case "l":
			r = "r";
			break;
		default:
			System.err.println("GetOrientFromPred: Erreur pred");
			break;
		}

		return r;
	}
	
	
	
	//Renvoie une hashmap qui associe pour chaque direction un voisin de e orienter par rapport a la position du robot actuel (voisin de e)
	public  HashMap<String, Elements> getVoisinsWithPos(Elements e,String st){
		HashMap<String, Elements> he = graphe.get(e);
		
		//Recherche de l'emplacement de l'etat precedent
		Set<String> keyList = he.keySet();
		String instruPred = st;


		HashMap<String, Elements> newhe = new HashMap<String, Elements>();
		
		for(String s : keyList) {
			
			switch (instruPred) {
			
			case "r":
				switch (s) {
					case "r":	newhe.put("u", he.get("r")); break;
					case "l":	newhe.put("s", he.get("l")); break;				
					case "s":	newhe.put("r", he.get("s")); break;				
					case "u":	newhe.put("l", he.get("u")); break;			
				}
				break;
				
			case "l":
				switch (s) {
				case "r":	newhe.put("s", he.get("r")); break;
				case "l":	newhe.put("u", he.get("l")); break;				
				case "s":	newhe.put("l", he.get("s")); break;				
				case "u":	newhe.put("r", he.get("u")); break;			
				}	
				break;
			case "s":
				switch (s) {
				case "r":	newhe.put("l", he.get("r")); break;
				case "l":	newhe.put("r", he.get("l")); break;				
				case "s":	newhe.put("u", he.get("s")); break;				
				case "u":	newhe.put("s", he.get("u")); break;			
				}	
				break;
			case "u":
				newhe.put(s, he.get(s)); 
				break;
					
			}
		
		}
		
		/* mtn qu'on a les chemins orienter par la position sur la case, il faut transformer suivant les consignes donnees: virage -> tout droit, intersection -> soit gauche soit droite*/
		//Chemin qui tient compte du type de l'element
		HashMap<String, Elements> newhe2 = new HashMap<String, Elements>();
		
		keyList = newhe.keySet();
		for(String s : keyList) {
			
			if( e.getType() == TypeElement.VirageBD || e.getType() == TypeElement.VirageBG || e.getType() == TypeElement.VirageHD || e.getType() == TypeElement.VirageHG ) {
				
				if ( s == "l" || s == "r") {
					newhe2.put("s", newhe.get(s));
				}
				else {
					newhe2.put(s, newhe.get(s));

				}
					
			}
			
			else if( e.getType() == TypeElement.CroisementDHB || e.getType() == TypeElement.CroisementGDB || e.getType() == TypeElement.CroisementGDH 
					|| e.getType() == TypeElement.CroisementGHB ) {
				
				if( s == "s" && newhe.containsKey("r")) {
					newhe2.put("l", newhe.get(s));

				}
				else if( s == "s" && newhe.containsKey("l")) {
					newhe2.put("r", newhe.get(s));

				}
				else	{
					newhe2.put(s, newhe.get(s));

				}
				
				
			}
			
			else {
				newhe2.put(s, newhe.get(s));
			}
			
		}
		
		//Affichage la case la pose, les differentes hash map et la hashmap utile
		//System.out.println("WithPos: "+e+" Pos: "+st+" He:"+he+" newHe: "+newhe+" newhe2: "+newhe2);
		//System.out.println("\tVoisinWithPos: "+e+" Pos: "+st+" dep: "+newhe2+"\n");

		return newhe2;
		
	}
	
	/**
	Dit si le deplacement est possible, c'est a dire si aller dans tel direction est permis par le grahe en prenant
	Une position de la case du robot  afin d'orienter les choix possible,
	Utilisation qui marche pour tout les cas, utilisation par défaut quand on connait pas la case precedente ou si on souhaite parcourir dans un chemin 
	**/
	public boolean deplacementPossibleWithPos(Elements depart,String direction,String pos) {
		return getVoisinsWithPos(depart, pos).containsKey(direction);
	}
	
	public Elements deplacementPos(Elements depart, String direction, String pos) {
		return getVoisinsWithPos(depart, pos).get(direction);
	}
	
	public String deplacement(Elements depart,Elements arrivee,String pos) {
		 HashMap<String, Elements> a = getVoisinsWithPos(depart, pos);
		 Set<String> key = a.keySet();
		 String f = "";
		 for( String p : key) {
			 if (a.get(p) == arrivee) {
				 f = p;
			 }
		 }
		 return f;
	}
	
	
	//Modification le 23 avril
	public String newPosAfterDep(Elements depart,Elements arrivee) {
		//System.out.print("Newpos "+depart+":"+arrivee+" ");
		 HashMap<String, Elements> a = getVoisins(depart);
		 Set<String> key = a.keySet();
		 String f = "";
		 String g = "";
		 for( String p : key) {
			 if (a.get(p) == arrivee) {
				 f = p;
			 }
		 }
		 
		 switch (f) {
		 case "l":
			 g = "r";//Avant l
			 break;
		 case "r":
			 g = "l";
			 break;
		 case "s":
			 g = "u";
			 break;
		 case "u":
			 g = "s";
			 break;
		 
		 }
		 //System.out.println(g);
		 return g;
	}
	
	public String deplacementDefault(Elements e) {
		String t;
		
		if( e.getType() == TypeElement.CroisementDHB || e.getType() == TypeElement.CroisementGDB || e.getType() == TypeElement.CroisementGDH 
				|| e.getType() == TypeElement.CroisementGHB ) {
			t = "l";
		}
		else {
			t = "s";
		}
			
		return t;
	}
	


}

package comm;

import java.io.IOException;

import carte.Carte;
import carte.Elements;
import carte.GrapheMap;
import lejos.pc.comm.NXTCommException;
import protagonistes.Robot;
import protagonistes.TypeProtagoniste;
import protagonistes.Victime;
import tests.TestsCarte;

public class Commande implements ICommande {
	/* ----- Attributs ---- */
	private BTcommunication bt;
	private Robot robot;
	private Carte c;
	private GrapheMap gm;
	private Elements oldPosCarte;
	private Elements posCarte;		//position sur la carte
	private String pos=new String();
	private Boolean simulation = true;

	/* ----- Constructeurs ----- */
	public void defaut() {
		setCarte(new TestsCarte().carteType4());
		Elements debut=c.GetAt(0,1); 
		//realPos = c.GetAt(0, 0);
		robot=new Robot(c.findLigneElem(debut), c.findColElem(debut),TypeProtagoniste.robotIA , "Test"); 
		setPositionCarte(debut);
		pos="u";
	}
	
	
	public void on(String nomR,String adr,String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		if(simulation) {
			bt = null;
		}
		else {
			bt=new BTcommunication(nomR,adr,nomC);
			if ( bt.getEstConnect() )
				System.out.println("Communication OK avec " + bt.getNomRobot() + ".");
			defaut();
		}


	}

	public void on(BTcommunication bt) {
		this.bt=bt;
		defaut();
	}

	public void on(BTcommunication bt,Carte c,Robot robot,Elements posCarte, String pos) {
		this.bt=bt;
		this.robot=robot;
		this.c=c;
		gm=new GrapheMap(c);
		this.posCarte=posCarte;
		this.pos=pos;
	}

	/* ----- Getter ---- */
	public Robot getRobot() {
		return robot;
	}

	public String getPosRobot() {
		return pos;
	}

	public Elements getPosElemRobot() {
		return posCarte;
	}
	
	public Carte getCarte() {
		return c;
	}
	
	public Boolean getSimulation() {
		return simulation;
	}



	/* ----- Setter ----- */
	public void setRobot(Robot robot) {
		this.robot=robot;
	}

	public void setCarte(Carte c) {
		this.c=c;
		gm=new GrapheMap(c);
	}

	public void setPositionCarte(Elements pos) {
		posCarte=pos;
	}

	public void setPos(String pos) {
		this.pos=pos;
	}

	public void setParamRobot(String newPos,Elements newPositionCarte,Robot roboto) {
		pos=newPos;
		posCarte=newPositionCarte;
		robot.setPosAbscisses(roboto.getPosAbscisses());
		robot.setPosOrdonnes(roboto.getPosOrdonnes());
	}

	public void setSimulation(Boolean simulation) {
		this.simulation = simulation;
	}
	/* ---- Methodes ----- */
	@Override
	public void off() throws BTcommConnectException, IOException, InterruptedException {
		if(!simulation)
			bt.deconnexion();
	}

	@Override
	public void avancer() throws BTcommConnectException{
		if(!simulation)
			bt.envoie("s\n");
		oldPosCarte = posCarte;
		posCarte=gm.deplacementPos(posCarte,"s",pos);
		pos = gm.newPosAfterDep(oldPosCarte, posCarte);
		robot.setPosAbscisses(c.findLigneElem(oldPosCarte));
		robot.setPosOrdonnes(c.findColElem(oldPosCarte));
	}

	@Override
	public void gauche() throws BTcommConnectException {
		if(!simulation)
			bt.envoie("l\n");
		oldPosCarte = posCarte;
		posCarte=gm.deplacementPos(posCarte,"l",pos);
		pos = gm.newPosAfterDep(oldPosCarte, posCarte);
		robot.setPosAbscisses(c.findLigneElem(oldPosCarte));
		robot.setPosOrdonnes(c.findColElem(oldPosCarte));
	}

	@Override
	public void droite() throws BTcommConnectException{
		if(!simulation)
			bt.envoie("r\n");
		oldPosCarte = posCarte;
		posCarte=gm.deplacementPos(posCarte,"r",pos);
		pos = gm.newPosAfterDep(oldPosCarte, posCarte);
		robot.setPosAbscisses(c.findLigneElem(oldPosCarte));
		robot.setPosOrdonnes(c.findColElem(oldPosCarte));
	}

	@Override
	public void demitour() throws BTcommConnectException{
		if(!simulation)
			bt.envoie("u\n");
		oldPosCarte = posCarte;
		posCarte=gm.deplacementPos(posCarte,"u",pos);
		pos = gm.newPosAfterDep(oldPosCarte, posCarte);
		//robot.setPosAbscisses(c.findLigneElem(posCarte));
		//robot.setPosOrdonnes(c.findColElem(posCarte));
	}

	@Override
	public void ramasser() throws BTcommConnectException{
		if(!simulation)
			bt.envoie(" t\n");
		//recherche d une victime correspondant a la case
		Elements vicE;
		Victime vic=null;
		for (Victime v : c.getListeVictimes()) {
			vicE=c.GetAt(v.getPosAbscisses(),v.getPosOrdonnes());
			if (posCarte == vicE) {
				vic=v;
				robot.takeVictim(vic);
				c.getListeVictimes().remove(vic);//suppression de la victime dans la liste
			}
		}
	}

	public void ramasser(Victime v) throws BTcommConnectException {
		if(!simulation)
			bt.envoie("t\n");
		robot.takeVictim(v);
		c.getListeVictimes().remove(v);
	}

	@Override
	public void deposer() throws BTcommConnectException{
		if(!simulation)
			bt.envoie("d\n");
		//System.out.println("Victime : " + robot.getTabVictimes());
		robot.dropVictim();
	}

	public void envoie(String message) throws BTcommConnectException{
		if(!simulation)
			bt.envoie(message);
	}

	public String reception() throws BTcommConnectException{
		if(!simulation)
			return bt.reception();
		else
			return "reception: null car simulation";
	}
	
	//A utiliser pour pouvoir calculer les differentes pos plus facilement en donnant la vrai post du robot
	public void setPositionPos(Elements realPosition,String posi) {
		if(gm.getVoisins(realPosition).containsKey(posi)) {
			System.out.println("SetRealPos: il existe un voisin");
			posCarte = gm.getVoisins(realPosition).get(posi);
			pos = gm.newPosAfterDep(realPosition, posCarte);
			System.out.println("SetRealPos: realPos: "+realPosition+" posCarte: "+posCarte+" new pos:"+pos );
		}
		else {
			System.out.println("SetRealPos: il n'existe pas de voisin");

		}
		
		
	}

}
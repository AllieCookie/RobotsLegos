package tests;

import java.io.IOException;
import java.util.ArrayList;

import affichage.InterfaceCommande;
import carte.Carte;
import carte.Elements;
import carte.TypeElement;
import comm.BTcommConnectException;
import comm.BTcommunication;
import comm.Commande;
import iA.Controleur;
import iA.IArobot;
import iA.IAbyControl;
import lejos.pc.comm.NXTCommException;
import protagonistes.Hopital;
import protagonistes.Robot;
import protagonistes.TypeProtagoniste;
//import protagonistes.Robot;
//import protagonistes.TypeProtagoniste;
import protagonistes.Victime;

public class Main2 {
	/* tests communications et interface de commandes */
	public void testTelecommande() {
		InterfaceCommande ic = new InterfaceCommande();
		Commande com = new Commande();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {ic.create(com); } });

	}
	public static Carte carteType() {
		int absc = 3;
		int ord = 4;

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
	public Carte carteExam() {
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


		//Integration dans carte
		for(int i=0;i<absc;i++) {
			for(int j=0;j<ord;j++) {
				myMap.SetAt(i, j, al[i][j]);
			}

		}


		return myMap;

	}

	/* tests avec IA 
	public void testsAvecIAsem22() throws BTcommConnectException {
		Carte c = carteType();
		ArrayList<Elements> victimes = new ArrayList<>();
		String pos= "u\n" ;
		Elements depart = c.GetAt(0, 0);

		victimes.add(c.GetAt(1,1));
		victimes.add(c.GetAt(2,3));

		ArrayList<Elements> hopital = new ArrayList<>();
		hopital.add(c.GetAt(2,0));

		IArobot robot = new IArobot(c,"MilsonRob\n",pos);
		robot.run(depart, victimes, hopital,1);
	}*/


	/*carte de l'exam
	public void testsIAExam26() throws BTcommConnectException {
		Carte c = carteExam();

		String pos= "r\n" ;

		ArrayList<Elements> victimes = new ArrayList<>();

		victimes.add(c.GetAt(2,4));
		victimes.add(c.GetAt(1,0));

		Elements depart = c.GetAt(0,1);

		ArrayList<Elements> hopital = new ArrayList<>();
		hopital.add(c.GetAt(0,4));

		int placesDansAmbulance=1;


		IArobot robot = new IArobot(c,"MilsonRob\n",pos);
		robot.run(depart, victimes, hopital,placesDansAmbulance);
	}*/


	/*carte de l'exam 
	public void testsIAsem29() throws BTcommConnectException {
		Carte c = carteExam();

		String pos= "r\n" ;

		ArrayList<Elements> victimes = new ArrayList<>();

		victimes.add(c.GetAt(2,4));
		victimes.add(c.GetAt(1,0));

		Elements depart = c.GetAt(0,1);

		ArrayList<Elements> hopital = new ArrayList<>();
		hopital.add(c.GetAt(0,4));

		int placesDansAmbulance=1;

		IArobot robot = new IArobot(c,pos,firnen("MR"));
		robot.run(depart, victimes, hopital);
	}*/

	/*public void testsIAProtagoniste() throws BTcommConnectException, NXTCommException, IOException, InterruptedException{
		Carte c = carteExam();
		ArrayList<Victime> victimes = new ArrayList<>();
		String pos= "u\n" ;
		Elements depart = c.GetAt(4,4);

		Victime v1 = new Victime(4,1);
		Victime v2 = new Victime(1,1);
		victimes.add(v1);
		victimes.add(v2);

		ArrayList<Hopital> hopital = new ArrayList<>();
		Hopital h = new Hopital(0,4);
		hopital.add(h);

		IArobot robot = new IArobot(c,pos,"Firnen","00:16:53:16:2E:5B","MR");
		robot.run(depart, victimes, hopital);
	}*/

	//notre robot
	public BTcommunication firnen(String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		//code PIN : 0000
		return new BTcommunication("Firnen","00:16:53:16:2E:5B",nomC+"\n");
	}

	//robot de Pierre
	public BTcommunication glaedr(String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		//code PIN : ????
		return new BTcommunication("Glaedr","00:16:53:1C:15:FC",nomC+"\n"); 
	}

	public void testsConnexion() throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		BTcommunication test = firnen("MilsonRob");
		test.envoie("u\n");

		try{
			Thread.sleep(5000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}

		while ( test.reception() != null ) {
			System.out.println("Reception : " +test.reception());
		}

		test.deconnexion();

	}

	public void testTelecommandeRob1(BTcommunication bt) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		Commande comm=new Commande();
		comm.on(bt);
		InterfaceCommande ic = new InterfaceCommande();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {ic.create(comm); } });

	}
/*
	public void testsIAProtagoniste(BTcommunication bt) throws BTcommConnectException, NXTCommException, IOException, InterruptedException{
		Carte c = carteExam();
		ArrayList<Victime> victimes = new ArrayList<>();
		String pos= "u\n" ;
		Elements depart = c.GetAt(4,4);

		Victime v1 = new Victime(4,1);
		Victime v2 = new Victime(1,1);
		victimes.add(v1);
		victimes.add(v2);

		ArrayList<Hopital> hopital = new ArrayList<>();
		Hopital h = new Hopital(0,4);
		hopital.add(h);

		IArobot robot = new IArobot(c,pos,bt);
		robot.run(depart, victimes, hopital);


	}*/


	

	public void testsEntreDeuxRobots() throws BTcommConnectException, NXTCommException, IOException, InterruptedException {

		//deux robots
		//robot 1 reçoit une instruction
		//il renvoie l'instruction au pc et au robot2 qui exécute l'info

		Commande commR1=new Commande();
		commR1.on(firnen("P1"));

		Commande commR2=new Commande();
		commR2.on(glaedr("P2"));

		commR1.avancer();
		String messR1=commR1.reception();
		System.out.println(messR1);


		messR1=commR1.reception();
		if (messR1 == "Firnen") {
			System.out.println(messR1);
		}else {
			commR2.envoie(messR1+"\n");
		}

		try{
			Thread.sleep(10000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();

		}
		commR1.off();
		commR2.off();

	}

	
	public void testsIAderVer() throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		
		Carte c = carteExam();
		ArrayList<Victime> victimes = new ArrayList<>();
		String pos= "u" ;
		Elements depart = c.GetAt(3,1);

		Victime v1 = new Victime(4,1);
		Victime v2 = new Victime(2,1);
		victimes.add(v1);
		victimes.add(v2);

		ArrayList<Hopital> hopital = new ArrayList<>();
		Hopital h = new Hopital(0,4);
		hopital.add(h);

		
		IArobot robot = new IArobot(c,pos,"Firnen","00:16:53:16:2E:5B","Milson\n");
		robot.run(depart,victimes, hopital);
	}
	
	public void testsDeuxRobots() throws BTcommConnectException, NXTCommException, IOException, InterruptedException {

		testTelecommandeRob1(glaedr("MR"));

		testsIAderVer();


	}
	
	
	public void tests2IA() throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		Carte c = carteExam();
		//Victimes
		ArrayList<Victime> victimes = new ArrayList<>();
		Victime v1 = new Victime(4,1);
		Victime v2 = new Victime(2,1);
		victimes.add(v1);
		victimes.add(v2);
		
		//Hopitaux
		ArrayList<Hopital> hopital = new ArrayList<>();
		Hopital h = new Hopital(0,4);
		hopital.add(h);
		
		//Robots
		String pos1 = "u";
		String pos2 = "l";
		Elements depart1 = c.GetAt(1,0);
		Elements depart2 = c.GetAt(2,3);
		Robot rob1=new Robot(c.findLigneElem(depart1), c.findColElem(depart1),TypeProtagoniste.robotIA , "Firnen");
		Robot rob2=new Robot(c.findLigneElem(depart2), c.findColElem(depart2),TypeProtagoniste.robotIA , "Glaedr");
		ArrayList<Robot> robots = new ArrayList<>();
		robots.add(rob1);
		robots.add(rob2);
		
		//remplissage de la carte
		c.setListeHopitaux(hopital);
		c.setListeVictimes(victimes);
		c.setListeRobots(robots);
		
		//IA robot
		IArobot robot1 = new IArobot(c,depart1,pos1,"Firnen","00:16:53:16:2E:5B","Milson\n",rob1);
		IArobot robot2 = new IArobot(c,depart2,pos2,"Glaedr","00:16:53:1C:15:FC","Milson\n",rob2);
	
		robot1.start();
		robot2.start();
		
		
		
	}
	
	// ==> OK, à la fin du traîtement de Firnen, Glaedr n'a plus de victime à sauver.
	
	
	public void testControleurVer() throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		Carte c = carteExam();
		//Victimes
		ArrayList<Victime> victimes = new ArrayList<>();
		Victime v1 = new Victime(4,1);
		//Victime v2 = new Victime(2,4);
		victimes.add(v1);
		//victimes.add(v2);
		
		//Hopitaux
		ArrayList<Hopital> hopital = new ArrayList<>();
		Hopital h = new Hopital(4,3);
		hopital.add(h);
		
		//Robots
		String pos1 = "u";
		//String pos2 = "l";
		Elements depart1 = c.GetAt(3,2);
		//Elements depart2 = c.GetAt(2,3);
		Robot rob1=new Robot(c.findLigneElem(depart1), c.findColElem(depart1),TypeProtagoniste.robotIA , "Firnen");
		//Robot rob2=new Robot(c.findLigneElem(depart2), c.findColElem(depart2),TypeProtagoniste.robotIA , "Glaedr");
		ArrayList<Robot> robots = new ArrayList<>();
		robots.add(rob1);
		//robots.add(rob2);
		
		//remplissage de la carte
		c.setListeHopitaux(hopital);
		c.setListeVictimes(victimes);
		c.setListeRobots(robots);
		
		//IA robot
		IAbyControl robot1 = new IAbyControl(c,depart1,pos1,"Firnen","00:16:53:16:2E:5B","Milson\n",rob1);
		//IAbyControl robot2 = new IAbyControl(c,depart2,pos2,"Glaedr","00:16:53:1C:15:FC","Milson\n",rob2);
		ArrayList<IAbyControl> IArobots = new ArrayList<>();
		IArobots.add(robot1);
		//IArobots.add(robot2);


		//Controleur
		Controleur control = new Controleur(c,IArobots);
	
		control.go();
		
	}
	


	/* ----- main ----- */
	public static void main(String[] args) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		Main2 m=new Main2();
		//m.testTelecommande();
		//m.testsAvecIA();
		//m.testsIAExam26();
		//m.testsIAsem29();
		//m.testsIAProtagoniste(m.firnen("MR"));
		//m.testsConnexion();
		//m.testsDeuxRobots();
		//m.testsEntreDeuxRobots();
		//m.testsIAderVer();
		//m.tests2IA();
		m.testControleurVer();
		
	}


}

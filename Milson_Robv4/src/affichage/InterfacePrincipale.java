package affichage;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import carte.Carte;
import carte.CarteImagee;
import carte.Elements;
import carte.GrapheMap;
import comm.BTcommConnectException;
import comm.Commande;
import iA.Controleur;
import iA.IAbyControl;
import lejos.pc.comm.NXTCommException;
import log.Log;
import protagonistes.Hopital;
import protagonistes.Protagoniste;
import protagonistes.Robot;
import protagonistes.RobotData;
import protagonistes.TypeProtagoniste;
import protagonistes.Victime;
import tests.TestsCarte;

/*
 * V4: gestion de plusieurs robots  IA avec un Joueur
 * Positionnement des robots vers l'interieur de la case
 *  
 */
public class InterfacePrincipale {
	Commande com;
	
	/*
	String nomControlleur = "MilsonRob";
	String nomRobot = "Glaedr"; 
	String adr = "00:16:53:1C:15:FC"; 
	*/
	
	String nomControlleur = "MilsonRob";
	String nomRobot = "Firnen"; 
	String adr = "00:16:53:16:2E:5B"; 
	
	Boolean on;
	
	//creation de la telecommande
	private JFrame frame ;
	private GridLayout layout ;
	private JPanel monPannel ; 
	
	private boolean onGoing;
	private boolean simulation;
	
	private Robot robotJoueur;
	private Robot robotIA;
	private JTextArea ja;
	private JSplitPane jsplit2;
	private JSplitPane jsplit1;
	private JSplitPane jsplitv;
	private JSplitPane  jsplitParamList; //Separation entre les boutons pour les objets et la liste des objets (victimes / hopitaux)
	
	private JButton boutonStartStop;
	private JButton boutonSimulation;
	private JPanel jcarte;
	private Log log;
	

	private GrapheMap gm;
	String posJoueur;
	String posIA;
	Elements positionJoueur;
	Elements positionIA;
	
	private ArrayList<Robot> listeRobots;
	private ArrayList<Victime> listeVictimes;
	private ArrayList<Hopital> listeHopitaux;
		
	//HashMap qui associe pour un Robot sa position dans une case
	private HashMap<Robot, String> hmRobotPos;
	//HashMap qui associe pour un Robot sa case
	private HashMap<Robot, Elements> hmRobotPosition;
	private HashMap<Robot, Commande> hmRobotCom;
	private HashMap<Robot, String> hmRobotEtat;
	private HashMap<Robot, RobotData> hmRobotData;

	Carte carte;
	CarteImagee carteIm;
	
	/*Creation de l'interface
	 * Prends en parametre une telecommande associé au joueur et une carte 
	 * 
	 */
	public void create(Commande c,Carte carte) {
		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		simulation = true;
		
		//init list
		listeRobots = new ArrayList<Robot>();
		listeVictimes = new ArrayList<Victime>();
		listeHopitaux = new ArrayList<Hopital>();
		
		//init hashMap
		hmRobotPos = new HashMap<Robot, String>();
		hmRobotPosition =  new HashMap<Robot, Elements>();
		hmRobotCom = new HashMap<Robot, Commande>();
		hmRobotEtat = new HashMap<Robot, String>();
		hmRobotData = new HashMap<Robot, RobotData>();
		
		//jeu de test de base
		listeVictimes.add(new Victime(2, 4));
		listeHopitaux.add(new Hopital(4, 3));
		
		onGoing = false; // A true lorsque l'ia est lancé
		
		//init comm
		com = c;
		on = false;
		
		//init joueur
		robotJoueur = new Robot(0, 1, TypeProtagoniste.robotHumain,"robotHumain");
		positionJoueur = carte.GetAt(robotJoueur.getPosAbscisses(),robotJoueur.getPosOrdonnes());
		posJoueur = "s";
		
		//init IA
		robotIA = new Robot(1, 1, TypeProtagoniste.robotIA,"robotIA");
		positionIA = carte.GetAt(robotIA.getPosAbscisses(),robotIA.getPosOrdonnes());
		posIA = "s";
		
		//init robotData 
		RobotData firnen = new RobotData("RobotJoueur", "Firnen", "00:16:53:16:2E:5B");
		RobotData glaedr = new RobotData("RobotIA", "Glaedr", "00:16:53:1C:15:FC");
		
		listeRobots.add(robotJoueur);
		listeRobots.add(robotIA);
		
		hmRobotEtat.put(robotJoueur, "Déconnecté");
		hmRobotEtat.put(robotIA, "Déconnecté");

		hmRobotPosition.put(robotJoueur, positionJoueur);
		hmRobotPosition.put(robotIA, positionIA);
		
		hmRobotPos.put(robotJoueur, posJoueur);
		hmRobotPos.put(robotIA, posIA);
		
		hmRobotData.put(robotJoueur, firnen);
		hmRobotData.put(robotIA, glaedr);
		
		//init commande 
		Commande comIA = new Commande();
		
		hmRobotCom.put(robotJoueur, com);
		hmRobotCom.put(robotIA, comIA);

		
		//init log
		log  = new Log("MilsonRob_Rapport");
		
		//init fenetre
		frame = new JFrame("Controle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.setSize(1200, 800);

		//Partie telecommande;
		JPanel jtel = creationTelecommande();
		
		//Partie carte
		this.carte = carte;
		carte.setListeRobots(listeRobots);
		carte.setListeHopitaux(listeHopitaux);
		carte.setListeVictimes(listeVictimes);

		carteIm = new CarteImagee(carte);
		jcarte = createCarte(carteIm);
		gm = new GrapheMap(carte);
		
		
		//Partie console, parametrage
		JPanel jparam = createParam();
		
		ja = new JTextArea("--Initialisation--\n");
		
		JScrollPane jscroll = new JScrollPane(ja, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsplitParamList = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listeObjetMapAffichage(), jparam);
		jsplitParamList.setDividerLocation(300);
		jsplitParamList.setDividerSize(5);
		jsplitv = new JSplitPane(JSplitPane.VERTICAL_SPLIT,jscroll,jsplitParamList);
		jsplit2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsplitv, jcarte);
		
		jsplit1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jtel,jsplit2);
		
		jsplitv.setDividerSize(5);
		jsplitv.setDividerLocation(400);
		//jsplit1.setDividerLocation(200);
		jsplit1.setDividerSize(5);
		//jsplit2.setDividerLocation(300);
		jsplit2.setDividerSize(5);

		frame.add(jsplit1);
		
		//frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//panneau pour la liste des objet
		listeObjetMapAffichage();
		

	}
	
	//Lancement des IA, lorsque le bouton "Start" est appuyé
	private void lancement()  {
		
		boutonStartStop.setEnabled(false);
		
		message_action("Lancement partie!");
		message_action(listeVictimes+" "+listeHopitaux);
		System.out.println("nb victime: "+listeVictimes.size()+" nb hopitaux: "+listeHopitaux.size());
  	  	onGoing = true;
    		
  	  	IAbyControl iaControl;
    	String pos;
    	Commande com;
    	Elements position;
    		
    	ArrayList<IAbyControl> IArobots = new ArrayList<>();
    		
    	//Ajout dans le tableau IArobots et parametrage des robots piloté par l'IA
    	for(Robot r: listeRobots) {
    		if(r.getType() == TypeProtagoniste.robotIA) {
    				
    			if(hmRobotEtat.get(r) == "Connecté") {
	      			message_action(r.getIdentifier()+" est utiliser pour l'IA");
	      			position = hmRobotPosition.get(r);
	      			pos = hmRobotPos.get(r);
	      			com = hmRobotCom.get(r);
	      				
	      			iaControl = new IAbyControl(carte,position,pos,com,r);
	      			IArobots.add(iaControl);
    			}
    			else {
	      				message_action(r.getIdentifier()+" n'est pas utilié pour l'IA car il n'est pas connecté");
    			}
    		}
    	}
    	
  		Controleur control = new Controleur(carte,IArobots);

  	  	//Nouveau Thread qui va lancer le controleur d'IA
		new Thread(new Runnable() {
		      public void run() {

		      		//Controleur
		      		message_action("Lancement IA avec "+IArobots.size()+" robots controler par la machine");
		      		control.go();
		}} ).start();
		

		
		//Nouveau thread charger d'actualiser la carte chaque seconde jusuq'à la fin de l'IA
		new Thread(new Runnable() {
		     public void run() {
		    	 
		    	Commande com1;
		    	//Il faut laisser un délai le temps que les IA se lancent
		 		try{
					Thread.sleep(2000);
				}catch(InterruptedException ex){
					Thread.currentThread().interrupt();
				}
		 		
		  		while(onGoing) {
		  			onGoing = false;
		  		
		  			for(IAbyControl iabc : IArobots) {
		  				onGoing = onGoing || !control.aFini(iabc);
		  			}
			 		try{
						Thread.sleep(500);
					}catch(InterruptedException ex){
						Thread.currentThread().interrupt();
					}
		  			javax.swing.SwingUtilities.invokeLater(new Runnable() {
		  				public void run() {
		  					refresh();
		  				}
		  			} );
		  		}
		  		
		  		//On met à jour les nouvelles pos des IA 
		    	for(Robot r: listeRobots) {
		    		if(r.getType() == TypeProtagoniste.robotIA) {
		    				
		    			if(hmRobotEtat.get(r) == "Connecté") {
			      			com1 = hmRobotCom.get(r);

			      			hmRobotPosition.put(r, com1.getPosElemRobot() );
			      			hmRobotPos.put(r, com1.getPosRobot());				
		    			}		    	
		    		}
		    	}
		  		boutonStartStop.setEnabled(true);
		   }} ).start();
		    	 
		


	}
	
	//Creation de la barre de bouton en bas de l'interface
	private JPanel createParam() {
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		
		JPanel param = new JPanel();
		param.setLayout(fl);
		JButton bouton1 = new JButton("Add Victime");
		JButton bouton2 = new JButton("Add Hopital");
		JButton boutonAddRobot = new JButton("Add IA");
		boutonSimulation = new JButton("Simulation: "+simulation);


		boutonStartStop = new JButton("Start");
		
		bouton1.addActionListener(new ButtonAddEvent());
		bouton2.addActionListener(new ButtonAddEvent());
		boutonAddRobot.addActionListener(new ButtonAddEvent());
		boutonSimulation.addActionListener(new ButtonSimu());
		

		boutonStartStop.addActionListener(new ButtonBeginEvent());
		param.add(bouton1);
		param.add(bouton2);
		param.add(boutonAddRobot);
		param.add(boutonSimulation);
		param.add(boutonStartStop);
		
		

		return param;
	}
	
	private JPanel createCarte(CarteImagee carteIm) {

		layout = new GridLayout(carteIm.getAbscisses(),carteIm.getOrdonnees(),0,0);
		monPannel = new JPanel(layout) ;
		
		for(int i =0 ; i< carteIm.getAbscisses() ; ++i) {
			for(int j =0 ; j< carteIm.getOrdonnees() ; ++j) {
				ImageIcon im = carteIm.getImageIconAt(i, j);
				JLabel image = new JLabel (im);
				monPannel.add(image);
			}
		}
			
	
		return monPannel;
		
	}
	
	private JPanel creationTelecommande() {
		
		// Création, paramétrage et positionnement des composants
		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
	
		JPanel jmain = new JPanel(new GridLayout(4, 1));
		
		JButton buttonL = new JButton("l");
		JButton buttonS = new JButton("s");
		JButton buttonR = new JButton("r");
		JButton buttonV = new JButton("u");
		JButton buttonT = new JButton("t");
		JButton buttonD = new JButton("d");
		JButton buttonO = new JButton("o");
		
		buttonL.setBackground(Color.white);
		buttonS.setBackground(Color.white);
		buttonR.setBackground(Color.white);
		buttonV.setBackground(Color.white);
		buttonT.setBackground(Color.white);
		buttonD.setBackground(Color.white);
		buttonO.setBackground(Color.white);
	
	
		buttonL.addActionListener(new ButtonEvent());
		buttonS.addActionListener(new ButtonEvent());
		buttonR.addActionListener(new ButtonEvent());
		buttonV.addActionListener(new ButtonEvent());
		buttonT.addActionListener(new ButtonEvent());
		buttonD.addActionListener(new ButtonEvent());
		buttonO.addActionListener(new ButtonEvent());
	
	
		JLabel jl1 = new JLabel("Mouvement:");
		jl1.setForeground(Color.BLACK);
		JLabel jl2 = new JLabel("Action:");
		jl2.setForeground(Color.BLACK);
	
		
		Box line1=new Box(BoxLayout.X_AXIS);
		line1.add(Box.createHorizontalGlue());
		line1.add(buttonS);
		line1.add(Box.createHorizontalGlue());
		Box line2=new Box(BoxLayout.X_AXIS);
		line2.add(buttonL);
		line2.add(buttonO);
		line2.add(buttonR);
		Box line3=new Box(BoxLayout.X_AXIS);
		line3.add(Box.createHorizontalGlue());
		line3.add(buttonV);
		line3.add(Box.createHorizontalGlue());
	
		jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
		jp.add(line1);
		jp.add(line2);
		jp.add(line3);

		jp2.add(buttonD);
		jp2.add(buttonT);
		
		jmain.add(jl1);
		jmain.add(jp);
		jmain.add(jl2);
		jmain.add(jp2);
		
		return jmain;
	}
	
	/*Panneau qui liste les protagonistes avec les differentes actions possible 
	 * Modification de la position
	 * Suppression du protagoniste
	 * Parametrage du Robot
	 */
	public JScrollPane listeObjetMapAffichage() {

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

		JButton b0,b1,b2,bO;
		JLabel lab;
		Box line;
		String etat;
		
		for(Victime v : listeVictimes) {
			
			b1 = new JButton("Edit");

			b1.addActionListener(new ButtonListEvent(v));
			
			b2 = new JButton("Delete");
			
			b2.addActionListener(new ButtonListEvent(v));
			line = new Box(BoxLayout.X_AXIS);
			line.add(new JLabel("Victime en: "+v.getPosAbscisses()+","+v.getPosOrdonnes()));
			line.add(Box.createHorizontalGlue());
			line.add(b1);
			line.add(b2);

			pan.add(line);
		}
		
		for(Hopital h : listeHopitaux) {
			
			b1 = new JButton("Edit");
			b1.addActionListener(new ButtonListEvent(h));
		
			b2 = new JButton("Delete");
			b2.addActionListener(new ButtonListEvent(h));
			
			line = new Box(BoxLayout.X_AXIS);
			line.add(new JLabel("Hopital en: "+h.getPosAbscisses()+","+h.getPosOrdonnes()));
			line.add(Box.createHorizontalGlue());
			line.add(b1);
			line.add(b2);

			pan.add(line);
		}
		
		for(Robot r : listeRobots) {
			
			bO = new JButton("o");
			bO.addActionListener(new ButtonListEvent(r));
			
			b0 = new JButton("Robot");
			b0.addActionListener(new ButtonListEvent(r));
			b1 = new JButton("Edit");
			b1.addActionListener(new ButtonListEvent(r));
		
			b2 = new JButton("Delete");
			b2.addActionListener(new ButtonListEvent(r));
			
			line = new Box(BoxLayout.X_AXIS);
			line.add(new JLabel(r.getIdentifier()+" en: "+r.getPosAbscisses()+","+r.getPosOrdonnes()));
			line.add(Box.createHorizontalGlue());
			
			etat = hmRobotEtat.get(r);
			lab = new JLabel(etat+"    ");
			if(etat == "Déconnecté") {
				lab.setForeground(Color.RED);
			}
			else if (etat == "Connecté"){
				lab.setForeground(Color.GREEN);
			}
			else {
				lab.setForeground(Color.ORANGE);
			}
			
			line.add(lab);
			line.add(bO);
			line.add(b0);
			line.add(b1);
			line.add(b2);
			pan.add(line);
		}
		JScrollPane scroll = new JScrollPane(pan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		
		return scroll;
				
		
	}
	
	//Actualisation de la carte
	public void refresh() {
		carte.setListeRobots(listeRobots);
		carte.setListeVictimes(listeVictimes);
		carte.setListeHopitaux(listeHopitaux);


		CarteImagee carteIm2 = new CarteImagee(carte);
		//System.out.println("refresh");
		
		JPanel monPannel2 = new JPanel(layout) ;
		for(int i =0 ; i< carteIm2.getAbscisses() ; ++i) {
			for(int j =0 ; j< carteIm2.getOrdonnees() ; ++j) {
				ImageIcon im = carteIm2.getImageIconAt(i, j);
				JLabel image = new JLabel (im);
				monPannel2.add(image);
			}
		}
		
		JPanel jcarte2 = createCarte(carteIm2);
		jsplit2.setRightComponent(jcarte2);
		
		int divider = jsplitParamList.getDividerLocation();
		jsplitParamList.setLeftComponent(listeObjetMapAffichage());
		jsplitParamList.setDividerLocation(divider);
		
	}
	
	
	
	
	public void message_action(String s) {
		ja.append(s+"\n");
	}
	
	public void message_action_robot(String nom_robot, String s) {
		ja.append("<<"+nom_robot+">> "+s+"\n");
	}
	
	public void erase() {
		frame.remove(monPannel);
		frame.setVisible(true);
		frame.repaint();
	}
	
	/*Fonction qui sert à traiter les différents ajouts / modifications sur les protagonistes
	 * Prend en parametre le type de l'action ainsi que le protagoniste ciblé
	 */
	public void actionPopUp(String type, Protagoniste p) {
		
		Boolean passage ;
		String a = "";
		String b = "";
		Elements ele;
		String newpos = "u";
		int x = 0,y = 0;
		

		//Tant que l'utilisateur ne rentre pas des informations valides
		do {
			
			passage = false;
			
			String retour = JOptionPane.showInputDialog("Coordonnees: x,y");
			System.out.println("Retour:"+retour);
			
			if(retour == null) {
				return;
			}
			
			StringTokenizer st = new StringTokenizer(retour, ",");
			
			if(st.hasMoreTokens()) {
									
				a = st.nextToken();
				
				if(st.hasMoreTokens()) {
					b = st.nextToken();
					passage = true;
				}
			}
			
			if(passage) {
				
				x = Integer.parseInt(a);
				y = Integer.parseInt(b);
				
				//Si l'intervalle n'est pas valide
				if(  ! (x >= 0 && x < carte.getAbscisses() && y >= 0 && y < carte.getOrdonnees()) || carte.estVide(x, y) ) {
					System.out.println("Echec intervalle non valide");
					message_action("Position incorrect: position pas dans l'intervalle");
					passage = false;

				}
				
				//Si la case n'est pas déja occupé 
				else if(type != "Set Joueur" && type != "Set IA" && (carte.GetAt(x, y).isHospitalInThere() || carte.GetAt(x, y).isVictimInThere())) {
					System.out.println("Echec position deja occupe");
					message_action("Position incorrect:victime/hopital déja présent");
					passage = false;	
					
				}
				//Verifier la position
				else if(type == "Set IA" || type == "Set Joueur" || type == "Add IA") {
					//recuperer les positions possibles
					ele = carte.GetAt(x, y);
					HashMap<String, Elements> resultat = gm.getVoisins(ele);
					Set<String> res = resultat.keySet();
					
					
					String[] tab = new String[res.size()];
					int i = 0;
					for(String s:res) {
						switch(s) {
						
						case "s":
							tab[i] = "Haut";
							break;
						case "u":
							tab[i] = "Bas";

							break;
						case "l":
							tab[i] = "Gauche";

							break;
						case "r":
							tab[i] = "Droite";
							break;
						}
						i++;
					}
		
					int o = JOptionPane.showOptionDialog(frame, "Choisir la position", "test", JOptionPane.DEFAULT_OPTION, 
							JOptionPane.QUESTION_MESSAGE, null, tab, tab[0]);
					if(JOptionPane.CLOSED_OPTION == o ) {
						System.out.println("quitter sans choisir");
						passage =false;
					}
					else {
						switch(tab[o]) {
						
						case "Haut":
							newpos = "s";
							break;
						case "Bas":
							newpos = "u";

							break;
						case "Gauche":
							newpos = "l";

							break;
						case "Droite":
							newpos = "r";
							break;
						}
					}

				}
			}

		}
		
		while(!passage);
		
		System.out.println("Ok");
		System.out.println("Nouvelle pos: "+type+" "+a+" et "+b);

		switch(type) {
		case "Add Victime":
			Victime vic = new Victime(x, y);
			message_action("Ajout victime en "+x+","+y);
			listeVictimes.add(vic);
			break;
		case "Add Hopital":
			Hopital hop = new Hopital(x, y);
			message_action("Ajout hopital en "+x+","+y);
			listeHopitaux.add(hop);
			break;
		case "Add IA":
			Robot newRobot = new Robot(x, y, TypeProtagoniste.robotIA, "robot"+listeRobots.size());
			Commande comm = new Commande();
			RobotData rd = new RobotData("RobotIA"+listeRobots.size(), "Glaedr", "00:16:53:1C:15:FC");

			
			listeRobots.add(newRobot);
			hmRobotEtat.put(newRobot, "Déconnecté");
			hmRobotPos.put(newRobot, newpos);
			hmRobotPosition.put(newRobot, carte.GetAt(x, y));
			hmRobotCom.put(newRobot, comm);
			hmRobotData.put(newRobot, rd);
			break;
		case "Set Joueur":
			message_action("Modification de la pos du robot en "+x+","+y+" a "+newpos);
			p.setPosAbscisses(x);
			p.setPosOrdonnes(y);
			Robot r1 = (Robot) p;

			positionJoueur = carte.GetAt(x, y);
			posJoueur = newpos;
			
			com.setPos(posJoueur);
			com.setPositionCarte(positionJoueur);
			
			
			hmRobotPos.put(r1, posJoueur);
			hmRobotPosition.put(r1, positionJoueur);
			break;
		case "Set IA":
			message_action("Modification de la pos de l'ia en "+x+","+y+" a "+newpos);
			Robot r = (Robot) p;
			r.setPosAbscisses(x);
			r.setPosOrdonnes(y);
			
			positionIA= carte.GetAt(x, y);
			posIA = newpos;
			
			hmRobotPos.put(r, posIA);
			hmRobotPosition.put(r, positionIA);
			message_action("case ciblé: "+positionIA+" "+posIA);

			break;
		case "Modification":
			message_action("Modification de la pos du protagoniste en: "+p.getPosAbscisses()+","+p.getPosOrdonnes()+" en "+x+","+y);
			p.setPosAbscisses(x);
			p.setPosOrdonnes(y);

		
		}
		refresh();


	}
	
	//DialogPane pour parametrer le robot
	public void interfaceRobot(Robot r) {
		
		RobotData rd = hmRobotData.get(r);
		
		JTextField fieldControlleur = new JTextField(rd.getNomControlleur());
		JTextField fieldRobot = new JTextField(rd.getNomRobot());
		JTextField FieldfieldAdr = new JTextField(rd.getAdr());
		
		final JComponent[] composants = new JComponent[] {
				new JLabel("Nom controlleur:"),
				fieldControlleur,
		        new JLabel("Nom Robot (connexion):"),
		        fieldRobot,
		        new JLabel("Adresse (connexion):"),
		        FieldfieldAdr
		};
		
		int result = JOptionPane.showConfirmDialog(null, composants, "Parametrage Robot", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			message_action("Param Robot " +
		    		fieldControlleur.getText() + ", " +
		            fieldRobot.getText() + ", " +
		            FieldfieldAdr.getText());
			
			r.setIdentifier(fieldControlleur.getText());
			rd.setAdr(FieldfieldAdr.getText());
			rd.setNomControlleur(fieldControlleur.getText());
			rd.setNomRobot(fieldRobot.getText());
			
		} else {
		    System.out.println("Cancel result = " + result);
		}
		refresh();
	}
	
	
	
	public void gestionConnexionRobot(Robot r) {
		String etat = hmRobotEtat.get(r);
		Commande com = hmRobotCom.get(r);
		RobotData rd = hmRobotData.get(r);
		System.out.println("CHangement etat robot");
		Boolean echec  = false;
		
		if(etat != "Connecté") {
			hmRobotEtat.put(r, "Connexion en cours");
			System.out.println("Connexion en cours");
			refresh();
			
			try {
				com.on(rd.getNomRobot(), rd.getAdr(), rd.getNomControlleur() );
				com.setRobot(r);
				com.setCarte(carte);
				com.setPos(hmRobotPos.get(r));
				com.setPositionCarte(hmRobotPosition.get(r));
				
				if(r == robotJoueur) {
					
				}
			} catch (BTcommConnectException | NXTCommException | IOException | InterruptedException e) {
				echec = true;
				//e.printStackTrace();
			}
			if(echec) {
				hmRobotEtat.put(r, "Echec");
			}
			else {
				hmRobotEtat.put(r, "Connecté");
			}
		}
		else {
			try {
				com.off();
			} catch (BTcommConnectException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
			hmRobotEtat.put(r, "Déconnecté");

		}
		
		refresh();
	}
	
	public class ButtonSimu implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
				Set<Robot> h = hmRobotCom.keySet();
				Commande com;
				for(Robot r : h) {
					com  = hmRobotCom.get(r);
					
					if(hmRobotEtat.get(r) == "Connecté") {
						try {
							com.off();
						} catch (BTcommConnectException | IOException | InterruptedException e1) {
							e1.printStackTrace();
						}
						hmRobotEtat.put(r, "Déconnecté");
					}
					
					if(simulation){
						com.setSimulation(false);
					}
					else {
						com.setSimulation(true);

					}

				}
				
				if(simulation) {
					message_action("Arret Simulation");
					simulation = false;

				}
				else {
					message_action("Lancement Simulation");
					simulation = true;

				}
				boutonSimulation.setText("Simulation: "+simulation);
				refresh();

			
		}
	
	} 
	
	//Listener du bouton start 
	public class ButtonBeginEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			lancement();
		}
	
	} 
	
	//Listener pour la partie modification des protagoniste 
	//Action a effectuer/ afficher selon le type du protagoniste
	public class ButtonListEvent implements ActionListener{
		 
		 Protagoniste p;
		 
		 
		public ButtonListEvent(Protagoniste v) {
				p = v;
		}
			
		@Override
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			System.out.println("Vicitme associe: "+p+" "+p.getPosAbscisses()+","+p.getPosOrdonnes());
			
			if(action == "Delete") {
				if(p instanceof Victime) {
					listeVictimes.remove(p);
					carte.setListeVictimes(listeVictimes);
					refresh();
				}
				else if(p instanceof Hopital) {
					listeHopitaux.remove(p);
					carte.setListeHopitaux(listeHopitaux);
					refresh();
				}
				else if(p instanceof Robot) {
					listeRobots.remove(p);
					carte.setListeRobots(listeRobots);
					refresh();
				}
				else System.err.println("Delete: echec, ni hopital ni victime");
			}
			//Suivant le type du protagoniste, on parametre le choix pour la fonction actionPopUp
			else if(action == "Edit") {
				if(p instanceof Robot) {
					if(p.getType() == TypeProtagoniste.robotIA) {
						actionPopUp("Set IA", p);

					}
					else {
						actionPopUp("Set Joueur", p);

					}
				}
				else {
					actionPopUp("Modification", p);
				}
			}
			else if(action == "Robot" && p instanceof Robot) {
				interfaceRobot((Robot) p);
			}
			else if(action == "o" && p instanceof Robot) {
				gestionConnexionRobot((Robot) p);
			}
			else {
				System.err.println("ButtonListEvent: Action list Event non reconnu");
			}
		}
	}
	 

	//Action sur les boutons d'ajout de victime et d'hopitaux ainsi que pour la modification de la pos du robot
	public class ButtonAddEvent implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			String type = e.getActionCommand();
			actionPopUp(type,null);			
		}
		 
	 }
	 //Action sur la telecommande 
	 public class ButtonEvent implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				System.out.println(action);
				Elements suivant;
				
				
				try {
				
				// on verifie que le joueur est connecté sinon on ne fait rien
				if(hmRobotEtat.get(robotJoueur) != "Connecté") {
					message_action_robot("Joueur", "Impossible de se déplacer car il est pas connecté !");
				}
				
				// gestion du dépot des victimes
				else if(action == "d") {
					message_action_robot("Joueur", "Deposse une victime!");
					boolean deposer = false;
					
					for (Hopital h : listeHopitaux) {
						//vicE=carte.GetAt(v.getPosAbscisses(),v.getPosOrdonnes());
						if ( robotJoueur.getPosAbscisses() ==  h.getPosAbscisses() && robotJoueur.getPosOrdonnes() ==  h.getPosOrdonnes()) {
							com.deposer();
							deposer = true;
							break;
							
						}
						
					}
					if(!deposer) {
						message_action_robot("Joueur", "..... Mais il n'y a pas d'hopital !");

					}
				}
				//gestion du ramassage de victime
				else if(action == "t") {
					message_action_robot("Joueur", "Ramasse une victime!");
					boolean ramasser = false;
					for (Victime v : listeVictimes) {
						if ( robotJoueur.getPosAbscisses() ==  v.getPosAbscisses() && robotJoueur.getPosOrdonnes() ==  v.getPosOrdonnes()) {
							com.ramasser(v);
							ramasser = true;
							break;
						}
						
					}
					if(!ramasser) {
						message_action_robot("Joueur", "..... Mais il y a personne !");
					}
				}
				//Verification si le deplacement est possible sinon on ne fait rien
				else if(gm.deplacementPossibleWithPos(positionJoueur, action, posJoueur)) {
					
					suivant = gm.deplacementPos(positionJoueur, action, posJoueur);
					posJoueur=gm.newPosAfterDep(positionJoueur,suivant);
					positionJoueur=suivant;
					hmRobotPos.put(robotJoueur, posJoueur);
					hmRobotPosition.put(robotJoueur, positionJoueur);
				
					//suivant le bouton appuyer on se déplace
					switch (action) {
					case "s":
						
						log.ecrireDeplacement(robotJoueur, action);
						message_action_robot("Joueur", "Avance!");
						com.avancer();
						break;
					case "u":
						message_action_robot("Joueur", "Demi-tour!");
						com.demitour();
						break;
					case "r":
						message_action_robot("Joueur", "A droite!");
						com.droite();
						break;
					case "l":
						message_action_robot("Joueur", "A gauche!");
						com.gauche();
						break;
					}
	
					
					message_action_robot("Joueur","Ce deplace vers "+positionJoueur+":"+posJoueur+":"+robotJoueur.getPosAbscisses()+robotJoueur.getPosOrdonnes());

					refresh();
	
					}
				
				else if (action != "o"){
					message_action_robot("Joueur", "action impossible!");
					message_action_robot("Joueur", "possibilité:"+gm.getVoisinsWithPos(positionJoueur, posJoueur));
				}
				
				
				} catch(BTcommConnectException bt) { 
					bt.printStackTrace();
				}
				
				
			}
			
			
	}
	 
	 
	 public void affichage(Carte ci,Commande com) {
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
			 public void run() {create(com,ci); } });
	 }
	 
	 public static void main(String [] args){
		 
		 TestsCarte ts = new TestsCarte();
		 Carte map = ts.carteType4();
		 InterfacePrincipale ip = new InterfacePrincipale();
		 //ic.create();
		 Commande com = new Commande();
		 
		 ip.affichage(map, com);
		 
	

	 }
}

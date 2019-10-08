package comm;
/* imports exceptions */
import java.io.IOException;

/* imports pour la reception de donnees */
import java.io.InputStream;
import java.io.DataInputStream;

/* imports pour l'envoie de donnees */
import java.io.OutputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
/* import pour se connecter au robot par bluetooth */
import lejos.pc.comm.NXTInfo;

public class BTcommunication extends Thread{
	/* ----- Attributs ----- */
	private InputStream is = null;
	private OutputStream os = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private String nomRobot=new String();
	private String nomControleur = new String();
	private String adr = null;
	public boolean isWaiting = false;
	private NXTComm comm ;
	private boolean isConnected;

	/* ----- Methodes ----- */
	/* si pc est connecte avec robot */
	public boolean getEstConnect() {
		return isConnected;
	}

	/* nom du robot */
	public String getNomRobot() {
		return nomRobot;
	}
	
	/* nom de celui qui controle le robot */
	public String getNomControleur() {
		return nomControleur;
	}
	
	/* initialisation de la connexion */
	public BTcommunication(String nomR, String adr,String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException {
		super();
		this.nomRobot = nomR;
		this.nomControleur=nomC;
		this.adr=adr;
		initiate();
	}
	
	/* connexion au robot par bluetooth */
	public void initiate () throws NXTCommException, IOException, InterruptedException, BTcommConnectException {
		System.out.println(this.getName() + " : Connecting...");
		comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH,this.nomRobot, this.adr);
		isConnected = comm.open(nxtInfo);
		
		if (isConnected) {
			System.out.println(this.getName() + " : Connected");
		} else {
			System.out.println(this.getName() + " : Connection failed");
		}
		
		is = comm.getInputStream();
		os = comm.getOutputStream();
		dis = new DataInputStream(is);
		dos = new DataOutputStream(os);
		
		envoie(nomControleur);
		nomRobot=this.reception();

	}



	/* deconnexion au robot */
	public void deconnexion() throws BTcommConnectException, IOException, InterruptedException {
		Thread.sleep(2000);
		dis.close();
		dos.close();
		comm.close();
		System.out.println(this.getName() + " : End connection");
	}


	/* reception des donnees du robot */
	public String reception() throws BTcommConnectException {
		String mot = new String();
		if (getEstConnect()) {

			//ouverture flux
			InputStream recup=comm.getInputStream(); 
			DataInputStream drecup = new DataInputStream(recup); 

			char c;
			//recuperation du premier caractere pour initialiser mot
			try {

				mot=Character.toString(drecup.readChar());
				//recupere les donnees jusqu'a ce qu'il n'y ait plus rien a recuperer
				while ( recup.available() != 0 ) { 
					c = drecup.readChar();
					mot+=c; //remplissage du mot
				}

				//fermeture flux
				drecup.close();
				recup.close();

			} catch (IOException e) {
				System.err.println("BTcommunication.reception.");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}else{
			throw new BTcommConnectException(getEstConnect());
		}

		return mot;
	}

	/* envoie de donnees au robot */
	public void envoie(String message) throws BTcommConnectException {
		if (isConnected){
			
			//ouverture flux
			OutputStream envoie=comm.getOutputStream();
			DataOutputStream denvoie=new DataOutputStream(envoie);
			
			try {
				//envoie au robot 
				denvoie.writeUTF(message);

				//fermeture flux
				denvoie.close();
				envoie.close();

			} catch (IOException e) {
				System.err.println("BTcommunication.envoie.");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}else{
			throw new BTcommConnectException(isConnected);
		}
		
	}

}

package protagonistes;


/* Classe qui contient les parametres de connexion d'un robot
 * nomControlleur
 * nomRobot (pour la connexion)
 * adresse (pour la connexion)
 */
public class RobotData {
	
	String nomControlleur = "MilsonRob";
	String nomRobot = "Firnen"; 
	String adr = "00:16:53:16:2E:5B"; 
	
	public RobotData(String nC, String nR, String adresse) {
		nomControlleur = nC;
		nomRobot = nR;
		adr = adresse;
	}

	public String getNomControlleur() {
		return nomControlleur;
	}

	public void setNomControlleur(String nomControlleur) {
		this.nomControlleur = nomControlleur;
	}

	public String getNomRobot() {
		return nomRobot;
	}

	public void setNomRobot(String nomRobot) {
		this.nomRobot = nomRobot;
	}

	public String getAdr() {
		return adr;
	}

	public void setAdr(String adr) {
		this.adr = adr;
	}

	
}

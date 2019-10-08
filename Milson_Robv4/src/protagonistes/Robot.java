package protagonistes;

public class Robot extends Protagoniste {

	private boolean isTransportingVictim ;
	private Victime tabVictimes[] ;
	private int nbVictimesMax ;
	private int nbVictimesTransportees ;

	public Robot(int absc , int ord , TypeProtagoniste t) {
		super(absc , ord , t) ;
		if (t.toString() != "robotIA" && t != TypeProtagoniste.robotHumain) {
			System.err.println("Erreur : UN ROBOT N'EST NI UNE VICTIME NI UN HOPITAL ");
		}
		nbVictimesMax = 2 ;
		tabVictimes = new Victime[nbVictimesMax] ;
		nbVictimesTransportees = 0 ;
		isTransportingVictim = false ;
	}

	public Robot(int absc , int ord , TypeProtagoniste t ,String id) {
		super(absc , ord , t,id) ;
		if (t.toString() != "robotIA" && t != TypeProtagoniste.robotHumain) {
			System.err.println("Erreur : UN ROBOT N'EST NI UNE VICTIME NI UN HOPITAL ");
		}
		nbVictimesMax = 1 ;
		tabVictimes = new Victime[nbVictimesMax] ;
		nbVictimesTransportees = 0 ;
		isTransportingVictim = false ;
	}


	public void takeVictim(Victime v) {
		if (tabVictimes.length > nbVictimesTransportees) {
			tabVictimes[nbVictimesTransportees] = v ;
			nbVictimesTransportees ++ ;
			isTransportingVictim = true ;
			v.setInAmbulance(true);
		}
		else {
			System.err.println("Le Robot est plein , il ne peut transporter de victime supplémentaire");
		}
	}

	public void dropVictim() {
        for (int i = 0 ; i<nbVictimesTransportees ; ++ i) {
            tabVictimes[i].setInAmbulance(false);
            tabVictimes[i].setSafe(true);
        }
		tabVictimes = new Victime[nbVictimesMax] ;
		nbVictimesTransportees =0 ;
		isTransportingVictim = false ;
	}

	public boolean isTransportingVictim() {
		return isTransportingVictim;
	}


	public int getNbVictimesMax() {
		return nbVictimesMax;
	}



	public Victime[] getTabVictimes() {
		return tabVictimes;
	}

	public int getNbVictimesTransportees() {
		return nbVictimesTransportees;
	}

	public void setNbVictimesMax(int nbVictimesMax) {
		this.nbVictimesMax = nbVictimesMax;
		Victime tabAux[] = new Victime[nbVictimesMax];
		for (int i=0 ; i< nbVictimesTransportees ;++i) {
			tabAux[i] = tabVictimes[i] ;
		}
		tabVictimes = tabAux ;
	}

	public String toString() {
		return super.getPosAbscisses() + ","+super.getPosOrdonnes();
	}
	/*
	@Override
	public String toString() {
		String ret = this.getIdentifier() + "[isTransportingVictim=" + isTransportingVictim + ", listeVictimesTransportees= " ;
		if (nbVictimesTransportees > 0) {
			for (int i = 0 ; i< nbVictimesTransportees ;++i) {
				ret += tabVictimes[i].getIdentifier() + " ,";
			}
		}
		else {
			ret+= "[] ,";
		}
		ret +=  "  Type="+ this.getType() + "]";
		return ret ;
	}*/




}

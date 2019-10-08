package protagonistes;

public class Victime extends Protagoniste {

	private boolean isInAmbulance ;
	private boolean isSafe ;
	
	public Victime(int absc , int ord ) {
		super(absc , ord , TypeProtagoniste.victime) ;
		isSafe = false ;
		isInAmbulance = false ;
	}
	
	public Victime(int absc , int ord , String id ) {
		super(absc , ord , TypeProtagoniste.victime , id) ;
		isSafe = false ;
		isInAmbulance = false ;
	}

	public boolean isInAmbulance() {
		return isInAmbulance;
	}

	public void setInAmbulance(boolean isInAmbulance) {
		this.isInAmbulance = isInAmbulance;
	}

	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}


	@Override
	public String toString() {
		return this.getIdentifier() + " [isInAmbulance=" + isInAmbulance + ", isSafe=" + isSafe +  "]";
	}
	
	
}

package protagonistes;

public class Hopital extends Protagoniste{
	
	public Hopital(int absc , int ord) {
		super(absc,ord , TypeProtagoniste.hopital) ;
	}
	
	public Hopital(int absc , int ord , String id) {
		super(absc,ord , TypeProtagoniste.hopital ,id) ;
	}

	@Override
	public String toString() {
		return "Hopital " + this.getIdentifier() ;
	}
	
	
}

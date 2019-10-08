package protagonistes;

public abstract class Protagoniste {
	
	private int posAbscisses ;
	private int posOrdonnes ;
	private TypeProtagoniste type ;
	private String identifier ;
	
	public Protagoniste(int absc , int ord , TypeProtagoniste type) {
		posAbscisses = absc ;
		posOrdonnes = ord ;
		this.type = type ;
	}
	
	public Protagoniste(int absc , int ord , TypeProtagoniste type , String id) {
		posAbscisses = absc ;
		posOrdonnes = ord ;
		this.type = type ;
		identifier = id ;
	}
	
	
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public TypeProtagoniste getType() {
		return type;
	}
	public void setType(TypeProtagoniste type) {
		this.type = type;
	}
	
	public int getPosAbscisses() {
		return posAbscisses;
	}
	public void setPosAbscisses(int posAbscisses) {
		this.posAbscisses = posAbscisses;
	}
	
	public int getPosOrdonnes() {
		return posOrdonnes;
	}
	public void setPosOrdonnes(int posOrdonnes) {
		this.posOrdonnes = posOrdonnes;
	}
	
	
	
}

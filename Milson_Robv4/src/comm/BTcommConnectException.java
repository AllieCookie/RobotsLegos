package comm;

@SuppressWarnings("serial")
public class BTcommConnectException extends Exception{
	
	public BTcommConnectException(boolean val) {
		if (val)
			System.err.println("BTcommunication - Le robot est d�j� connect�.");
		else
			
			System.err.println("BTcommunication - Le robot n'est pas connect�.");
	}
	
}

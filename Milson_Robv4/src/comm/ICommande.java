package comm;

import java.io.IOException;

import lejos.pc.comm.NXTCommException;

public interface ICommande {
	abstract void on(String nomR,String adr,String nomC) throws BTcommConnectException, NXTCommException, IOException, InterruptedException;
	abstract void on(BTcommunication tr);
	abstract void off() throws BTcommConnectException, IOException, InterruptedException ;
	abstract void avancer() throws BTcommConnectException;
	abstract void gauche() throws BTcommConnectException;
	abstract void droite() throws BTcommConnectException;
	abstract void demitour() throws BTcommConnectException;
	abstract void ramasser() throws BTcommConnectException;
	abstract void deposer() throws BTcommConnectException;
}

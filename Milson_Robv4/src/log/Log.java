package log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import protagonistes.*;

public class Log {

		private PrintWriter writer ;
		private DateFormat dateFormat,dateFormat2 ;
		private int nbLignes ;
		
		public Log(String nomLog ) {
			dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			dateFormat2 = new SimpleDateFormat("yyyy-mm-dd");
			//dateFormat2.format(Calendar.getInstance().getTime()) +
			String path = "./logs/"+ nomLog +  ".txt" ;
			nbLignes = 0 ;
			try {
				writer = new PrintWriter(path) ;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur dans la création du fichier de Log");
				e.printStackTrace();
			}
		}
		
		public void ecrireDeplacement(Robot r, String s) {
			Date date = Calendar.getInstance().getTime();
			String buffer = dateFormat.format(date);
			buffer = buffer + "."+ r.getIdentifier() +"."+ r.getPosAbscisses() + "." + r.getPosOrdonnes() + ".mouv= "+s;
			writer.println(buffer);
			nbLignes ++ ;
		}
		
		public void prendreVictime(Robot r) {
			Date date = Calendar.getInstance().getTime();
			String buffer = dateFormat.format(date) ;
			buffer = buffer + "."+ r.getIdentifier() +"."+ r.getPosAbscisses() + "." + r.getPosOrdonnes() + ".prendre";
			writer.println(buffer);
			nbLignes ++ ;
		}
		
		public void deposerVictime(Robot r ) {
			Date date = Calendar.getInstance().getTime();
			String buffer = dateFormat.format(date) ;
			buffer = buffer + "."+ r.getIdentifier() +"."+ r.getPosAbscisses() + "." + r.getPosOrdonnes() + ".deposer";
			writer.println(buffer);
			nbLignes ++ ;
		}
		
		public void closeWriter() {
			writer.println("Nombre de Lignes du Log : " + nbLignes);
			writer.close();
		}
		
}

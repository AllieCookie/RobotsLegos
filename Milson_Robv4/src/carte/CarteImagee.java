package carte;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;

import protagonistes.Robot;

public class CarteImagee {
	
	int absc ;
	int ord ;
	ImageIcon tabImages[][] ;
	Carte carte; // ajout du 9 mai
	
	public CarteImagee(Carte carte) {
		this.carte = carte;
		absc = carte.getAbscisses();
		ord = carte.getOrdonnees() ;
		tabImages = new ImageIcon[absc][ord] ;
		for(int i =0 ; i<absc ; ++i) {
			for(int j =0 ; j<ord ; ++j) {
				Elements e = carte.GetAt(i, j) ;
				tabImages[i][j] = createImageIconFromElement(e);
				
			}
		}
	}
	
	private ImageIcon createImageIconFromElement (Elements e) {
		ImageIcon rendu ;
		ImageIcon route ; //Permet d'initialiser le fond de la carte
		List<Robot> lr;
		
		switch (e.getType().toString()) {
			case "CroisementGDH" : route = new ImageIcon("./imagesCases/GDH.png"); ;
			break ;
			case "CroisementDHB" : route = new ImageIcon("./imagesCases/DHB.png"); ;
			break ;
			case "CroisementGDB" : route = new ImageIcon("./imagesCases/GDB.png"); ;
			break ;
			case "CroisementGHB" : route = new ImageIcon("./imagesCases/GHB.png"); ;
			break ;
			case "VirageBD" : route = new ImageIcon("./imagesCases/BD.png"); ;
			break ;
			case "VirageBG" : route = new ImageIcon("./imagesCases/BG.png"); ;
			break ;
			case "VirageHD" : route = new ImageIcon("./imagesCases/HD.png"); ;
			break ;
			case "VirageHG" : route = new ImageIcon("./imagesCases/HG.png"); ;
			break ;
			case "RouteDG" : route = new ImageIcon("./imagesCases/DG.png"); ;
			break ;
			case "RouteHB" : route = new ImageIcon("./imagesCases/HB.png"); ;
			break ;
			default: route = new ImageIcon("./imagesCases/VIDE.png"); ;
			break ;
		}
		rendu = route ;
		
		if (e.isHospitalInThere()) {
			rendu = concat(rendu, new ImageIcon("./imagesProtagonistes/Hopital.png")) ;
		}
		
		
		if(e.isVictimInThere()) {
			rendu = concat(rendu , new ImageIcon("./imagesProtagonistes/PLS.png"));
		}
		
		if (e.isRobotInThere()) {
			lr = carte.getListeRobots(); // 9 mai
			rendu = concat(rendu, new ImageIcon("./imagesProtagonistes/Milson.png"));
		}
		
		return rendu ;
	}
	
	private ImageIcon concat(ImageIcon img1 , ImageIcon img2 ) {
		BufferedImage img3 = new BufferedImage( img1.getIconWidth(), img1.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g1 = img3.createGraphics();
		// paint the Icon to the BufferedImage.
		img1.paintIcon(null, g1, 0,0);
		g1.dispose();
	BufferedImage img4 = new BufferedImage( img2.getIconWidth(), img2.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		g1 = img4.createGraphics();
		// paint the Icon to the BufferedImage.
		img2.paintIcon(null, g1, 0,0);
		g1.dispose();
		
		int w = Math.max(img1.getIconWidth(), img2.getIconWidth()) ;
		int h = Math.max(img1.getIconHeight(), img2.getIconHeight());
		BufferedImage img = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics() ;
	
		g.drawImage(img3,0,0, null);
		g.drawImage(img4, (img3.getWidth()/2-(img4.getWidth()/2)), (img3.getHeight()/2 - (img4.getHeight()/2)), null);
		g.dispose();
		
		return new ImageIcon(img) ;
	}
	
	public ImageIcon getImageIconAt (int absc , int ord) {
		return tabImages[absc][ord] ;
	}
	
	public int getAbscisses() {
		return absc;
	}


	public int getOrdonnees() {
		return ord;
	}
	
	/*switch (e.getType().toString()) {
	case "CroisementGDH" : tabImages[i][j] = new ImageIcon("./imagesCases/GDH.png"); ;
	break ;
	case "CroisementDHB" : tabImages[i][j] = new ImageIcon("./imagesCases/DHB.png"); ;
	break ;
	case "CroisementGDB" : tabImages[i][j] = new ImageIcon("./imagesCases/GDB.png"); ;
	break ;
	case "CroisementGHB" : tabImages[i][j] = new ImageIcon("./imagesCases/GHB.png"); ;
	break ;
	case "VirageBD" : tabImages[i][j] = new ImageIcon("./imagesCases/BD.png"); ;
	break ;
	case "VirageBG" : tabImages[i][j] = new ImageIcon("./imagesCases/BG.png"); ;
	break ;
	case "VirageHD" : tabImages[i][j] = new ImageIcon("./imagesCases/HD.png"); ;
	break ;
	case "VirageHG" : tabImages[i][j] = new ImageIcon("./imagesCases/HG.png"); ;
	break ;
	case "RouteDG" : tabImages[i][j] = new ImageIcon("./imagesCases/DG.png"); ;
	break ;
	case "RouteHB" : tabImages[i][j] = new ImageIcon("./imagesCases/HB.png"); ;
	break ;
}*/
	
}

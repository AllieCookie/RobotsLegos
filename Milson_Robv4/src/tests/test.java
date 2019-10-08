package tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class test extends JFrame implements ActionListener {
	  public test() {
	    super("Freeze");
	    JButton freezer = new JButton("Freeze");
	    freezer.addActionListener(this);
	    add(freezer);
	    pack();
	  }

	  public void actionPerformed(ActionEvent e) {
	    try {
	      Thread.sleep(4000);
	    } catch (InterruptedException e1) {
	    }
	  }

	  public static void main(String... args) {
	    test edt = new test();
	    edt.setVisible(true);
	  }
	}
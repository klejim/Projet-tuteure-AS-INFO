/**
 * 
 */
package project.view;


import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.network.*;

/**
 * @author yoann
 * 
 * Classe créant la fenêtre de statut de réseau
 *
 */
public class StatusWindow extends JFrame {
	
	private int nbSubStations;
	
		
	/**
	 * Constructeur par défaut
	 */
	public StatusWindow () {
		super();
		
		this.nbSubStations = 1;
		build();
	}
	
	/**
	 * Initialisation de la JFrame StatusWindow
	 */
	private void build() {
		
		// init fenêtre
		this.setTitle("Statut Réseau");
		this.setSize(500,600);
		this.setLocationRelativeTo(null); 
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(initUI());
		this.validate();
		this.repaint();		
	}
	
	
	
	
	/**
	 * JPanel principal
	 * @return
	 */	
	private JPanel initUI() {
		
		JPanel panel = new JPanel();
		
		// 2 columns and as many rows as requested
		panel.setLayout(new GridLayout(0,2));
		
        
		//One static label
//        JLabel station1 = new JLabel();
//        station1.setText("Station 1 is great");
//        
//        		
//        panel.add(station1);
//        panel.add(this.labelText);
//        
//        panel.add(new JLabel("station 3"));
//        panel.add(new JLabel("station 4"));      
//        panel.add(new JLabel("station 5"));
		return panel;
	}
	
	/**
	 * Mise à jour de la fenêtre sur la constitution du réseau
	 * @param ntw réseau à afficher
	 */
	public void updateUI(Network ntw) {
		this.nbSubStations = ntw.count(SubStation.class)[0];
		System.out.println("Nombre de sous-sations : " + this.nbSubStations);

		
		ArrayList<Node> nodes = ntw.getNodes();
		
		
		
		
		// mise à jour de la fenêtre
		this.validate();
		this.repaint();
	}
	
	
	/**
	 * Main de test pour StatusWindows
	 * @param args aucun parametre nécessaire
	 */
	public static void main(String[] args) {
		StatusWindow myWindow = new StatusWindow();
		myWindow.setVisible(true);	
		
		Scanner sc = new Scanner(System.in);
		
		 Network myNetwork = new Network(0,0,0);
		 
		 //myWindow.updateUI(myNetwork);
		
		while(true) {
			myWindow.getLabelText().setText(sc.next());
			System.out.println("OK");
		}
	}

}

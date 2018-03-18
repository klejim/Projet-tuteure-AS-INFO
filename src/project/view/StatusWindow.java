/**
 * 
 */
package project.view;


import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author yoann
 *
 */
public class StatusWindow extends JFrame {
	
	
	private JLabel labelText;
	
	

	/**
	 * Constructeur par défaut
	 */
	public StatusWindow () {
		super();
		this.labelText = new JLabel("Station 2");
		build();
	}
	
	/**
	 * Initialisation de la JFrame StatusWindow
	 */
	private void build() {
		this.setTitle("Statut Réseau");
		this.setSize(400,600);
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
        JLabel station1 = new JLabel();
        station1.setText("Station 1 is great");
        
       
		
        panel.add(station1);
        panel.add(this.labelText);
        
        panel.add(new JLabel("station 3"));
        panel.add(new JLabel("station 4"));      
        panel.add(new JLabel("station 5"));
		return panel;
	}
	
	/**
	 * @return the labelText
	 */
	public JLabel getLabelText() {
		return labelText;
	}

	/**
	 * @param labelText the labelText to set
	 */
	public void setLabelText(JLabel labelText) {
		
		this.labelText = labelText;
		
	}

	/**
	 * Main de test pour StatusWindows
	 * @param args aucun parametre nécessaire
	 */
	public static void main(String[] args) {
		StatusWindow myWindow = new StatusWindow();
		myWindow.setVisible(true);	
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			myWindow.getLabelText().setText(sc.next());
			System.out.println("OK");
		}
	}

}

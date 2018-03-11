/**
 * 
 */
package project.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author yoann
 *
 */
public class StatusWindow extends JFrame {
	
	public StatusWindow () {
		super();
		build();
	}
	
	/**
	 * Create and initialise the StatusWindow JFrame 
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
	 * Create window content
	 * @return
	 */	
	private JPanel initUI() {
		
		// 2 columns and as many rows as requested
		JPanel panel = new JPanel();
		
		//One static label		
		panel.setLayout(new GridLayout(0,2));
              
        JLabel station1 = new JLabel("Station 1");
        station1.setText("Station 1 is great");
		
        panel.add(station1);
        panel.add(new JLabel("station 2"));
        panel.add(new JLabel("station 3"));
        panel.add(new JLabel("station 4"));        
		return panel;
	}

	/**
	 * Test main function for statusWindow
	 * @param args
	 */
	public static void main(String[] args) {
		StatusWindow myWindow = new StatusWindow();
		myWindow.setVisible(true);				
	}

}

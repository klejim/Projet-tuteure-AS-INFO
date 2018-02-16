/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatriceswing;

import calculatrice.Calculatrice;
import calculatrice.Calculatrice.Operation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CalculatriceSwing extends JFrame{
    private JLabel resultLabel, opLabel;
    private final Calculatrice calc;
    private boolean newNumber;
    /**
     * Classes implémentant AbstractAction
     */
    abstract class CalcAction extends AbstractAction{
        CalculatriceSwing frame;
        CalcAction(CalculatriceSwing calc, String text){
            super(text);
            this.frame = calc;
        }
    }
    class EqualAction extends CalcAction{
        public EqualAction(CalculatriceSwing calc,String text){
            super(calc,text);
        }
        @Override
        public void actionPerformed(ActionEvent event){
            frame.setRLText(String.valueOf(calc.doEquals()));
            frame.setOpLText("");
            newNumber = true;
        }
    }
    public class OpAction extends CalcAction{
        Operation op;
        public OpAction(CalculatriceSwing calc,String text,Operation op){
            super(calc,text);
            this.op = op;
        }
        @Override
        public void actionPerformed(ActionEvent event){
             JButton btn = (JButton)event.getSource();
             JLabel resultText = frame.getResultLabel();
             String str = resultText.getText() + " " + btn.getText() + " ";
             frame.setOpLText(frame.getOpLabel().getText()+str);
             frame.setRLText(String.valueOf(calc.doOperation(op)));
             newNumber = true;
        }
    }
    class PointAction extends CalcAction{
        public PointAction(CalculatriceSwing calc,String text){
            super(calc,text);
        }
        @Override
        public void actionPerformed(ActionEvent e){
            JLabel label = this.frame.getResultLabel();
            frame.setRLText(label.getText() + ".");
        }
    }
    class NumberAction extends CalcAction{
        public NumberAction(CalculatriceSwing calc,String text){
            super(calc,text);
        }
        @Override
        public void actionPerformed(ActionEvent event){
            System.out.println(Thread.currentThread().getName());
            JButton btn = (JButton)event.getSource();
            JLabel resultText = frame.getResultLabel();
            if (frame.newNumber) {
                frame.setRLText(btn.getText());
                frame.newNumber = false;
                System.out.println("youhou");
            }
            else {
                frame.setRLText(resultText.getText() + btn.getText());
            }
            frame.calc.feedNumber(Double.parseDouble(resultText.getText()));
        }
    }
    /**
     * Méthodes de CalculatriceSwing
     */
    public CalculatriceSwing(){
		super();
        calc = new Calculatrice();
        newNumber = true;
		build();
        initUI();
    }
    
    public JLabel getResultLabel(){
        return resultLabel;
    }
    
    public void setRLText(String text){
        resultLabel.setText(text);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        this.getContentPane().add(resultLabel,c);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }
    
    public void setOpLText(String text){
        opLabel.setText(text);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        this.getContentPane().add(opLabel,c);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }
    
    public JLabel getOpLabel(){
        return opLabel;
    }
    
    private void addJButtonToPane(JButton btn, int x, int y,JPanel panel){
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = y;
        c.gridx = x;
        c.ipadx = 5;
        c.ipady = 5;
        panel.add(btn,c);
    }
    
    private JPanel initUI(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        opLabel = new JLabel("");
        opLabel.setMinimumSize(new Dimension(95*4,30));
        panel.add(opLabel,c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        resultLabel = new JLabel("");
        resultLabel.setMinimumSize(new Dimension(95*4,60));
        panel.add(resultLabel,c);
        
        for(int i=0;i<3;i++){
            c = new GridBagConstraints();
            c.gridy = 2;
            c.gridx = i;
            JLabel label = new JLabel("");
            label.setMinimumSize(new Dimension(95,60));
            panel.add(label,c);
        }
        int x = 0, y = 5;
        for(int i=0;i<9;i++){
            JButton btn = new JButton(new NumberAction(this,String.valueOf(i+1)));
            btn.setMinimumSize(new Dimension(95,60));
            addJButtonToPane(btn,x,y,panel);
            x += 1;
            if (x == 3){
                x = 0;
                y -= 1;
            }
        }
        Operation ops[] = {Operation.DIVIDE,Operation.MULTIPLY,Operation.SUBSTRACT,Operation.ADD,Operation.EQUALS};
        for (int i=0;i<5;i++){
            Operation op = ops[i];
            String opString = null;
            switch(op){
                case ADD: opString = "+";break;
                case SUBSTRACT: opString = "-";break;
                case MULTIPLY: opString = "*";break;
                case DIVIDE: opString = "/";break;
                case EQUALS: opString = "=";break;
            }
            JButton btnOp;
            if (op.equals(Operation.EQUALS)){
                btnOp = new JButton(new EqualAction(this,opString));
            }
            else{
                btnOp = new JButton(new OpAction(this,opString,op));
            }
            btnOp.setMinimumSize(new Dimension(95,60));
            addJButtonToPane(btnOp,3,2+i,panel);
        }
        JButton btn0 = new JButton(new NumberAction(this,"0"));
        btn0.setMinimumSize(new Dimension(95,60));
        addJButtonToPane(btn0,1,6,panel);
        JButton btnPt = new JButton(new PointAction(this,"."));
        btnPt.setMinimumSize(new Dimension(95,60));
        addJButtonToPane(btnPt,2,6,panel);
        return panel;
    }
 
    private void build(){
		this.setTitle("Calculatrice");
		this.setSize(400,600);
		this.setLocationRelativeTo(null); 
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(initUI());
		this.validate();
		this.repaint();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       SwingUtilities.invokeLater(()->{
           CalculatriceSwing calc = new CalculatriceSwing();
           calc.setVisible(true);
       });
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatrice;

import java.util.Scanner;

/**
 *d
 * @author Jimenez
 */
public class Calculatrice {
    public enum Operation{ADD,SUBSTRACT,MULTIPLY,DIVIDE,EQUALS};
    private enum State{LEFT_OP,RIGHT_OP};
    private Double leftOperand, rightOperand;
    private Operation bufferedOp;
    
    public Calculatrice(){
        leftOperand = null;
        rightOperand = 0.0;
        bufferedOp = null;
    }
    
    public double feedNumber(double n){
        // ajoute n à l'opérande de droite s'il existe et le crée avec une valeur de n sinon
        if (rightOperand == null){rightOperand = n;}
        else{rightOperand = n;}
        return rightOperand;
    }
    
    public double doOperation(Operation op){
        // si l'opérande de droite est null, on se contente d'affecter la valeur op à bufferedOp
        /* si l'opérande de gauche existe et si bufferedOp est null, exécute op entre lui et l'opérande de droite, puis copie le résultat dans leftOperand
        // si l'opérande de gauche existe et que bufferedOp n'est pas null, exécute bufferedOp entre les deux opérandes puis copie le résultat dans leftOperand et
        // assigne la valeur op à bufferedOp
        // sinon, copie la valeur de rightOperand dans leftOperand et assigne la valeur op à bufferedOp
        */
        if (rightOperand == null){
            bufferedOp = op;
        }
        else if (leftOperand != null){
            Operation tmp = (bufferedOp==null)?op:bufferedOp;
            double result = 0;
            switch(tmp){
                case ADD:
                    result = leftOperand + rightOperand;
                    break;
                case SUBSTRACT:
                    result = leftOperand - rightOperand;
                    break;
                case MULTIPLY:
                    result = leftOperand * rightOperand;
                    break;
                case DIVIDE:
                    result = leftOperand / rightOperand;
                    break;
            }
            leftOperand = result;
            rightOperand = 0.0;
            bufferedOp = (bufferedOp==null)?null:op;
        }
        else{
            leftOperand = rightOperand;
            rightOperand = 0.0;
            bufferedOp = op;
        }
        return leftOperand;
    }
    
    public double doEquals(){
        double result = 0;
        if (bufferedOp != null && leftOperand != null){
            result = doOperation(bufferedOp);
            rightOperand = result;
            leftOperand = null;
            bufferedOp = null;
        }
        return result;
    }
    
    public static void main(String args[]){
        Calculatrice calc = new Calculatrice();
        Scanner scan = new Scanner(System.in);
        String line = "";
        while (!line.equals("quit")){
            System.out.print(">>>");
            line = scan.nextLine();
            if (line.equals("+")){
                System.out.println(calc.doOperation(Operation.ADD));
            }
            else if (line.equals("-")){
                System.out.println(calc.doOperation(Operation.SUBSTRACT));
            }
            else if (line.equals("/")){
                System.out.println(calc.doOperation(Operation.DIVIDE));
            }
            else if (line.equals("*")){
                System.out.println(calc.doOperation(Operation.MULTIPLY));
            }
            else if (line.equals("=")){
                System.out.println(calc.doEquals());
            }
            else if (!line.equals("quit")){
                System.out.println(calc.feedNumber(Integer.parseInt(line)));
            }
            
        }
    }
}

package calculatricefx;

import calculatrice.Calculatrice;
import calculatrice.Calculatrice.Operation;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class CalculatriceFX extends Application {
    private Calculatrice calc;
    private Label opText,resultText;
    boolean newNumber;
    private Stage stage;
    /** 
     * appelée avant start, utile pour ... initialiser, oui. Elle s'exécute dans le thread principal.
     * Important : le code de la partie graphique sera exécuté dans un thread spécifique à JavaFX. Il est interdit de créer des objets Stage
     * ou Scene dans un autre thread que celui de JavaFX (et donc dans cette méthode). Créer d'autres objets est autorisé. 
    */
    @Override
    public void init(){
        calc = new Calculatrice();
        newNumber = true;
    }
    // appelée après init, elle s'exécute dans le thread de JavaFX.
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        initUI();
    }
    
    public void initUI(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        opText = new Label("");
        opText.setMinHeight(30);
        opText.getStyleClass().add("label-small"); // classe utilisée dans le .css
        resultText = new Label("");
        resultText.setMinHeight(60);
        resultText.getStyleClass().add("label-big");
        grid.add(opText, 0, 0, 4, 1);
        grid.add(resultText, 0, 1, 4, 1);
        // création d'une ligne vide entre les label et les boutons
        for (int i=0;i<3;i++){
            grid.add(new Text(""), i, 2);
        }
        // handler pour les évènements de clic sur un bouton correspondant à un chiffre
        EventHandler<ActionEvent> handlerN = (ActionEvent event) -> {
			Button btn1 = (Button)event.getSource();
			if (newNumber){
                resultText.setText(btn1.getText());
                newNumber = false;
			}
			else {
                resultText.setText(resultText.getText() + btn1.getText());
            }
			calc.feedNumber(Double.parseDouble(resultText.getText()));
        };
        // création des boutons de 1 à 9
        int x = 0, y = 5;
        for (int i=1;i<10;i++){
            Button btn = new Button();
            btn.setText(String.valueOf(i));
            btn.getStyleClass().add("button-number");
            btn.setMinSize(95, 60);
            btn.setOnAction(handlerN);
            grid.add(btn, x, y);
            x += 1;
            if (x == 3){
                x = 0;
                y -= 1;
            }
        }
        // création du bouton "0" et de celui pour le séparateur décimal
        Button btn0 = new Button("0");
        btn0.getStyleClass().add("button-number");
        btn0.setOnAction(handlerN);
        btn0.setMinSize(95,60);
        grid.add(btn0, 1, 6);
        Button btnPt = new Button(".");
        btnPt.getStyleClass().add("button-number");
        btnPt.setOnAction((ActionEvent event)->{
            resultText.setText(resultText.getText() + ".");
        });
        btnPt.setMinSize(95,60);
        grid.add(btnPt,2,6);
        // création des boutons pour les différentes opérations
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
            Button btnOp = new Button(opString);
            btnOp.getStyleClass().add("button-op");
            EventHandler<ActionEvent> handler;
            if (op == Operation.EQUALS){
                handler = (ActionEvent event)->{
                    resultText.setText(String.valueOf(calc.doEquals()));
                    opText.setText("");
                    newNumber = true;
                };
            }
            else {
                handler = (ActionEvent event)->{
                    Button btn = (Button)event.getSource();
                    String str = resultText.getText() + " " + btn.getText() + " ";
                    opText.setText(opText.getText()+str);
                    resultText.setText(String.valueOf(calc.doOperation(op)));
                    newNumber = true;
                };
            }
            btnOp.setOnAction(handler);
            btnOp.setMinSize(95,60);
            grid.add(btnOp,3,2+i);
        }
        Scene scene = new Scene(grid);
        scene.getStylesheets().add(CalculatriceFX.class.getResource("styles.css").toExternalForm());
        stage.setTitle("Calculatrice");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
        
    }
    public static void main(String[] args) {
        launch(args);
    }
}

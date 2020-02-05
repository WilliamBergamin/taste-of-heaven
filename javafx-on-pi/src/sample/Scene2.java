package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static sample.Constants.*;

public class Scene2 {

    private Stage primaryStage;
    private String machineToken;
    private StringBuffer orderToken = new StringBuffer();

    public Scene2(String machineToken){
        this.machineToken=machineToken;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        ProgressIndicator progressIndicator = new ProgressIndicator();

        Button btn = new Button();
        btn.setText("Cancel");
        btn.setStyle(BUTTONSTYLE);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backScene();
            }
        });

        vb.getChildren().add(new Label("Scan bar code!"));
        vb.getChildren().add(progressIndicator);
        vb.getChildren().add(btn);

        Scene scene2 = new Scene(vb, WIDTH, HEIGHT);

        scene2.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                orderToken.append(event.getCharacter());
                if (orderToken.length() == ORDERKEYLENGHT){
                    System.out.println(orderToken.toString());
                    nextScene(orderToken.toString());
                }
            }
        });
        primaryStage.setScene(scene2);

    }

    private void backScene(){
        Scene1 scene1 = new Scene1(this.machineToken);
        scene1.getScene(primaryStage);
    }

    private void nextScene(String orderToken){
        Scene3 scene3 = new Scene3(this.machineToken, orderToken);
        scene3.getScene(primaryStage);
    }
}

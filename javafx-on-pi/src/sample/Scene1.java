package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static sample.Constants.*;

public class Scene1 {

    private Stage primaryStage;
    private Machine machine;

    public Scene1(Machine machine){
        this.machine = machine;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setText("Get Your Drink");
        btn.setStyle(BUTTONSTYLE);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextScene();
            }
        });

        vb.getChildren().add(btn);

        Scene scene1 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene1);
    }

    private void nextScene(){
        Scene2 scene2 = new Scene2(this.machine);
        scene2.getScene(primaryStage);
    }
}

package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Scene2 {

    private Stage primaryStage;

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setText("Get Your Drink");
        btn.setStyle("-fx-background-color: #7e7e7e;\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-text-fill: white;");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextScene();
            }
        });

        vb.getChildren().add(btn);

        Scene scene1 = new Scene(vb,900,400);

//        return scene1;
        primaryStage.setTitle("Get Your Drink");
        primaryStage.setScene(scene1);
    }

    private void nextScene(){
        Scene3 scene3 = new Scene3();
        scene3.getScene(primaryStage);
    }
}

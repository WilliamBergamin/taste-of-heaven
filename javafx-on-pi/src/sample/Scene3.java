package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static sample.Constants.*;

public class Scene3 {

    private Stage primaryStage;
    private String machineToken;

    public Scene3(String machineToken){
        this.machineToken=machineToken;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        ProgressIndicator progressIndicator = new ProgressIndicator();

        Button failedBtn = new Button(); // temp button
        failedBtn.setText("failed"); // temp button
        failedBtn.setOnAction(new EventHandler<ActionEvent>() { // temp button
            @Override // temp button
            public void handle(ActionEvent event) { // temp button
                failedScene(); // temp button
            } // temp button
        }); // temp button

        Button passedbutton = new Button(); // temp button
        passedbutton.setText("pass"); // temp button
        passedbutton.setOnAction(new EventHandler<ActionEvent>() { // temp button
            @Override // temp button
            public void handle(ActionEvent event) { // temp button
                nextScene(); // temp button
            } // temp button
        }); // temp button

        vb.getChildren().add(new Label("Processing!"));
        vb.getChildren().add(progressIndicator);
        vb.getChildren().add(failedBtn);
        vb.getChildren().add(passedbutton);

        Scene scene3 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene3);
    }

    private void failedScene(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Order Failed");
        alert.show();
        Scene1 scene1 = new Scene1(this.machineToken);
        scene1.getScene(primaryStage);
    }

    private void nextScene(){
        Scene1 scene1 = new Scene1(this.machineToken);
        scene1.getScene(primaryStage);
    }
}

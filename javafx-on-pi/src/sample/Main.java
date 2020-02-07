package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import org.json.JSONObject;

import static sample.Constants.*;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        final TextField machineTokenTextField = new TextField();
        machineTokenTextField.setMaxWidth(200);
        final TextField eventTokenTextField = new TextField();
        eventTokenTextField.setMaxWidth(200);

        Button btn = new Button();
        btn.setText("enter");
        btn.setStyle(BUTTONSTYLE);
        btn.setPadding(new Insets(10, 15, 10, 15));

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String machineToken = machineTokenTextField.getText();
                String eventKey = eventTokenTextField.getText();
                if (!machineToken.isEmpty() && !eventKey.isEmpty()) {
                    ServerHelper helper = new ServerHelper();
                    JSONObject response = helper.addMachineToEvent(machineToken, eventKey);
                    if (response == null){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid credentials!");
                        alert.show();
                    }else {
                        nextScenes(new Machine(machineToken, eventKey));
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("We Need More Info!");
                    alert.show();
                }
            }
        });

        Label label1 = new Label("Machine Token");
        label1.setStyle(LABELSTYLE);

        Label label2 = new Label("Event Key");
        label2.setStyle(LABELSTYLE);

        vb.getChildren().add(label1);
        vb.getChildren().add(machineTokenTextField);
        vb.getChildren().add(label2);
        vb.getChildren().add(eventTokenTextField);
        vb.getChildren().add(btn);

        primaryStage.setTitle("taste-of-heaven");
        primaryStage.setScene(new Scene(vb, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void testScenes(Machine machine){
        // TODO register machine to event before moving on
        Scene4 scene4 = new Scene4(machine);
        scene4.getScene(primaryStage);
    }

    private void nextScenes(Machine machine){
        // TODO register machine to event before moving on
        Scene1 scene1 = new Scene1(machine);
        scene1.getScene(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
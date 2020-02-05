package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import static sample.Constants.*;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        final TextField machineTokenTextField = new TextField();
        machineTokenTextField.setMaxWidth(200);
        final TextField eventTokenTextField = new TextField();
        eventTokenTextField.setMaxWidth(200);
        Button btn = new Button();
        btn.setText("enter");
        btn.setStyle(BUTTONSTYLE);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String machineToken = machineTokenTextField.getText();
                String eventToken = eventTokenTextField.getText();
                if (!machineToken.isEmpty() && !eventToken.isEmpty()) {
                    nextScenes(machineToken, eventToken);
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("We Need More Info!");
                    alert.show();
                }
            }
        });

        vb.getChildren().add(new Label("Machine token: "));
        vb.getChildren().add(machineTokenTextField);
        vb.getChildren().add(new Label("Event token: "));
        vb.getChildren().add(eventTokenTextField);
        vb.getChildren().add(btn);

        primaryStage.setTitle("taste-of-heaven");
        primaryStage.setScene(new Scene(vb, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void nextScenes(String machineToken, String eventToken){
        // TODO register machine to event before moving on
        Scene1 scene1 = new Scene1(machineToken);
        scene1.getScene(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
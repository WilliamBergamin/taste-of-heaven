package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static sample.Constants.*;

public class Scene3 {

    private Stage primaryStage;
    private Machine machine;
    private String orderToken;
    private Label label = new Label("Fetching Order!");

    public Scene3(Machine machine, String orderToken){
        this.machine=machine;
        this.orderToken=orderToken;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        ProgressIndicator progressIndicator = new ProgressIndicator();

        vb.getChildren().add(label);
        vb.getChildren().add(progressIndicator);

        Scene scene3 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene3);


        Task<Void> nextSceneSleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        nextSceneSleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                nextScene();
            }
        });


        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (aquireOrder()){
                    label.setText("Processing!!!!");
                    new Thread(nextSceneSleeper).start();
                }else{
                    failedScene();
                }
            }
        });
        new Thread(sleeper).start();
    }

    private boolean aquireOrder(){
        //TODO get order info from cloud
        if ("051111407592".equals(orderToken)){
            return true;
        }
        return false;
    }

    private void failedScene(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Unable to fetch Order!");
        alert.showAndWait();
        Scene1 scene1 = new Scene1(this.machine);
        scene1.getScene(primaryStage);
    }

    private void nextScene(){
        Scene4 scene4 = new Scene4(this.machine);
        scene4.getScene(primaryStage);
    }
}

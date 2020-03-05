package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import static sample.Constants.*;

public class Scene3 {

    private Stage primaryStage;
    private String orderToken;
    private Label label = new Label("Order Fetched!");

    public Scene3(String orderToken){
        this.orderToken=orderToken;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(PROGRESSSTYLE);

        label.setStyle(LABELSTYLE);

        vb.getChildren().add(label);
        vb.getChildren().add(progressIndicator);

        Scene scene3 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene3);


        Task<Void> nextSceneSleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        nextSceneSleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                ServerHelper helper = new ServerHelper();
                JSONObject response = helper.postOrderCompleted();
                if (response == null){
                    errorScene();
                }else {
                    nextScene();
                }
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
                label.setText("Processing!!!!");
                new Thread(nextSceneSleeper).start();
            }
        });
        new Thread(sleeper).start();
    }

    private void errorScene(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("SOMETHING WENT TERRIBLY WRONG!");
        alert.showAndWait();
        SceneError sceneError = new SceneError();
        sceneError.getScene(primaryStage);
    }

    private void nextScene(){
        Scene4 scene4 = new Scene4();
        scene4.getScene(primaryStage);
    }
}

package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static sample.Constants.HEIGHT;
import static sample.Constants.WIDTH;

public class Scene4 {

    private Stage primaryStage;
    private String machineToken;
    private Label label = new Label("ENJOY YOUR BEVERAGE!");

    public Scene4(String machineToken){
        this.machineToken=machineToken;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        vb.getChildren().add(label);

        Scene scene4 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene4);


        Task<Void> nextSceneSleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(5000);
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

        new Thread(nextSceneSleeper).start();
    }

    private void nextScene(){
        Scene1 scene1 = new Scene1(this.machineToken);
        scene1.getScene(primaryStage);
    }
}

package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static sample.Constants.HEIGHT;
import static sample.Constants.WIDTH;

public class Scene4 {

    private Stage primaryStage;
    private Machine machine;
    private Image image;

    public Scene4(Machine machine){
        this.machine=machine;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        image = new Image(getClass().getResourceAsStream("static/drink-responsibly.png"));

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        CustomVBox vb = new CustomVBox();

        vb.getChildren().add(imageView);

        Scene scene4 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene4);

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

        new Thread(nextSceneSleeper).start();
    }

    private void nextScene(){
        Scene1 scene1 = new Scene1(this.machine);
        scene1.getScene(primaryStage);
    }
}

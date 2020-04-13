package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static sample.Constants.HEIGHT;
import static sample.Constants.WIDTH;

public class SceneOrderCompleted {

    private Stage primaryStage;
    private Image image;

    public void getScene(Stage primaryStage){

        System.out.println("Entered scene completed");
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
        SceneHome sceneHome = new SceneHome();
        sceneHome.getScene(primaryStage);
    }
}

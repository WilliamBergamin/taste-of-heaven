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

public class SceneError {

    private Stage primaryStage;
    private Machine machine;
    private Label label = new Label("THIS MACHINE IS OUT OF SERVICE!");

    public SceneError(Machine machine){
        this.machine=machine;
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        vb.getChildren().add(label);

        Scene sceneError = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(sceneError);

        //TODO a lot of thing are missing here
    }
}

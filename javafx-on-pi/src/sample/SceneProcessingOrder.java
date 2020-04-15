package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import static sample.Constants.*;

public class SceneProcessingOrder {

    private Stage primaryStage;
    private JSONObject order;
    private Label label = new Label("Order Fetched!");

    public SceneProcessingOrder(JSONObject order) {
        this.order = order;
    }

    public void getScene(Stage primaryStage) {

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(PROGRESSSTYLE);

        label.setStyle(LABELSTYLE);

        vb.getChildren().add(label);
        vb.getChildren().add(progressIndicator);

        Scene scene3 = new Scene(vb, WIDTH, HEIGHT);

        primaryStage.setScene(scene3);

        //TODO fix this for multiple drinks in one order
        System.out.println(order.getJSONArray("drinks").getJSONObject(0).toString());
        MachineMicrocontrolerHelper.sendNewOrder(order.getJSONArray("drinks").getJSONObject(0));
        label.setText("Processing!!!!");
        Task<Void> sendOrderToMicro = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                while (MachineMicrocontrolerHelper.getMicrocontrolerState() == "processing") {
                    //wait on microcontroller
                }
                return null;
            }
        };
        sendOrderToMicro.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (Machine.getState() == "error") {
                    errorScene();
                } else {
                    JSONObject response = ServerHelper.postOrderCompleted();
                    if (response == null) {
                        System.out.println("problem with server sending confirmation of order finished");
                        errorScene();
                    } else {
                        nextScene();
                    }
                }
            }
        });
        new Thread(sendOrderToMicro).start();

    }

    private void errorScene() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("SOMETHING WENT TERRIBLY WRONG!");
        alert.showAndWait();
        SceneError sceneError = new SceneError();
        sceneError.getScene(primaryStage);
    }

    private void nextScene() {
        SceneOrderCompleted sceneOrderCompleted = new SceneOrderCompleted();
        sceneOrderCompleted.getScene(primaryStage);
    }
}

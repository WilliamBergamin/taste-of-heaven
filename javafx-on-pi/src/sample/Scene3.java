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
    private JSONObject order;
    private Label label = new Label("Order Fetched!");

    public Scene3(JSONObject order){
        this.order=order;
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

        //TODO fix this for multiple drinks in one order
        for (int i=0; 1<order.getJSONArray("drinks").length(); i++) {
            MachineMicrocontrolerHelper.sendNewOrder(order.getJSONArray("drinks").getJSONObject(i));
            label.setText("Processing!!!!");
            Task<Void> sendOrderToMicro = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    while(MachineMicrocontrolerHelper.getMicrocontrolerState() != "done"){
                        System.out.println("waiting on microcontroler to finish");
                    }
                    return null;
                }
            };
            sendOrderToMicro.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    if (Machine.getState() == "error"){
                        errorScene();
                    }else {
                        JSONObject response = ServerHelper.postOrderCompleted();
                        if (response == null){
                            System.out.println("problem with server sending confirmation of order finished");
                            errorScene();
                        }else {
                            nextScene();
                        }
                    }
                }
            });
            new Thread(sendOrderToMicro).start();
        }
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

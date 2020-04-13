package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import static sample.Constants.*;

public class SceneReceiveOrder {

    private Stage primaryStage;
    private StringBuffer orderKey = new StringBuffer();

//    private final TextField tempTextField = new TextField();


    public void getScene(Stage primaryStage){

        System.out.println("Entered scene receiver");
        ScannerHelper.scannerOn();

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(PROGRESSSTYLE);

        Button CancelBtn = new Button();
        CancelBtn.setText("Cancel");
        CancelBtn.setStyle(BUTTONSTYLE);
        CancelBtn.pressedProperty().addListener((observable, wasPressed, pressed) -> {
            if (pressed) {
                CancelBtn.setStyle(PRESSEDBUTTONSTYLE);
            } else {
                CancelBtn.setStyle(BUTTONSTYLE);
            }
        });
        CancelBtn.setPadding(new Insets(10, 15, 10, 15));
        CancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backScene();
            }
        });

//        Button manualBtn = new Button();
//        manualBtn.setText("Manual Enter");
//        manualBtn.setStyle(BUTTONSTYLE);
//        manualBtn.pressedProperty().addListener((observable, wasPressed, pressed) -> {
//            if (pressed) {
//                manualBtn.setStyle(PRESSEDBUTTONSTYLE);
//            } else {
//                manualBtn.setStyle(BUTTONSTYLE);
//            }
//        });
//        manualBtn.setPadding(new Insets(10, 15, 10, 15));
//        manualBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                orderKey.delete(0, orderKey.length());
//                orderKey.append(tempTextField.getText());
//                onOrderKeyEnter();
//            }
//        });

//        tempTextField.setMaxWidth(200);

        Label label1 = new Label("Scan Barcode!");
        label1.setStyle(LABELSTYLE);

        vb.getChildren().add(label1);
        vb.getChildren().add(progressIndicator);
        vb.getChildren().add(CancelBtn);
//        vb.getChildren().add(tempTextField);
//        vb.getChildren().add(manualBtn);

        Scene scene2 = new Scene(vb, WIDTH, HEIGHT);

        scene2.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                orderKey.append(event.getCharacter());
                if (orderKey.length() == ORDERKEYLENGHT){
                    System.out.println("got key:"+orderKey);
                    onOrderKeyEnter();
                }
            }
        });
        primaryStage.setScene(scene2);

    }

    private void onOrderKeyEnter(){
        JSONObject response = ServerHelper.getOrderInEvent(orderKey.toString());
        if (response == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Invalide Order!");
            alert.show();
            orderKey.delete(0, ORDERKEYLENGHT);
        }else {
            System.out.println("got order:"+response.toString());
            Machine.setSelectedOrder(response.getString("order_key"));
            nextScene(response);
        }
    }

    private void backScene(){
        ScannerHelper.scannerOff();
        SceneHome sceneHome = new SceneHome();
        sceneHome.getScene(primaryStage);
    }

    private void nextScene(JSONObject order){
        System.out.println("going to process order");
        ScannerHelper.scannerOff();
        SceneProcessingOrder sceneProcessingOrder = new SceneProcessingOrder(order);
        sceneProcessingOrder.getScene(primaryStage);
    }
}

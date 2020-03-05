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

public class Scene2 {

    private Stage primaryStage;
    private StringBuffer orderKey = new StringBuffer();

    private final TextField tempTextField = new TextField();

    public Scene2(){
    }

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(PROGRESSSTYLE);

        Button btn = new Button();
        btn.setText("Cancel");
        btn.setStyle(BUTTONSTYLE);
        btn.pressedProperty().addListener((observable, wasPressed, pressed) -> {
            if (pressed) {
                btn.setStyle(PRESSEDBUTTONSTYLE);
            } else {
                btn.setStyle(BUTTONSTYLE);
            }
        });
        btn.setPadding(new Insets(10, 15, 10, 15));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backScene();
            }
        });

        Button manualBtn = new Button();
        manualBtn.setText("Manual Enter");
        manualBtn.setStyle(BUTTONSTYLE);
        manualBtn.pressedProperty().addListener((observable, wasPressed, pressed) -> {
            if (pressed) {
                manualBtn.setStyle(PRESSEDBUTTONSTYLE);
            } else {
                manualBtn.setStyle(BUTTONSTYLE);
            }
        });
        manualBtn.setPadding(new Insets(10, 15, 10, 15));
        manualBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                orderKey.delete(0, orderKey.length());
                orderKey.append(tempTextField.getText());
                onOrderKeyEnter();
            }
        });

        tempTextField.setMaxWidth(200);

        Label label1 = new Label("Scan Barcode!");
        label1.setStyle(LABELSTYLE);

        vb.getChildren().add(label1);
        vb.getChildren().add(progressIndicator);
        vb.getChildren().add(btn);
        vb.getChildren().add(tempTextField);
        vb.getChildren().add(manualBtn);

        Scene scene2 = new Scene(vb, WIDTH, HEIGHT);

        scene2.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                orderKey.append(event.getCharacter());
                if (orderKey.length() == ORDERKEYLENGHT){
                    onOrderKeyEnter();
                }
            }
        });
        primaryStage.setScene(scene2);

    }

    private void onOrderKeyEnter(){
        System.out.println(orderKey.toString());
        ServerHelper helper = new ServerHelper();
        JSONObject response = helper.getOrderInEvent(orderKey.toString());
        if (response == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Invalide Order!");
            alert.show();
        }else {
            nextScene(orderKey.toString());
        }
    }

    private void backScene(){
        Scene1 scene1 = new Scene1();
        scene1.getScene(primaryStage);
    }

    private void nextScene(String orderToken){
        Scene3 scene3 = new Scene3(orderToken);
        scene3.getScene(primaryStage);
    }
}

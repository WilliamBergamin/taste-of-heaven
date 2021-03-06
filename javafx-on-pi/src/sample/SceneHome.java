package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static sample.Constants.*;

public class SceneHome {

    private Stage primaryStage;

    public void getScene(Stage primaryStage){

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        Label label1 = new Label("Place your cup!");
        label1.setStyle(LABELSTYLE);

        Image image = new Image(getClass().getResourceAsStream("static/logo.png"));

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(175);
        imageView.setPreserveRatio(true);

        Button btn = new Button();
        btn.setText("START");
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
                nextScene();
            }
        });

        vb.getChildren().add(imageView);
        vb.getChildren().add(label1);
        vb.getChildren().add(btn);

        Scene scene1 = new Scene(vb, WIDTH, HEIGHT);
        
        primaryStage.setScene(scene1);
    }

    private void nextScene(){
        SceneReceiveOrder sceneReceiveOrder = new SceneReceiveOrder();
        sceneReceiveOrder.getScene(primaryStage);
    }
}

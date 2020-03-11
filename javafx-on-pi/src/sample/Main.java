package sample;

import com.pi4j.io.gpio.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import org.json.JSONException;
import org.json.JSONObject;

import static sample.Constants.*;

public class Main extends Application {

    private Stage primaryStage;
    private final GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.pin = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "GPIO for barcode reader", PinState.HIGH);
        this.pin.setShutdownOptions(true, PinState.LOW);

        this.primaryStage = primaryStage;

        CustomVBox vb = new CustomVBox();

        final TextField machineTokenTextField = new TextField();
        machineTokenTextField.setMaxWidth(200);
        machineTokenTextField.setStyle(TEXTFEILDSTYLE);
        machineTokenTextField.focusedProperty().addListener((observable, wasPressed, pressed) -> {
            if (pressed) {
                machineTokenTextField.setStyle(TEXTFEILDFOCUSEDSTYLE);
            } else {
                machineTokenTextField.setStyle(TEXTFEILDSTYLE);
            }
        });
        final TextField eventTokenTextField = new TextField();
        eventTokenTextField.setMaxWidth(200);
        eventTokenTextField.setStyle(TEXTFEILDSTYLE);
        eventTokenTextField.focusedProperty().addListener((observable, wasPressed, pressed) -> {
            if (pressed) {
                eventTokenTextField.setStyle(TEXTFEILDFOCUSEDSTYLE);
            } else {
                eventTokenTextField.setStyle(TEXTFEILDSTYLE);
            }
        });

        Button btn = new Button();
        btn.setText("ENTER");
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
                String machineToken = machineTokenTextField.getText();
                String eventKey = eventTokenTextField.getText();
                if (!machineToken.isEmpty() && !eventKey.isEmpty()) {
                    JSONObject response = ServerHelper.getMachineData(machineToken);
                    //TODO store data of machine
                    if (response == null){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid credentials!");
                        alert.show();
                    }else {
                        Machine.setMachineToken(machineToken);
                        Machine.setEventKey(eventKey);
                        try {
                            Machine.initializeFromJSON(response);
                            nextScenes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Something went very wrong!");
                            alert.show();
                        }
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("We Need More Info!");
                    alert.show();
                }
            }
        });

        Label label1 = new Label("Machine Token");
        label1.setStyle(LABELSTYLE);

        Label label2 = new Label("Event Key");
        label2.setStyle(LABELSTYLE);

        vb.getChildren().add(label1);
        vb.getChildren().add(machineTokenTextField);
        vb.getChildren().add(label2);
        vb.getChildren().add(eventTokenTextField);
        vb.getChildren().add(btn);

        primaryStage.setTitle("taste-of-heaven");
        primaryStage.setScene(new Scene(vb, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void nextScenes(){
        pin.low();
        gpio.shutdown();
        Scene1 scene1 = new Scene1();
        scene1.getScene(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
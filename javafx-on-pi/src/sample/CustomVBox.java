package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CustomVBox extends VBox{

    public CustomVBox(){
        super();
        this.setPadding(new Insets(10, 50, 50, 50));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.backgroundProperty().setValue(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}

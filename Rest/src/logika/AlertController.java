package logika;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AlertController {


    public static void display(String poruka) {

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ALERT BOX");
        window.setWidth(350);
        window.setMinHeight(200);

        Label lb = new Label(poruka);
        lb.setTextAlignment(TextAlignment.CENTER);
        lb.setPadding(new Insets(0,5,20,5));
        lb.setStyle("-fx-font-size: 20;");
        lb.setWrapText(true);
        lb.setPrefWidth(250);
        lb.setMinHeight(100);
        Button closeButton = new Button("OK");
        closeButton.setPrefWidth(150);
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white");
        closeButton.setOnAction(e -> window.close());

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(20, 40, 40 ,40));
        vb.getChildren().addAll(lb, closeButton);
        vb.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vb);
        scene.getStylesheets().add(AlertController.class.getResource("/css/Viper.css").toExternalForm());
        /*File file = new File("css/Viper.css");
        String css1 = file.toString();
        scene.getStylesheets().add(css1);
*/
        window.setScene(scene);
        window.showAndWait();


    }


}

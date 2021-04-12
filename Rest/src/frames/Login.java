package frames;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    public void display() throws IOException {
        Stage window=new Stage();
        FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
    }
}

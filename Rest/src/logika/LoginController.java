package logika;

import domen.Konobar;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    private String sifra = "";
    @FXML
    GridPane loginGrid;
    static Stage window = new Stage();

    ArrayList<Konobar> listaKonobara = Controller.vratiKontrolora().vratiKonobare();
    static Konobar konobar = new Konobar();

    public LoginController() {
        if (window.getModality() == Modality.NONE) {
            window.initModality(Modality.APPLICATION_MODAL);

        }

    }

    public void initialize() {
        int y = 1;
        for (int i = 1; i <= 9; i++) {
            if (i > 3 * y) {
                y++;
            }
            //String buttonText = "Button "+i ;
            Button button = new Button(i + "");
            button.setPrefWidth(Double.MAX_VALUE);
            button.setPrefHeight(Double.MAX_VALUE);


            loginGrid.add(button, (i - 1 - ((y - 1) * 3)), y - 1);
            button.setOnAction(e -> {
                // whatever you need here: you know the button pressed is the
                // one and only button the handler is registered with
                // System.out.println(buttonText + " clicked");
                sifra += button.getText();
                proveriSifru();
                // System.out.println(sifra);
            });
        }


    }


    private void proveriSifru() {

        for (Konobar k : listaKonobara) {

            if (k.getSifra().equals(sifra)) {
                konobar = k;


                for (Node n : glava.getChildren()) {
                    //   System.out.println(n.getId());
                    if (n.getId().equals("main")) {
                        BorderPane bp= (BorderPane) n;
                        glava.getChildren().clear();
                        glava.getChildren().add(bp);
                        mainController.postaviKonobara(konobar);
                        break;
                    }
                }/*
                for (Node n : glava.getChildren()) {
                    //   System.out.println(n.getId());
                    if (n.getId().equals("login")) {
                        mainController.postaviKonobara(konobar);
                        glava.getChildren().remove(n);
                    }

                }*/

                break;
                //   window.close();
            }

        }
    }

    static StackPane glava;
    static FXMLController mainController;

    public Konobar display(StackPane sp, FXMLController lc) throws IOException {
        glava = sp;
        mainController = lc;
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                we.consume();
            }
        });

        //  window.setTitle("Login");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane login = fxmlLoader.load(LoginController.class.getResource("../fxml/login.fxml"));
        //fc=fxmlLoader.getController();
        login.setId("login");
        glava.getChildren().add(login);
        //   Scene scene = new Scene(login);
        //  window.setScene(scene);
        //  window.showAndWait();

        return konobar;
    }


    public void ponovoBiranje() {
        sifra = "";

    }
}

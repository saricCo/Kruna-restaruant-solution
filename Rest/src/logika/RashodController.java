package logika;

import domen.Proizvod;
import domen.Rashodovanje;
import domen.VrstaRashodovanja;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Date;

public class RashodController {


    boolean zaUnos;
    public ComboBox<Proizvod> proizvod;
    public ComboBox<VrstaRashodovanja> razlog;
    private Rashodovanje rashod;

    public void initialize() {
        rashod = new Rashodovanje();
        proizvod.setItems(FXCollections.observableArrayList(Controller.vratiKontrolora().vratiProizvode()));
        razlog.setItems(FXCollections.observableArrayList(Controller.vratiKontrolora().vratiVrsteRashodovanja()));
        // comboBoxRacun.setItems(FXCollections.observableArrayList(Controller.vratiKontrolora().vratiNaziveFirmi()));
        zaUnos = false;
    }

    public void zatvoriProzor(ActionEvent event) {
        for (Node n : glava.getChildren()) {
            //   System.out.println(n.getId());
            if (n.getId().equals("main")) {
                BorderPane bp = (BorderPane) n;
                glava.getChildren().clear();
                glava.getChildren().add(bp);
                break;
            }
        }
    }

    public void potvriRashodavanje(ActionEvent event) {
        zaUnos = true;
        rashod.setRazlog(razlog.getValue());
        rashod.setProizvod(proizvod.getValue());
        rashod.setVreme(new Date());
        boolean rezultat = Controller.vratiKontrolora().unesiRashod(rashod);
        if (!rezultat) {
            zaUnos = false;
            AlertController ac = new AlertController();
            ac.display("Greska prilikom unosa rashodovanja!!");
        }

        fxmlController.zavrsiRashodovanje(rashod);

        for (Node n : glava.getChildren()) {
            //   System.out.println(n.getId());
            if (n.getId().equals("main")) {
                BorderPane bp = (BorderPane) n;
                glava.getChildren().clear();
                glava.getChildren().add(bp);
                break;
            }
        }

    }

    public boolean zaUnos() {
        return zaUnos;
    }

    public Rashodovanje vratiRashodovanje() {
        return rashod;

    }

    static StackPane glava;
    static FXMLController fxmlController;
    public void dodaj(StackPane sp, FXMLController cnt) {
        glava=sp;
        fxmlController=cnt;
    }
}

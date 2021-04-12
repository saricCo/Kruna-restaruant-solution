package logika;

import domen.Proizvod;
import domen.StavkaRacuna;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class OpcijeController {
    StackPane glava;
    FXMLController mainController;
    StavkaRacuna sr;
    Proizvod proizvod;

    String broj;
    String oblik = "Grami";

    public GridPane grid;
    public Label nazivLbl;
    public Label cenaLbl;
    public Label kolicinaLbl;
    public Label ukupnoLbl;
    public Label trenutnaKolicina;

    public ImageView slikaProizvoda;

    public void ucitaj(StackPane sp, FXMLController fxmlController, Proizvod p, Image img) {
        broj="";
        sr = new StavkaRacuna(0, p, 0);
        glava = sp;
        mainController = fxmlController;
        proizvod = p;

        slikaProizvoda.setImage(img);
        popuni(p);

    }

    public void popuni(Proizvod p) {
        nazivLbl.setText(p.getNaziv());
        cenaLbl.setText("Cena: " + p.getCena() + "");


    }

    public void zavrsi() {
        if(sr.getKolicina()==0){
            return;
        }

        mainController.dodajURacun(sr);
        zatvori();
    }


    public void obrisiBroj() {

        if (broj.length() == 0) {
            return;
        } else {
           broj= broj.substring(0, broj.length() - 1);
        }
        trenutnaKolicina.setText("Količina: "+broj);
    }

    public void potvrdiBroj() {

        int kolicina = Integer.parseInt(broj);

        kolicinaLbl.setText("Količina: " + kolicina);
        sr.setKolicina(kolicina);
        if (oblik.equals("porcija")) {

            ukupnoLbl.setText("Ukupno: ~~");
        } else {
            ukupnoLbl.setText("Ukupno: " + kolicina * proizvod.getCena());

        }
    }

    public void izmeniBroj(ActionEvent ae){
        Button b= (Button) ae.getSource();
        String id=b.getId();
        id=id.replace("broj","");
        if(broj.isEmpty() && id.equals("0")){
            return;
        }
        broj+=id;

        trenutnaKolicina.setText("Količina: "+broj);
    }


    public void zatvori() {
        for (Node n : glava.getChildren()) {
            if (n.getId().equals("main")) {
                BorderPane bp = (BorderPane) n;
                glava.getChildren().clear();
                glava.getChildren().add(bp);
                break;
            }
        }
    }

}

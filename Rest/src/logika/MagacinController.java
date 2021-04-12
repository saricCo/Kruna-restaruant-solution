package logika;

import domen.Proizvod;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashMap;

public class MagacinController {


    public HBox mainVBox;

    static StackPane glava;
    static FXMLController fxmlController;


    public void initialize() {

    }


    public void ucitaj(StackPane sp, FXMLController fm) {
        glava = sp;
        fxmlController = fm;
        // ArrayList<Proizvod> proizvodi=Controller.vratiKontrolora().vratiProizvode();
        HashMap<Proizvod, Integer> stanje = Controller.vratiKontrolora().vratiStanje();

        // HBox hb=new HBox();
        // Label lb=new Label("RB")
        int i = 1;
        VBox rb = new VBox();
        VBox naziv = new VBox();
        VBox cena = new VBox();
        VBox kolicina = new VBox();
        rb.getChildren().add(new Label("Rb"));
        rb.setAlignment(Pos.TOP_CENTER);
        naziv.getChildren().add(new Label("Naziv"));
        cena.getChildren().add(new Label("Cena"));
        cena.setAlignment(Pos.TOP_CENTER);
        kolicina.getChildren().add(new Label("Kolicina"));
        kolicina.setAlignment(Pos.TOP_CENTER);

        for (Proizvod p : stanje.keySet()) {

            rb.getChildren().add(new Label(i+"."));
            naziv.getChildren().add(new Label(p.getNaziv()));
            cena.getChildren().add(new Label(p.getCena()+""));
            kolicina.getChildren().add(new Label(stanje.get(p)+" "));


/*
            HBox hb = new HBox();
            Label lb = new Label(i +". ");
            Label lb1 = new Label(p.getNaziv());
            Region region = new Region();
            Label lb2 = new Label(p.getCena()+" ");
            Label lb3 = new Label(stanje.get(p)+" ");
            hb.setHgrow(region, Priority.ALWAYS);
            hb.getChildren().addAll(lb, lb1, region, lb2,lb3);


            mainVBox.getChildren().add(hb);*/
            i++;
        }
        mainVBox.getChildren().addAll(rb,naziv,cena,kolicina);



    }


    public void dodajStanje() {
        fxmlController.dodajNaStanje();
        zatvori();

    }


    public void zatvori() {
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
}

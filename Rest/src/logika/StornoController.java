package logika;

import domen.Racun;
import domen.StavkaRacuna;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StornoController {
    public VBox leviScroll;
    public VBox desniScroll;
    public Label brojArtikala;
    public Label ukupnaVrednost;

    StackPane glava;

    Racun glavni;

    ArrayList<StavkaRacuna> listaZaBrisanje;
    FXMLController mainController;

    public void ucitaj(StackPane sp, Racun glavniRacun, FXMLController fmController) {
        listaZaBrisanje = new ArrayList<>();
        mainController = fmController;
        glavni = glavniRacun;
        glava = sp;

/*        for (Node n : pregledLevo.getChildren()) {
            leviScroll.getChildren().add(n);
            n.setOnMouseEntered(e -> {
                System.out.println("USO"+n.getTypeSelector());
                n.setStyle("-fx-strikethrough: true;");
            });
            n.setOnMouseExited(e -> {
                n.setStyle("-fx-strikethrough: false;");
            });
        }*/
        // leviScroll.getChildren().add(pregledLevo);
        VBox ceo = new VBox();
        String nazivStolaString = vratiFormatiranString(glavniRacun.getBrojStola());
        //int broj = Integer.parseInt(trenutniSto.substring(trenutniSto.length() - 1));
        //String nazivStolaString = "Sto br. " + broj;

        /*Label nazivStola = new Label(nazivStolaString);
        System.out.println(nazivStolaString);
        nazivStola.setId("prikazRacunaNazivStola");
        HBox header = new HBox();
        header.getChildren().add(nazivStola);
*/
        HBox headerRacun = new HBox();
        Label lb = new Label(nazivStolaString);
        lb.setId("nazivStola");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String vreme = sdf.format(glavniRacun.getVreme());
        System.out.println(vreme);
        Label lb1 = new Label(vreme);
        Region region = new Region();
        headerRacun.setHgrow(region, Priority.ALWAYS);
        headerRacun.getChildren().addAll(lb, region, lb1);
        headerRacun.getStyleClass().add("donjiBorder");

        ceo.getChildren().add(headerRacun);


        boolean first = true;
        for (StavkaRacuna sr : glavniRacun.getListaStavki()) {

            HBox hb = new HBox();
            // hb.setId(sr.getProizvod().getId()+"");
            if (first) {
                first = false;
                hb.setPadding(new Insets(10, 0, 0, 0));
            }
            Label kolicina = new Label(sr.getKolicina() + "x ");
            kolicina.setId(sr.getProizvod().getId() + "");

            Text nazivProizvoda = new Text(sr.getProizvod().getNaziv());
            double cena = sr.getKolicina() * sr.getProizvod().getCena();
            Label cenaLB = new Label(cena + "");
            Region region2 = new Region();
            hb.setHgrow(region2, Priority.ALWAYS);
            hb.getChildren().addAll(kolicina, nazivProizvoda, region2, cenaLB);
            hb.setUserData(sr);


            hb.setOnMouseEntered(e -> {
                //  System.out.println("USO"+n.getTypeSelector());
                nazivProizvoda.setStyle("-fx-strikethrough: true;");
            });
            hb.setOnMouseExited(e -> {
                nazivProizvoda.setStyle("-fx-strikethrough: false;");
            });
            hb.setOnMouseClicked(e -> {
                Label lbl = (Label) hb.lookup("#" + sr.getProizvod().getId());
                String brojString = lbl.getText();
                brojString = brojString.replaceAll("\\D+", "");
                int broj = Integer.parseInt(brojString);
                if (broj == 1) {
                    ceo.getChildren().remove(hb);
                } else {
                    broj--;
                    lbl.setText(broj + "x ");
                }
                // StavkaRacuna srZaDodavanje=sr;
                // srZaDodavanje.setKolicina(sr);
                dodajUListu(sr);
                //listaZaBrisanje.add(srZaDodavanje);
                dodajUDesniScroll();



           /*     HBox neki=new HBox();
                for(Node n: hb.getChildren()){
                    neki.getChildren().add(n);
                }

               // HBox neki= (HBox) hb.getParent().lookup("#"+sr.getProizvod().getId());
                desniScroll.getChildren().add(neki);
                kolicina.setText((sr.getKolicina() - 1) + "x ");



                HBox red = new HBox();
                // hb.setId(sr.getProizvod().getId()+"");
                if (first) {
                    first = false;
                    hb.setPadding(new Insets(10, 0, 0, 0));
                }
                Label kolicinaNovo = new Label(sr.getKolicina() + "x ");
                kolicina.setId("kolicina");

                Text nazivProizvoda = new Text(sr.getProizvod().getNaziv());
                double cena = sr.getKolicina() * sr.getProizvod().getCena();
                Label cenaLB = new Label(cena + "");
                Region region2 = new Region();
                hb.setHgrow(region2, Priority.ALWAYS);
                hb.getChildren().addAll(kolicina, nazivProizvoda, region2, cenaLB);
*/
                /*
                if (sr.getKolicina() == 1) {
                    for (int i = 0; i < glavni.getListaStavki().size(); i++) {
                        if (glavni.getListaStavki().get(i).getProizvod() == sr.getProizvod()) {
                            glavni.getListaStavki().remove(i);
                        }
                    }


                    //glavniRacun.getListaStavki().remove(((HBox)e.getSource()).getUserData());
                } else {

                    sr.setKolicina(sr.getKolicina() - 1);
                }
*/

            });

            ceo.getChildren().add(hb);

        }


        leviScroll.getChildren().add(ceo);

        brojArtikala.setText("Broj artikala: " + glavniRacun.getListaStavki().size());
        ukupnaVrednost.setText("Ukupno: " + glavniRacun.getIznos());


    }

    private void dodajUListu(StavkaRacuna sr) {
        if (listaZaBrisanje.contains(sr)) {
            for (StavkaRacuna srOrdjidji : listaZaBrisanje) {
                if (srOrdjidji.getProizvod() == sr.getProizvod()) {
                    srOrdjidji.setKolicina(srOrdjidji.getKolicina() + 1);
                    break;
                }
            }
        } else {
            StavkaRacuna srZaUnos = new StavkaRacuna(0, sr.getProizvod(), 1);
            listaZaBrisanje.add(srZaUnos);
        }
    }

    private void dodajUDesniScroll() {
        desniScroll.getChildren().clear();
        boolean first = true;


        for (StavkaRacuna sr : listaZaBrisanje) {
            HBox hb = new HBox();

            if (first) {
                first = false;
                hb.setPadding(new Insets(10, 0, 0, 0));
            }
            Label kolicina = new Label(sr.getKolicina() + "x ");

            Text nazivProizvoda = new Text(sr.getProizvod().getNaziv());
            double cena = sr.getKolicina() * sr.getProizvod().getCena();
            Label cenaLB = new Label(cena + "");
            Region region2 = new Region();
            hb.setHgrow(region2, Priority.ALWAYS);
            hb.getChildren().addAll(kolicina, nazivProizvoda, region2, cenaLB);
            desniScroll.getChildren().add(hb);

        }

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

    public void sacuvaj() {

        //glavni.getListaStavki().removeAll(listaZaBrisanje);


        if (mainController.storno.containsKey(glavni.getVreme())) {


            mainController.storno.get(glavni.getVreme()).getListaStavki().addAll(listaZaBrisanje);
        } else {
            Racun r=new Racun(0,glavni.getVreme(),glavni.getKonobar(),listaZaBrisanje,glavni.getNacinPlacanjaID(),glavni.getBrojStola());
            mainController.storno.put(glavni.getVreme(), r);
        }
        //  mainController.dodajUStorno(glavni,listaZaBrisanje);

        for (int i = glavni.getListaStavki().size() - 1; i >= 0; i--) {
            StavkaRacuna sr = glavni.getListaStavki().get(i);
            if (listaZaBrisanje.contains(sr)) {
                for (StavkaRacuna stZaBris : listaZaBrisanje) {
                    if (stZaBris.getProizvod() == sr.getProizvod()) {
                        if (sr.getKolicina() == stZaBris.getKolicina()) {
                            glavni.getListaStavki().remove(sr);
                        } else {
                            sr.setKolicina(sr.getKolicina() - stZaBris.getKolicina());
                        }
                        break;
                    }
                }

            }
        }
        if(glavni.getListaStavki().size()==0){
            mainController.zavrsiRacun(glavni);
            mainController.listaRacuna.remove(glavni);
          //  mainController.stanje.remove(glavni.getBrojStola());
        }


        //mainController.stanje.get(glavni.getBrojStola()).setListaStavki(glavni.getListaStavki());
        mainController.popuniPrikazRacuna();
        zatvori();
    }


    public String vratiFormatiranString(String unos) {
        int broj = 0;
        String naziv;
        try {
            broj = Integer.parseInt(unos.substring(unos.length() - 2));
            naziv = "Sto br. " + broj;
        } catch (NumberFormatException e) {
            naziv = "Za poneti";
            try {
                broj = Integer.parseInt(unos.substring(unos.length() - 1));
                naziv = "Sto br. " + broj;
            } catch (NumberFormatException e1) {
                naziv = "Za poneti";
            }

        }
        return naziv;
    }

}

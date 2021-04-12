package logika;

import domen.Konobar;
import domen.Proizvod;
import domen.Racun;
import domen.StavkaRacuna;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class PregledRacunaController {

    @FXML
    VBox mainPane;
    public VBox prihodiVBox;
    public VBox rashodiVBox;
    public VBox dodatoStanjeVBox;
    public VBox stornoVBox;
    public HBox konobariHBox;

    private void dodajPrihode(ArrayList<Racun> listaRacuna) {
        ArrayList<VBox> listica = new ArrayList<>();
        listica.add(mainPane);
        listica.add(prihodiVBox);
        for (VBox ubaciU : listica) {


            HashMap<Proizvod, Integer> listaProdatihProizvoda = new HashMap<>();
            for (Racun r : listaRacuna) {
                for (StavkaRacuna sr : r.getListaStavki()) {
                    if (listaProdatihProizvoda.containsKey(sr.getProizvod())) {
                        listaProdatihProizvoda.put(sr.getProizvod(), listaProdatihProizvoda.get(sr.getProizvod()) + sr.getKolicina());
                    } else {
                        listaProdatihProizvoda.put(sr.getProizvod(), sr.getKolicina());
                    }
                }
            }
            VBox prihodiVB = new VBox();
            // prihodiVB.setId("prihodi");

            Label lb4 = new Label("PRIHODI");
            lb4.setPrefWidth(Double.MAX_VALUE);
            lb4.getStyleClass().add("borderDole");
            prihodiVB.getChildren().add(lb4);
            double suma = 0;
            for (Proizvod p : listaProdatihProizvoda.keySet()) {

                HBox hb = new HBox();
                int kolicina = listaProdatihProizvoda.get(p);
                Label lb = new Label(kolicina + " x ");
                Label lb1 = new Label(p.getNaziv());
                Region region = new Region();
                suma += p.getCena() * kolicina;
                Label lb2 = new Label(p.getCena() * kolicina + "");
                hb.setHgrow(region, Priority.ALWAYS);
                hb.getChildren().addAll(lb, lb1, region, lb2);

                prihodiVB.getChildren().add(hb);
            }


            Label lbRezultat1 = new Label("Suma: " + suma);

            lbRezultat1.setPrefWidth(Double.MAX_VALUE);
            lbRezultat1.getStyleClass().add("borderGore");

            prihodiVB.getChildren().add(lbRezultat1);
            ubaciU.getChildren().add(prihodiVB);
        }
    }

    public void dodajRashode(ArrayList<Racun> listaRashodovanja) {
        ArrayList<VBox> listica = new ArrayList<>();
        listica.add(mainPane);
        listica.add(rashodiVBox);


        for (VBox unesiU : listica) {

            VBox rashodi = new VBox();
            Label lb5 = new Label("RASHODI");
            lb5.setPrefWidth(Double.MAX_VALUE);
            lb5.getStyleClass().add("borderDole");
            rashodi.getChildren().add(lb5);
            for (Racun r : listaRashodovanja) {

                for (StavkaRacuna sr : r.getListaStavki()) {

                    HBox hb = new HBox();
                    int kolicina = sr.getKolicina();
                    Label lb = new Label(kolicina + " x ");
                    Label lb1 = new Label(sr.getProizvod().getNaziv());
                    Region region = new Region();
                    Label lb2 = new Label(sr.getProizvod().getCena() * kolicina + "");
                    hb.setHgrow(region, Priority.ALWAYS);
                    hb.getChildren().addAll(lb, lb1, region, lb2);
                    rashodi.getChildren().add(hb);
                }

                Label lbRezultat = new Label("Suma: " + r.getIznos());
                lbRezultat.setPrefWidth(Double.MAX_VALUE);
                lbRezultat.getStyleClass().add("borderGore");
                rashodi.getChildren().add(lbRezultat);
            }
            unesiU.getChildren().add(rashodi);
        }


    }

    public void dodajNaStanje(ArrayList<Racun> listaDodatoNaStanje) {
        ArrayList<VBox> listica = new ArrayList<>();
        listica.add(mainPane);
        listica.add(dodatoStanjeVBox);


        for (VBox unesiU : listica) {

            VBox stanje = new VBox();
            Label lbStanje = new Label("Dodato na stanje");
            lbStanje.setPrefWidth(Double.MAX_VALUE);
            lbStanje.getStyleClass().add("borderDole");
            stanje.getChildren().add(lbStanje);
            for (Racun r : listaDodatoNaStanje) {

                for (StavkaRacuna sr : r.getListaStavki()) {

                    HBox hb = new HBox();
                    int kolicina = sr.getKolicina();
                    Label lb = new Label(kolicina + " x ");
                    Label lb1 = new Label(sr.getProizvod().getNaziv());
                    Region region = new Region();
                    Label lb2 = new Label(sr.getProizvod().getCena() * kolicina + "");
                    hb.setHgrow(region, Priority.ALWAYS);
                    hb.getChildren().addAll(lb, lb1, region, lb2);
                    stanje.getChildren().add(hb);
                }
                Label lbRezultat = new Label("Suma: " + r.getIznos());
                lbRezultat.setPrefWidth(Double.MAX_VALUE);
                lbRezultat.getStyleClass().add("borderGore");
                stanje.getChildren().add(lbRezultat);

            }
            unesiU.getChildren().add(stanje);
        }


    }

    public void dodajStorno(TreeMap<Date, Racun> storno) {
        ArrayList<VBox> listica = new ArrayList<>();
        listica.add(mainPane);
        listica.add(stornoVBox);


        for (VBox unesiU : listica) {

            VBox stornoBox = new VBox();
            Label lbStanje = new Label("Storno");
            lbStanje.setPrefWidth(Double.MAX_VALUE);
            lbStanje.getStyleClass().add("borderDole");
            stornoBox.getChildren().add(lbStanje);

            for (Date d : storno.keySet()) {

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Label head = new Label(sdf.format(d));
                stornoBox.getChildren().add(head);

                double suma = 0;
                for (StavkaRacuna sr : storno.get(d).getListaStavki()) {
                    HBox hb = new HBox();
                    int kolicina = sr.getKolicina();
                    Label lb = new Label(kolicina + " x ");
                    Label lb1 = new Label(sr.getProizvod().getNaziv());
                    Region region = new Region();
                    Label lb2 = new Label(sr.getProizvod().getCena() * kolicina + "");
                    suma += sr.getProizvod().getCena() * sr.getKolicina();
                    hb.setHgrow(region, Priority.ALWAYS);
                    hb.getChildren().addAll(lb, lb1, region, lb2);
                    stornoBox.getChildren().add(hb);
                }
                Label lbRezultat = new Label("Suma: " + suma);

                lbRezultat.setPrefWidth(Double.MAX_VALUE);
                lbRezultat.getStyleClass().add("borderGore");
                stornoBox.getChildren().add(lbRezultat);


            }
            unesiU.getChildren().add(stornoBox);
        }


    }


    ArrayList<Racun> listaRacunaFull;
    ArrayList<Racun> listaRashodovanjaFull;
    ArrayList<Racun> listaDodatoNaStanjeFull;
    TreeMap<Date, Racun> stornoFull;


    public void popuni(Konobar k) {
        mainPane.getChildren().clear();
        prihodiVBox.getChildren().clear();
        rashodiVBox.getChildren().clear();
        dodatoStanjeVBox.getChildren().clear();
        stornoVBox.getChildren().clear();
        if (k == null) {
            dodajPrihode(listaRacunaFull);
            dodajRashode(listaRashodovanjaFull);
            dodajNaStanje(listaDodatoNaStanjeFull);
            dodajStorno(stornoFull);
            for (Node n : navbar.getChildren()) {
                Button b = (Button) n;
                String id = b.getId();
                id = id.replace("Btn", "");
                if (id.equals("sve")) {
                    b.setText("Sve (*)");
                } else if (id.equals("prihodi")) {
                    b.setText("Prihodi (" + listaRacunaFull.size() + ")");
                } else if (id.equals("rashodi")) {
                    b.setText("Rashodi (" + listaRashodovanjaFull.size() + ")");
                }else if (id.equals("storno")) {
                    b.setText("Storno (" + stornoFull.size() + ")");
                }else if (id.equals("magacin")) {
                    b.setText("Dodato (" + listaDodatoNaStanjeFull.size() + ")");
                }


            }

            return;

        } else {
            ArrayList<Racun> listaRacunaPart = new ArrayList<>();
            ArrayList<Racun> listaRashodovanjaPart = new ArrayList<>();
            ArrayList<Racun> listaDodatoNaStanjePart = new ArrayList<>();
            TreeMap<Date, Racun> stornoPart = new TreeMap<>();

            for (Racun r : listaRacunaFull) {
                if (r.getKonobar() == k)
                    listaRacunaPart.add(r);
            }
            for (Racun r : listaRashodovanjaFull) {
                if (r.getKonobar() == k)
                    listaRashodovanjaPart.add(r);
            }
            for (Racun r : listaRashodovanjaFull) {
                if (r.getKonobar() == k)
                    listaRashodovanjaPart.add(r);
            }

            for (Date d : stornoFull.keySet()) {
                if (stornoFull.get(d).getKonobar() == k)
                    stornoPart.put(d, stornoFull.get(d));
            }


            dodajPrihode(listaRacunaPart);
            dodajRashode(listaRashodovanjaPart);
            dodajNaStanje(listaDodatoNaStanjePart);
            dodajStorno(stornoPart);

            for (Node n : navbar.getChildren()) {
                Button b = (Button) n;
                String id = b.getId();
                id = id.replace("Btn", "");
                if (id.equals("sve")) {
                    b.setText("Sve (*)");
                } else if (id.equals("prihodi")) {
                    b.setText("Prihodi (" + listaRacunaPart.size() + ")");
                } else if (id.equals("rashodi")) {
                    b.setText("Rashodi (" + listaRashodovanjaPart.size() + ")");
                }else if (id.equals("storno")) {
                    b.setText("Storno (" + stornoPart.size() + ")");
                }else if (id.equals("magacin")) {
                    b.setText("Dodato (" + listaDodatoNaStanjePart.size() + ")");
                }


            }

        }

    }

    public HBox navbar;

    public void ucitaj(ArrayList<Racun> listaRacuna, ArrayList<Racun> listaRashodovanja, ArrayList<Racun> listaDodatoNaStanje, TreeMap<Date, Racun> storno, ArrayList<Konobar> listaKonobara) {

        listaRacunaFull = listaRacuna;
        listaRashodovanjaFull = listaRashodovanja;
        listaDodatoNaStanjeFull = listaDodatoNaStanje;
        stornoFull = storno;
        //listaKonobaraFull = listaKonobara;
        popuni(null);

        for (Konobar k : listaKonobara) {
            Button b = new Button(k.getImeIPrezime());
            b.setText(k.getImeIPrezime());
            b.setId(k.getId() + "");
            b.setUserData(k);
            b.setOnAction(e -> izabranKonobar(e));
            konobariHBox.getChildren().add(b);

        }

        if (!listaRashodovanja.isEmpty()) {


        }

        if (!listaDodatoNaStanje.isEmpty()) {


        }

        //listaDodatoNaStanje


    }

    public void izabranKonobar(ActionEvent e) {
        Button b = (Button) e.getSource();
        //  Konobar k = (Konobar) b.getUserData();
        String id = b.getId();

        if (id.equals("sviKonobari")) {
            popuni(null);

        } else {
            Konobar k = (Konobar) b.getUserData();
            popuni(k);
        }

        // ucitaj();

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

    static StackPane glava;
    public StackPane mainStack;

    public void dodaj(StackPane sp) {
        glava = sp;
    }


    public void promeniMain(ActionEvent e) {
        Button btn = (Button) e.getSource();
        String id = btn.getId();
        String paneNaziv = id.replace("Btn", "");
        paneNaziv += "Pane";


        int i = 0;
        for (Node n : mainStack.getChildren()) {
            if (!n.getId().isEmpty() && n.getId().equals(paneNaziv)) {
                mainStack.getChildren().get(i).toFront();
                break;
            }
            i++;
        }


    }

}

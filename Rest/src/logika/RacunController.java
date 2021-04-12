package logika;

import domen.NacinPlacanja;
import domen.Racun;
import domen.StavkaRacuna;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;

public class RacunController {


    public VBox pregledLevo;
    public BorderPane mainWindow;
    public Label brojArtikala;
    public Label ukupnaVrednost;
    public ComboBox<NacinPlacanja> comboBoxRacun;
    public Label randomObj;

    Racun glavniRacun;
    String glavniSto;
    /*static Stage window = new Stage();

    static Racun glavniRacun;
    static String sto;

    public RacunController() {
        if (window.getModality() == Modality.NONE) {
            window.initModality(Modality.APPLICATION_MODAL);

        }


    }

    public RacunController(Racun r, String trenutniSto) {
        glavniRacun = r;
        sto = trenutniSto;

    }

    public void ispisi() {
        System.out.println(glavniRacun.toString() + "  /   " + sto);
    }

    public void initialize() {
        System.out.println("bamba");


       // ucitaj(glavniRacun, sto);


    }
*/
    Boolean zaUnos;

    public void initialize() {
        comboBoxRacun.setItems(FXCollections.observableArrayList(Controller.vratiKontrolora().vratiNaziveFirmi()));
        zaUnos = false;
    }

    public void ucitaj(Racun r, String trenutniSto) {
        glavniRacun = r;

        glavniSto = trenutniSto;
        VBox ceo = new VBox();
        String nazivStolaString = vratiFormatiranString(r.getBrojStola());
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
        String vreme = sdf.format(r.getVreme());
        System.out.println(vreme);
        Label lb1 = new Label(vreme);
        Region region = new Region();
        headerRacun.setHgrow(region, Priority.ALWAYS);
        headerRacun.getChildren().addAll(lb, region, lb1);
        headerRacun.getStyleClass().add("donjiBorder");

        ceo.getChildren().add(headerRacun);


        boolean first = true;
        for (StavkaRacuna sr : r.getListaStavki()) {

            HBox hb = new HBox();
            if (first) {
                first = false;
                hb.setPadding(new Insets(10, 0, 0, 0));
            }
            Label kolicina = new Label(sr.getKolicina() + "x ");

            Label nazivProizvoda = new Label(sr.getProizvod().getNaziv());
            double cena = sr.getKolicina() * sr.getProizvod().getCena();
            Label cenaLB = new Label(cena + "");
            Region region2 = new Region();
            hb.setHgrow(region2, Priority.ALWAYS);
            hb.getChildren().addAll(kolicina, nazivProizvoda, region2, cenaLB);

            ceo.getChildren().add(hb);

        }


        pregledLevo.getChildren().add(ceo);

        brojArtikala.setText("Broj artikala: " + r.getListaStavki().size());
        ukupnaVrednost.setText("Ukupno: " + r.getIznos());


    }

    public void zatvoriProzor() {
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

    public boolean vratiZaUnos() {
        return zaUnos;
    }


    public void stampajRacun(ActionEvent event) {
        // glavniRacun.setNacinPlacanjaID();

        System.out.println(glavniRacun.getBrojStola());
        int nacinPlacanjaID = 0;
        if (nacinPlacanja == "") {
            AlertController.display("Izaberite način plaćanja");
            return;
        }
        if (nacinPlacanja.equals("Racun")) {

            NacinPlacanja np = comboBoxRacun.getValue();
            if (np == null) {
                AlertController.display("Izaberite polje iz padajuće liste");
                return;
            }
            nacinPlacanjaID = np.getId();
        } else if (nacinPlacanja.equals("Kes")) {
            nacinPlacanjaID = 1;
        } else if (nacinPlacanja.equals("Kartica")) {
            nacinPlacanjaID = 2;
        }

        glavniRacun.setNacinPlacanjaID(nacinPlacanjaID);
        glavniRacun.setBrojStola(glavniSto);
        boolean uspesanUnos = Controller.vratiKontrolora().unesiRacun(glavniRacun);
        if (!uspesanUnos) {

            AlertController.display("NE USPESAN UNOS RACUNA!");
            return;
        }

        zaUnos = true;
        // ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        fmController.zavrsiRacun(glavniRacun);

        for (Node n : glava.getChildren()) {
            //   System.out.println(n.getId());
            if (n.getId().equals("main")) {
                BorderPane bp = (BorderPane) n;
                glava.getChildren().clear();
                glava.getChildren().add(bp);
                break;
            }
        }


        try {

            FileOutputStream fileOut = new FileOutputStream("src/save/racuni.ser");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(glavniRacun);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    String nacinPlacanja = "";

    public void izaberiNacinPlacanja(ActionEvent event) {
        Button btn = (Button) event.getSource();
        System.out.println(btn.getText());
        nacinPlacanja = btn.getText();

        for (Node b : btn.getParent().getChildrenUnmodifiable()) {
            Button button = (Button) b;
            button.setStyle("-fx-background-color:#cfcfcf");
        }
        btn.setStyle("-fx-background-color:#e04646");
        if (nacinPlacanja.equals("Račun")) {
            nacinPlacanja = "Racun";
            comboBoxRacun.setVisible(true);
            randomObj.setText("Izaberite račun");

        } else if (nacinPlacanja.equals("Keš")) {
            nacinPlacanja = "Kes";
            comboBoxRacun.setVisible(false);
            randomObj.setText("");
        } else {
            comboBoxRacun.setVisible(false);
            randomObj.setText("");
        }

        // btn.setStyle("button:#e04646");

        // System.out.println(btn.getParent().getChildrenUnmodifiable().get(0));


    }

    public Racun vratiDatu() {

        return glavniRacun;
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

    StackPane glava;
    FXMLController fmController;

    public void dodaj(StackPane sp, FXMLController fxmlController) {
        glava = sp;
        fmController = fxmlController;

    }

    public void stornoPrikaz(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/prikazStorno.fxml"));
            BorderPane root1 = (BorderPane) fxmlLoader.load();

           // dodajBlurMejnu();
            zatvoriProzor();
            fmController.dodajBlurMejnu();
            glava.getChildren().add(root1);
            StornoController sc=fxmlLoader.getController();
            sc.ucitaj(glava,glavniRacun,fmController);

           // DodajProizvodController dpc = fxmlLoader.getController();
          //  dpc.dodajGlavu(glava, this);

            //((MenuItem)ae.getSource()).getParentMenu()

        /*    Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Dodavanje proizvoda");
            stage.setScene(new Scene(root1));
            // stage.setAlwaysOnTop(true);
            // stage.setFullScreen(true);
            stage.showAndWait();
*/


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*
    public static void setGlavniRacun(Racun glavniRacun) {
        RacunController.glavniRacun = glavniRacun;
    }

    public static void setSto(String sto) {
        RacunController.sto = sto;
    }

    public boolean display() throws IOException {

        window.setTitle("Racun");
        BorderPane racuNPane = FXMLLoader.load(RacunController.class.getResource("../fxml/prikazRacuna.fxml"));

        Scene scene = new Scene(racuNPane);
        window.setScene(scene);
        window.show();

       /* window.setTitle("Racun");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/prikazRacuna.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ABC");
        stage.setScene(new Scene(root1));
        stage.show();

        Scene scene = new Scene(racun);
        window.setScene(scene);
        window.showAndWait();

        return true;
    }

*/
}

package logika;

import domen.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FXMLController {


    public MenuButton prikazStolova;
    public Label vremeID;
    public Label dateID;
    public MenuButton konobarName;
    public StackPane mainWindow;
    public StackPane prikazProizvodaStackPane;
    public VBox prikazDesno;
    public StackPane desniDeo;
    public VBox prikazRacuna;
    public VBox prikazDesnoTrenutni;
    public StackPane mainPartView;
    Konobar konobar;
    String trenutniSto = "";
    public ScrollPane scrollPane1;
    public ScrollPane scrollPane2;
    final double SPEED = 0.01;
    public StackPane glava;

    ArrayList<Racun> listaRacuna;
    TreeMap<String, Racun> stanje = new TreeMap<>();
    Racun trenutniRacunBiranja = new Racun();
    TreeMap<Date, Racun> storno;
    ArrayList<Konobar> listaKonobara;

    public void dodajBlurMejnu() {
        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color: rgba(150, 162, 181, 0.7);");
        ap.setId("boja");
        glava.getChildren().add(ap);

    }

    public void initialize() throws IOException {
        storno = new TreeMap<>();
        listaRashodovanja = new ArrayList<>();
        listaRacuna = new ArrayList<>();
        rashodi = new ArrayList<>();
        listaDodatoNaStanje = new ArrayList<>();
        listaKonobara = new ArrayList<>();
        dodajBlurMejnu();
        LoginController lc = new LoginController();
        konobar = lc.display(glava, this);

        // konobar=new Konobar(0,"Aleksandar ","1215");
        // setKonobar();
        setVreme();
        setDate();
        popuniMeni();
        ubrzajScrollPane();
        dodajBrojeveNaStolove();
        //System.out.println(vremeID.getFont());


        proveriStanje();
    }

    private void proveriStanje() {
        ArrayList<Racun> listRacuna = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("src/save/racuni.ser")));
            while (true) {
                try {
                    Racun r = (Racun) in.readObject();
                    listRacuna.add(r);
                } catch (EOFException ex) {
                    // end of file case
                    break;
                }

            }
            listaRacuna = listRacuna;

            //   contacts = (TreeMap< String, ContactInfo >) in.readObject();

            in.close();
        } catch (Exception exc) {
            System.out.println("Error displaying contacts.");
        }

        try {
            ObjectInputStream in = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("src/save/stanje.ser")));
            TreeMap<String, Racun> saveStanje = (TreeMap<String, Racun>) in.readObject();
            for(String key : saveStanje.keySet()){
              try{
                  dodajStvariNaSto(key);
              }
              catch (IndexOutOfBoundsException e){
                  System.out.println("Ne radi kako treba");
              }
            }
            stanje = saveStanje;
            popuniPrikazRacuna();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dodajBrojeveNaStolove() {


        for (Node nesto : mainPartView.getChildren()) {
            int i = 1;
            String deo = "";
            AnchorPane ap = (AnchorPane) nesto;
            if (ap.getId().equals("terasaAnchor")) {
                deo = "b";
            } else if (ap.getId().equals("malaSalaAnchor")) {
                deo = "a";
            }
            Set<Node> neki = ap.lookupAll("StackPane");
            for (Node n : neki) {
                Text bs = new Text(i + deo);

                bs.getStyleClass().add("brojStola");
                StackPane sp = (StackPane) n;
                sp.setAlignment(Pos.CENTER);
                sp.getChildren().add(bs);
                sp.getChildren().get(1).toFront();
                i++;
            }

        }


    }

    private void ubrzajScrollPane() {
        scrollPane1.setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            scrollPane1.setVvalue(scrollPane1.getVvalue() - deltaY);
        });
        scrollPane2.setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            scrollPane2.setVvalue(scrollPane2.getVvalue() - deltaY);
        });
    }

    private void popuniMeni() {
        HBox hb5 = (HBox) prikazProizvodaStackPane.getParent().lookup("#navBar");
        ObservableList<Node> listaNeka = hb5.getChildren();


        //   ArrayList<VrstaProizvoda> listaVrsta = Controller.vratiKontrolora().vratiVrsteProizvoda();

        HashMap<VrstaProizvoda, ArrayList<Proizvod>> mapa = new HashMap<>();
        ArrayList<Proizvod> listaProiz = Controller.vratiKontrolora().vratiProizvode();

        HashMap<String, ArrayList<Proizvod>> listaProizvodaINaziva = new HashMap<>();
//ACOf

        for (Proizvod p : listaProiz) {

            for (Node n : listaNeka) {

                if (n.getId().contains(p.getVrsta().getNaziv())) {
                    if (!listaProizvodaINaziva.containsKey(n.getId())) {
                        listaProizvodaINaziva.put(n.getId(), new ArrayList<>());
                    }
                    listaProizvodaINaziva.get(n.getId()).add(p);

                    break;
                }
            }

/*
            if (mapa.containsKey(p.getVrsta())) {
                mapa.get(p.getVrsta()).add(p);
            } else {
                mapa.put(p.getVrsta(), new ArrayList<>());
                mapa.get(p.getVrsta()).add(p);
            }
*/
        }


        // BITNO I HITNOq
        //  VrstaProizvoda vp1=new VrstaProizvoda(6,"Najcesce");
        // mapa.put(vp1,new ArrayList<>());
        for (Map.Entry me : listaProizvodaINaziva.entrySet()) {

            //  ((ArrayList<Proizvod>)me.getValue()).sort(Comparator.comparing(e->e.getBrojBiranja()));

            // me.getValue(vp);
            me.getKey();
            GridPane gp = new GridPane();
            gp.setVgap(10);
            gp.setHgap(10);

            int x = 0;
            int y = 0;
            //  int brojac=0;
            for (Proizvod p : (ArrayList<Proizvod>) me.getValue()) {
            /*    if(brojac<5 && ((VrstaProizvoda) me.getKey()).getNaziv()!="Najcesce"){
                    brojac++;
                    mapa.get(vp1).add(p);
                }
            */

                VBox vb = new VBox(7);
                vb.getStyleClass().add("proizvod");
                vb.setUserData(p);
                ImageView iv = new ImageView();
                iv.setPreserveRatio(true);
                // iv.setFitHeight(100);
                iv.setFitWidth(155);
                if (p.getVrsta().getNaziv() == "Salata") {
                    iv.setFitWidth(240);
                }
                String deoPutanje = p.getNaziv().replaceAll(" ", "_").toLowerCase();
                String nazivSlike = deoPutanje + p.getKolicina();
                Image img = new Image("/assets/slikeProizvoda/default1.jpg");
                //  Image img= new Image(getClass().getResource("../assets/slikeProizvoda/default.jpg").toString(),true);
                try {
                    // System.out.println(nazivSlike);
                    img = new Image("/assets/slikeProizvoda/" + nazivSlike + ".jpg");
                    // System.out.println(img.getHeight());

                } catch (Exception e) {

                }

                iv.setImage(img);
                Label lb = new Label();
                lb.setText(p.getNaziv() + " " + p.getKolicina());

                lb.setEllipsisString("");
                lb.setTextAlignment(TextAlignment.CENTER);
                lb.setWrapText(true);
                lb.setContentDisplay(ContentDisplay.CENTER);
                lb.setMaxHeight(Double.MAX_VALUE);
                lb.setMaxWidth(Double.MAX_VALUE);
                lb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(iv, lb);
                vb.setId(p.getNaziv() + "_" + p.getId());


                vb.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        StavkaRacuna sr = new StavkaRacuna(0, p, 1);
                        dodajURacun(sr);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        otvoriOpcijeProizvoda(e, nazivSlike);
                    }

                });
                gp.add(vb, x, y);
                x++;
                if (x % 6 == 0 && x != 0) {
                    y++;
                    x = 0;
                }

            }
            // gp.setStyle("-fx-background-color: transparent");
            gp.setId((String) me.getKey());
            prikazProizvodaStackPane.getChildren().add(gp);

        }


        // mapa.entrySet().stream().sorted(Comparator.comparing(e->e.getValue().));


/*

        for (VrstaProizvoda vp : listaVrsta) {
            ArrayList<Proizvod> listaProizvoda = Controller.vratiKontrolora().vratiProizvodeSaVrstom(vp);
            GridPane gp = new GridPane();
            gp.setVgap(10);
            gp.setHgap(10);
            int x = 0;
            int y = 0;
            for (Proizvod p : listaProizvoda) {

                VBox vb = new VBox(7);
                vb.getStyleClass().add("proizvod");
                vb.setUserData(p);
                ImageView iv = new ImageView();
                iv.setPreserveRatio(true);
                // iv.setFitHeight(100);
                iv.setFitWidth(155);
                if (p.getVrsta().getNaziv() == "Salata") {
                    iv.setFitWidth(240);
                }
                String deoPutanje = p.getNaziv().replaceAll(" ", "_").toLowerCase();
                String nazivSlike = deoPutanje + p.getKolicina();
                Image img = new Image("/assets/slikeProizvoda/default1.jpg");
                //  Image img= new Image(getClass().getResource("../assets/slikeProizvoda/default.jpg").toString(),true);
                try {
                    // System.out.println(nazivSlike);
                    img = new Image("/assets/slikeProizvoda/" + nazivSlike + ".jpg");
                    // System.out.println(img.getHeight());

                } catch (Exception e) {

                }

                iv.setImage(img);
                Label lb = new Label();
                // lb.setMinHeight(50);
                lb.setText(p.getNaziv() + " " + p.getKolicina());
                TextFlow tf = new TextFlow();
                // lb.setMinHeight(60);

                lb.setEllipsisString("");
                lb.setTextAlignment(TextAlignment.CENTER);
                lb.setWrapText(true);
                lb.setContentDisplay(ContentDisplay.CENTER);
                lb.setMaxHeight(Double.MAX_VALUE);
                lb.setMaxWidth(Double.MAX_VALUE);
                lb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(iv, lb);
                vb.setId(p.getNaziv() + "_" + p.getId());
                vb.setOnMouseClicked(e -> dodajURacun(e));
                gp.add(vb, x, y);
                x++;
                if (x % 6 == 0 && x != 0) {
                    y++;
                    x = 0;
                }

            }
            // gp.setStyle("-fx-background-color: transparent");
            gp.setId(vp.getNaziv());
            prikazProizvodaStackPane.getChildren().add(gp);
        }*/
        // System.out.println("OVO JE PROSLO");
    }


    public void dodajURacun(StavkaRacuna sr) {
        if (trenutniSto.equals("za poneti")) {

            Button btn1 = (Button) prikazRacunaDesnoSve.lookup("#printRacun");
            Button btn2 = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
            btn1.setStyle("-fx-background-color: green;");
            btn1.setDisable(false);
            btn2.setStyle("-fx-background-color: grey;");
            btn2.setDisable(true);
        } else {
            Button btn1 = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
            btn1.setStyle("-fx-background-color: green;");
            btn1.setDisable(false);
            Button btn2 = (Button) prikazRacunaDesnoSve.lookup("#printRacun");
            btn2.setStyle("-fx-background-color: grey;");
            btn2.setDisable(true);

        }


        // VBox vb = (VBox) event.getSource();
        //  String naziv = vb.getId();
        //System.out.println(naziv);
        //  Proizvod p = (Proizvod) vb.getUserData();
        // StavkaRacuna sr = new StavkaRacuna(0, p, 1);
        trenutniRacunBiranja.dodajStavku(sr);
/*
        if (stanje.containsKey(trenutniSto)) {
            Racun r = stanje.get(trenutniSto);
            r.dodajStavku(sr);
            stanje.put(trenutniSto, r);
        } else {
            ArrayList<StavkaRacuna> lista = new ArrayList<>();
            lista.add(sr);
            Racun r = new Racun(0, new Date(), konobar.getId(), lista);
            stanje.put(trenutniSto, r);
        }
*/
        prikazDesno.getChildren().clear();
        int broj = 0;
        String nazivStola = vratiFormatiranString(trenutniSto);


        dodajUDesniMeni(nazivStola, trenutniRacunBiranja, prikazDesno, boje[broj]);
        //   dodajUDesniMeni();

        // StavkaRacuna sr=new StavkaRacuna(0,);
        // stanje.put(trenutniSto,)
    }

    //prikazRacuna


    public void popuniPrikazRacuna() {
        //   prikazRacuna.getChildren().add();

        prikazRacuna.getChildren().clear();
        for (String key : stanje.keySet()) {

            int broj = Integer.parseInt(key.substring(key.length() - 1));

            String nazivStola = vratiFormatiranString(key);
            dodajUDesniMeni(nazivStola, stanje.get(key), prikazRacuna, boje[broj]);
        }
//#006CCC
    }

    public String vratiFormatiranString(String unos) {
        int broj = 0;
        String naziv;
        try {
            broj = Integer.parseInt(unos.substring(unos.length() - 2));
            naziv = "Sto br. " + broj;
        } catch (NumberFormatException e) {

            try {
                broj = Integer.parseInt(unos.substring(unos.length() - 1));
                naziv = "Sto br. " + broj;
            } catch (NumberFormatException e1) {
                naziv = unos;
            }

        } catch (IndexOutOfBoundsException e) {
            naziv = unos;
        }
        return naziv;
    }

    String[] boje = {"#3392ff", "#0088ff", "#6898e3", "#5497ff", "#006CCC", "#2b80ff", "#1c76fc", "#4457eb", "#6bc9ff", "#162bcc"};

    ArrayList<StavkaRacuna> listaStornoStavki;

    private void dodajUDesniMeni(String sto, Racun r, VBox hbRoot, String color) {

        //hbRoot.getChildren().clear();
        if (r == null) {
            return;
        }
        VBox ceo = new VBox();
        ceo.setId("nekiID");
        ceo.setUserData(r);


        //ceo.setPadding(new Insets(5,15,15,10));
        HBox headerRacun = new HBox();
        headerRacun.setId("headerRacuna");
        Label lb = new Label(sto);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Label lb1 = new Label(sdf.format(r.getVreme()));
        // lb1.setAlignment(Pos.CENTER_RIGHT);
        Region region = new Region();
        headerRacun.setHgrow(region, Priority.ALWAYS);
        headerRacun.getChildren().addAll(lb, region, lb1);

       /* Random color = new Random();
        int rand_num = color.nextInt(0xffffff + 1);
        String colorCode = String.format("#%06x", rand_num);



        */
        headerRacun.setStyle("-fx-background-color:" + color);
        ceo.getChildren().add(headerRacun);


        VBox sredina = new VBox();
        ceo.getChildren().add(sredina);
        VBox svi = new VBox();
        for (StavkaRacuna sr : r.getListaStavki()) {
            HBox hb = new HBox();
            Label kolicina = new Label(sr.getKolicina() + "x ");

            Label nazivProizvoda = new Label(sr.getProizvod().getNaziv());
            double cena = sr.getKolicina() * sr.getProizvod().getCena();
            Label cenaLB = new Label(cena + "");
            Region region2 = new Region();
            hb.setHgrow(region2, Priority.ALWAYS);


            hb.getChildren().addAll(kolicina, nazivProizvoda, region2, cenaLB);

            svi.getChildren().add(hb);
        }


        if ("prikazRacuna".equals(hbRoot.getId())) {

            // ceo.getChildren().add(sredina);
            VBox newVbox = new VBox();
            if (svi.getChildren().size() > 4) {
                //int i = 0;
                for (int i = 0; i < svi.getChildren().size(); i++) {

                    if (i < 4) {
                        continue;
                    } else {
                        newVbox.getChildren().add(svi.getChildren().get(i));
                        i--;
                    }

                }
                Label lb0 = new Label("------------------------");
                lb0.setPrefWidth(Double.MAX_VALUE);
                lb0.setAlignment(Pos.CENTER);
                sredina.getChildren().addAll(svi.getChildren());
                sredina.getChildren().addAll(lb0, newVbox);

                newVbox.setVisible(false);
                newVbox.setManaged(false);


                ceo.setOnMouseEntered(e -> {

                    lb0.setVisible(false);
                    lb0.setManaged(false);

                    newVbox.setVisible(true);
                    newVbox.setManaged(true);
                });

                ceo.setOnMouseExited(e -> {

                    lb0.setVisible(true);
                    lb0.setManaged(true);
                    newVbox.setVisible(false);
                    newVbox.setManaged(false);
                });

            } else {
                sredina.getChildren().addAll(svi.getChildren());
            }


            ceo.setOnMouseClicked(e -> {

                try {

                    int broj = Integer.parseInt(sto.substring(sto.length() - 1));
                    String nazivStola = "sto" + broj;
                    VBox racunVB = (VBox) e.getSource();
                    Racun r2 = (Racun) racunVB.getUserData();
                    otvoriRacunPanel(r2);
                    /*if (otvoriRacunPanel(r2)) {
                        e.consume();
                        System.out.println(broj);
                        StackPane spMain = (StackPane) mainPartView.lookup("#sto" + broj);

                        ImageView iv = (ImageView) spMain.getChildren().get(0);
                        spMain.getChildren().clear();
                        spMain.getChildren().add(iv);

                        hbRoot.getChildren().remove(ceo);
                        stanje.remove(nazivStola);
                    }*/

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            });
        } else {

            sredina.getChildren().addAll(svi.getChildren());
        }


        HBox footerRacun = new HBox();
        Label ukupnaCena = new Label(r.getIznos() + "");
        Region region1 = new Region();
        footerRacun.setHgrow(region1, Priority.ALWAYS);

        footerRacun.getChildren().addAll(region1, ukupnaCena);
        footerRacun.setId("footerRacuna");
        footerRacun.setStyle(" -fx-border-style: solid hidden hidden hidden;\n" +
                "    -fx-border-width: 1;\n" +
                "    -fx-border-color:" + color + ";");

        ceo.getChildren().add(footerRacun);
        ceo.setStyle("-fx-border-style: solid;\n" +
                "    -fx-border-width: 0 0 0 1.5;\n" +
                "    -fx-border-color: " + color + ";");

        hbRoot.getChildren().add(ceo);
    }



    public void centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

    private void dodajUDesniMeni() {
        prikazDesno.getChildren().clear();
        HBox headerRacun = new HBox();
        Label lb = new Label(trenutniSto);
        lb.setId("nazivStola");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Label lb1 = new Label(sdf.format(trenutniRacunBiranja.getVreme()));
        headerRacun.getChildren().addAll(lb, lb1);
        prikazDesno.getChildren().add(headerRacun);
        for (StavkaRacuna sr : trenutniRacunBiranja.getListaStavki()) {
            HBox hb = new HBox();
            Label kolicina = new Label(sr.getKolicina() + "x ");

            Label nazivProizvoda = new Label(sr.getProizvod().getNaziv());
            double cena = sr.getKolicina() * sr.getProizvod().getCena();
            Label cenaLB = new Label(cena + "");
            hb.getChildren().addAll(kolicina, nazivProizvoda, cenaLB);
            prikazDesno.getChildren().add(hb);
        }
        HBox footerRacun = new HBox();
        Label ukupnaCena = new Label(trenutniRacunBiranja.getIznos() + "");
        footerRacun.getChildren().add(ukupnaCena);
        prikazDesno.getChildren().add(footerRacun);
    }

    ArrayList<Racun> rashodi;

    public void otvoriRacun(ActionEvent ae) {
        //   System.out.println( stanje.get(trenutniSto).toString());


        if (stanje.containsKey(trenutniSto)) {
            try {
                otvoriRacunPanel(stanje.get(trenutniSto));
                promeniGlavniProzor("mainPartView");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (trenutniSto.equals("za poneti")) {
            try {
                otvoriRacunPanel(trenutniRacunBiranja);
                promeniGlavniProzor("mainPartView");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    ArrayList<Racun> listaDodatoNaStanje;

    public void stampajSlip() {
        if (trenutniSto == "za poneti") {
            try {
                otvoriRacunPanel(trenutniRacunBiranja);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (trenutniSto == "rashod") {
            rashodi.add(trenutniRacunBiranja);
            promeniGlavniProzor("mainPartView");

            Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
            btn.setText("Štampaj Slip");
            trenutniRacunBiranja = new Racun();
            return;

        } else if (trenutniSto == "Dodaj na stanje") {
            listaDodatoNaStanje.add(trenutniRacunBiranja);
            boolean rezultat = Controller.vratiKontrolora().unesiStanje(trenutniRacunBiranja);
            if (!rezultat) {
                System.out.println("GRESKA U UNOSU!");
            }

            promeniGlavniProzor("mainPartView");

            Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
            btn.setText("Štampaj Slip");
            trenutniRacunBiranja = new Racun();
            return;

        }
      /*  if (trenutniSto == "za poneti") {
            Racun r = trenutniRacunBiranja;

            try {
                boolean potvrda = otvoriRacunPanel(trenutniRacunBiranja);
                if (potvrda) {

                    trenutniRacunBiranja = new Racun();
                    prikazDesno.getChildren().clear();
                    promeniGlavniProzor("mainPartView");
                } else {
                    return;
                }
            } catch (IOException e) {
                return;
            }
            return;
        }*/
        dodajStvariNaSto(trenutniSto);

        if (stanje.containsKey(trenutniSto)) {
            Racun r = stanje.get(trenutniSto);
            for (StavkaRacuna sr : trenutniRacunBiranja.getListaStavki()) {
                r.dodajStavku(sr);
            }
            // System.out.println(r.getListaStavki().get(0).getKolicina() + " GAAAAA");
            stanje.put(trenutniSto, r);
        } else {
          /*  ArrayList<StavkaRacuna> lista = new ArrayList<>();
            lista.add(sr);
            Racun r = new Racun(0, new Date(), konobar.getId(), lista);*/
            stanje.put(trenutniSto, trenutniRacunBiranja);
        }

        try {

            FileOutputStream fileOut = new FileOutputStream("src/save/stanje.ser", false);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(stanje);
            objectOut.close();
            System.out.println("Stanje  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (StavkaRacuna sr1 : stanje.get(trenutniSto).getListaStavki()) {
            System.out.println(sr1.getProizvod().toString() + " " + sr1.getKolicina());
        }
        promeniGlavniProzor("mainPartView");
        //  promeniGlavniProzor(1);
        trenutniRacunBiranja = new Racun();
        prikazDesno.getChildren().clear();
        popuniPrikazRacuna();

    }

    public void dodajStvariNaSto(String brojStola) {

        StackPane spMain = (StackPane) mainPartView.lookup("#" + brojStola);

        if (stanje.containsKey(brojStola)) {
            return;
        }

        StackPane sp = new StackPane();
        if (spMain.getParent().getId().contains("manji")) {


            Random rand = new Random();

            int n = rand.nextInt(2);
            ImageView ivNeko = new ImageView();
            Image img = new Image("/assets/small/slikaMali" + (n + 1) + ".png");
            ivNeko.setImage(img);
            ivNeko.getStyleClass().add("dodatoNaSto");
            sp.getChildren().add(ivNeko);
        } else {

            ImageView ivNeko = new ImageView();
            Image img = new Image("/assets/small/slika11a.png");
            ivNeko.setImage(img);
            ivNeko.getStyleClass().add("dodatoNaSto");

            sp.getChildren().add(ivNeko);
            ImageView ivNeko1 = new ImageView();
            Image img1 = new Image("/assets/small/slika11.png");
            ivNeko1.setImage(img1);
            ivNeko1.getStyleClass().add("dodatoNaSto");

            sp.getChildren().add(ivNeko1);
          /*  for (int i = 0; i < 5; i++) {
                ImageView ivNeko = new ImageView();
                Image img = new Image("/assets/small/slika" + i + ".png");
                ivNeko.setImage(img);
                sp.getChildren().add(ivNeko);
            }*/
        }


        spMain.getChildren().add(sp);
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(spMain.getChildren());
        Collections.swap(workingCollection, 1, 2);
        spMain.getChildren().setAll(workingCollection);
    }

    public void setKonobar() {
        konobarName.setText(konobar.getImeIPrezime());
    }

    public void setVreme() {

        Thread timerThread = new Thread(() -> {
            // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            while (true) {
                String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> {
                    vremeID.setText(time);
                });
                try {
                    Thread.sleep(60000); //60 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //final String time = simpleDateFormat.format(new Date());

            }
        });
        timerThread.start();//start the thread and its ok
    }

    public void setDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. yyyy.");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
        String dan = simpleDateFormat1.format(new Date());
        String danZaDodavanje = "";

        String dan1 = simpleDateFormat2.format(new Date());
        // System.out.println(dan1);
        switch (dan1) {
            case "01" -> dan1 = "Januar";
            case "02" -> dan1 = "Februar";
            case "03" -> dan1 = "Mart";
            case "04" -> dan1 = "April";
            case "05" -> dan1 = "Maj";
            case "06" -> dan1 = "Jun";
            case "07" -> dan1 = "Jul";
            case "08" -> dan1 = "Avgust";
            case "09" -> dan1 = "Septembar";
            case "10" -> dan1 = "Oktobar";
            case "11" -> dan1 = "Novembar";
            case "12" -> dan1 = "Decembar";
            default -> dan1 = "";
        }
        ;


        switch (dan) {
            case "Mon" -> danZaDodavanje = "Ponedeljak";
            case "Tue" -> danZaDodavanje = "Utorak";
            case "Wed" -> danZaDodavanje = "Sreda";
            case "Thu" -> danZaDodavanje = "Četvrtak";
            case "Fri" -> danZaDodavanje = "Petak";
            case "Sat" -> danZaDodavanje = "Subota";
            case "Sun" -> danZaDodavanje = "Nedelja";
        }
        //“Mon”, “Tue”, “Wed”, “Thu”, “Fri”, “Sat” and “Sun”
        //System.out.println(dan1);
        String date = simpleDateFormat.format(new Date());
        date = date.replace(". ", ". " + dan1 + " ");
        dateID.setText(danZaDodavanje + ", " + date);
    }


    public void prikaziOpcije() {
        prikazStolova.show();
    }

    public void skloniOpcije() {

        //prikazStolova.lookup(".menu-item");

    }

    public void closeAll() {

        //prikazStolova.lookup(".menu-item");
        prikazStolova.hide();
    }

    public BorderPane main;

    public void logout() throws IOException {

        konobar = new Konobar();
        dodajBlurMejnu();
        //main.setOpacity(0.6);
        LoginController lc = new LoginController();
        konobar = lc.display(glava, this);
        if (!listaKonobara.contains(konobar)) {
            listaKonobara.add(konobar);
        }
    }

    public void postaviKonobara(Konobar k) {
        konobar = k;
        if (!listaKonobara.contains(konobar)) {
            listaKonobara.add(konobar);
        }
        konobarName.setText(k.getImeIPrezime());
    }

    public VBox prikazRacunaDesnoSve;

    public void izabranSto(MouseEvent event) {
        staviPicePrvo();
        prikazDesno.getChildren().clear();

        osvetliDugmice(false, false);


        // System.out.println();
        StackPane sp = new StackPane();
        try {

            sp = (StackPane) event.getSource();
        } catch (Exception e) {
            return;
        }

        //ImageView iv = (ImageView) event.getSource();
        String naziv = sp.getId();
        trenutniSto = naziv;

        trenutniRacunBiranja = new Racun(0, new Date(), konobar, new ArrayList<StavkaRacuna>(), 0, trenutniSto);


        promeniGlavniProzor("pregledMenu");
        //promeniGlavniProzor(1);

        prikazDesnoTrenutni.getChildren().clear();
        if (stanje.containsKey(trenutniSto)) {
            osvetliDugmice(false, true);
            Label lb = new Label("Trenutno Stanje");
            lb.setPrefWidth(Double.MAX_VALUE);
            lb.setPadding(new Insets(0, 0, 0, 7));
            prikazDesnoTrenutni.getChildren().add(lb);
        }
        dodajUDesniMeni(trenutniSto, stanje.get(trenutniSto), prikazDesnoTrenutni, boje[3]);
      /*  String source1 = event.getSource().toString(); //yields complete string
        String source2 = event.getPickResult().getIntersectedNode().getId(); //returns JUST the id of the object that was clicked
        System.out.println("Full String: " + source1);
        System.out.println("Just the id: " + source2);
        System.out.println(" " + source2);*/

    }

    public void nazadNaStolove() {

        Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
        btn.setText("Štampaj Slip");

        promeniGlavniProzor("mainPartView");
    }

    private void promeniGlavniProzor(int tapNaPrvoMesto) {


        //  desniDeo.getChildren().get(1).toFront();
        ObservableList<Node> workingCollection1 = FXCollections.observableArrayList(desniDeo.getChildren());
        Collections.swap(workingCollection1, 0, tapNaPrvoMesto);
        desniDeo.getChildren().setAll(workingCollection1);

        ObservableList<Node> workingCollection = FXCollections.observableArrayList(mainWindow.getChildren());
        Collections.swap(workingCollection, 0, tapNaPrvoMesto);
        mainWindow.getChildren().setAll(workingCollection);

    }

    private void promeniGlavniProzor(String naziv) {

        int i = 0;
        for (Node n : mainWindow.getChildren()) {
            if (n.getId().equals(naziv)) {
                mainWindow.getChildren().get(i).toFront();
                break;
            }
            i++;
        }

        String nazivNeki = "";
        if (naziv == "mainPartView") {
            nazivNeki = "osvezi2";
        } else {
            nazivNeki = "prikazRacunaDesnoSve";
        }
        int y = 0;
        for (Node n : desniDeo.getChildren()) {
            // System.out.println(n.getId());
            System.out.println(n.getId());
            if (n.getId().equals(nazivNeki)) {
                System.out.println("LETS GO");
                desniDeo.getChildren().get(y).toFront();
                break;
            }

            y++;
        }
        //prikazRacunaDesnoSve

    }


    private void staviPicePrvo() {
        int i = 0;
        for (Node n : prikazProizvodaStackPane.getChildren()) {
            if (n.getId().equals("SokINapici")) {
                prikazProizvodaStackPane.getChildren().get(i).toFront();
                break;
            }
            i++;
        }
    }

    public void promeniMeni(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String naziv = btn.getId();
        // System.out.println(naziv);
        int i = 0;
        for (Node n : prikazProizvodaStackPane.getChildren()) {
            if (n.getId().equals(naziv)) {
                prikazProizvodaStackPane.getChildren().get(i).toFront();
                break;
            }
            i++;
        }
    }


    public void prikazStolova(ActionEvent event) {
        MenuItem mi = (MenuItem) event.getSource();
        String dugmeKliknuto = mi.getText();

        String anchorNaziv = "";
        if (dugmeKliknuto.equals("Sala")) {
            anchorNaziv = "salaAnchor";
        } else if (dugmeKliknuto.equals("Mala Sala")) {
            anchorNaziv = "malaSalaAnchor";

        } else if (dugmeKliknuto.equals("Terasa")) {
            anchorNaziv = "terasaAnchor";

        }
        int i = 0;
        // System.out.println(mainPartView.getChildren().size());
        for (Node n : mainPartView.getChildren()) {
            if (n.getId().equals(anchorNaziv)) {
                mainPartView.getChildren().get(i).toFront();
                break;
            }
            i++;
        }
        promeniGlavniProzor("mainPartView");

        //pregledMenu


        //  System.out.println(" " + );
    }

    public void osvetliDugmice(boolean slip, boolean racun) {

        if (slip) {
            Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
            btn.setDisable(false);
            btn.setStyle("-fx-background-color: green;");
        } else {
            Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
            btn.setDisable(true);
            btn.setStyle("-fx-background-color: gray;");
        }
        if (racun) {
            Button btn = (Button) prikazRacunaDesnoSve.lookup("#printRacun");
            btn.setDisable(false);
            btn.setStyle("-fx-background-color: green;");
        } else {
            Button btn = (Button) prikazRacunaDesnoSve.lookup("#printRacun");
            btn.setDisable(true);
            btn.setStyle("-fx-background-color: gray;");
        }

    }

    public void iskljuciProgram() {
        ((Stage) main.getScene().getWindow()).close();
    }

    public void otvoriBrzoBiranje() {

        osvetliDugmice(false, false);
        Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
        btn.setText("Štampaj Slip");
        staviPicePrvo();
        prikazDesno.getChildren().clear();
        prikazDesnoTrenutni.getChildren().clear();
        trenutniSto = "za poneti";
        trenutniRacunBiranja = new Racun(0, new Date(), konobar, new ArrayList<StavkaRacuna>(), 0, trenutniSto);
        promeniGlavniProzor("pregledMenu");

    }

    public void obnoviMeni() {
        prikazProizvodaStackPane.getChildren().clear();
        popuniMeni();
    }

    public void otvoriDodajProizvod(ActionEvent ae) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dodajProizvod.fxml"));
            BorderPane root1 = (BorderPane) fxmlLoader.load();

            dodajBlurMejnu();
            glava.getChildren().add(root1);

            DodajProizvodController dpc = fxmlLoader.getController();
            dpc.dodajGlavu(glava, this);

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

    public void zavrsiRacun(Racun r) {
        listaRacuna.add(r);

        if (r.getBrojStola().equals("za poneti")) {
            promeniGlavniProzor("mainPartView");
            return;
        }

        //  System.out.println(r.getBrojStola()+" ovo gledamo");
        int broj = Integer.parseInt(r.getBrojStola().substring(r.getBrojStola().length() - 1));

        //  System.out.println(broj);
        StackPane spMain = (StackPane) mainPartView.lookup("#" + r.getBrojStola());

        //ovdenesto
        // brojStola
        Text txt = new Text("0");
        for (Node n : spMain.getChildren()) {
            if (n.getStyleClass().contains("brojStola")) {
                txt = (Text) n;
                break;
            }
        }
        ImageView iv = (ImageView) spMain.getChildren().get(0);
        spMain.getChildren().clear();
        spMain.getChildren().addAll(iv, txt);


        for (Node n : prikazRacuna.getChildren()) {
            if (n.getUserData() == r) {
                prikazRacuna.getChildren().remove(n);
                break;
            }
        }


        stanje.remove(r.getBrojStola());
    }

    private void otvoriRacunPanel(Racun r) throws IOException {
        //  System.out.println("IDE GASS");

        // System.out.println(r.getVreme());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/prikazRacuna.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();


        dodajBlurMejnu();
        glava.getChildren().add(root1);
        RacunController rc = fxmlLoader.getController();
        rc.ucitaj(r, trenutniSto);
        rc.dodaj(glava, this);

        /*
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        RacunController rc = fxmlLoader.getController();
        rc.ucitaj(r, trenutniSto);

        stage.showAndWait();
        Racun r1 = rc.vratiDatu();
        Boolean zaUnos = rc.vratiZaUnos();
        System.out.println(zaUnos);
        if (!zaUnos) {
            return false;
        } else {
            listaRacuna.add(r1);
            return true;

        }*/


    }

    int trenutna = 0;

    public void promeniProstorijuSaScrollom(ScrollEvent se) {

        String[] opcije = {"Sala", "Mala Sala", "Terasa"};


        if (se.getDeltaY() > 0) {
            if (trenutna == 2) {
                return;
            } else {
                trenutna += 1;
            }

        } else {
            if (trenutna == 0) {
                return;
            } else {
                trenutna -= 1;
            }
        }

        String anchorNaziv = "";
        if (opcije[trenutna].equals("Sala")) {
            anchorNaziv = "salaAnchor";
        } else if (opcije[trenutna].equals("Mala Sala")) {
            anchorNaziv = "malaSalaAnchor";

        } else if (opcije[trenutna].equals("Terasa")) {
            anchorNaziv = "terasaAnchor";

        }
        int i = 0;
        // System.out.println(mainPartView.getChildren().size());
        for (Node n : mainPartView.getChildren()) {
            if (n.getId().equals(anchorNaziv)) {
                mainPartView.getChildren().get(i).toFront();
                break;
            }
            i++;
        }
        promeniGlavniProzor("mainPartView");

    }

    public void presekStanja() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/pregledRacuna.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            dodajBlurMejnu();
            glava.getChildren().add(root1);
            /*
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Pregled stanja");
            stage.setScene(new Scene(root1));*/
            PregledRacunaController pr = fxmlLoader.getController();
            pr.ucitaj(listaRacuna, rashodi, listaDodatoNaStanje, storno, listaKonobara);
            pr.dodaj(glava);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Rashodovanje> listaRashodovanja;

    public void zavrsiRashodovanje(Rashodovanje rs) {
        listaRashodovanja.add(rs);
    }

    public void openRashodPanel() {
        osvetliDugmice(false, false);


        Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
        btn.setText("Završi rashod");
        staviPicePrvo();
        prikazDesno.getChildren().clear();
        prikazDesnoTrenutni.getChildren().clear();
        trenutniSto = "rashod";
        trenutniRacunBiranja = new Racun(0, new Date(), konobar, new ArrayList<StavkaRacuna>(), 0, trenutniSto);
        promeniGlavniProzor("pregledMenu");
        /*
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/prikazRashod.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            dodajBlurMejnu();
            glava.getChildren().add(root1);
            RashodController rc = fxmlLoader.getController();
            rc.dodaj(glava, this);


        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    private void otvoriOpcijeProizvoda(MouseEvent e, String nazivSlike) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/opcije.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            dodajBlurMejnu();
            glava.getChildren().add(root1);
            OpcijeController oc = fxmlLoader.getController();
            VBox vb = (VBox) e.getSource();
            Proizvod p = (Proizvod) vb.getUserData();


            Image img = new Image("/assets/slikeProizvoda/default1.jpg");
            //  Image img= new Image(getClass().getResource("../assets/slikeProizvoda/default.jpg").toString(),true);
            try {
                // System.out.println(nazivSlike);
                img = new Image("/assets/slikeProizvoda/" + nazivSlike + ".jpg");
                // System.out.println(img.getHeight());

            } catch (Exception en) {

            }


            oc.ucitaj(glava, this, p, img);

            // RashodController rc = fxmlLoader.getController();
            //rc.dodaj(glava, this);


        } catch (IOException en) {
            en.printStackTrace();
        }
    }

    public void openMagacinPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/magacin.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            dodajBlurMejnu();
            glava.getChildren().add(root1);
            MagacinController mc = fxmlLoader.getController();
            // RashodController rc = fxmlLoader.getController();
            //rc.dodaj(glava, this);
            mc.ucitaj(glava, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dodajNaStanje() {
        osvetliDugmice(false, false);


        Button btn = (Button) prikazRacunaDesnoSve.lookup("#printSlip");
        btn.setText("Dodaj na stanje");
        staviPicePrvo();
        prikazDesno.getChildren().clear();
        prikazDesnoTrenutni.getChildren().clear();
        trenutniSto = "Dodaj na stanje";
        trenutniRacunBiranja = new Racun(0, new Date(), konobar, new ArrayList<StavkaRacuna>(), 0, trenutniSto);
        promeniGlavniProzor("pregledMenu");

    }

}

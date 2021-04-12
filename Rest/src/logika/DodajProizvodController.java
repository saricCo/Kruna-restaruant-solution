package logika;

import domen.Proizvod;
import domen.VrstaProizvoda;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class DodajProizvodController {


    @FXML
    private javafx.scene.control.Button closeButton;
    public TextField nazivTF;
    public TextField kolicinaTF;
    public TextField cenaTF;
    public ComboBox<VrstaProizvoda> vrstaCB;
    public Button dodajSliku;
    FileChooser fileChooser;
    File slika;

    public void initialize() {
        fileChooser = new FileChooser();
        vrstaCB.setItems(FXCollections.observableArrayList(Controller.vratiKontrolora().vratiVrsteProizvoda()));

    }

    public void izaberiSliku(ActionEvent e) {
        File file = fileChooser.showOpenDialog(((Button) e.getSource()).getScene().getWindow());
        slika = file;


    }


    @FXML
    public void zatvoriProzor() {
        for (Node n : glava.getChildren()) {
            if (n.getId().equals("main")) {
                BorderPane bp = (BorderPane) n;
                glava.getChildren().clear();
                glava.getChildren().add(bp);
                break;
            }
        }
    }

    public void dodajProizvod() {
        String naziv = nazivTF.getText();
        VrstaProizvoda vrsta = vrstaCB.getValue();
        String kolicina = kolicinaTF.getText();
        String cena = cenaTF.getText();
        // System.out.println(cena);
        if (naziv.isEmpty() || vrsta == null || kolicina.isEmpty() || cena.isEmpty()) {
            return;
        }
        Double cenaBroj;
        try {
            cenaBroj = Double.parseDouble(cena);
        } catch (NumberFormatException ex) {
            System.out.println("Given String is not parsable to double");
            return;
        }
        Proizvod p = new Proizvod(0, naziv, cenaBroj, kolicina, vrsta,0);

        try {
            String deoPutanje = p.getNaziv().replaceAll(" ", "_").toLowerCase();
            String nazivSlike = deoPutanje + p.getKolicina();
            Image image = ImageIO.read(slika);


           // BufferedImage bi = this.createResizedCopy(image, 250, 150, true);
            BufferedImage bi1= ImageIO.read(slika );
            ImageIO.write(bi1, "jpg", new File("C:\\Users\\Pumkin-duke\\IdeaProjects\\Rest\\src\\assets\\slikeProizvoda\\" + nazivSlike + ".jpg"));

            // FileOutputStream os = new FileOutputStream(new   File("/assets/slikeProizvoda/"+nazivSlike+".jpg"));

            // FileOutputStream saveFile=new FileOutputStream("SaveObj.sav");

            /*ObjectOutputStream save = new ObjectOutputStream(os);
            save.writeObject(slika);
            os.close();
            save.close();
*/

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        boolean unet = Controller.vratiKontrolora().unesiProizvod(p);
        if (unet) {

            for (Node n : glava.getChildren()) {
                //   System.out.println(n.getId());
                if (n.getId().equals("main")) {
                    BorderPane bp = (BorderPane) n;
                    glava.getChildren().clear();
                    glava.getChildren().add(bp);
                    break;
                }
            }
            fcontroller.obnoviMeni();
        }

    }

    BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    static StackPane glava;
    static FXMLController fcontroller;

    public void dodajGlavu(StackPane sp, FXMLController fxmlController) {
        glava=sp;
        fcontroller=fxmlController;
    }
}

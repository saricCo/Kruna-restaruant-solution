package domen;

import java.io.Serializable;
import java.util.Objects;

public class Proizvod implements Serializable {
    private int id;
    private String naziv;
    private double cena;
    private String kolicina;
    private VrstaProizvoda vrsta;
    private int brojBiranja;

    public Proizvod() {

    }

    public Proizvod(int id, String naziv, double cena, String kolicina, VrstaProizvoda vrsta,int brojBiranja) {
        this.id = id;
        this.naziv = naziv;
        this.cena = cena;
        this.kolicina = kolicina;
        this.vrsta = vrsta;
        this.brojBiranja=brojBiranja;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getKolicina() {
        return kolicina;
    }

    public void setKolicina(String kolicina) {
        this.kolicina = kolicina;
    }

    public VrstaProizvoda getVrsta() {
        return vrsta;
    }

    public void setVrsta(VrstaProizvoda vrsta) {
        this.vrsta = vrsta;
    }

    public int getBrojBiranja() {
        return brojBiranja;
    }

    public void setBrojBiranja(int brojBiranja) {
        this.brojBiranja = brojBiranja;
    }

    @Override
    public String toString() {
        return naziv+" "+kolicina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proizvod proizvod = (Proizvod) o;
        return id == proizvod.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

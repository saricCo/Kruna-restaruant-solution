package domen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Racun implements Serializable {
    private int id;
    private Date vreme;
    private double iznos;
    private Konobar konobar;
    private ArrayList<StavkaRacuna> listaStavki;
    private int nacinPlacanjaID;
    private String brojStola;

    public Racun(int id, Date vreme, Konobar konobar, ArrayList<StavkaRacuna> lista, int nacinPlacanjaID, String brojStola) {
        this.id = id;
        this.vreme = vreme;
        this.konobar = konobar;
        this.listaStavki = lista;
        iznos = saberiStavke(lista);
        this.nacinPlacanjaID = nacinPlacanjaID;
        this.brojStola = brojStola;
    }

    private double saberiStavke(ArrayList<StavkaRacuna> lista) {
        double suma = 0;
        for (StavkaRacuna sr : lista) {
            suma += sr.getProizvod().getCena() * sr.getKolicina();
        }
        return suma;
    }


    public Racun() {
        this.listaStavki = new ArrayList<>();
    }

    public void dodajStavku(StavkaRacuna sr) {
        for (StavkaRacuna stavka : this.getListaStavki()) {
            if (stavka.getProizvod().getNaziv().equals(sr.getProizvod().getNaziv())) {
                stavka.setKolicina(stavka.getKolicina() + sr.getKolicina());
                this.iznos += sr.getProizvod().getCena() * sr.getKolicina();
                System.out.println(stavka.getKolicina());
                return;
            }
        }

        this.getListaStavki().add(sr);
        this.iznos += sr.getProizvod().getCena() * sr.getKolicina();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<StavkaRacuna> getListaStavki() {
        return listaStavki;
    }

    public void setListaStavki(ArrayList<StavkaRacuna> listaStavki) {
        this.listaStavki = listaStavki;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public double getIznos() {
        double suma = 0;
        for (StavkaRacuna sr : this.getListaStavki()) {
            suma += sr.getProizvod().getCena() * sr.getKolicina();
        }
        return suma;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public Konobar getKonobar() {
        return konobar;
    }

    public void setKonobar(Konobar konobar) {
        this.konobar = konobar;
    }

    public int getNacinPlacanjaID() {
        return nacinPlacanjaID;
    }

    public void setNacinPlacanjaID(int nacinPlacanjaID) {
        this.nacinPlacanjaID = nacinPlacanjaID;
    }

    public String getBrojStola() {
        return brojStola;
    }

    public void setBrojStola(String brojStola) {
        this.brojStola = brojStola;
    }

    @Override
    public String toString() {
        return "Racun{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Racun racun = (Racun) o;
        return id == racun.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

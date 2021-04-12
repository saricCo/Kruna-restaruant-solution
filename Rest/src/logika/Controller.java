package logika;

import db.DBBroker;
import domen.*;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuButton;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */


    /**
     * @author Pumkin-duke
     */
    public static Controller instanca;
    DBBroker db;

    private Controller() {
        db = new DBBroker();
    }

    public static Controller vratiKontrolora() {
        if (instanca == null) {
            instanca = new Controller();
        }
        return instanca;
    }

    public boolean unesiProizvod(Proizvod p) {
        boolean uspesno = false;
        db.otvoriKonekciju();
        db.ucitajDrajver();
        uspesno = db.unesiProizvod(p);
        db.commit();
        db.zatvoriKonekciju();
        return uspesno;
    }


    public ArrayList<Konobar> vratiKonobare() {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        ArrayList<Konobar> lista = db.vratiKonobare();
        db.commit();
        db.zatvoriKonekciju();
        return lista;
    }

    public ArrayList<VrstaProizvoda> vratiVrsteProizvoda() {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        ArrayList<VrstaProizvoda> lista = db.vratiVrsteProizvoda();
        db.commit();
        db.zatvoriKonekciju();
        return lista;
    }

    public ArrayList<Proizvod> vratiProizvodeSaVrstom(VrstaProizvoda vp) {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        ArrayList<Proizvod> lista = db.vratiProizvodeSaVrstom(vp);
        db.commit();
        db.zatvoriKonekciju();
        return lista;
    }

    public ArrayList<NacinPlacanja> vratiNaziveFirmi() {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        ArrayList<NacinPlacanja> lista = db.vratiNaziveFirmi();
        db.commit();
        db.zatvoriKonekciju();
        return lista;
    }

    public boolean unesiRacun(Racun glavniRacun) {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        boolean rezultat = db.unesiRacun(glavniRacun);
        db.commit();
        db.zatvoriKonekciju();
        return rezultat;
    }

    public ArrayList<Proizvod> vratiProizvode() {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        ArrayList<Proizvod> rezultat = db.vratiProizvode();
        db.commit();
        db.zatvoriKonekciju();
        return rezultat;
    }

    public ArrayList<VrstaRashodovanja> vratiVrsteRashodovanja() {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        ArrayList<VrstaRashodovanja> rezultat = db.vratiVrsteRashodovanja();
        db.commit();
        db.zatvoriKonekciju();
        return rezultat;
    }

    public boolean unesiRashod(Rashodovanje rashod) {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        boolean rezultat = db.unesiRashod(rashod);
        db.commit();
        db.zatvoriKonekciju();
        return rezultat;
    }

    public HashMap<Proizvod, Integer> vratiStanje() {
        HashMap<Proizvod, Integer> stanje=new HashMap<>();
        db.otvoriKonekciju();
        db.ucitajDrajver();
        stanje = db.vratiStanje();
        db.commit();
        db.zatvoriKonekciju();
        return stanje;
    }

    public boolean unesiStanje(Racun trenutniRacunBiranja) {
        db.otvoriKonekciju();
        db.ucitajDrajver();
        boolean rezultat = db.unesiStanje(trenutniRacunBiranja);
        db.commit();
        db.zatvoriKonekciju();
        return rezultat;
    }
}

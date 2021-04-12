/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domen.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pumkin-duke
 */
public class DBBroker {

    Connection konekcija;
    public static DBBroker instanca;

    public static DBBroker getInstance() {
        if (instanca == null) {
            instanca = new DBBroker();
        }
        return instanca;
    }

    public void ucitajDrajver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void otvoriKonekciju() {
        try {
            konekcija = DriverManager.getConnection("jdbc:mysql://localhost:3306/kruna", "root", "root");
            konekcija.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zatvoriKonekciju() {

        try {
            konekcija.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void commit() {
        try {
            konekcija.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void rollback() {

        try {
            konekcija.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public boolean unesiProizvod(Proizvod p) {
        boolean unet = true;
        String sql = "INSERT INTO proizvodi (naziv,cena,kolicina,vrsta) VALUES(?,?,?,?)";

        try {
            PreparedStatement ps = konekcija.prepareStatement(sql);


            ps.setString(1, p.getNaziv());
            ps.setDouble(2, p.getCena());
            ps.setString(3, p.getKolicina());
            ps.setInt(4, p.getVrsta().getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            unet = false;
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);

        }
        return unet;
    }

    public ArrayList<Konobar> vratiKonobare() {
        ArrayList<Konobar> lista = new ArrayList<>();

        String upit = "SELECT * FROM kruna.konobar;";
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                Konobar k = new Konobar();

                k.setId(rs.getInt("idKonobara"));
                k.setImeIPrezime(rs.getString("imeIPrezime"));
                k.setSifra(rs.getString("sifra"));
                lista.add(k);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public ArrayList<VrstaProizvoda> vratiVrsteProizvoda() {
        ArrayList<VrstaProizvoda> lista = new ArrayList<>();

        String upit = "SELECT * FROM kruna.vrsta_proizvoda;";
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                VrstaProizvoda vp = new VrstaProizvoda();
                vp.setId(rs.getInt("idVrste"));
                vp.setNaziv(rs.getString("naziv"));
                lista.add(vp);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public ArrayList<Proizvod> vratiProizvodeSaVrstom(VrstaProizvoda vp) {
        ArrayList<Proizvod> lista = new ArrayList<>();

        String upit = "SELECT * FROM kruna.proizvodi WHERE vrsta=" + vp.getId();
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                Proizvod p = new Proizvod();
                p.setId(rs.getInt("idProizvod"));
                p.setNaziv(rs.getString("naziv"));
                p.setCena(rs.getDouble("cena"));
                p.setKolicina(rs.getString("kolicina"));
                p.setVrsta(vp);
                lista.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;

    }

    public ArrayList<NacinPlacanja> vratiNaziveFirmi() {
        ArrayList<NacinPlacanja> lista = new ArrayList<>();

        String upit = "SELECT * FROM kruna.nacin_placanja WHERE naziv='Racun'";
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                NacinPlacanja np = new NacinPlacanja();
                np.setId(rs.getInt("id"));
                np.setNaziv(rs.getString("naziv"));
                np.setOpis(rs.getString("dodatno"));
                lista.add(np);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public boolean unesiRacun(Racun glavniRacun) {
        boolean unet = true;
        String sql = "INSERT INTO racun (vreme,iznos,konobarID,nacinPlacanjaID,stoBroj) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement ps = konekcija.prepareStatement(sql);


            ps.setTimestamp(1, new Timestamp(glavniRacun.getVreme().getTime()));
            ps.setDouble(2, glavniRacun.getIznos());
            ps.setInt(3, glavniRacun.getKonobar().getId());
            ps.setInt(4, glavniRacun.getNacinPlacanjaID());
            ps.setString(5, glavniRacun.getBrojStola());
            ps.executeUpdate();


            int idRacuna = vratiLastIDRacun();
            for (StavkaRacuna sr : glavniRacun.getListaStavki()) {
                unesiStavkuRacuna(sr, idRacuna);
            }

        } catch (SQLException ex) {
            unet = false;
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            return unet;

        }
        return unet;
    }

    public int vratiLastIDRacun() {
        int broj = 0;

        String upit = "SELECT MAX(idRacun) as max FROM kruna.racun;";
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                broj = rs.getInt("max");

            }

        } catch (SQLException ex) {
            broj = -1;
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return broj;
    }

    public boolean unesiStavkuRacuna(StavkaRacuna sr, int idRacun) {
        boolean unet = true;
        String sql = "INSERT INTO stavka_racuna (idRacun,idProizvod,kolicina) VALUES(?,?,?)";

        try {
            PreparedStatement ps = konekcija.prepareStatement(sql);


            ps.setInt(1, idRacun);
            ps.setInt(2, sr.getProizvod().getId());
            ps.setInt(3, sr.getKolicina());
            ps.executeUpdate();

            boolean izmena = izmeniStanjeProizvoda(sr.getProizvod());

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }
        return unet;
    }

    public boolean izmeniStanjeProizvoda(Proizvod p) {
        boolean unet = false;
        String sql = "UPDATE proizvodi SET brojBiranja=brojBiranja+1 WHERE idProizvod=" + p.getId();


        try {
            PreparedStatement updateQuery = konekcija.prepareStatement(sql);
            //  updateQuery.setString(1, "20");
            // updateQuery.setString(2, "40");

            updateQuery.executeUpdate();
            unet = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return unet;
    }

    public ArrayList<Proizvod> vratiProizvode() {
        ArrayList<Proizvod> lista = new ArrayList<>();

        String upit = "SELECT * FROM kruna.proizvodi as p Inner join vrsta_proizvoda as vp on p.vrsta=vp.idVrste ORDER BY vp.naziv, p.brojBiranja DESC,p.idProizvod;";
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                Proizvod p = new Proizvod();
                p.setId(rs.getInt("idProizvod"));
                p.setNaziv(rs.getString("p.naziv"));
                p.setCena(rs.getDouble("cena"));
                p.setKolicina(rs.getString("kolicina"));

                VrstaProizvoda vp = new VrstaProizvoda();
                vp.setId(rs.getInt("vrsta"));
                vp.setNaziv(rs.getString("vp.naziv"));

                p.setVrsta(vp);
                lista.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public ArrayList<VrstaRashodovanja> vratiVrsteRashodovanja() {
        ArrayList<VrstaRashodovanja> lista = new ArrayList<>();

        String upit = "SELECT * FROM kruna.razlog_rashodovanja;";

        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                VrstaRashodovanja vr = new VrstaRashodovanja();


                vr.setId(rs.getInt("id"));
                vr.setRazlog(rs.getString("razlog"));
                lista.add(vr);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public boolean unesiRashod(Rashodovanje rashod) {
        boolean unet = true;
        String sql = "INSERT INTO rashodovanja (proizvodID,razlogID,vreme) VALUES(?,?,?)";

        try {
            PreparedStatement ps = konekcija.prepareStatement(sql);

            ps.setInt(1, rashod.getProizvod().getId());
            ps.setInt(2, rashod.getRazlog().getId());
            ps.setTimestamp(3, new Timestamp(rashod.getVreme().getTime()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            unet = false;
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            return unet;

        }
        return unet;
    }

    public HashMap<Proizvod, Integer> vratiStanje() {
        HashMap<Proizvod, Integer> lista = new HashMap<>();

        String upit = "SELECT * FROM kruna.proizvodi as p Inner join vrsta_proizvoda as vp on p.vrsta=vp.idVrste right join stanje s on p.idProizvod=s.proizvodID ORDER BY p.naziv,vp.naziv, p.brojBiranja DESC,p.idProizvod;";
        try {
            Statement s = konekcija.createStatement();
            ResultSet rs = s.executeQuery(upit);

            while (rs.next()) {
                Proizvod p = new Proizvod();

                p.setId(rs.getInt("idProizvod"));
                p.setNaziv(rs.getString("p.naziv"));
                p.setCena(rs.getDouble("cena"));
                p.setKolicina(rs.getString("kolicina"));

                VrstaProizvoda vp = new VrstaProizvoda();
                vp.setId(rs.getInt("vrsta"));
                vp.setNaziv(rs.getString("vp.naziv"));

                p.setVrsta(vp);
                p.setBrojBiranja(rs.getInt("brojBiranja"));

                int stanje = rs.getInt("stanje");

                lista.put(p, stanje);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;

    }

    public boolean unesiStanje(Racun trenutniRacunBiranja) {
        boolean unet = true;


        HashMap<Proizvod, Integer> stanje = vratiStanje();

        for (StavkaRacuna sr : trenutniRacunBiranja.getListaStavki()) {
            if (!stanje.containsKey(sr.getProizvod())) {
                String sql = "INSERT INTO stanje (proizvodID,stanje) VALUES(?,?) ";
                try {
                    PreparedStatement ps = konekcija.prepareStatement(sql);

                    ps.setInt(1, sr.getProizvod().getId());
                    ps.setInt(2, sr.getKolicina());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    unet = false;
                    Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
                    return unet;

                }
            } else {
                String sql = "UPDATE stanje SET stanje=stanje+" + sr.getKolicina() + " WHERE proizvodID=" + sr.getProizvod().getId();


                try {
                    PreparedStatement updateQuery = konekcija.prepareStatement(sql);
                    updateQuery.executeUpdate();
                    unet = true;
                } catch (SQLException throwables) {
                    unet = false;
                    throwables.printStackTrace();
                    return unet;

                }


            }
        }


        return unet;

    }
}

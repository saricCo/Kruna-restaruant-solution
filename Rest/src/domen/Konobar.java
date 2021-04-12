package domen;

import java.io.Serializable;
import java.util.Objects;

public class Konobar implements Serializable {
    private int id;
    private String imeIPrezime;
    private String sifra;

    public Konobar() {
    }

    public Konobar(int id, String imeIPrezime, String sifra) {
        this.id = id;
        this.imeIPrezime = imeIPrezime;
        this.sifra = sifra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImeIPrezime() {
        return imeIPrezime;
    }

    public void setImeIPrezime(String imeIPrezime) {
        this.imeIPrezime = imeIPrezime;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Konobar konobar = (Konobar) o;
        return id == konobar.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

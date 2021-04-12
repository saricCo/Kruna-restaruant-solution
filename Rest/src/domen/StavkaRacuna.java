package domen;

import java.io.Serializable;
import java.util.Objects;

public class StavkaRacuna implements Serializable {
    private int id;
    private Proizvod proizvod;
    private int kolicina;

    public StavkaRacuna() {
    }

    public StavkaRacuna(int id, Proizvod proizvod, int kolicina) {
        this.id = id;
        this.proizvod = proizvod;
        this.kolicina = kolicina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StavkaRacuna that = (StavkaRacuna) o;
        return Objects.equals(proizvod, that.proizvod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proizvod);
    }
}

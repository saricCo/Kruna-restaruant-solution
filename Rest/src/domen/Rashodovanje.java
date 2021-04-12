package domen;

import java.util.Date;
import java.util.Objects;

public class Rashodovanje {
    private int id;
    private Proizvod proizvod;
    private VrstaRashodovanja razlog;
    private Date vreme;

    public Rashodovanje() {
    }

    public Rashodovanje(int id, Proizvod proizvodID, VrstaRashodovanja razlog, Date vreme) {
        this.id = id;
        this.proizvod = proizvodID;
        this.razlog = razlog;
        this.vreme = vreme;
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

    public VrstaRashodovanja getRazlog() {
        return razlog;
    }

    public void setRazlog(VrstaRashodovanja razlog) {
        this.razlog = razlog;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rashodovanje that = (Rashodovanje) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

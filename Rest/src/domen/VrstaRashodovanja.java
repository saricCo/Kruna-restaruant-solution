package domen;

import java.util.Objects;

public class VrstaRashodovanja {
    private int id;
    private String razlog;

    public VrstaRashodovanja(int id, String razlog) {
        this.id = id;
        this.razlog = razlog;
    }

    public VrstaRashodovanja() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazlog() {
        return razlog;
    }

    public void setRazlog(String razlog) {
        this.razlog = razlog;
    }

    @Override
    public String toString() {
        return razlog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VrstaRashodovanja that = (VrstaRashodovanja) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

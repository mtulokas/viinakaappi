package viinakaappi.viinakaappi.domain;

public class Drinkki {
    
    private int id;
    private String nimi;

    public Drinkki(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    @Override
    public String toString() {
        return "Drinkki{" + "id=" + id + ", nimi=" + nimi + '}';
    }
    
    
    
}

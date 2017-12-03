package viinakaappi.viinakaappi.domain;

public class Drinkki {
    
    private int id;
    private String nimi;
    private String ohje;

    public Drinkki(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Drinkki(int id, String nimi, String ohje) {
        this.id = id;
        this.nimi = nimi;
        this.ohje = ohje;
    }

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getOhje() {
        return ohje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

    @Override
    public String toString() {
        return "Drinkki{" + "id=" + id + ", nimi=" + nimi + ", ohje=" + ohje + '}';
    }


    
    
    
}

package viinakaappi.viinakaappi.domain;

public class Raakaaine {
    
    private int id;
    private String nimi;
    private String maara;

    public Raakaaine(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Raakaaine(int id, String nimi, String maara) {
        this.id = id;
        this.nimi = nimi;
        this.maara = maara;
    }
    

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getMaara() {
        return maara;
    }
        
    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    @Override
    public String toString() {
        return "Raakaaine{" + "id=" + id + ", nimi=" + nimi + '}';
    }

    
    
    
}

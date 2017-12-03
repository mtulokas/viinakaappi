package viinakaappi.viinakaappi.domain;

public class Raakaaine implements Comparable<Raakaaine>{

    private int id;
    private String nimi;
    private int saldo;
    private String maara;

    public Raakaaine(int id, String nimi, int saldo) {
        this.id = id;
        this.nimi = nimi;
        this.saldo = saldo;
    }

    public Raakaaine(int id, String nimi, int saldo, String maara) {
        this.id = id;
        this.nimi = nimi;
        this.saldo = saldo;
        this.maara = maara;
    }

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public int getSaldo() {
        return saldo;
    }
    
    public boolean getIfSaldo (){
        if (saldo == 1){
            return true;
        }
        return false;
    }
    
    public String getSaldoText () {
        if (saldo == 1){
            return "Kaapissa";
        }
        return "Ei löydy";
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

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    @Override
    public String toString() {
        return "Raakaaine{" + "id=" + id + ", nimi=" + nimi + ", saldo=" + saldo + ", maara=" + maara + '}';
    }

    @Override
    public int compareTo(Raakaaine t) {
        if (this.getIfSaldo() && !t.getIfSaldo()){
            return -1;
        } else if (!this.getIfSaldo() && t.getIfSaldo()){
            return 1;
        } else {
            return 0;
        }
            
            
    }

}

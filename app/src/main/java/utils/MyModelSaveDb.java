package utils;

public class MyModelSaveDb {
    double la,lo;
    String addresses, str_userid,token;

    public MyModelSaveDb() {
    }

    public MyModelSaveDb(double la, double lo, String addresses, String str_userid, String token) {
        this.la = la;
        this.lo = lo;
        this.addresses = addresses;
        this.str_userid = str_userid;
        this.token = token;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getStr_userid() {
        return str_userid;
    }

    public void setStr_userid(String str_userid) {
        this.str_userid = str_userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

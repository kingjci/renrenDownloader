package jc.entity;

/**
 * Created by ½ð³É on 2015/7/27.
 */
public class PublicKey {

    //private boolean isEncrypt;
    private String e;
    private String n;
    //private String maxdigits;
    private String rkey;


    public void setE(String e) {
        this.e = e;
    }

    public void setN(String n) {
        this.n = n;
    }

    public void setRkey(String rkey) {
        this.rkey = rkey;
    }

    public String getE() {
        return e;
    }

    public String getN() {
        return n;
    }

    public String getRkey() {
        return rkey;
    }

}

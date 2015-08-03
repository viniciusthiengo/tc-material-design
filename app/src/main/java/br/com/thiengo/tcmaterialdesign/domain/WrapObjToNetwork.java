package br.com.thiengo.tcmaterialdesign.domain;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public class WrapObjToNetwork {
    private Car car;
    private Contact contact;
    private String method;
    private boolean isNewer;
    private String term;


    public WrapObjToNetwork(Car car, String method, boolean isNewer) {
        this.car = car;
        this.method = method;
        this.isNewer = isNewer;
    }
    public WrapObjToNetwork(Car car, String method, String term) {
        this.car = car;
        this.method = method;
        this.term = term;
    }
    public WrapObjToNetwork(Car car, String method, Contact contact) {
        this.car = car;
        this.method = method;
        this.contact = contact;
    }


    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isNewer() {
        return isNewer;
    }

    public void setIsNewer(boolean isNewer) {
        this.isNewer = isNewer;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}

package ca.myandroid.mueataz.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private Account account;
    private String name;
    private String family;
    private String phone;
    private String sin;
    private String photo;

    public Customer(Account account, String name, String family, String phone, String sin, String photo) {
        this.account = account;
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.sin = sin;
        this.photo = photo;
    }

    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    public String getOpenDate() {
        return account.getOpenDate();
    }

    public String getBalance() {
        return account.getBalance();
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public String getPhone() {
        return phone;
    }

    public String getSin() {
        return sin;
    }

    public String getPhoto() {
        return photo.replace(".png", "");
    }
}

package ca.myandroid.mueataz.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountNumber;
    private String openDate;
    private String balance;

    public Account(String accountNumber, String openDate, String balance) {
        this.accountNumber = accountNumber;
        this.openDate = openDate;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getBalance() {
        return balance;
    }
}


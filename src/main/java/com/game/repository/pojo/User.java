package com.game.repository.pojo;

import java.io.Serializable;

public class User implements Serializable{


    private String name;

    private Double balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}

package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Account {

    @NotBlank ( message = "Account ID cannot be blank" )
    private int account_id;
    @NotBlank ( message = "Balance cannot be blank" )
    private BigDecimal balance;

    public Account(int account_id, BigDecimal balance) {
        this.account_id = account_id;
        this.balance = balance;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}

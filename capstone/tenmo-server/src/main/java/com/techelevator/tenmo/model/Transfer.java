package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private int transfer_status_id;
    private int account_from;
    private int account_to;
    private BigDecimal amount;
    private String transfer_type_desc;
    private String transfer_status_desc;
    private String userNameFrom;
    private String userNameTo;

    public Transfer() {}

    public Transfer( int transfer_id, int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal amount ) {
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }

    public Transfer(int transfer_id, int account_from, int account_to, BigDecimal amount, String transfer_type_desc, String transfer_status_desc) {
        this.transfer_id = transfer_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
        this.transfer_type_desc = transfer_type_desc;
        this.transfer_status_desc = transfer_status_desc;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }

    public void setUserNameFrom(String userNameFrom) {
        this.userNameFrom = userNameFrom;
    }

    public String getUserNameTo() {
        return userNameTo;
    }

    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getAccount_from(/*int account_from*/) {
        return this.account_from;
    }


    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getTransfer_type_desc() {
        return transfer_type_desc;
    }

    public void setTransfer_type_desc(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }

    public String getTransfer_status_desc() {
        return transfer_status_desc;
    }

    public void setTransfer_status_desc(String transfer_status_desc) {
        this.transfer_status_desc = transfer_status_desc;
    }

    @Override
    public String toString() {
        return "Transfer ID: " + transfer_id + "  ||  amount: " + amount;
    }

    public String display() {
        return "Id: " + transfer_id
        "\nFrom: " +
        "\nTo: " +
        "\nType: " +
        "\nStatus: " +
        "\nAmount: " +

    }
}

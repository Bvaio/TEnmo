package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransferJdbcDaoTest {

    private static final User USER_1 = new User(Long.valueOf(500), "weLoveTests", "weLoveTests", "Janice");
    private static final User USER_2 = new User(Long.valueOf(501), "sleepyTester", "sleepyTester", "Aja");
    private static final User USER_3 = new User(Long.valueOf(502), "almostDone", "almostDone", "Pedro");


    private static final Transfer TRANSFER_1 = new Transfer(1000, 501, 601, new BigDecimal(50), "send", "pending" );
    private static final Transfer TRANSFER_2 = new Transfer(1001, 502, 602, new BigDecimal(100), "send", "approved");
    private static final Transfer TRANSFER_3 = new Transfer(1002, 501, 501, new BigDecimal(100), "send", "pending");
    private static final Transfer TRANSFER_5 = new Transfer();

    private TransferJdbcDao sut;

    @Test
    public void userList_should_return_all_except_current_user() {
        List<User> testList = sut.userList(500);
        Assert.assertEquals(2, testList.size());

    }

    @Test
    public void transferList() {
    }

    @Test
    public void addTransfer() {
    }

    @Test
    public void sendBucks() {
    }
}

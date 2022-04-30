package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class AccountJdbcDaoTest extends BaseDaoTests{

    private static final Account ACCOUNT_1 = new Account(500, new BigDecimal(500));
    private static final Account ACCOUNT_2 = new Account(501, new BigDecimal(1000));
    private static final Account ACCOUNT_3 = new Account(502, new BigDecimal(500));
    private static final Account ACCOUNT_4 = new Account(503, new BigDecimal(50));

    private AccountJdbcDao sut;
    private Account testAccount;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        sut = new AccountJdbcDao(jdbcTemplate);
    }

    @Test
    public void getBalance_should_return_correct_balance_with_id() {

        BigDecimal testAccount = sut.getBalance(500);
        Assert.assertEquals(500, testAccount);

        BigDecimal testAccount2 = sut.getBalance(503);
        Assert.assertEquals(50, testAccount2);

    }
    @Test
    public void getBalance_should_return_null_when_id_not_found(){

        BigDecimal testAccount5 = sut.getBalance(504);
        Assert.assertNull(testAccount5);
    }
}

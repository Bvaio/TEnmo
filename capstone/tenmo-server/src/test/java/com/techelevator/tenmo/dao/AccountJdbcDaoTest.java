package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountJdbcDaoTest extends BaseDaoTests{
    private AccountJdbcDao accountJdbcDao;

    private static final Account ACCOUNT_1 = new Account(500, BigDecimal.valueOf(500));
    private static final Account ACCOUNT_2 = new Account(501, BigDecimal.valueOf(1000));
    private static final Account ACCOUNT_3 = new Account(502, BigDecimal.valueOf(500));
    private static final Account ACCOUNT_4 = new Account(503, BigDecimal.valueOf(50));

    @Before
    public void setUpData(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate( dataSource );
        accountJdbcDao = new AccountJdbcDao( jdbcTemplate );

    }

    @Test
    public void getBalance_should_return_correct_balance_with_id() {
        BigDecimal testAccount = accountJdbcDao.getBalance(500);

        BigDecimal expected = new BigDecimal( 500 );
        assertThat( ACCOUNT_1.getBalance() ).isEqualByComparingTo( testAccount );

        BigDecimal testAccount4 = accountJdbcDao.getBalance(503);
        assertThat( ACCOUNT_4.getBalance() ).isEqualByComparingTo( testAccount4 );

    }

    @Test
    public void getBalance_should_return_null_when_id_not_found(){
        BigDecimal testAccount5 = accountJdbcDao.getBalance(504);
        Assert.assertNull(testAccount5);
    }

}

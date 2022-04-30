package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountJdbcDaoTest extends BaseDaoTests{
    private static SingleConnectionDataSource dataSource;
    private AccountJdbcDao accountJdbcDao;

    private static final Account ACCOUNT_1 = new Account(500, BigDecimal.valueOf(500));
    private static final Account ACCOUNT_2 = new Account(501, BigDecimal.valueOf(1000));
    private static final Account ACCOUNT_3 = new Account(502, BigDecimal.valueOf(500));
    private static final Account ACCOUNT_4 = new Account(503, BigDecimal.valueOf(50));


    private Account testAccount;

    @BeforeClass
    public static void setUp() {
        dataSource = new SingleConnectionDataSource(); // opens and makes a single connection, requires you to close it though
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit( false );
    }

    @Before
    public void setUpData(){
        String sql = "INSERT INTO tenmo_user (user_id, username, password_hash)\n" +
                "VALUES (500, 'weLovesTests', 'unknown');\n" +
                "INSERT INTO tenmo_user (user_id, username, password_hash)\n" +
                "VALUES (501, 'sleepyTester', 'unknown');\n" +
                "INSERT INTO tenmo_user (user_id, username, password_hash)\n" +
                "VALUES (502, 'almostDone', 'unknown') ;\n" +
                "INSERT INTO tenmo_user (user_id, username, password_hash)\n" +
                "VALUES (503, 'funTimes', 'unknown');\n" +
                "\n" +
                "INSERT INTO account (account_id, user_id, balance) VALUES (400,500, 500) ;\n" +
                "INSERT INTO account (account_id, user_id, balance) VALUES (401, 501, 1000) ;\n" +
                "INSERT INTO account (account_id, user_id, balance) VALUES (402, 502, 500) ;\n" +
                "INSERT INTO account (account_id, user_id, balance) VALUES (403, 503, 50) ;\n" +
                "\n" +
                "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (300, 1, 2, 400, 401, 50);\n" +
                "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (301, 1, 2, 401, 402, 100);\n" +
                "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (302, 1, 2, 402, 403, 100);";

        JdbcTemplate jdbcTemplate = new JdbcTemplate( dataSource );
        jdbcTemplate.update( sql );
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

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @AfterClass
    public static void closeDataSource() {
        dataSource.destroy();
    }
}

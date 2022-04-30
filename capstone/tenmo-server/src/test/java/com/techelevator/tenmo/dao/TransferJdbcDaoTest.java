package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransferJdbcDaoTest extends BaseDaoTests {


//    private static final User USER_1 = new User(Long.valueOf(500), "weLoveTests", "weLoveTests", "Janice");
//    private static final User USER_2 = new User(Long.valueOf(501), "sleepyTester", "sleepyTester", "Aja");
//    private static final User USER_3 = new User(Long.valueOf(502), "almostDone", "almostDone", "Pedro");


//    private static final Transfer TRANSFER_1 = new Transfer(1000, 500, 601, new BigDecimal(50), "send", "pending" );
//    private static final Transfer TRANSFER_2 = new Transfer(1001, 501, 602, new BigDecimal(100), "send", "approved");
//    private static final Transfer TRANSFER_3 = new Transfer(1002, 502, 502, new BigDecimal(100), "send", "pending");
//    private static final Transfer TRANSFER_5 = new Transfer();

    private TransferJdbcDao sut;

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


    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @AfterClass
    public static void closeDataSource() {
        dataSource.destroy();
    }
}

package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TransferJdbcDaoTest extends BaseDaoTests {


//    private static final User USER_1 = new User(Long.valueOf(500), "weLoveTests", "weLoveTests", "Janice");
//    private static final User USER_2 = new User(Long.valueOf(501), "sleepyTester", "sleepyTester", "Aja");
//    private static final User USER_3 = new User(Long.valueOf(502), "almostDone", "almostDone", "Pedro");
//
//
//    private static final Transfer TRANSFER_1 = new Transfer(1000, 500, 601, new BigDecimal(50), "send", "pending" );
//    private static final Transfer TRANSFER_2 = new Transfer(1001, 501, 602, new BigDecimal(100), "send", "approved");
//    private static final Transfer TRANSFER_3 = new Transfer(1002, 502, 502, new BigDecimal(100), "send", "pending");
//    private static final Transfer TRANSFER_5 = new Transfer();








    private TransferJdbcDao transferJdbcDao;
    private JdbcUserDao jdbcUserDao;
    private AccountJdbcDao accountJdbcDao;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        accountJdbcDao = new AccountJdbcDao(jdbcTemplate);
        transferJdbcDao = new TransferJdbcDao(jdbcTemplate, accountJdbcDao, jdbcUserDao);
    }

    @Test
    public void userList_should_return_all_except_current_user() {
        List<User> testList = transferJdbcDao.userList(500);
        Assert.assertEquals(3, testList.size());

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


//    @After
//    public void rollback() throws SQLException {
//        dataSource.getConnection().rollback();
//    }
//
//    @AfterClass
//    public static void closeDataSource() {
//        dataSource.destroy();
//    }
}

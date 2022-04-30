package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TransferJdbcDaoTest extends BaseDaoTests {


//    private static final User USER_1 = new User(Long.valueOf(500), "weLoveTests", "weLoveTests", "Janice");
//    private static final User USER_2 = new User(Long.valueOf(501), "sleepyTester", "sleepyTester", "Aja");
//    private static final User USER_3 = new User(Long.valueOf(502), "almostDone", "almostDone", "Pedro");


    //    private static final Transfer TRANSFER_1 = new Transfer(1000, 500, 601, new BigDecimal(50), "send", "pending" );
//    private static final Transfer TRANSFER_2 = new Transfer(1001, 501, 602, new BigDecimal(100), "send", "approved");
//    private static final Transfer TRANSFER_3 = new Transfer(1002, 502, 502, new BigDecimal(100), "send", "pending");
//    private static final Transfer TRANSFER_5 = new Transfer();
//    private static SingleConnectionDataSource dataSource;
    private TransferJdbcDao transferJdbcDao;
    private JdbcUserDao jdbcUserDao;
    private AccountJdbcDao accountJdbcDao;
    public Transfer testAddedTransfer;

    @Before
    public void setup(){
        JdbcTemplate  jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        accountJdbcDao = new AccountJdbcDao(jdbcTemplate);
        transferJdbcDao = new TransferJdbcDao(jdbcTemplate, accountJdbcDao, jdbcUserDao);
        testAddedTransfer = new Transfer(1, 2, 400,403, BigDecimal.valueOf(50));
    }


    @Test
    public void userList_should_return_all_except_userId_500() {
        boolean isInList = false;

        List<User> testList = transferJdbcDao.userList(500);
        Assert.assertEquals(3, testList.size());
        for (User user : testList) {
            isInList = user.getId() == 500;
        }
        Assert.assertFalse(isInList);

    }

    @Test
    public void transferList_should_return_list_of_transferHistory_for_userId501() {

        List<Transfer> testTransferList = transferJdbcDao.transferList(501);
        Assert.assertEquals(2, testTransferList.size());
        int countFrom = 0;
        int countTo = 0;
        for(Transfer transfer: testTransferList) {
            if (transfer.getUserNameFrom().equals("sleepyTester")) {
                countFrom++;
            }
            if (transfer.getUserNameTo().equals("sleepyTester")) {
                countTo++;
            }
        }
        Assert.assertEquals(1, countFrom);
        Assert.assertEquals(1, countTo);
    }

    @Test
    public void addTransfer_should_increase_user() {

        boolean testAddTransfer = transferJdbcDao.addTransfer(500, testAddedTransfer);
        Assert.assertTrue(testAddTransfer);

       int newId = testAddedTransfer.getTransfer_id();

       Assert.assertTrue(newId == 3001);



//       Assert.assertEquals(testAddedTransfer, testAddTransfer);
    }

    @Test
    public void sendBucks() {
    }


}

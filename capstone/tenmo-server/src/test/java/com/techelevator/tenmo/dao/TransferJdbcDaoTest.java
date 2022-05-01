package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferJdbcDaoTest extends BaseDaoTests {
    private TransferJdbcDao transferJdbcDao;
    private JdbcUserDao jdbcUserDao;
    private AccountJdbcDao accountJdbcDao;
    public Transfer testAddedTransfer;
    private Transfer testSendMoreThanHave;

    @Before
    public void setup(){
        JdbcTemplate  jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        accountJdbcDao = new AccountJdbcDao(jdbcTemplate);
        transferJdbcDao = new TransferJdbcDao(jdbcTemplate, accountJdbcDao, jdbcUserDao);
        testAddedTransfer = new Transfer(2, 2, 400,403, BigDecimal.valueOf(50));
        testSendMoreThanHave = new Transfer(2, 2, 402, 400, BigDecimal.valueOf(10000));
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
    public void adding_a_newTransfer_should_return_true() {
        int simulateUserInput = 503;
        testAddedTransfer.setAccount_to( simulateUserInput );

        boolean testAddTransfer = transferJdbcDao.addTransfer(500, testAddedTransfer);
        Assert.assertTrue(testAddTransfer);
    }

    @Test
    public void sendBucks() {
        int simulateUserInput = 503;
        testAddedTransfer.setAccount_to( simulateUserInput );

        transferJdbcDao.addTransfer(500, testAddedTransfer);
        boolean testSendBucks = transferJdbcDao.sendBucks(500, testAddedTransfer);
        Assert.assertTrue(testSendBucks);

        BigDecimal testAccountFrom = accountJdbcDao.getBalance(500);
        BigDecimal expectFrom = BigDecimal.valueOf( 450 );
        assertThat( testAccountFrom  ).isEqualByComparingTo( expectFrom );

        BigDecimal testAccountTo = accountJdbcDao.getBalance(503);
        BigDecimal expectTo = BigDecimal.valueOf( 100 );
        assertThat( testAccountTo ).isEqualByComparingTo( expectTo );
    }

    @Test
    public void sendBucks_should_return_false_if_notEnoughMoney(){
        int userInput = 500;
        testSendMoreThanHave.setAccount_to(userInput);
        transferJdbcDao.addTransfer(502, testSendMoreThanHave);

        boolean testSendBucks = transferJdbcDao.sendBucks(502, testSendMoreThanHave);
        Assert.assertFalse(testSendBucks);

        BigDecimal testAccountSender = accountJdbcDao.getBalance(502);
        BigDecimal expectFrom = BigDecimal.valueOf( 500 );
        assertThat( testAccountSender  ).isEqualByComparingTo( expectFrom );

        BigDecimal testAccountTo = accountJdbcDao.getBalance(500);
        BigDecimal expectTo = BigDecimal.valueOf( 500 );
        assertThat( testAccountTo ).isEqualByComparingTo( expectTo );

    }
}

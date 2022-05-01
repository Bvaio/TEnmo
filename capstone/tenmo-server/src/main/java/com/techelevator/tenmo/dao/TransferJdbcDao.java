package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferJdbcDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    private AccountJdbcDao accountJdbcDao;
    private JdbcUserDao jdbcUserDao;

    public TransferJdbcDao(JdbcTemplate jdbcTemplate, AccountJdbcDao accountJdbcDao, JdbcUserDao jdbcUserDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountJdbcDao = accountJdbcDao;
        this.jdbcUserDao = jdbcUserDao;
    }


    @Override
    public List<User > userList(int user_id) {
        List< User > users = new ArrayList<>();
        for (User user: jdbcUserDao.findAll()) {
            if(user.getId() != user_id){
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public List< Transfer > transferList( int from_id ) {
        List< Transfer > transfers = new ArrayList<>();
        int userAccountId = getAccountIdFromUserId( from_id );
        String sql = "SELECT transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount FROM transfer WHERE account_from = ? OR account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet( sql, userAccountId, userAccountId );
        while ( results.next() ) {
            Transfer transfer = new Transfer();
            transfer.setTransfer_id(results.getInt("transfer_id"));
            transfer.setTransfer_type_id(results.getInt("transfer_type_id"));
            transfer.setTransfer_status_id(results.getInt("transfer_status_id"));
            transfer.setUserNameFrom( getUsernameFromAccountId( results.getInt("account_from") ) );
            transfer.setUserNameTo( getUsernameFromAccountId( results.getInt("account_to") ) );
            transfer.setAmount(results.getBigDecimal("amount"));

            transfers.add( transfer );
        }
        return transfers;
    }

    @Override
    public Transfer transferListDetail( int from_id, int transfer_id ) {
        Transfer transfer = null;
        int userAccountId = getAccountIdFromUserId( from_id );
        String sql = "SELECT transfer_id, transfer_status_desc, transfer_type_desc, account_from, account_to, amount " +
                "FROM transfer JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "WHERE account_from = ? OR account_to = ? AND transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet( sql, userAccountId, userAccountId, transfer_id );
        while ( results.next() ) {
            transfer = mapRowToTransferDisplay( results );
        }
        return transfer;
    }

    @Override
    public boolean addTransfer( int from_id, Transfer transfer ) {
        String sql = "INSERT INTO transfer ( transfer_type_id, transfer_status_id, account_from, account_to, amount ) VALUES ( ?, ?, ?, ?, ? );";
        return jdbcTemplate.update( sql, transfer.getTransfer_type_id(), transfer.getTransfer_status_id(), getAccountIdFromUserId( from_id )
                , getAccountIdFromUserId( transfer.getAccount_to() ), transfer.getAmount() ) == 1;
    }

    @Override
    public boolean sendBucks( int from_id, Transfer transfer ) {
        int fromAccountID = getAccountIdFromUserId(from_id);
        boolean updateStatusSuccess = false;

        if ( isSameAccountId( from_id, transfer.getAccount_to() ) && transfer.getTransfer_type_id() == 2 && transfer.getTransfer_status_id() == 2 ) {
            int toAccountID = getAccountIdFromUserId( transfer.getAccount_to() );
            boolean updateBalance = false;

            if ( checkBalanceAccount(from_id, transfer.getAmount()) ) {
                updateBalance = updatedBalance(fromAccountID, toAccountID, transfer.getAmount());
//                if (updateBalance) {
//                    String updateStatus = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
//                    return jdbcTemplate.update( updateStatus, transfer.getTransfer_id() ) == 1;
//                } else {
//                    String updateStatus = "UPDATE transfer SET transfer_status_id = 3 WHERE transfer_id = ?";
//                    jdbcTemplate.update( updateStatus, transfer.getTransfer_id() );
//                }
                updateStatusSuccess = updateBalance;
            }
        }
        return updateStatusSuccess;
    }

    private boolean checkBalanceAccount( int from_id, BigDecimal amount ) {
        return accountJdbcDao.getBalance( from_id ).compareTo( amount ) >= 0;
    }

    private boolean isSameAccountId( int from_id, int to_id ) {
        return from_id != to_id;
    }

    private int getAccountIdFromUserId( int user_id ) {
        int account_id = 0;
        String sql = "SELECT account_id FROM account WHERE user_id = ( SELECT user_id FROM tenmo_user WHERE user_id = ? );";
        SqlRowSet results = jdbcTemplate.queryForRowSet( sql, user_id );
        if ( results.next() ) {
            account_id = results.getInt( "account_id" );
        }
        return account_id;
    }

    private String getUsernameFromAccountId( int account_id ) {
        String username = "";
        String sql = "SELECT username FROM tenmo_user WHERE user_id = ( SELECT user_id FROM account WHERE account_id = ? );";
        SqlRowSet findUsername = jdbcTemplate.queryForRowSet( sql, account_id );
        if ( findUsername.next() ) {
            username = findUsername.getString( "username" );
        }
        return username;
    }

    private boolean updatedBalance( int from_id, int to_id, BigDecimal amount ) {
        boolean toAccountSuccess = false;
        boolean fromAccountSuccess = false;

        String transferToAccount = "UPDATE account SET balance = ( balance + ? ) WHERE account_id = ?;";
        toAccountSuccess = jdbcTemplate.update( transferToAccount, amount, to_id ) == 1;

        String transferFromAccount = "UPDATE account SET balance = ( balance - ? ) WHERE account_id = ?;";
        fromAccountSuccess = jdbcTemplate.update( transferFromAccount, amount, from_id ) == 1;

        return toAccountSuccess && fromAccountSuccess;
    }

//    private Transfer mapRowToTransfer(SqlRowSet results) {
//        Transfer transfer = new Transfer();
//        transfer.setTransfer_id(results.getInt("transfer_id"));
//        transfer.setTransfer_type_id(results.getInt("transfer_type_id"));
//        transfer.setTransfer_status_id(results.getInt("transfer_status_id"));
//        transfer.setAccount_from(results.getInt("account_from"));
//        transfer.setAccount_to(results.getInt("account_to"));
//        transfer.setAmount(results.getBigDecimal("amount"));
//        return transfer;
//    }

    private Transfer mapRowToTransferDisplay( SqlRowSet results ) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(results.getInt("transfer_id"));
        transfer.setTransfer_type_desc( results.getString("transfer_type_desc") );
        transfer.setTransfer_status_desc( results.getString("transfer_status_desc") );
        transfer.setUserNameFrom( getUsernameFromAccountId( results.getInt("account_from") ) );
        transfer.setUserNameTo( getUsernameFromAccountId( results.getInt("account_to") ) );
        transfer.setAmount(results.getBigDecimal("amount") );
        return transfer;
    }


}

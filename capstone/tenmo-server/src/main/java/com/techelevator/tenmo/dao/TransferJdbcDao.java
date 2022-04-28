package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferJdbcDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    private AccountJdbcDao accountJdbcDao;

    public TransferJdbcDao(JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountJdbcDao = new AccountJdbcDao( jdbcTemplate );
    }


    @Override
    public List< String > userList() {
        List< String > users = new ArrayList<>();

        String sql = "SELECT user_id, username FROM tenmo_user";
        SqlRowSet results = jdbcTemplate.queryForRowSet( sql );
        while( results.next() ) {
//            User user = new User();
//            user.setId( results.getLong( "user_id" ) );
//            user.setUsername( results.getString( "username") );
//            user.setActivated( results.getBoolean( "activated" ) );
//            user.setAuthorities( results.getRow( "authorities" ) );
            long id = results.getLong( "user_id" );
            String username = results.getString( "username");


            users.add( "ID: " + id + "  ||  Username: " + username );
        }

        return users;
    }

//    transfer Type : { 1 : Request, 2 : Send }
//    transfer status: { 1 : Pending, 2 : Approved, 3 : Rejected }

//    @Override
    public Transfer addTransfer(int from_id, int to_id, BigDecimal amount ) {
        Transfer transfer = null;

        String sql = "INSERT INTO transfer ( transfer_type_id, transfer_status_id, account_from, account_to, amount ) VALUES ( 1, 1, ?, ?, ? ) RETURNING transfer_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet( sql, getAccountIdFromUserId( from_id ), getAccountIdFromUserId( to_id ), amount );

        if ( results.next() ) {
            transfer = new Transfer();
            transfer.setTransfer_id( results.getInt( "transfer_id" ) );
            transfer.setAccount_from(results.getInt("account_from"));
            transfer.setAccount_to(results.getInt("account_to"));
            transfer.setAmount(results.getBigDecimal("amount"));
        }

        return transfer;
    }

    @Override
    public boolean sendBucks( int from_id, int to_id, BigDecimal amount ) {
        Transfer transfer = null;
        boolean updateBalance = false;
        boolean updateStatusSuccess = false;

        if ( checkBalanceAccount( from_id, amount ) && isSameAccountId( from_id, to_id ) ) {
            transfer = addTransfer( from_id, to_id, amount );
            updateBalance = updatedBalance( from_id, to_id, amount );
            if ( updateBalance ) {
                String updateStatus = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
                updateStatusSuccess = jdbcTemplate.update(updateStatus, transfer.getTransfer_id()) == 1;
            }
        }
        return updateStatusSuccess;
    }

    @Override
    public List<Transfer> transferList(int from_id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, getAccountIdFromUserId(from_id));
        while (results.next()) {
            Transfer transfer = new Transfer();
            transfer.setTransfer_id(results.getInt("transfer_id"));
            transfer.setTransfer_type_id(results.getInt("transfer_type_id"));
            transfer.setAmount(results.getBigDecimal("amount"));
            transfer.setTransfer_status_id(results.getInt("transfer_status"));
            transfer.setAccount_from(results.getInt("account_from"));
            transfer.setAccount_to(results.getInt("account_to"));
            transfers.add(transfer);
        }
        return transfers;
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

    private boolean updatedBalance( int from_id, int to_id, BigDecimal amount ) {
        boolean toAccountSuccess = false;
        boolean fromAccountSuccess = false;

        String transferToAccount = "UPDATE account SET balance = ( balance + ? ) WHERE user_id = ?;";
        toAccountSuccess = jdbcTemplate.update(transferToAccount, amount, to_id) == 1;

        String transferFromAccount = "UPDATE account SET balance = ( balance - ? ) WHERE user_id = ?;";
        fromAccountSuccess = jdbcTemplate.update( transferFromAccount, amount, from_id ) == 1;

        return toAccountSuccess && fromAccountSuccess;
    }
}

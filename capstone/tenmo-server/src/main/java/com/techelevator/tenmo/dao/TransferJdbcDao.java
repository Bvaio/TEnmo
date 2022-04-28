package com.techelevator.tenmo.dao;

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

    public TransferJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public boolean sendBucks( int from_id, int to_id, BigDecimal amount ) {
        boolean toAccountSuccess = false;
        boolean fromAccountSuccess = false;

        String transferToAccount = "UPDATE account SET balance = balance + ? WHERE user_id = ?";
        toAccountSuccess = jdbcTemplate.update( transferToAccount, amount, to_id ) == 1;

        String transferFromAccount = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
        fromAccountSuccess = jdbcTemplate.update( transferFromAccount, amount, from_id ) == 1;


        return toAccountSuccess && fromAccountSuccess;
    }
}

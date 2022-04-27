package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

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
            User user = new User();
            user.setId( results.getLong( "user_id" ) );
            user.setUsername( results.getString( "username") );
//            user.setActivated( results.getBoolean( "activated" ) );
//            user.setAuthorities( results.getRow( "authorities" ) );

            users.add( user.toString() );
        }

        return users;
    }
}

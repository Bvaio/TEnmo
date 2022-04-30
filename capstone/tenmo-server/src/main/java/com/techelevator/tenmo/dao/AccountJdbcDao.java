package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class AccountJdbcDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public AccountJdbcDao( JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance( int id ) {
        String sql = ("SELECT balance FROM account WHERE user_id = ( SELECT user_id FROM tenmo_user WHERE user_id = ? )");
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return results.getBigDecimal("balance");
        }
        return null;
    }

}

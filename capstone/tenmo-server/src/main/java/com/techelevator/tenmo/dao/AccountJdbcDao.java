package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountJdbcDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public AccountJdbcDao( JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance( int current_account_id ) {
        String sql = ("SELECT balance FROM account WHERE user_id = ( SELECT user_id FROM tenmo_user WHERE user_id = ? )");
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, current_account_id);
        if (results.next()) {
            return results.getBigDecimal("balance");
        }
        return null;
    }

}

package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public List< String > userList();
    public boolean sendBucks( int from_id, int to_id, BigDecimal amount );

}

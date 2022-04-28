package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public List< String > userList();
    public boolean addTransfer( int from_id, Transfer transfer );
    public boolean sendBucks( int from_id, Transfer transfer );

//    public List<Transfer> transferList(int from_id);
    public List<String> transferList( int from_id );

}

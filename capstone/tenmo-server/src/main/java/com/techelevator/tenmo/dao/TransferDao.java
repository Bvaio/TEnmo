package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {
    public List<User > userList(int user_id);
    public boolean addTransfer( int from_id, Transfer transfer );
    public boolean sendBucks( int from_id, Transfer transfer );

    public List< Transfer > transferList(int from_id);
    public Transfer transferListDetail( int from_id, int transfer_id );

}

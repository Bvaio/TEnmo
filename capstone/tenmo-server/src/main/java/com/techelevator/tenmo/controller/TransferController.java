package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize( "isAuthenticated()" )
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping( path = "/users", method = RequestMethod.GET )
    public List< User > listUsers(Principal principal) {
        return transferDao.userList(userDao.findIdByUsername(principal.getName()));
    }

//    @RequestMapping(path = "/list", method = RequestMethod.GET)
//    public List<Transfer> transferList( /*Principal principal */){
//        return transferDao.transferList( /*userDao.findIdByUsername( principal.getName() )*/ );
//    }
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<String> transferList( Principal principal ){
        return transferDao.transferList( userDao.findIdByUsername( principal.getName() ) );
    }

//    @RequestMapping( path = "/transfer/{to_id}/{amount}", method = RequestMethod.POST )
//    public boolean addTransfer( Principal principal, @PathVariable int to_id, @PathVariable BigDecimal amount ) {
//        return transferDao.sendBucks( userDao.findIdByUsername( principal.getName() ), to_id, amount );
//    }
    @RequestMapping( path = "/transfer", method = RequestMethod.POST )
    public boolean addTransfer( Principal principal, @RequestBody Transfer transfer ) {
        return transferDao.addTransfer( userDao.findIdByUsername( principal.getName() ), transfer );
    }

    @RequestMapping( path = "/transfer/send", method = RequestMethod.PUT )
    public boolean sendBucks(Principal principal, @RequestBody Transfer transfer ) {
        return transferDao.sendBucks( userDao.findIdByUsername(principal.getName()), transfer );
    }

}

package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

//    @RequestMapping( path = "/transfer", method = RequestMethod.GET )
//    public List<String> listUsers() {
//        return transferDao.userList();
//    }
    @RequestMapping( path = "/users", method = RequestMethod.GET )
    public List< String > listUsers() {
        return transferDao.userList();
    }

    @RequestMapping( path = "/transfer/{id}", method = RequestMethod.PUT )
    public boolean sendBucks(Principal principal, @PathVariable int id, BigDecimal amount ) {
        return transferDao.sendBucks( userDao.findIdByUsername(principal.getName()), id, amount );
    }
}

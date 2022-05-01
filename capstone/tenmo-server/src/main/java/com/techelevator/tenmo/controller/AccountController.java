package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize( "isAuthenticated()" )
public class AccountController {
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao ){
        this.accountDao = accountDao;
        this.userDao = userDao;

    }

    @RequestMapping( value = "/account/balance", method = RequestMethod.GET )
    public BigDecimal getBalance( Principal principal ) {
        return accountDao.getBalance( userDao.findIdByUsername( principal.getName() ) );

    }

}

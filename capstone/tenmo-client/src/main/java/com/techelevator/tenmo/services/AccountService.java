package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.apiguardian.api.API;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.math.BigDecimal;


public class AccountService {
    private final String API_BASE_URL; /* = "http://localhost:8080/account/"; */
//    private String authenticationToken;
    private RestTemplate restTemplate = new RestTemplate();

//    public void setAuthenticationToken( String token ) {
//        this.authenticationToken = token;
//    }

    public AccountService( String url ) {
        this.API_BASE_URL = url;
    }

    public BigDecimal viewCurrentBalance( AuthenticatedUser user ) {
        BigDecimal balance = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        headers.setBearerAuth( user.getToken() );
        HttpEntity< Account > entity = new HttpEntity<>( headers );
        try {
            balance = restTemplate.exchange( API_BASE_URL + "account/balance", HttpMethod.GET, entity, BigDecimal.class ).getBody();
        } catch ( RestClientResponseException ex ) {
            BasicLogger.log( ex.getMessage() );
        }
        return balance;
//        Account account = null;
//        try {
//            account = restTemplate.getForObject(API_BASE_URL + "/account/balance", Account.class );
//        } catch ( RestClientResponseException ex ) {
//            ex.getMessage();
//        }
//        return account.getBalance();
    }

}

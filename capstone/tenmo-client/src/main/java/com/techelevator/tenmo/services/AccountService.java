package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.math.BigDecimal;

public class AccountService {
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService( String url ) {
        API_BASE_URL = url;
    }

    public BigDecimal viewCurrentBalance( AuthenticatedUser currentUser ) {
        BigDecimal balance = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        headers.setBearerAuth( currentUser.getToken() );
        HttpEntity< Account > entity = new HttpEntity<>( headers );
        try {
            balance = restTemplate.exchange( API_BASE_URL + "account/balance", HttpMethod.GET, entity, BigDecimal.class ).getBody();
        } catch ( RestClientResponseException ex ) {
            BasicLogger.log( ex.getMessage() );
        }
        return balance;
    }

}

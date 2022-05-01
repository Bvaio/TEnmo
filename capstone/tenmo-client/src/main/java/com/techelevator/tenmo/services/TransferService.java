package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class TransferService {
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService( String url ) {
        API_BASE_URL = url;
    }

    public User[] listUsers(AuthenticatedUser currentUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<String[]> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET, entity, User[].class).getBody();
    }

    public Transfer[] transferList(AuthenticatedUser currentUser){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer[]> entity = new HttpEntity<>(httpHeaders);

        Transfer[] transfers = null;
        try{
            transfers = restTemplate.exchange(API_BASE_URL + "/list", HttpMethod.GET, entity, Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return transfers;
    }

    public Transfer transferListDetail(AuthenticatedUser currentUser, int transfer_id ){ // work on getting this to work
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(httpHeaders);

        Transfer transfer = null;
        try{
            transfer = restTemplate.exchange(API_BASE_URL + "/list/" + transfer_id, HttpMethod.GET, entity, Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return transfer;
    }

    public boolean addTransfer(AuthenticatedUser currentUser, Transfer transfer){
        boolean success = false;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        try {
            success = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.POST, entity, boolean.class).getBody();
        }catch (RestClientResponseException ex){
            BasicLogger.log(ex.getMessage());
        }
        return success;
    }

    public boolean sendBucks(AuthenticatedUser currentUser, Transfer transfer ) {
       boolean success = false;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        try {
            success = restTemplate.exchange(API_BASE_URL + "/transfer/send", HttpMethod.PUT, entity, Boolean.class).getBody();
        }catch (RestClientResponseException ex){
            BasicLogger.log(ex.getMessage());
        }
        return success;
    }

}

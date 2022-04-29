package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import io.cucumber.java.bs.A;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        System.out.println(accountService.viewCurrentBalance(currentUser));
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        Transfer[] transfers = transferService.transferList( currentUser );
        for (Transfer transfer : transfers ) {
            System.out.println();
            System.out.println( transfer.details() );
            System.out.println();
        }
        int findTransfer = consoleService.promptForInt("Select ID to check: ");
        String printDetail = "";
        for ( Transfer transfer : transfers ) {
            if ( transfer.getTransfer_id() == findTransfer ) {
                printDetail = transferService.transferListDetail( currentUser, findTransfer);
            }
        }
        if ( printDetail.length() > 0 ) {
            System.out.println( printDetail );
        } else {
            System.out.println( "Transfer Id not found" );
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        User[] users = transferService.listUsers(currentUser);
        for (User user : users) {
            System.out.println(user.getId() + " " + user.getUsername());
        }
        int toUserId = consoleService.promptForInt("Please enter the user id of the recipient: ");
        boolean isInList = false;
        for (User user : users) {
            if (user.getId() == toUserId) {
                isInList = true;
                break;
            }
        }
        if (isInList) {
            BigDecimal amountToSend = consoleService.promptForBigDecimal("Please enter the amount of money you wish to send: $");
            if (amountToSend.compareTo(BigDecimal.valueOf(0)) > 0) {
                Transfer transfer = new Transfer();
                transfer.setTransfer_type_id(2);
                transfer.setTransfer_status_id(1);
                transfer.setAccount_from(currentUser.getUser().getId().intValue());
                transfer.setAccount_to(toUserId);
                transfer.setAmount(amountToSend);

                transferService.addTransfer(currentUser, transfer);
                transferService.sendBucks(currentUser, transfer);
            } else {
                System.out.println("Please enter a valid amount.");
            }
        } else {
            System.out.println("Please enter a valid user id number.");
        }
    }


//        BigDecimal amountToSend = consoleService.promptForBigDecimal("amount to send");
//        System.out.println(transferService.sendBucks());


    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}

package ua.artcode.service;

import ua.artcode.exception.AppException;
import ua.artcode.model.AccountType;
import ua.artcode.model.UserAccount;

import java.util.Set;

public interface SimpleAuthenticationService {

    UserAccount authenticate(String username, String password) throws AppException;

    UserAccount registrate(String username, String password, String email, AccountType accountType) throws AppException;

    UserAccount registrate(String username, String password, String email) throws AppException;

    Set<String> getAllUser();

}

package com.brainwave.atmapp.service;

import com.brainwave.atmapp.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto, String pin);

    AccountDto getAccountByCardNumber(String atmCardNumber);

    AccountDto depositByCardNumber(String atmCardNumber, double amount);

    AccountDto withdrawByCardNumber(String atmCardNumber, double amount);

    List<AccountDto> getAllAccounts();

    void deleteAccountByCardNumber(String atmCardNumber);

    String authenticateAccount(String atmCardNumber, String pin);

    AccountDto transferFunds(String fromCardNumber, String toCardNumber, double amount);

    void changePin(String atmCardNumber, String oldPin, String newPin);

}
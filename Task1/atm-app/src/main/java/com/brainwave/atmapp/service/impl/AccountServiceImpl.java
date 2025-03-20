package com.brainwave.atmapp.service.impl;

import com.brainwave.atmapp.dto.AccountDto;
import com.brainwave.atmapp.entity.Account;
import com.brainwave.atmapp.exception.AccountException;
import com.brainwave.atmapp.mapper.AccountMapper;
import com.brainwave.atmapp.repository.AccountRepository;
import com.brainwave.atmapp.service.AccountService;
import com.brainwave.atmapp.util.HashUtil;
import com.brainwave.atmapp.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;

    public AccountServiceImpl(AccountRepository accountRepository, JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }



    @Override
    public AccountDto createAccount(AccountDto accountDto, String pin) {
        String hashedPin = HashUtil.sha256Hash(pin);
        String generatedCardNumber = generateUniqueCardNumber();

        Account newAccount = new Account(null,
                accountDto.accountHolderName(),
                accountDto.balance(),
                generatedCardNumber,
                hashedPin);

        Account savedAccount = accountRepository.save(newAccount);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountByCardNumber(String atmCardNumber) {
        Account account = accountRepository.findByAtmCardNumber(atmCardNumber)
                .orElseThrow(() -> new AccountException("Account not found"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto depositByCardNumber(String atmCardNumber, double amount) {
        Account account = accountRepository.findByAtmCardNumber(atmCardNumber)
                .orElseThrow(() -> new AccountException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public AccountDto withdrawByCardNumber(String atmCardNumber, double amount) {
        Account account = accountRepository.findByAtmCardNumber(atmCardNumber)
                .orElseThrow(() -> new AccountException("Account not found"));
        if (account.getBalance() < amount) {
            throw new AccountException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> Accounts = accountRepository.findAll();
        return Accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccountByCardNumber(String atmCardNumber) {
        Account account = accountRepository.findByAtmCardNumber(atmCardNumber)
                .orElseThrow(() -> new AccountException("Account not found"));
        accountRepository.delete(account);
    }


    @Override
    public String authenticateAccount(String atmCardNumber, String pin) {
        Account account = accountRepository.findByAtmCardNumber(atmCardNumber)
                .orElseThrow(() -> new AccountException("Account not found"));

        String hashedPin = HashUtil.sha256Hash(pin);
        if (!hashedPin.equals(account.getPin())) {
            throw new AccountException("Invalid credentials");
        }

        return jwtUtil.generateToken(atmCardNumber);
    }

    @Override
    public AccountDto transferFunds(String fromCardNumber, String toCardNumber, double amount) {
        Account fromAccount = accountRepository.findByAtmCardNumber(fromCardNumber)
                .orElseThrow(() -> new AccountException("Sender account not found"));

        Account toAccount = accountRepository.findByAtmCardNumber(toCardNumber)
                .orElseThrow(() -> new AccountException("Receiver account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new AccountException("Insufficient funds for transfer");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return AccountMapper.mapToAccountDto(fromAccount);
    }

    @Override
    public void changePin(String atmCardNumber, String oldPin, String newPin) {
        Account account = accountRepository.findByAtmCardNumber(atmCardNumber)
                .orElseThrow(() -> new AccountException("Account not found"));

        String hashedOldPin = HashUtil.sha256Hash(oldPin);
        if (!hashedOldPin.equals(account.getPin())) {
            throw new AccountException("Incorrect old PIN");
        }

        String hashedNewPin = HashUtil.sha256Hash(newPin);
        account.setPin(hashedNewPin);
        accountRepository.save(account);
    }



    private String generateUniqueCardNumber() {
        Random random = new SecureRandom();
        String cardNumber;
        boolean exists;

        do {
            StringBuilder numberBuilder = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                numberBuilder.append(random.nextInt(10));
            }
            cardNumber = numberBuilder.toString();
            exists = accountRepository.findByAtmCardNumber(cardNumber).isPresent();
        } while (exists);

        return cardNumber;
    }

}

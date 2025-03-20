package com.brainwave.atmapp.mapper;

import com.brainwave.atmapp.dto.AccountDto;
import com.brainwave.atmapp.entity.Account;


public class AccountMapper {

    public static Account mapToAccount(AccountDto accountDto, String pin){
        return new Account(
                accountDto.id(),
                accountDto.accountHolderName(),
                accountDto.balance(),
                accountDto.atmCardNumber(),
                pin
        );
    }

    public static AccountDto mapToAccountDto(Account account){
        return new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance(),
                account.getAtmCardNumber()
        );
    }
}
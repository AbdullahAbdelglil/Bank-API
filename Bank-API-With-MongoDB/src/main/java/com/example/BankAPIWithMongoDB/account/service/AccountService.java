package com.example.BankAPIWithMongoDB.account.service;

import com.example.BankAPIWithMongoDB.account.entity.Account;

import java.util.List;

public interface AccountService {
    String generateAccountNumber(String owner);

    Account save(Account account);

    Account update(Integer id, Account updatedAccount);

    Account getAccountById(Integer id);

    Account getAccountByAccountNumber(String accountNumber);

    List<Account> getAllAccounts();

    Boolean deleteAccount(Integer id);

    Boolean withdraw(String accountId, Double value);

    Boolean deposit(String accountId, Double value);

    Boolean transfer(String fromAccountNumber, String toAccountNumber, Double value);

}

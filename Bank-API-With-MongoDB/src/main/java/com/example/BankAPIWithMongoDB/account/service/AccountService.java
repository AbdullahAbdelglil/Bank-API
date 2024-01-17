package com.example.BankAPIWithMongoDB.account.service;

import com.example.BankAPIWithMongoDB.account.entity.Account;

import java.util.List;

public interface AccountService {

    Boolean save(Account account);
    Boolean update(Integer id, Account updatedAccount);
    Account getAccountById(Integer id);

    List<Account> getAllAccounts();

    Boolean deleteAccount(Integer id);

    Boolean withdraw(Integer accountId, Double value);

    Boolean deposit (Integer accountId, Double value);

    Boolean transfer(Integer fromAccountId, Integer toAccountId, Double value);

}

package com.example.BankAPIWithMongoDB.account.repository;

import com.example.BankAPIWithMongoDB.account.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface AccountRepository extends MongoRepository<Account,Integer> {
    boolean existsAccountByOwner(String owner);
    Account findAccountByAccountNumber(String accountNumber);
}

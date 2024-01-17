package com.example.BankAPIWithMongoDB.account.service;

import com.example.BankAPIWithMongoDB.account.entity.Account;
import com.example.BankAPIWithMongoDB.account.repository.AccountRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    private HazelcastInstance hazelcastInstance ;

    private IMap<Integer,Account> accountsImap;

    private Integer lastId;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
        this.hazelcastInstance = Hazelcast.newHazelcastInstance();
        this.accountsImap = hazelcastInstance.getMap("accounts");

        this.lastId = getLastId();
    }

    @Override
    public Account getAccountById(Integer id) {

        Account account = accountsImap.get(id);

        if (account == null) {

            Optional<Account> dbAccount = accountRepository.findById(id);

            if (dbAccount.isPresent()){
                account = dbAccount.get();
                accountsImap.put(account.get_id(),account);
            }
        }

        return account;
    }

    @Override
    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }

    @Override
    public Boolean save(Account account) {

        account.setOwner(account.getOwner().toLowerCase());


        if (!accountRepository.existsAccountByOwner(account.getOwner())) {

            account.set_id(++lastId);

            accountsImap.put(account.get_id(), account);

            accountRepository.save(account);

            return true;
        }

        return false;
    }

    @Override
    public Boolean update(Integer id, Account updatedAccount) {

        updatedAccount.set_id(id);

        Account dbAccount = getAccountById(id);

        if(dbAccount!=null && dbAccount.equals(updatedAccount)) {
            accountsImap.put(id, updatedAccount);
            accountRepository.save(updatedAccount);
            return true;
        }

        else if (dbAccount != null) {

            String updatedOwner = updatedAccount.getOwner().toLowerCase();
            String dbOwner = dbAccount.getOwner();

            if (!(updatedOwner.isBlank() && updatedOwner.isEmpty())) {

                updatedAccount.setOwner(updatedOwner);

                if (!accountRepository.existsAccountByOwner(updatedOwner) ||
                        updatedOwner.equals(dbOwner)) {

                    accountsImap.put(id, updatedAccount);
                    accountRepository.save(updatedAccount);

                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public Boolean deleteAccount(Integer id) {

        Account mapAccount = accountsImap.get(id);

        if (mapAccount != null) {

            accountsImap.remove(id);

            accountRepository.deleteById(id);

            return true;
        }

        else if(accountRepository.existsById(id)) {

            accountRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Override
    public Boolean withdraw(Integer accountId, Double value) {

        Account account = getAccountById(accountId);

        if (account != null) {

            Double accountBalance = account.getBalance();

            if (accountBalance >= value) {

                accountBalance -= value;

                account.setBalance(accountBalance);

                update(accountId, account);

                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean deposit(Integer accountId, Double value) {

        Account account = getAccountById(accountId);

        if (account != null) {

            Double accountBalance = account.getBalance();

            accountBalance += value;

            account.setBalance(accountBalance);

            update(accountId, account);

            return true;
        }

        return false;
    }


    @Override
    public Boolean transfer(Integer from_AccountId, Integer to_AccountId, Double value) {

        return (withdraw(from_AccountId, value) && deposit(to_AccountId, value));
    }

    private Integer getLastId() {

        List<Account> accountList = getAllAccounts();

        Account lastAccount =  accountList.get(accountList.size()-1);

        return lastAccount.get_id();

    }
}

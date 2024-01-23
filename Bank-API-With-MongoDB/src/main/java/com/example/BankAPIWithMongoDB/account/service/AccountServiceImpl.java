package com.example.BankAPIWithMongoDB.account.service;

import com.example.BankAPIWithMongoDB.account.entity.Account;
import com.example.BankAPIWithMongoDB.account.repository.AccountRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private HazelcastInstance hazelcastInstance;

    private IMap<Integer, Account> accountsImap;

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

            if (dbAccount.isPresent()) {
                account = dbAccount.get();
                accountsImap.put(account.get_id(), account);
            }
        }

        return account;
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {

        //if the passed account number don't contain '0001' at the end, append it. but
        //note: to perform this operation; the account number in this case must achieve the account number formula.
        if (accountNumber.length() == 10 && accountNumber.matches("^[A-Z]{2}00\\d{6}")) {
            accountNumber = accountNumber.concat("0001");
        }

        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }

    @Override
    public Account save(Account account) {

        String owner = account.getOwner();

        if (owner != null) {
            String accountNumber = generateAccountNumber(owner);

            //while the generated number is already in use, generate another one (not impossible !).
            while (getAccountByAccountNumber(accountNumber) != null) {
                accountNumber = generateAccountNumber(owner);
            }

            account.setAccountNumber(accountNumber);
            account.set_id(++lastId);
            account.setCreation_date(LocalDate.now());

            Double balance = account.getBalance();

            //if there is no passed balance, or the balance<0, set it 0.
            if (balance == null || balance < 0) account.setBalance(0.0);


            accountsImap.put(account.get_id(), account);
            accountRepository.save(account);

            return account;
        }

        //don't accept any new account without owner name
        else return null;
    }

    @Override
    public Account update(String accountNumber, Account updatedAccount) {

        Account dbAccount = getAccountByAccountNumber(accountNumber);

        if (dbAccount != null) {

            //if there is any passed id, or account number, ignore them.
            updatedAccount.set_id(dbAccount.get_id());
            updatedAccount.setAccountNumber(dbAccount.getAccountNumber());
            updatedAccount.setCreation_date(dbAccount.getCreation_date());

            //if the passed owner is null or>30 char, don't accept it.
            String owner = updatedAccount.getOwner();
            if (owner == null || owner.length() > 30) {
                updatedAccount.setOwner(dbAccount.getOwner());
            }

            //if the passed owner is null, or<0, don't accept it.
            if (updatedAccount.getBalance() == null || updatedAccount.getBalance() < 0) {
                updatedAccount.setBalance(dbAccount.getBalance());
            }

            accountsImap.put(dbAccount.get_id(), updatedAccount);
            accountRepository.save(updatedAccount);

            return updatedAccount;
        }

        return null;
    }

    @Override
    public Boolean deleteAccount(Integer id) {

        Account mapAccount = accountsImap.get(id);

        if (mapAccount != null) {

            accountsImap.remove(id);

            accountRepository.deleteById(id);

            return true;
        } else if (accountRepository.existsById(id)) {

            accountRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Override
    public Boolean withdraw(String accountNumber, Double value) {

        Account account = getAccountByAccountNumber(accountNumber);

        if (account != null) {

            Double accountBalance = account.getBalance();

            if (accountBalance >= value) {

                accountBalance -= value;

                account.setBalance(accountBalance);

                update(account.getAccountNumber(), account);

                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean deposit(String accountNumber, Double value) {

        Account account = getAccountByAccountNumber(accountNumber);

        if (account != null) {

            Double accountBalance = account.getBalance();

            accountBalance += value;

            account.setBalance(accountBalance);

            update(account.getAccountNumber(), account);

            return true;
        }

        return false;
    }


    @Override
    public Boolean transfer(String from_AccountNumber, String to_AccountNumber, Double value) {

        if (getAccountByAccountNumber(from_AccountNumber) != null
            && getAccountByAccountNumber(to_AccountNumber) != null) {

             withdraw(from_AccountNumber, value) ;
             deposit(to_AccountNumber, value);

             return true;
        }
        return false;
    }

    private Integer getLastId() {

        List<Account> accountList = getAllAccounts();

        if(accountList.isEmpty()) return 0;

        Account lastAccount = accountList.get(accountList.size() - 1);
        return lastAccount.get_id();

    }

    @Override
    public String generateAccountNumber(String owner) {

        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(owner.substring(0, 2).toUpperCase());
        accountNumber.append("00");

        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;

        accountNumber.append(randomNumber);
        accountNumber.append("0001");

        return accountNumber.toString();
    }
}

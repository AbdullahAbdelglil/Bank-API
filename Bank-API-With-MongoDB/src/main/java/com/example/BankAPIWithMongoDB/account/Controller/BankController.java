package com.example.BankAPIWithMongoDB.account.Controller;

import com.example.BankAPIWithMongoDB.account.entity.Account;
import com.example.BankAPIWithMongoDB.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/V1/accounts")
public class BankController {

    private final AccountService accountService;

    @Autowired
    public BankController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    public List<Account> getAllAccounts() {

        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Account getAccountByAccountNumber(@PathVariable("accountNumber") String accountNumber) {

        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/deposit/{amount}/in/{accountNumber}")
    public String deposit(@PathVariable("amount") Double value,
                          @PathVariable("accountNumber") String accountNumber) {

        if(accountService.deposit(accountNumber, value)) {
            return "Deposit process done successfully";
        }

        return """
                Deposit process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct account number.
                """;
    }


    @GetMapping("/withdraw/{amount}/form/{accountNumber}")
    public String withdraw(@PathVariable("accountNumber") String accountNumber,
                           @PathVariable("amount") Double value) {

        if(accountService.withdraw(accountNumber, value)) {
            return "Withdrawal process done successfully";
        }

        return """
                Withdrawal process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct account number.
                - Make sure that your funds is enough.
                """;
    }

    @GetMapping("/transfer/{amount}/from/{from_accNum}/to/{to_accNum}")
    public String transfer(@PathVariable("amount") Double value,
                           @PathVariable("from_accNum") String from,
                           @PathVariable("to_accNum") String to ){

        if(accountService.transfer(from,to, value)) {
            return "transfer process done successfully";
        }

        return """
                transfer process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct accounts numbers.
                - Make sure that your funds is enough.
                """;
    }

    @PutMapping("/{accountNumber}")
    public Account updateAccount(@PathVariable("accountNumber") String accountNumber,
                                 @RequestBody Account updatedAccount) {

        return accountService.update(accountNumber, updatedAccount);
    }

    @PostMapping("")
    public Account addNewAccount(@RequestBody Account account) {

        return accountService.save(account);
    }

    @DeleteMapping("/{accountId}")
    public String deleteAccount(@PathVariable("accountId") Integer accountId) {

        if(accountService.deleteAccount(accountId)) {
            return "Account- "+accountId+" deleted successfully";
        }

        return "Account id- "+accountId+" not found !";
    }


}

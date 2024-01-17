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

    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable("accountId") Integer accountId) {

        return accountService.getAccountById(accountId);
    }

    @GetMapping("/{accountId}/deposit/{value}")
    public String deposit(@PathVariable("accountId") Integer accountId,
                          @PathVariable("value") Double value) {

        if(accountService.deposit(accountId, value)) {
            return "Deposit process done successfully";
        }

        return """
                Deposit process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct ID.
                """;
    }


    @GetMapping("/{accountId}/withdraw/{value}")
    public String withdraw(@PathVariable("accountId") Integer accountId,
                          @PathVariable("value") Double value) {

        if(accountService.withdraw(accountId, value)) {
            return "Withdrawal process done successfully";
        }

        return """
                Withdrawal process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct ID.
                - Make sure that your funds is enough.
                """;
    }

    @GetMapping("/transfer/{value}/from/{from-accountId}/to/{to-accountId}")
    public String transfer(@PathVariable("from-accountId") Integer from,
                                        @PathVariable("to-accountId") Integer to,
                                        @PathVariable("value") Double value) {

        if(accountService.transfer(from,to, value)) {
            return "transfer process done successfully";
        }

        return """
                transfer process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct ID's.
                - Make sure that your funds is enough.
                """;
    }

    @PutMapping("/{accountId}")
    public String updateAccount(@PathVariable("accountId") Integer id,
                                @RequestBody Account updatedAccount) {

        if(accountService.update(id, updatedAccount)) {
            return updatedAccount.toString();
        }

        else return """
                Updating failed !
                
                Quick fixes:
                - Make sure that you have entered the correct ID.
                - Choose another owner name.""";
    }

    @PostMapping("")
    public String addNewAccount(@RequestBody Account account) {

        if(accountService.save(account)) {
            return account.toString();
        }

        else return "This account already exist\nQuick fixes: Choose another owner name.";
    }

    @DeleteMapping("/{accountId}")
    public String deleteAccount(@PathVariable("accountId") Integer accountId) {

        if(accountService.deleteAccount(accountId)) {
            return "Account- "+accountId+" deleted successfully";
        }

        return "Account id- "+accountId+" not found !";
    }


}

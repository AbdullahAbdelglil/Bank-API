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

    @GetMapping("/")
    public Account getAccountByAccountNumber(@RequestParam("accountNumber") String accountNumber) {

        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/{accountID}")
    public Account getAccountById(@PathVariable("accountID") Integer accountId) {

        return accountService.getAccountById(accountId);
    }

    @GetMapping("/deposit")
    public String deposit(@RequestParam("amount") Double value,
                          @RequestParam("accountNumber") String accountNumber) {

        if(accountService.deposit(accountNumber, value)) {
            return "Deposit process done successfully";
        }

        return """
                Deposit process failed.
                
                Quick fixes:
                - Make sure that you have entered the correct account number.
                """;
    }


    @GetMapping("/withdraw")
    public String withdraw(@RequestParam("accountNumber") String accountNumber,
                           @RequestParam("amount") Double value) {

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

    @GetMapping("/transfer")
    public String transfer(@RequestParam("amount") Double value,
                           @RequestParam("from") String from,
                           @RequestParam("to") String to ){

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

    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable("accountId") Integer id,
                                 @RequestBody Account updatedAccount) {

        return accountService.update(id, updatedAccount);
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

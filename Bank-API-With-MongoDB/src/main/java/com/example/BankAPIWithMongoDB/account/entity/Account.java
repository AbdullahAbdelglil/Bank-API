package com.example.BankAPIWithMongoDB.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "accounts")
public class Account {

    @Id
    private Integer _id;

    private String accountNumber;

    private String owner;

    private Double balance;

    private LocalDate creation_date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(_id, account._id) && Objects.equals(accountNumber, account.accountNumber) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, accountNumber);
    }

}

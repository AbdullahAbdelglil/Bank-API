package com.example.BankAPIWithMongoDB.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "accounts")
public class Account {

    @Id
    private Integer _id;

    private String owner;

    private Double balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(_id, account._id) && Objects.equals(owner, account.owner) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, owner);
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t\"id\": " + _id +
                "\n\t\"owner\": " + owner +
                "\n\t\"balance\": " + balance +
                "\n}";
    }
}

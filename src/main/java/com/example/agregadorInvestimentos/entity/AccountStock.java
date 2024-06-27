package com.example.agregadorInvestimentos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_account_stocks")
public class AccountStock {

    @EmbeddedId
    private StockAccountId id;

    @ManyToOne
    @MapsId("stockId")
    @JoinColumn(name = "stock_id")
    private Account account;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Stock stock;

    @Column(name = "quantity")
    private Integer quantity;

    public AccountStock() {
    }

    public AccountStock(StockAccountId id, Account account, Stock stock, Integer quantity) {
        this.id = id;
        this.account = account;
        this.stock = stock;
        this.quantity = quantity;
    }

    public StockAccountId getId() {
        return id;
    }

    public void setId(StockAccountId id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

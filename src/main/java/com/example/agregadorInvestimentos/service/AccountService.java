package com.example.agregadorInvestimentos.service;

import com.example.agregadorInvestimentos.controller.dto.AccountStockResponseDto;
import com.example.agregadorInvestimentos.controller.dto.AssociateAccountStockDto;
import com.example.agregadorInvestimentos.entity.AccountStock;
import com.example.agregadorInvestimentos.entity.StockAccountId;
import com.example.agregadorInvestimentos.repository.AccountRepository;
import com.example.agregadorInvestimentos.repository.AccountStockRepository;
import com.example.agregadorInvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    private StockRepository stockRepository;

    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void associateStock(String accountId, AssociateAccountStockDto associateAccountStockDto) {

        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(associateAccountStockDto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //DTO -> ENTITY

        var id = new StockAccountId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                associateAccountStockDto.quantity()
        );

        accountStockRepository.save(entity);

    }

    public List<AccountStockResponseDto> listStocks(String accountId) {

        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return account.getAccountStocks()
                .stream().map(as -> new AccountStockResponseDto(as.getStock().getStockId(), as.getQuantity(), 0.0))
                .toList();
    }
}

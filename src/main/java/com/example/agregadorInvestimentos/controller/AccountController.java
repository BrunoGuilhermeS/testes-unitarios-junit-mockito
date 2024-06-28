package com.example.agregadorInvestimentos.controller;

import com.example.agregadorInvestimentos.controller.dto.AccountStockResponseDto;
import com.example.agregadorInvestimentos.controller.dto.AssociateAccountStockDto;
import com.example.agregadorInvestimentos.controller.dto.CreateAccountDTO;
import com.example.agregadorInvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId, @RequestBody AssociateAccountStockDto associateAccountStockDto) {
        accountService.associateStock(accountId, associateAccountStockDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> associateStock(@PathVariable("accountId") String accountId) {
        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok().build();
    }
}

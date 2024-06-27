package com.example.agregadorInvestimentos.repository;

import com.example.agregadorInvestimentos.entity.Account;
import com.example.agregadorInvestimentos.entity.AccountStock;
import com.example.agregadorInvestimentos.entity.StockAccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, StockAccountId> {
		
	
	
}

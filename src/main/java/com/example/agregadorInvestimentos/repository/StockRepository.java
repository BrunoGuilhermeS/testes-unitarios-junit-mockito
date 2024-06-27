package com.example.agregadorInvestimentos.repository;

import com.example.agregadorInvestimentos.entity.Account;
import com.example.agregadorInvestimentos.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
		
	
	
}

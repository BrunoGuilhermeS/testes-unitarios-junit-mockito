package com.example.agregadorInvestimentos.service;

import com.example.agregadorInvestimentos.controller.dto.CreateStockDto;
import com.example.agregadorInvestimentos.entity.Stock;
import com.example.agregadorInvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        //Converter DTO para Entity
        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );
        stockRepository.save(stock);
    }
}

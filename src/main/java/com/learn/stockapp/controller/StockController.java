package com.learn.stockapp.controller;

/*
 * StockController is responsible for handling HTTP requests related to stocks.
 * It interacts with the StockService to perform CRUD operations on stock data.
 * autowire the stockService , and  implement all the  methods of StockService interface
 * and thows the exeption and exception will be handled by the GlobalExceptionHandler
 * class.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.learn.stockapp.service.StockService;
import com.learn.stockapp.exceptions.StockNotFoundException;
import com.learn.stockapp.exceptions.StockAlreadyExistException;
import com.learn.stockapp.model.Stock;
import com.learn.stockapp.model.StockList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;


 

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow cross-origin requests from any origin
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, welcome to the Stock App!";
    }


    // Add methods to handle HTTP requests here, e.g., getAllStocks, getStockById, saveStock, deleteStock, etc.
    // Example method to get all stocks

    //  method to get all stocks. It should return the ResposnseEntity with the list of stocks

    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    // Example method to get a stock by its symbol
    @GetMapping("/stocks/{symbol}")
    public ResponseEntity<Stock> getStockByIdEntity(@PathVariable String symbol) throws StockNotFoundException {
        Stock stock = stockService.getStockById(symbol);
        return ResponseEntity.ok(stock);
    }

    // Fixed endpoint - use separate path for external API calls
    @GetMapping("/stocks/external/country/{country}")
    public ResponseEntity<List<Stock>> getStocksByCountryFromApi(@PathVariable String country) {
        StockList stocks = stockService.getStocksByCountryFromAPI(country);
        List<Stock> stockList1 = stocks.getData();
        return ResponseEntity.ok(stockList1);
    }


    // method to save a stock 
    @PostMapping("/stocks")
    public ResponseEntity<String> saveStock(@RequestBody Stock stock) throws StockAlreadyExistException {
        System.out.println("Saving stock: " + stock);
        stockService.saveStock(stock);
        return ResponseEntity.ok("Stock saved successfully");
    }

    // to delete a stock by its symbol using delete mapping

    @DeleteMapping("/stocks/{symbol}")
    public ResponseEntity<String> deleteStock(@PathVariable String symbol) throws StockNotFoundException {
        stockService.deleteStock(symbol);
        return ResponseEntity.ok("Stock deleted successfully");
    }
    
    

}

package com.learn.stockapp.service;


/*
 * interface for StockService with the followig    methods
 * 1. getAllStocks() - returns a list of all stocks from the database
 * 
 * 2. getStocksByCountry(String country) - returns a list of stocks by country from the database
 * 3. saveStocks(Stock stock)) - saves a stock to the database
 * 4. deleteStock(String symbol) - deletes a stock from the database by symbol
 * 5. getStockById(String symbol) - returns a stock by symbol from the database
 */

import com.learn.stockapp.model.Stock;
import com.learn.stockapp.model.StockList;
import java.util.List;
import com.learn.stockapp.exceptions.StockNotFoundException;
import com.learn.stockapp.exceptions.StockAlreadyExistException;
 

public interface StockService {

    public List<Stock> getAllStocks();
    public List<Stock> getStocksByCountry(String country);
    public StockList getStocksByCountryFromAPI(String country);
    public Stock getStockById(String symbol ) throws StockNotFoundException;
    public void saveStock(Stock stock) throws StockAlreadyExistException;
    public void deleteStock(String symbol) throws StockNotFoundException;

}
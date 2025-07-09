package com.learn.stockapp.service;



/*
 * implement all the methods of StockService interface
 */
import com.learn.stockapp.model.Stock;
import com.learn.stockapp.model.StockList;
import com.learn.stockapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import com.learn.stockapp.exceptions.StockNotFoundException;
import com.learn.stockapp.exceptions.StockAlreadyExistException;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public List<Stock> getStocksByCountry(String country) {
        return stockRepository.findByCountry(country);
    }

    @Override
    public StockList getStocksByCountryFromAPI(String country) {
        String  apiUrl = "https://api.twelvedata.com/stocks?country=" + country; // Replace with actual API URL
        StockList stocks= restTemplate.getForObject(apiUrl, StockList.class);
        return stocks;
    }

    @Override
    public Stock getStockById(String symbol) throws StockNotFoundException {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol must not be null or empty");
        }
        Optional<Stock> stock = stockRepository.findById(symbol);
        return stock.orElseThrow(() -> 
            new StockNotFoundException ("Stock with symbol '" + symbol + "' not found"));
    }

    /*
     * check if stock already exists in the database
     * if it exists, throw StockAlreadyExistException
     */
    @Override
    public void saveStock(Stock stock) throws StockAlreadyExistException {
        if (stockRepository.existsById(stock.getSymbol())) {
            throw new StockAlreadyExistException("Stock with symbol '" + stock.getSymbol() + "' already exists");
        }
        stockRepository.save(stock);
    }

    /* check is the stock exist,if not exist throw StockNotFoundException
     * if it exists, delete the stock from the database
     */
    @Override
    public void deleteStock(String symbol) throws StockNotFoundException {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol must not be null or empty");
        }
        if (!stockRepository.existsById(symbol)) {
            throw new StockNotFoundException("Stock with symbol '" + symbol + "' not found");
        }
        try {
            stockRepository.deleteById(symbol);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete stock with symbol '" + symbol + "'", e);
        }
    }
}

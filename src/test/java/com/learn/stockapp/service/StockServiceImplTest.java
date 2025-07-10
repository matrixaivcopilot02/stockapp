package com.learn.stockapp.service;

import com.learn.stockapp.model.Stock;
import com.learn.stockapp.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.learn.stockapp.model.StockList;
import org.springframework.web.client.RestTemplate;
 



class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    @Mock
    private RestTemplate restTemplate;

    private Stock stock1;
    private Stock stock2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //use AllArgsConstructor to initialize stock1 and stock2 and provide all values
        stock1 = new Stock("AAPL", "Apple Inc", "USD", "NASDAQ", "XNGS", "United States", "Common Stock");
        stock2 = new Stock("GOOGL", "Alphabet Inc", "USD", "NASDAQ", "XNGS", "United States", "Common Stock");
    }
    
    @Test
    void testGetAllStocks_ReturnsListOfStocks() {
      
        List<Stock> mockStocks = Arrays.asList(stock1, stock2);
        when(stockRepository.findAll()).thenReturn(mockStocks);
        List<Stock> result = stockService.getAllStocks();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        assertEquals("GOOGL", result.get(1).getSymbol());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testGetAllStocks_ReturnsEmptyList() {
        when(stockRepository.findAll()).thenReturn(Arrays.asList());

        List<Stock> result = stockService.getAllStocks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testGetStocksByCountry_ReturnsStocks() {
        List<Stock> mockStocks = Arrays.asList(stock1, stock2);
        when(stockRepository.findByCountry("United States")).thenReturn(mockStocks);

        List<Stock> result = stockService.getStocksByCountry("United States");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(stockRepository, times(1)).findByCountry("United States");
    }

    @Test
    void testGetStocksByCountry_ReturnsEmptyList() {
        when(stockRepository.findByCountry("Canada")).thenReturn(Arrays.asList());

        List<Stock> result = stockService.getStocksByCountry("Canada");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findByCountry("Canada");
    }

    @Test
    void testGetStockById_ReturnsStock() throws Exception {
        when(stockRepository.findById("AAPL")).thenReturn(java.util.Optional.of(stock1));

        Stock result = stockService.getStockById("AAPL");

        assertNotNull(result);
        assertEquals("AAPL", result.getSymbol());
        verify(stockRepository, times(1)).findById("AAPL");
    }

    @Test
    void testGetStockById_ThrowsStockNotFoundException() {
        when(stockRepository.findById("MSFT")).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(com.learn.stockapp.exceptions.StockNotFoundException.class, () -> {
            stockService.getStockById("MSFT");
        });

        assertTrue(exception.getMessage().contains("Stock with symbol 'MSFT' not found"));
        verify(stockRepository, times(1)).findById("MSFT");
    }

    @Test
    void testGetStockById_ThrowsIllegalArgumentException_WhenSymbolIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockService.getStockById(null);
        });
        assertEquals("Symbol must not be null or empty", exception.getMessage());
    }

    @Test
    void testGetStockById_ThrowsIllegalArgumentException_WhenSymbolIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockService.getStockById("   ");
        });
        assertEquals("Symbol must not be null or empty", exception.getMessage());
    }

    @Test
    void testSaveStock_Success() throws Exception {
        when(stockRepository.existsById(stock1.getSymbol())).thenReturn(false);

        stockService.saveStock(stock1);

        verify(stockRepository, times(1)).existsById(stock1.getSymbol());
        verify(stockRepository, times(1)).save(stock1);
    }

    @Test
    void testSaveStock_ThrowsStockAlreadyExistException() {
        when(stockRepository.existsById(stock1.getSymbol())).thenReturn(true);

        Exception exception = assertThrows(com.learn.stockapp.exceptions.StockAlreadyExistException.class, () -> {
            stockService.saveStock(stock1);
        });

        assertTrue(exception.getMessage().contains("already exists"));
        verify(stockRepository, times(1)).existsById(stock1.getSymbol());
        verify(stockRepository, never()).save(any());
    }

    @Test
    void testDeleteStock_Success() throws Exception {
        when(stockRepository.existsById(stock1.getSymbol())).thenReturn(true);

        stockService.deleteStock(stock1.getSymbol());

        verify(stockRepository, times(1)).existsById(stock1.getSymbol());
        verify(stockRepository, times(1)).deleteById(stock1.getSymbol());
    }

    @Test
    void testDeleteStock_ThrowsStockNotFoundException() {
        when(stockRepository.existsById("MSFT")).thenReturn(false);

        Exception exception = assertThrows(com.learn.stockapp.exceptions.StockNotFoundException.class, () -> {
            stockService.deleteStock("MSFT");
        });

        assertTrue(exception.getMessage().contains("not found"));
        verify(stockRepository, times(1)).existsById("MSFT");
        verify(stockRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteStock_ThrowsIllegalArgumentException_WhenSymbolIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockService.deleteStock(null);
        });
        assertEquals("Symbol must not be null or empty", exception.getMessage());
    }

    @Test
    void testDeleteStock_ThrowsIllegalArgumentException_WhenSymbolIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockService.deleteStock("   ");
        });
        assertEquals("Symbol must not be null or empty", exception.getMessage());
    }
    // test case for getStocksByCountryFromAPI 
    @Test
    void testGetStocksByCountryFromAPI_ReturnsStocks() throws Exception {
        // Assuming you have a valid API URL and the StockList class is properly

        String apiUrl = "https://api.twelvedata.com/stocks?country=United States"; // Replace with actual API URL
        StockList mockStockList = new StockList();
        mockStockList.setData(Arrays.asList(stock1, stock2)); // Assuming StockList has a setData method

        when(restTemplate.getForObject(apiUrl, StockList.class)).thenReturn(mockStockList);

        StockList result = stockService.getStocksByCountryFromAPI("United States");

        assertNotNull(result);
        assertEquals(2, result.getData().size());
        verify(restTemplate, times(1)).getForObject(apiUrl, StockList.class);
    }

    // @Test
    // void testGetStocksByCountry_ReturnsStocks() {
    //     List<Stock> mockStocks = Arrays.asList(stock1, stock2);
    //     when(stockRepository.findByCountry("United States")).thenReturn(mockStocks);

    //     List<Stock> result = stockService.getStocksByCountry("United States");

    //     assertNotNull(result);
    //     assertEquals(2, result.size());
    //     verify(stockRepository, times(1)).findByCountry("United States");
    // }

    // @Test
    // void testGetStocksByCountry_ReturnsEmptyList() {
    //     when(stockRepository.findByCountry("Canada")).thenReturn(Arrays.asList());

    //     List<Stock> result = stockService.getStocksByCountry("Canada");

    //     assertNotNull(result);
    //     assertTrue(result.isEmpty());
    //     verify(stockRepository, times(1)).findByCountry("Canada");
    // }

    // @Test
    // void testGetStockById_ReturnsStock() throws Exception {
    //     when(stockRepository.findById("AAPL")).thenReturn(java.util.Optional.of(stock1));

    //     Stock result = stockService.getStockById("AAPL");

    //     assertNotNull(result);
    //     assertEquals("AAPL", result.getSymbol());
    //     verify(stockRepository, times(1)).findById("AAPL");
    // }

    // @Test
    // void testGetStockById_ThrowsStockNotFoundException() {
    //     when(stockRepository.findById("MSFT")).thenReturn(java.util.Optional.empty());

    //     Exception exception = assertThrows(com.learn.stockapp.exceptions.StockNotFoundException.class, () -> {
    //         stockService.getStockById("MSFT");
    //     });

    //     assertTrue(exception.getMessage().contains("Stock with symbol 'MSFT' not found"));
    //     verify(stockRepository, times(1)).findById("MSFT");
    // }

    // @Test
    // void testGetStockById_ThrowsIllegalArgumentException_WhenSymbolIsNull() {
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         stockService.getStockById(null);
    //     });
    //     assertEquals("Symbol must not be null or empty", exception.getMessage());
    // }

    // @Test
    // void testGetStockById_ThrowsIllegalArgumentException_WhenSymbolIsEmpty() {
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         stockService.getStockById("   ");
    //     });
    //     assertEquals("Symbol must not be null or empty", exception.getMessage());
    // }

    // @Test
    // void testSaveStock_Success() throws Exception {
    //     when(stockRepository.existsById(stock1.getSymbol())).thenReturn(false);

    //     stockService.saveStock(stock1);

    //     verify(stockRepository, times(1)).existsById(stock1.getSymbol());
    //     verify(stockRepository, times(1)).save(stock1);
    // }

    // @Test
    // void testSaveStock_ThrowsStockAlreadyExistException() {
    //     when(stockRepository.existsById(stock1.getSymbol())).thenReturn(true);

    //     Exception exception = assertThrows(com.learn.stockapp.exceptions.StockAlreadyExistException.class, () -> {
    //         stockService.saveStock(stock1);
    //     });

    //     assertTrue(exception.getMessage().contains("already exists"));
    //     verify(stockRepository, times(1)).existsById(stock1.getSymbol());
    //     verify(stockRepository, never()).save(any());
    // }

    // @Test
    // void testDeleteStock_Success() throws Exception {
    //     when(stockRepository.existsById(stock1.getSymbol())).thenReturn(true);

    //     stockService.deleteStock(stock1.getSymbol());

    //     verify(stockRepository, times(1)).existsById(stock1.getSymbol());
    //     verify(stockRepository, times(1)).deleteById(stock1.getSymbol());
    // }

    // @Test
    // void testDeleteStock_ThrowsStockNotFoundException() {
    //     when(stockRepository.existsById("MSFT")).thenReturn(false);

    //     Exception exception = assertThrows(com.learn.stockapp.exceptions.StockNotFoundException.class, () -> {
    //         stockService.deleteStock("MSFT");
    //     });

    //     assertTrue(exception.getMessage().contains("not found"));
    //     verify(stockRepository, times(1)).existsById("MSFT");
    //     verify(stockRepository, never()).deleteById(any());
    // }

    // @Test
    // void testDeleteStock_ThrowsIllegalArgumentException_WhenSymbolIsNull() {
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         stockService.deleteStock(null);
    //     });
    //     assertEquals("Symbol must not be null or empty", exception.getMessage());
    // }

    // @Test
    // void testDeleteStock_ThrowsIllegalArgumentException_WhenSymbolIsEmpty() {
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         stockService.deleteStock("   ");
    //     });
    //     assertEquals("Symbol must not be null or empty", exception.getMessage());
    // }

}
package com.learn.stockapp.service;

import com.learn.stockapp.exceptions.StockNotFoundException;
 import com.learn.stockapp.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

 
    @InjectMocks
    private StockServiceImpl stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteStock_shouldDeleteStock_whenSymbolExists() throws StockNotFoundException {
        String symbol = "AAPL";
        when(stockRepository.existsById(symbol)).thenReturn(true);

        assertDoesNotThrow(() -> stockService.deleteStock(symbol));
        verify(stockRepository, times(1)).deleteById(symbol);
    }

    @Test
    void deleteStock_shouldThrowIllegalArgumentException_whenSymbolIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stockService.deleteStock(null));
        assertEquals("Symbol must not be null or empty", exception.getMessage());
        verify(stockRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteStock_shouldThrowIllegalArgumentException_whenSymbolIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stockService.deleteStock("  "));
        assertEquals("Symbol must not be null or empty", exception.getMessage());
        verify(stockRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteStock_shouldThrowStockNotFoundException_whenSymbolDoesNotExist() {
        String symbol = "GOOG";
        when(stockRepository.existsById(symbol)).thenReturn(false);

        Exception exception = assertThrows(StockNotFoundException.class, () -> stockService.deleteStock(symbol));
        assertEquals("Stock with symbol '" + symbol + "' not found", exception.getMessage());
        verify(stockRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteStock_shouldThrowRuntimeException_whenDeleteFails() {
        String symbol = "MSFT";
        when(stockRepository.existsById(symbol)).thenReturn(true);
        doThrow(new RuntimeException("DB error")).when(stockRepository).deleteById(symbol);

        Exception exception = assertThrows(RuntimeException.class, () -> stockService.deleteStock(symbol));
        assertTrue(exception.getMessage().contains("Failed to delete stock with symbol"));
        verify(stockRepository, times(1)).deleteById(symbol);
    }
}
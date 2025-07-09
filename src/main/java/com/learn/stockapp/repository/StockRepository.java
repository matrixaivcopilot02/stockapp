package com.learn.stockapp.repository;
import com.learn.stockapp.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository

public interface StockRepository  extends JpaRepository<Stock, String> {

    // Custom query methods can be defined here if nee findByCountry(String country);   

    // For example, to find stocks by country:
     List<Stock> findByCountry(String country);
}
package com.learn.stockapp.model;
 /*
  * create a class with following attributes
   data  with List of stocks
   use Lombok to generate getter and setter for all the attributes
   Use Lombok @Data to generate the NoArgsConstructor and AllArgsConstructor
   @NoArgsConstructor
   @AllArgsConstructor
  */
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockList {
    private List<Stock> data;
}
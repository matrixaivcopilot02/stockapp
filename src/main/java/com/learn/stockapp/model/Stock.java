package com.learn.stockapp.model;

 /* create a class with following attributes
  * {
    "symbol": "AAPL",
    "name": "Apple Inc",
    "currency": "USD",
    "exchange": "NASDAQ",
    "mic_code": "XNGS",
    "country": "United States",
    "type": "Common Stock"
  }
  Use Lombok to  generate getter and setter for all the attributes
  Use Lombok @Data to generate the NoArgsConstructor and AllArgsConstructor
  toString method
  use  @Entity annotation to specify the name of the table in 
  which the object will be stored
  annotate @id for symbol

  */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor  
@AllArgsConstructor
public class Stock {

    @Id
    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "mic_code")
    private String micCode;

    @Column(name = "country")
    private String country;

    @Column(name = "type")
    private String type;
}


 

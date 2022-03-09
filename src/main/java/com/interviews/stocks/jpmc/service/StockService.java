package com.interviews.stocks.jpmc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.interviews.stocks.jpmc.exception.StockException;
import com.interviews.stocks.jpmc.model.StockData;

@Service
public class StockService {

	static List<StockData> stockData = Arrays.asList(
			new StockData("TEA","Common",20,null,100),
			new StockData("POP","Preferred",8,3,100),
			new StockData("ALE","Common",23,null,60),
			new StockData("GIN","Preferred",8,2,100),
			new StockData("JOE","Common",13,null,250));

	BiFunction<StockData, Double, Double> dividendYield = (stockData, price) -> {
		Double dividendYield = stockData.getType().equalsIgnoreCase("Common") ? 
				stockData.getLastDividend()/price : 
					((Double.valueOf(stockData.getFixedDividend())/100)* stockData.getParValue())/price;
				return new BigDecimal(dividendYield).setScale(2, RoundingMode.HALF_UP).doubleValue();
		};
	
	public Double calculateDividendYield(String stockSymbol, double price) throws StockException {
		Optional<StockData> stocks = stockData.stream()
				.filter(stock -> stock.getStockSymbol().equalsIgnoreCase(stockSymbol))
				.findAny();
		if (stocks.isPresent()) {
			return dividendYield.apply(stocks.get(), price);
		} else {
			throw new StockException("No Stocks Found");
		}
				
	}

}

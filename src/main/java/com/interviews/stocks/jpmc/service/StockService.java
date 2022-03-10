package com.interviews.stocks.jpmc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.interviews.stocks.jpmc.exception.StockException;
import com.interviews.stocks.jpmc.model.StockData;
import com.interviews.stocks.jpmc.model.Trade;

@Service
public class StockService {

	static List<StockData> stockData = Arrays.asList(
			new StockData("TEA","Common",20,null,100),
			new StockData("POP","Preferred",8,3,100),
			new StockData("ALE","Common",23,null,60),
			new StockData("GIN","Preferred",8,2,100),
			new StockData("JOE","Common",13,null,250));
	
	static List<Trade> tradeList = new ArrayList<>();
	

	BiFunction<StockData, Double, Double> dividendYield = (stockData, price) -> {
		Double dividendYield = stockData.getType().equalsIgnoreCase("Common") ? 
				stockData.getLastDividend()/price : 
					((Double.valueOf(stockData.getFixedDividend())/100)* stockData.getParValue())/price;
				return new BigDecimal(dividendYield).setScale(2, RoundingMode.HALF_UP).doubleValue();
		};
	
	/**
	 * Calculate dividend yield based on the Stock type and Price	
	 * @param stockSymbol
	 * @param price
	 * @return
	 * @throws StockException
	 */
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
	
	/**
	 * Save Trade Information
	 * 
	 * @param trade
	 * @return
	 * @throws StockException
	 */
	public List<Trade> saveTrade(Trade trade) throws StockException {
		Optional<StockData> stocks = stockData.stream()
				.filter(stock -> stock.getStockSymbol()
						.equalsIgnoreCase(trade.getStockSymbol()))
				.findAny();
		if (stocks.isPresent()) {
			tradeList.add(trade);
		} else {
			throw new StockException("No Stocks Found");
		}
		return tradeList;
	}

	/**
	 * Calculate Volume Weighted Stock Price
	 * 
	 * @param stockSymbol
	 * @return
	 * @throws StockException
	 */
	public Double calculateVolumeWeightedStockPrice(String stockSymbol) 
			throws StockException {
		List<Trade> filteredTrade = tradeList.stream()
				.filter(trade -> 
					trade.getStockSymbol().equalsIgnoreCase(stockSymbol)
					&& trade.getDate().isAfter(LocalDateTime.now().minus(15, ChronoUnit.MINUTES)))
				.collect(Collectors.toList());
		if (!filteredTrade.isEmpty()) {
			AtomicReference<Double> summation = new AtomicReference<Double>(0.00);
			AtomicReference<Integer> quantity = new AtomicReference<Integer>(0);
			Function<Trade, Double> vmStockPrice =  trade -> 
					trade.getTradingPrice() * trade.getQuantity();
			filteredTrade.forEach(trade -> {
				summation.set(summation.get() + vmStockPrice.apply(trade));
				quantity.set(quantity.get() + trade.getQuantity());
			});
			
			return new BigDecimal(summation.get()/quantity.get())
					.setScale(2, RoundingMode.HALF_UP).doubleValue();
		} else {
			throw new StockException("No Stocks Found");
		}
	}

	/**
	 * 
	 * @return
	 */
	public Double calculateGeometricMean() {
		Double priceProducts = tradeList.stream().map(Trade::getTradingPrice)
				.reduce(1.00, (a,b)-> a*b);
		return new BigDecimal(Math.pow(priceProducts, 1.0/Double.valueOf(tradeList.size())))
		.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

}

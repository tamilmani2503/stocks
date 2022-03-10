package com.interviews.stocks.jpmc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interviews.stocks.jpmc.exception.StockException;
import com.interviews.stocks.jpmc.model.Trade;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

	@InjectMocks
	StockService stockService;
	
	@Test
	public void calculateDividendYieldCommonsType() throws Exception {
		Double dividendYield = stockService.calculateDividendYield("TEA", 70.99);
		assertEquals(0.28, dividendYield);
	}
	
	@Test
	public void calculateDividendYieldPreferredType() throws Exception {
		Double dividendYield = stockService.calculateDividendYield("POP", 70.99);
		assertEquals(0.04, dividendYield);
	}
	
	@Test
	public void calculateDividendYiedThrowsException() throws Exception {
		StockException exception = assertThrows(StockException.class,
				() -> stockService.calculateDividendYield("AAA", 90.00),
				"No Stocks Found"
				);
		assertTrue(exception.getMessage().contains("No Stocks Found"));
	}
	
	@Test
	public void saveTradeThrowsException() throws Exception {
		Trade trade = new Trade("AAA", 2, "Buy", 50.99);
		StockException exception = assertThrows(StockException.class,
				() -> stockService.saveTrade(trade),
				"No Stocks Found"
				);
		assertTrue(exception.getMessage().contains("No Stocks Found"));
	}
	
	@Test
	public void saveTradeNoExceptionThrown() throws Exception {
		Trade trade = new Trade("POP", 2, "Buy", 50.99);
		stockService.saveTrade(trade);
		trade = new Trade("POP", 1, "Sell", 50.99);
		List<Trade> tradeList = stockService.saveTrade(trade);
		assertEquals(2, tradeList.size());
	}
	
	@Test
	public void calculateVolumeWeightedStockPriceThrowException() throws Exception {
		Trade trade = new Trade("POP", 2, "Buy", 50.99);
		stockService.saveTrade(trade);
		StockException exception = assertThrows(StockException.class,
				() -> stockService.calculateVolumeWeightedStockPrice("AAA"),
				"No Stocks Found"
				);
		assertTrue(exception.getMessage().contains("No Stocks Found"));
	}
	
	@Test
	public void calculateVolumeWeightedStockPriceFilterByStock() throws Exception {
		Trade trade = new Trade("POP", 2, "Buy", 50.99);
		stockService.saveTrade(trade);
		trade = new Trade("POP", 2, "Buy", 30.99);
		stockService.saveTrade(trade);
		trade = new Trade("TEA", 2, "Buy", 30.99);
		stockService.saveTrade(trade);
		assertEquals(45.28, stockService.calculateVolumeWeightedStockPrice("POP"));
	}
	
	@Test
	public void calculateVolumeWeightedStockPriceFilterByStockAndTime() throws Exception {
		Trade trade = new Trade("POP", 2, "Buy", 50.99);
		stockService.saveTrade(trade);
		trade = new Trade("POP", 2, "Buy", 30.99);
		trade.setDate(LocalDateTime.now().minus(17, ChronoUnit.MINUTES));
		stockService.saveTrade(trade);
		trade = new Trade("TEA", 2, "Buy", 30.99);
		stockService.saveTrade(trade);
		assertEquals(47.35, stockService.calculateVolumeWeightedStockPrice("POP"));
	}
	
	@Test
	public void calculateGeometricMean() throws Exception {
		Double geometricMean = stockService.calculateGeometricMean();
		assertEquals(41.78, geometricMean);
	}
	
}

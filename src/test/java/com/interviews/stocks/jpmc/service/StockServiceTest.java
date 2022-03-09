package com.interviews.stocks.jpmc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interviews.stocks.jpmc.exception.StockException;

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
	public void calculateDividendYiedNoThrowsException() throws Exception {
		StockException exception = assertThrows(StockException.class,
				() -> stockService.calculateDividendYield("AAA", 90.00),
				"No Stocks Found"
				);
		assertTrue(exception.getMessage().contains("No Stocks Found"));
	}
}

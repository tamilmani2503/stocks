package com.interviews.stocks.jpmc.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.interviews.stocks.jpmc.exception.StockException;
import com.interviews.stocks.jpmc.model.Trade;
import com.interviews.stocks.jpmc.service.StockService;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class StockControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	StockService stockService;
	
	@Test
	public void verifyDividendYieldAPIIs2XX_onrightparameters() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield","TEA")
						.param("price", "12.00"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void calculatePEratioIs2XX_onrightparameters() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/peratio","TEA")
						.param("price", "12.00"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void verifyDividendYieldAPIIs4XX_onmissingrequestparams() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield","TEA"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void calculaPeRatioAPIIs4XX_onmissingrequestparams() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/peratio","TEA"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void verifyDividendYieldAPIIs4XX_onmissingpathparams() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield",""))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void calculatePeratioAPIIs4XX_onmissingpathparams() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/peratio",""))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void verifyDividendYieldReturnedForGivenPrice() throws Exception {
		when(stockService.calculateDividendYield(anyString(), anyDouble()))
			.thenReturn(0.32d);
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield","TEA")
				.param("price", "70.99"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.dividendYield", is(0.32d)));
		verify(stockService).calculateDividendYield(anyString(),anyDouble());
	}
	
	@Test
	public void calculatePeratio() throws Exception {
		when(stockService.calculatePERatio(anyString(), anyDouble()))
			.thenReturn(0.32d);
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/peratio","TEA")
				.param("price", "70.99"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.peRatio", is(0.32d)));
		verify(stockService).calculatePERatio(anyString(),anyDouble());
	}
	
	@Test
	public void verifyDividendYieldThrowsException() throws Exception {
		when(stockService.calculateDividendYield(anyString(), anyDouble()))
			.thenThrow(new StockException("No Stocks Found"));
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield","TEA")
				.param("price", "70.99"))
				.andExpect(status().isNotFound());
				
		verify(stockService).calculateDividendYield(anyString(),anyDouble());
	}
	
	@Test
	public void calculatePeRatioThrowsException() throws Exception {
		when(stockService.calculatePERatio(anyString(), anyDouble()))
			.thenThrow(new StockException("No Stocks Found"));
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/peratio","TEA")
				.param("price", "70.99"))
				.andExpect(status().isNotFound());
				
		verify(stockService).calculatePERatio(anyString(),anyDouble());
	}
	
	@Test
	public void saveTradeIs2xx() throws Exception {
		mockMvc.perform(post
				("/api/stocks/{stockSymbol}/trade","TEA")
				.content("{\"stockSymbol\":\"TEA\",\"indicator\":\"Buy\",\"tradePrice\":\"70.00\",\"quantity\":\"1\"}")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isCreated());
	}
	
	@Test
	public void saveTradeIs404WhenPathParamIsNotPassed() throws Exception {
		mockMvc.perform(post
				("/api/stocks/{stockSymbol}/trade","")
				.content("{\"stockSymbol\":\"TEA\",\"indicator\":\"Buy\",\"tradePrice\":\"70.00\",\"quantity\":\"1\"}")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void saveTradeSuccessfully() throws Exception {
		mockMvc.perform(post
				("/api/stocks/{stockSymbol}/trade","TEA")
				.content("{\"stockSymbol\":\"TEA\",\"indicator\":\"Buy\",\"tradePrice\":\"70.00\",\"quantity\":\"1\"}")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isCreated());
		verify(stockService).saveTrade(any(Trade.class));
	}
	
	@Test
	public void saveTradeSuccessfullyReturnsTrades() throws Exception {
		Trade trade = new Trade("POP", 1, "Buy", 70.00);
		List<Trade> tradeList = new ArrayList<>();
		tradeList.add(trade);
		when(stockService.saveTrade(any(Trade.class))).thenReturn(tradeList);
		mockMvc.perform(post
				("/api/stocks/{stockSymbol}/trade","POP")
				.content("{\"stockSymbol\":\"POP\",\"indicator\":\"Buy\",\"tradePrice\":\"70.00\",\"quantity\":\"1\"}")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.[0].stockSymbol", is("POP")));
				
		verify(stockService).saveTrade(any(Trade.class));
	}
	
	@Test
	public void calculateVolumeWeightedStockPrice() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/vwstockprice","POP")
				).andExpect(status().isOk());
			
	}
	
	@Test
	public void calculateVolumeWeightedStockPriceMethodInvoke() throws Exception {
		when(stockService.calculateVolumeWeightedStockPrice("POP")).thenReturn(40.00);
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/vwstockprice","POP")
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.volumeWeightedStockPrice", is(40.00)));
		verify(stockService).calculateVolumeWeightedStockPrice(anyString());
	}
	
	@Test
	public void calculateVolumeWeightedStockPriceThrows404() throws Exception {
		when(stockService.calculateVolumeWeightedStockPrice(anyString()))
				.thenThrow(new StockException("No Stocks Found"));
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/vwstockprice","POP")
				).andExpect(status().isNotFound());
		verify(stockService).calculateVolumeWeightedStockPrice(anyString());
	}
	
	@Test
	public void calculateGeometricMeanIS2XX() throws Exception {
		when(stockService.calculateGeometricMean()).thenReturn(40.00);
		mockMvc.perform(get ("/api/stocks/geometricmean"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.geometricMean", is(40.00)));
		verify(stockService).calculateGeometricMean();
	}
}

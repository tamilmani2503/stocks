package com.interviews.stocks.jpmc.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.interviews.stocks.jpmc.exception.StockException;
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
	public void verifyDividendYieldAPIIs4XX_onmissingrequestparams() throws Exception {
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield","TEA"))
				.andExpect(status().isBadRequest());
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
	public void verifyDividendYieldThrowsException() throws Exception {
		when(stockService.calculateDividendYield(anyString(), anyDouble()))
			.thenThrow(new StockException("No Stocks Found"));
		mockMvc.perform(get
				("/api/stocks/{stockSymbol}/dividendyield","TEA")
				.param("price", "70.99"))
				.andExpect(status().isNotFound());
				
		verify(stockService).calculateDividendYield(anyString(),anyDouble());
	}

}

package com.interviews.stocks.jpmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interviews.stocks.jpmc.exception.StockException;
import com.interviews.stocks.jpmc.model.DividendYield;
import com.interviews.stocks.jpmc.service.StockService;

@RestController
public class StockController {
	
	@Autowired
	StockService stockService;
	
	@GetMapping("/api/stocks/{stockSymbol}/dividendyield")
	public ResponseEntity<DividendYield> calculateDividendYield(
			@PathVariable("stockSymbol") String stockSymbol,
			@RequestParam(name="price",required=true) Double price) throws StockException {
		DividendYield response = new DividendYield();
		response.setDividendYield(stockService.calculateDividendYield(stockSymbol, price));
		return new ResponseEntity<DividendYield>(response,HttpStatus.OK);
	}
	
	@ExceptionHandler(value=StockException.class)
	public ResponseEntity<?> handleException(Exception exception) {
		return new ResponseEntity<String>("No Stocks Found",HttpStatus.NOT_FOUND);
	}
	

}

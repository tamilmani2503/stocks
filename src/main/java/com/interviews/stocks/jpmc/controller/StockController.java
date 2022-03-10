package com.interviews.stocks.jpmc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interviews.stocks.jpmc.exception.StockException;
import com.interviews.stocks.jpmc.model.DividendYield;
import com.interviews.stocks.jpmc.model.GeometricMean;
import com.interviews.stocks.jpmc.model.PERatio;
import com.interviews.stocks.jpmc.model.Trade;
import com.interviews.stocks.jpmc.model.VolumneWeightedStockPrice;
import com.interviews.stocks.jpmc.service.StockService;

@RestController
public class StockController {
	
	@Autowired
	StockService stockService;
	
	@GetMapping("/api/stocks/{stockSymbol}/dividendyield")
	public ResponseEntity<DividendYield> calculateDividendYield(
			@PathVariable(name="stockSymbol", required=true)   String stockSymbol,
			@RequestParam(name="price",required=true) Double price) throws StockException {
		DividendYield response = new DividendYield();
		response.setDividendYield(stockService.calculateDividendYield(stockSymbol, price));
		return new ResponseEntity<DividendYield>(response,HttpStatus.OK);
	}
	
	@GetMapping("/api/stocks/{stockSymbol}/peratio")
	public ResponseEntity<PERatio> calculatePeRatio(
			@PathVariable(name="stockSymbol", required=true)   String stockSymbol,
			@RequestParam(name="price",required=true) Double price) throws StockException {
		PERatio response = new PERatio();
		response.setPeRatio(stockService.calculatePERatio(stockSymbol, price));
		return new ResponseEntity<PERatio>(response,HttpStatus.OK);
	}
	
	@PostMapping("/api/stocks/{stockSymbol}/trade")
	public ResponseEntity<List<Trade>> saveTrade(@RequestBody Trade trade) throws StockException {
		List<Trade> tradeList = stockService.saveTrade(trade);
		return new ResponseEntity<List<Trade>>(tradeList,HttpStatus.CREATED);
	}
	
	@GetMapping("/api/stocks/{stockSymbol}/vwstockprice")
	public ResponseEntity<VolumneWeightedStockPrice> calculateVolumeWeightedStockPrice(
			@PathVariable(name="stockSymbol", required=true)  String stockSymbol) 
					throws StockException {
		VolumneWeightedStockPrice response = new VolumneWeightedStockPrice();
		response.setVolumeWeightedStockPrice(stockService.calculateVolumeWeightedStockPrice(stockSymbol));
		return new ResponseEntity<VolumneWeightedStockPrice>(response,HttpStatus.OK);
	}
	
	@GetMapping("/api/stocks/geometricmean")
	public ResponseEntity<GeometricMean> calculateGeometricMean() {
		GeometricMean response = new GeometricMean();
		response.setGeometricMean(stockService.calculateGeometricMean());
		return new ResponseEntity<GeometricMean>(response,HttpStatus.OK);
	}
	
	
	@ExceptionHandler(value=StockException.class)
	public ResponseEntity<?> handleException(Exception exception) {
		return new ResponseEntity<String>("No Stocks Found",HttpStatus.NOT_FOUND);
	}
	

}

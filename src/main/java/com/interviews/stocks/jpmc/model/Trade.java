package com.interviews.stocks.jpmc.model;

import java.time.LocalDateTime;
import java.util.Date;



public class Trade {

	
	private String stockSymbol;
	private int quantity;
	private String indicator;
	private double tradingPrice;
	private LocalDateTime date;

	public Trade(String stockSymbol, int quantity, String indicator, double tradingPrice) {
		this.stockSymbol = stockSymbol;
		this.quantity = quantity;
		this.indicator = indicator;
		this.tradingPrice = tradingPrice;
		this.date = LocalDateTime.now();
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public double getTradingPrice() {
		return tradingPrice;
	}

	public void setTradingPrice(double tradingPrice) {
		this.tradingPrice = tradingPrice;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Trade [stockSymbol=" + stockSymbol + ", quantity=" + quantity + ", indicator=" + indicator
				+ ", tradingPrice=" + tradingPrice + ", date=" + date + "]";
	}
	
	
	

}

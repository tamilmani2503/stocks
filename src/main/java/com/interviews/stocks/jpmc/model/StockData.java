package com.interviews.stocks.jpmc.model;

public class StockData {

	private String stockSymbol;
	private String type;
	private Integer lastDividend;
	private Integer fixedDividend;
	private Integer parValue;

	public StockData(String stockSymbol, String type, Integer lastDividend,
			Integer fixedDividend, Integer parValue) {
				this.stockSymbol = stockSymbol;
				this.type = type;
				this.lastDividend = lastDividend;
				this.fixedDividend = fixedDividend;
				this.parValue = parValue;
	
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLastDividend() {
		return lastDividend;
	}

	public void setLastDividend(Integer lastDividend) {
		this.lastDividend = lastDividend;
	}

	public Integer getFixedDividend() {
		return fixedDividend;
	}

	public void setFixedDividend(Integer fixedDividend) {
		this.fixedDividend = fixedDividend;
	}

	public Integer getParValue() {
		return parValue;
	}

	public void setParValue(Integer parValue) {
		this.parValue = parValue;
	}

	@Override
	public String toString() {
		return "StockData [stockSymbol=" + stockSymbol + ", type=" + type + ", lastDividend=" + lastDividend
				+ ", fixedDividend=" + fixedDividend + ", parValue=" + parValue + "]";
	}

	
	
}

package com.example.api.strategy;

import com.example.api.model.TradeSignal;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Interface for trading strategies
 */
public interface TradingStrategy {
    /**
     * Analyze market data and generate a trading signal
     * @param symbol Stock symbol (e.g., "AAPL")
     * @param bars Historical OHLCV data
     * @return TradeSignal with BUY, SELL, or HOLD
     */
    TradeSignal analyze(String symbol, JsonNode bars);

    /**
     * Get strategy name
     */
    String getName();
}

package com.example.api.strategy;

import com.example.api.model.TradeSignal;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Moving Average Crossover Strategy
 * BUY when fast MA crosses above slow MA
 * SELL when fast MA crosses below slow MA
 * 
 * This is a beginner-friendly strategy that works well for trending markets.
 */
@Component
public class MovingAverageCrossoverStrategy implements TradingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(MovingAverageCrossoverStrategy.class);
    
    private static final int FAST_MA_PERIOD = 20;   // 20-period moving average
    private static final int SLOW_MA_PERIOD = 50;   // 50-period moving average
    private static final int QUANTITY = 1;           // Buy 1 share at a time

    @Override
    public String getName() {
        return "Moving Average Crossover (" + FAST_MA_PERIOD + "/" + SLOW_MA_PERIOD + ")";
    }

    @Override
    public TradeSignal analyze(String symbol, JsonNode bars) {
        try {
            // Extract close prices from bars
            double[] closePrices = extractClosePrices(bars);
            
            if (closePrices.length < SLOW_MA_PERIOD + 1) {
                logger.warn("Not enough data for {} - need {} bars, got {}", 
                    symbol, SLOW_MA_PERIOD + 1, closePrices.length);
                return new TradeSignal(symbol, TradeSignal.SignalType.HOLD, 0, 0, 
                    "Insufficient data");
            }

            // Calculate moving averages
            double fastMA = calculateMA(closePrices, FAST_MA_PERIOD);
            double slowMA = calculateMA(closePrices, SLOW_MA_PERIOD);
            
            // Previous period for crossover detection
            double prevFastMA = calculateMA(closePrices, FAST_MA_PERIOD, 1);
            double prevSlowMA = calculateMA(closePrices, SLOW_MA_PERIOD, 1);
            
            double currentPrice = closePrices[0];
            
            logger.info("{} - Price: ${:.2f}, Fast MA: ${:.2f}, Slow MA: ${:.2f}", 
                symbol, currentPrice, fastMA, slowMA);

            // Golden Cross: Fast MA crosses above Slow MA
            if (fastMA > slowMA && prevFastMA <= prevSlowMA) {
                return new TradeSignal(symbol, TradeSignal.SignalType.BUY, currentPrice, QUANTITY,
                    String.format("Golden Cross: Fast MA (%.2f) crossed above Slow MA (%.2f)", fastMA, slowMA));
            }

            // Death Cross: Fast MA crosses below Slow MA
            if (fastMA < slowMA && prevFastMA >= prevSlowMA) {
                return new TradeSignal(symbol, TradeSignal.SignalType.SELL, currentPrice, QUANTITY,
                    String.format("Death Cross: Fast MA (%.2f) crossed below Slow MA (%.2f)", fastMA, slowMA));
            }

            return new TradeSignal(symbol, TradeSignal.SignalType.HOLD, currentPrice, 0, "No crossover");
            
        } catch (Exception e) {
            logger.error("Error analyzing {} with MA strategy", symbol, e);
            return new TradeSignal(symbol, TradeSignal.SignalType.HOLD, 0, 0, "Error: " + e.getMessage());
        }
    }

    /**
     * Extract close prices from bars data
     */
    private double[] extractClosePrices(JsonNode bars) {
        JsonNode barsArray = bars.get("bars");
        if (barsArray == null) {
            barsArray = bars; // In case bars is already the array
        }
        
        int size = barsArray.size();
        double[] prices = new double[size];
        
        for (int i = 0; i < size; i++) {
            prices[i] = barsArray.get(i).get("c").asDouble();
        }
        
        return prices;
    }

    /**
     * Calculate Simple Moving Average
     * @param prices Price array (most recent at index 0)
     * @param period Number of periods
     */
    private double calculateMA(double[] prices, int period) {
        double sum = 0;
        for (int i = 0; i < period && i < prices.length; i++) {
            sum += prices[i];
        }
        return sum / Math.min(period, prices.length);
    }

    /**
     * Calculate Simple Moving Average with offset
     * @param prices Price array (most recent at index 0)
     * @param period Number of periods
     * @param offset Offset for historical periods (1 = previous period)
     */
    private double calculateMA(double[] prices, int period, int offset) {
        double sum = 0;
        for (int i = offset; i < period + offset && i < prices.length; i++) {
            sum += prices[i];
        }
        return sum / Math.min(period, prices.length - offset);
    }
}

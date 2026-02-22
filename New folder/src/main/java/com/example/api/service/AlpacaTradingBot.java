package com.example.api.service;

import com.example.api.model.TradeSignal;
import com.example.api.strategy.TradingStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Alpaca Trading Bot
 * 
 * This bot:
 * 1. Fetches market data at regular intervals
 * 2. Analyzes price using the trading strategy
 * 3. Executes trades based on signals
 * 4. Logs all activities
 */
@Service
public class AlpacaTradingBot {
    private static final Logger logger = LoggerFactory.getLogger(AlpacaTradingBot.class);
    
    private final AlpacaClient alpacaClient;
    private final TradingStrategy strategy;
    private final List<String> watchList = new ArrayList<>();
    private volatile boolean isRunning = false;

    public AlpacaTradingBot(AlpacaClient alpacaClient, TradingStrategy strategy) {
        this.alpacaClient = alpacaClient;
        this.strategy = strategy;
        this.watchList.add("AAPL");  // Default watch list - modify as needed
        this.watchList.add("GOOGL");
    }

    /**
     * Start the trading bot
     */
    public void start() throws IOException {
        isRunning = true;
        logger.info("=== Starting Alpaca Paper Trading Bot ===");
        logger.info("Strategy: {}", strategy.getName());
        logger.info("Watch List: {}", watchList);
        
        // Check account info
        try {
            JsonNode account = alpacaClient.getAccount();
            logger.info("Account Status: {}", account.get("status"));
            logger.info("Initial Buying Power: ${}", account.get("buying_power"));
            logger.info("Initial Cash: ${}", account.get("cash"));
        } catch (Exception e) {
            logger.error("Failed to fetch account info", e);
            isRunning = false;
        }
    }

    /**
     * Stop the trading bot
     */
    public void stop() {
        isRunning = false;
        logger.info("=== Stopping Alpaca Trading Bot ===");
    }

    /**
     * Run analysis and execute trades
     * Scheduled to run every hour (adjust as needed: 60000ms = 1 minute, 3600000ms = 1 hour)
     * For testing: use @Scheduled(fixedDelay = 60000) for every minute
     */
    @Scheduled(fixedDelay = 3600000)  // Every hour
    public void executeTrading() {
        if (!isRunning) {
            return;
        }

        logger.info("\n========== Trading Bot Cycle [{}] ==========", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        for (String symbol : watchList) {
            try {
                executeTradingCycle(symbol);
            } catch (IOException e) {
                logger.error("Error processing {}: {}", symbol, e.getMessage());
            }
        }
    }

    /**
     * Execute trading cycle for a single symbol
     */
    private void executeTradingCycle(String symbol) throws IOException {
        // Fetch market data (last 100 bars with 1-day timeframe)
        JsonNode barsData = alpacaClient.getBars(symbol, "1d", 100);
        
        // Analyze with strategy
        TradeSignal signal = strategy.analyze(symbol, barsData.get(symbol));
        
        logger.info("{} - Signal: {}", symbol, signal);

        // Execute trade based on signal
        switch (signal.getSignalType()) {
            case BUY:
                executeBuy(symbol, signal);
                break;
            case SELL:
                executeSell(symbol, signal);
                break;
            case HOLD:
                logger.info("{} - HOLD - No action taken", symbol);
                break;
        }
    }

    /**
     * Execute a buy order
     */
    private void executeBuy(String symbol, TradeSignal signal) {
        try {
            logger.info("[{}] BUYING {}", symbol, signal.getQuantity());
            logger.info("  Reason: {}", signal.getReason());
            logger.info("  Price: ${:.2f}", signal.getPrice());
            
            JsonNode orderResponse = alpacaClient.buyMarket(symbol, signal.getQuantity());
            
            if (orderResponse.has("id")) {
                logger.info("  Order ID: {}", orderResponse.get("id"));
                logger.info("  Status: {}", orderResponse.get("status"));
            }
            
        } catch (IOException e) {
            logger.error("Failed to buy {}: {}", symbol, e.getMessage());
        }
    }

    /**
     * Execute a sell order
     */
    private void executeSell(String symbol, TradeSignal signal) {
        try {
            logger.info("[{}] SELLING {}", symbol, signal.getQuantity());
            logger.info("  Reason: {}", signal.getReason());
            logger.info("  Price: ${:.2f}", signal.getPrice());
            
            JsonNode orderResponse = alpacaClient.sellMarket(symbol, signal.getQuantity());
            
            if (orderResponse.has("id")) {
                logger.info("  Order ID: {}", orderResponse.get("id"));
                logger.info("  Status: {}", orderResponse.get("status"));
            }
            
        } catch (IOException e) {
            logger.error("Failed to sell {}: {}", symbol, e.getMessage());
        }
    }

    /**
     * Manually add symbol to watch list
     */
    public void addToWatchList(String symbol) {
        if (!watchList.contains(symbol)) {
            watchList.add(symbol);
            logger.info("Added {} to watch list", symbol);
        }
    }

    /**
     * Manually remove symbol from watch list
     */
    public void removeFromWatchList(String symbol) {
        watchList.remove(symbol);
        logger.info("Removed {} from watch list", symbol);
    }

    /**
     * Get current positions
     */
    public JsonNode getPositions() throws IOException {
        return alpacaClient.getPositions();
    }

    /**
     * Get account details
     */
    public JsonNode getAccount() throws IOException {
        return alpacaClient.getAccount();
    }

    /**
     * Check if bot is running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Get current watch list
     */
    public List<String> getWatchList() {
        return new ArrayList<>(watchList);
    }
}

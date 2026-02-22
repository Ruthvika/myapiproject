package com.example.api.controller;

import com.example.api.service.AlpacaTradingBot;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API for controlling the Alpaca Trading Bot
 */
@RestController
@RequestMapping("/api/trading-bot")
public class TradingBotController {
    
    private final AlpacaTradingBot tradingBot;

    public TradingBotController(AlpacaTradingBot tradingBot) {
        this.tradingBot = tradingBot;
    }

    /**
     * Start the trading bot
     * GET /api/trading-bot/start
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start() {
        try {
            tradingBot.start();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Trading bot started");
            response.put("isRunning", tradingBot.isRunning());
            response.put("watchList", tradingBot.getWatchList());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    /**
     * Stop the trading bot
     * POST /api/trading-bot/stop
     */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stop() {
        tradingBot.stop();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Trading bot stopped");
        response.put("isRunning", tradingBot.isRunning());
        return ResponseEntity.ok(response);
    }

    /**
     * Get bot status
     * GET /api/trading-bot/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("isRunning", tradingBot.isRunning());
        response.put("watchList", tradingBot.getWatchList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get account information
     * GET /api/trading-bot/account
     */
    @GetMapping("/account")
    public ResponseEntity<?> getAccount() {
        try {
            JsonNode account = tradingBot.getAccount();
            Map<String, Object> response = new HashMap<>();
            response.put("status", account.get("status").asText());
            response.put("buyingPower", account.get("buying_power").asDouble());
            response.put("cash", account.get("cash").asDouble());
            response.put("portfolioValue", account.get("portfolio_value").asDouble());
            response.put("dayTradingBuyingPower", account.get("daytrading_buying_power").asDouble());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get current positions
     * GET /api/trading-bot/positions
     */
    @GetMapping("/positions")
    public ResponseEntity<?> getPositions() {
        try {
            JsonNode positions = tradingBot.getPositions();
            return ResponseEntity.ok(positions);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Add symbol to watch list
     * POST /api/trading-bot/watch-list?symbol=AAPL
     */
    @PostMapping("/watch-list")
    public ResponseEntity<Map<String, Object>> addToWatchList(@RequestParam String symbol) {
        tradingBot.addToWatchList(symbol.toUpperCase());
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Added " + symbol + " to watch list");
        response.put("watchList", tradingBot.getWatchList());
        return ResponseEntity.ok(response);
    }

    /**
     * Remove symbol from watch list
     * DELETE /api/trading-bot/watch-list?symbol=AAPL
     */
    @DeleteMapping("/watch-list")
    public ResponseEntity<Map<String, Object>> removeFromWatchList(@RequestParam String symbol) {
        tradingBot.removeFromWatchList(symbol.toUpperCase());
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Removed " + symbol + " from watch list");
        response.put("watchList", tradingBot.getWatchList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get watch list
     * GET /api/trading-bot/watch-list
     */
    @GetMapping("/watch-list")
    public ResponseEntity<Map<String, Object>> getWatchList() {
        Map<String, Object> response = new HashMap<>();
        response.put("watchList", tradingBot.getWatchList());
        response.put("count", tradingBot.getWatchList().size());
        return ResponseEntity.ok(response);
    }
}

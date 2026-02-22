package com.example.api.service;

import com.example.api.config.AlpacaConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AlpacaClient - Handles all API calls to Alpaca Trading API
 */
@Service
public class AlpacaClient {
    private static final Logger logger = LoggerFactory.getLogger(AlpacaClient.class);
    
    private final AlpacaConfig config;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AlpacaClient(AlpacaConfig config) {
        this.config = config;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get account information
     */
    public JsonNode getAccount() throws IOException {
        String url = config.getBaseUrl() + "/v2/account";
        return sendGetRequest(url);
    }

    /**
     * Get current positions
     */
    public JsonNode getPositions() throws IOException {
        String url = config.getBaseUrl() + "/v2/positions";
        return sendGetRequest(url);
    }

    /**
     * Get specific position by symbol
     */
    public JsonNode getPosition(String symbol) throws IOException {
        String url = config.getBaseUrl() + "/v2/positions/" + symbol;
        return sendGetRequest(url);
    }

    /**
     * Place a market buy order
     */
    public JsonNode buyMarket(String symbol, int quantity) throws IOException {
        String jsonPayload = String.format(
            "{\"symbol\":\"%s\",\"qty\":%d,\"side\":\"buy\",\"type\":\"market\",\"time_in_force\":\"day\"}",
            symbol, quantity
        );
        return sendPostRequest(config.getBaseUrl() + "/v2/orders", jsonPayload);
    }

    /**
     * Place a market sell order
     */
    public JsonNode sellMarket(String symbol, int quantity) throws IOException {
        String jsonPayload = String.format(
            "{\"symbol\":\"%s\",\"qty\":%d,\"side\":\"sell\",\"type\":\"market\",\"time_in_force\":\"day\"}",
            symbol, quantity
        );
        return sendPostRequest(config.getBaseUrl() + "/v2/orders", jsonPayload);
    }

    /**
     * Get bars (OHLCV data) for a symbol
     * Timeframe: 1min, 5min, 15min, 1h, 1d
     */
    public JsonNode getBars(String symbol, String timeframe, int limit) throws IOException {
        String url = String.format(
            "%s/v1beta3/stocks/%s/bars?timeframe=%s&limit=%d",
            config.getDataUrl(), symbol, timeframe, limit
        );
        return sendGetRequest(url);
    }

    /**
     * Get latest bar (current price) for a symbol
     */
    public JsonNode getLatestBar(String symbol) throws IOException {
        String url = String.format(
            "%s/v1beta3/stocks/%s/bars/latest",
            config.getDataUrl(), symbol
        );
        return sendGetRequest(url);
    }

    /**
     * Cancel all open orders
     */
    public void cancelAllOrders() throws IOException {
        String url = config.getBaseUrl() + "/v2/orders";
        sendDeleteRequest(url);
    }

    /**
     * Get all orders
     */
    public JsonNode getOrders() throws IOException {
        String url = config.getBaseUrl() + "/v2/orders";
        return sendGetRequest(url);
    }

    /**
     * Send GET request to Alpaca API
     */
    private JsonNode sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
            .url(url)
            .addHeader("APCA-API-KEY-ID", config.getApiKey())
            .addHeader("APCA-API-SECRET-KEY", config.getSecretKey())
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                logger.error("API Error: {} - {}", response.code(), responseBody);
                throw new IOException("API request failed: " + response.code());
            }
            
            return objectMapper.readTree(responseBody);
        }
    }

    /**
     * Send POST request to Alpaca API
     */
    private JsonNode sendPostRequest(String url, String jsonPayload) throws IOException {
        Request request = new Request.Builder()
            .url(url)
            .post(okhttp3.RequestBody.create(
                jsonPayload,
                okhttp3.MediaType.parse("application/json")
            ))
            .addHeader("APCA-API-KEY-ID", config.getApiKey())
            .addHeader("APCA-API-SECRET-KEY", config.getSecretKey())
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                logger.error("API Error: {} - {}", response.code(), responseBody);
                throw new IOException("API request failed: " + response.code());
            }
            
            return objectMapper.readTree(responseBody);
        }
    }

    /**
     * Send DELETE request to Alpaca API
     */
    private void sendDeleteRequest(String url) throws IOException {
        Request request = new Request.Builder()
            .url(url)
            .delete()
            .addHeader("APCA-API-KEY-ID", config.getApiKey())
            .addHeader("APCA-API-SECRET-KEY", config.getSecretKey())
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("API Error: {} - {}", response.code(), response.body().string());
            }
        }
    }
}

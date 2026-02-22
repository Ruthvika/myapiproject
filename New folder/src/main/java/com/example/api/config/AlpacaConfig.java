package com.example.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration for Alpaca Trading API
 * Add these properties to application.properties:
 * alpaca.api-key=YOUR_API_KEY
 * alpaca.secret-key=YOUR_SECRET_KEY
 * alpaca.base-url=https://paper-api.alpaca.markets (for paper trading)
 */
@Component
@ConfigurationProperties(prefix = "alpaca")
public class AlpacaConfig {
    private String apiKey;
    private String secretKey;
    private String baseUrl = "https://paper-api.alpaca.markets";
    private String dataUrl = "https://data.alpaca.markets";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }
}

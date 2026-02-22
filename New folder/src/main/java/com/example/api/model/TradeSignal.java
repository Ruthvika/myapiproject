package com.example.api.model;

/**
 * Represents a trading signal
 */
public class TradeSignal {
    public enum SignalType {
        BUY, SELL, HOLD
    }

    private String symbol;
    private SignalType signalType;
    private double price;
    private int quantity;
    private String reason;
    private long timestamp;

    public TradeSignal(String symbol, SignalType signalType, double price, int quantity, String reason) {
        this.symbol = symbol;
        this.signalType = signalType;
        this.price = price;
        this.quantity = quantity;
        this.reason = reason;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSymbol() {
        return symbol;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("TradeSignal{symbol='%s', type=%s, price=%.2f, qty=%d, reason='%s'}", 
            symbol, signalType, price, quantity, reason);
    }
}

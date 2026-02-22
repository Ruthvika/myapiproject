# Alpaca Paper Trading Bot

A beginner-friendly algorithmic trading bot that uses the **Alpaca API** to execute trades automatically based on technical analysis.

## Features

✅ **Paper Trading** - Test strategies with virtual money (no real money at risk)  
✅ **Moving Average Crossover Strategy** - Simple, effective trend-following strategy  
✅ **REST API** - Control the bot via HTTP endpoints  
✅ **Real Market Data** - Uses actual stock prices from Alpaca  
✅ **Automatic Execution** - Runs on a schedule to analyze and execute trades  
✅ **Logging** - Track all activities and trading signals  

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed
3. **Alpaca Account** (free) - Sign up at https://app.alpaca.markets
   - Get your **API Key** and **Secret Key** from the dashboard
   - Use **Paper Trading** (simulated) by default

## Setup Instructions

### 1. Clone/Open the Project
```bash
cd myapiproject
```

### 2. Get Your Alpaca API Keys
1. Go to https://app.alpaca.markets/paper/dashboard/home
2. Click on your account name → API Keys
3. Copy your API Key and Secret Key
4. Keep them safe (don't share publicly!)

### 3. Configure Application
Edit `src/main/resources/application.properties`:

```properties
alpaca.api-key=YOUR_API_KEY_HERE
alpaca.secret-key=YOUR_SECRET_KEY_HERE
alpaca.base-url=https://paper-api.alpaca.markets  # Paper trading (simulated)
```

### 4. Build the Project
```bash
mvn clean install
```

### 5. Run the Application
```bash
mvn spring-boot:run
```

You should see:
```
=== Starting Alpaca Paper Trading Bot ===
Strategy: Moving Average Crossover (20/50)
Watch List: [AAPL, GOOGL]
```

## API Endpoints

### Start Trading Bot
```bash
curl -X POST http://localhost:8080/api/trading-bot/start
```

Response:
```json
{
  "status": "success",
  "message": "Trading bot started",
  "isRunning": true,
  "watchList": ["AAPL", "GOOGL"]
}
```

### Stop Trading Bot
```bash
curl -X POST http://localhost:8080/api/trading-bot/stop
```

### Check Bot Status
```bash
curl http://localhost:8080/api/trading-bot/status
```

### Get Account Information
```bash
curl http://localhost:8080/api/trading-bot/account
```

Response:
```json
{
  "status": "ACTIVE",
  "buyingPower": 100000.00,
  "cash": 100000.00,
  "portfolioValue": 100000.00,
  "dayTradingBuyingPower": 100000.00
}
```

### Get Current Positions
```bash
curl http://localhost:8080/api/trading-bot/positions
```

### Add Symbol to Watch List
```bash
curl -X POST "http://localhost:8080/api/trading-bot/watch-list?symbol=TSLA"
```

### Remove Symbol from Watch List
```bash
curl -X DELETE "http://localhost:8080/api/trading-bot/watch-list?symbol=AAPL"
```

### Get Watch List
```bash
curl http://localhost:8080/api/trading-bot/watch-list
```

## Trading Strategy Explained

### Moving Average Crossover (20/50)

This strategy uses two moving averages:
- **Fast MA (20 days)** - Responds quickly to price changes
- **Slow MA (50 days)** - Smoother, shows long-term trend

**Buy Signal (Golden Cross):**
When the 20-day MA crosses above the 50-day MA, it indicates an uptrend starting.

**Sell Signal (Death Cross):**
When the 20-day MA crosses below the 50-day MA, it indicates a downtrend starting.

```
        Price
          ↑
    20-day MA  ╱╲ (crosses up = BUY)
              ╱  ╲
    50-day MA ─────── (slower, baseline)
          
Example: BUY when fast MA > slow MA (Golden Cross)
         SELL when fast MA < slow MA (Death Cross)
```

## Trading Cycle

The bot runs on a schedule:

1. **Every Hour** (configurable in `AlpacaTradingBot.java`):
   - Fetches historical price data (100 daily bars)
   - Analyzes each symbol using the Moving Average Crossover strategy
   - Generates trading signals (BUY, SELL, HOLD)
   - Executes trades if signals are generated

2. **Logging**:
   - All activities are logged to console
   - Check the logs for trade executions and reasons

## Customization Guide

### Change Watch List
Edit `AlpacaTradingBot.java`:
```java
private final List<String> watchList = new ArrayList<>();

public AlpacaTradingBot(...) {
    this.watchList.add("AAPL");      // Change this
    this.watchList.add("GOOGL");     // Change this
}
```

### Change Trading Frequency
Edit `AlpacaTradingBot.java` - modify the `@Scheduled` annotation:
```java
@Scheduled(fixedDelay = 60000)      // Every 1 minute (for testing)
@Scheduled(fixedDelay = 300000)     // Every 5 minutes
@Scheduled(fixedDelay = 3600000)    // Every 1 hour (default)
```

### Change MA Periods
Edit `MovingAverageCrossoverStrategy.java`:
```java
private static final int FAST_MA_PERIOD = 20;    // Change this
private static final int SLOW_MA_PERIOD = 50;    // Change this
```

### Change Quantity per Trade
Edit `MovingAverageCrossoverStrategy.java`:
```java
private static final int QUANTITY = 1;   // Buy/sell this many shares
```

## Import a Custom Strategy

Create a new strategy by implementing the `TradingStrategy` interface:

```java
@Component
public class MyCustomStrategy implements TradingStrategy {
    @Override
    public String getName() {
        return "My Custom Strategy";
    }

    @Override
    public TradeSignal analyze(String symbol, JsonNode bars) {
        // Your analysis logic here
        return new TradeSignal(symbol, TradeSignal.SignalType.BUY, price, qty, reason);
    }
}
```

Then update `AlpacaTradingBot` to use your strategy.

## Important Notes

⚠️ **Paper Trading:**
- Uses virtual money (no real money)
- Testing environment only
- Perfect for learning and testing strategies

⚠️ **Before Going Live:**
1. Test thoroughly with paper trading
2. Monitor your strategy for a few weeks
3. Understand the risks
4. Change to live API key only when confident

⚠️ **Risk Management:**
- Start with small quantities
- Set stop-losses if needed
- Monitor your orders regularly
- Never risk money you can't afford to lose

## Troubleshooting

### "API request failed: 401"
- Check your API Key and Secret Key
- Make sure they're copied correctly in `application.properties`

### "Not enough data for X - need 51 bars, got N"
- The bot needs at least 51 days of historical data
- Try adding more symbols or waiting a few days

### No trades being executed
- Check if bot is running: `curl http://localhost:8080/api/trading-bot/status`
- Check the logs for error messages
- Verify the strategy is generating signals

### "pathspec 'develop' did not match any file(s) known to git"
- Make sure you're on the develop branch: `git checkout develop`

## Resources

- **Alpaca Documentation**: https://docs.alpaca.markets
- **API Reference**: https://alpaca.markets/docs/api-references/
- **Paper Trading**: https://app.alpaca.markets/paper/dashboard/home
- **Market Data API**: https://data.alpaca.markets

## Next Steps

1. ✅ Set up Alpaca account and get API keys
2. ✅ Configure the bot with your API keys
3. ✅ Start the bot and monitor paper trades
4. ✅ Customize the strategy as needed
5. ✅ Test for a few weeks before going live
6. ✅ Gradually increase trade sizes as you gain confidence

## License

This project is provided as-is for educational purposes.

## Disclaimer

Trading stocks involves risk of loss. This bot is for educational purposes only. Always test thoroughly with paper trading before risking real money. The author is not responsible for any losses incurred.

Happy Trading! 🚀📈

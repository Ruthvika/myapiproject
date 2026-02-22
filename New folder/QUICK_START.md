# Quick Start Guide - Alpaca Trading Bot

## 5-Minute Setup

### Step 1: Get Alpaca API Keys (2 minutes)
1. Go to https://app.alpaca.markets (create free account if needed)
2. Dashboard → API Keys
3. Copy your **API Key** and **Secret Key**

### Step 2: Update Configuration (1 minute)
Edit `src/main/resources/application.properties`:

```properties
alpaca.api-key=YOUR_KEY_HERE
alpaca.secret-key=YOUR_SECRET_HERE
```

### Step 3: Build & Run (2 minutes)
```bash
mvn clean install
mvn spring-boot:run
```

### Step 4: Start Trading
```bash
curl -X POST http://localhost:8080/api/trading-bot/start
```

### Step 5: Monitor
```bash
curl http://localhost:8080/api/trading-bot/status
curl http://localhost:8080/api/trading-bot/account
```

## That's it! 🎉

Your bot is now analyzing AAPL and GOOGL with the Moving Average Crossover strategy and will execute trades automatically!

## Check Logs
Watch the console output for trading signals and executions.

## Change Watch List
```bash
# Add a stock
curl -X POST "http://localhost:8080/api/trading-bot/watch-list?symbol=TSLA"

# Remove a stock
curl -X DELETE "http://localhost:8080/api/trading-bot/watch-list?symbol=AAPL"

# View current list
curl http://localhost:8080/api/trading-bot/watch-list
```

## Project Structure
```
src/
├── main/
│   ├── java/com/example/api/
│   │   ├── ApiApplication.java          (Main app, enables scheduling)
│   │   ├── config/
│   │   │   └── AlpacaConfig.java        (Alpaca API configuration)
│   │   ├── controller/
│   │   │   └── TradingBotController.java (REST API endpoints)
│   │   ├── model/
│   │   │   └── TradeSignal.java         (Trading signal data)
│   │   ├── service/
│   │   │   ├── AlpacaClient.java        (API communication)
│   │   │   └── AlpacaTradingBot.java    (Main bot logic)
│   │   └── strategy/
│   │       ├── TradingStrategy.java     (Strategy interface)
│   │       └── MovingAverageCrossoverStrategy.java (MA crossover strategy)
│   └── resources/
│       └── application.properties       (Configuration file)
```

## Key Concepts

**Paper Trading**: Virtual money, no real risk - perfect for learning!

**Moving Average Crossover**: 
- Buy when 20-day MA crosses above 50-day MA (uptrend)
- Sell when 20-day MA crosses below 50-day MA (downtrend)

**REST API**: Control the bot via HTTP from any client

## Next Steps
- Read `TRADING_BOT_README.md` for full documentation
- Customize the strategy in `MovingAverageCrossoverStrategy.java`
- Add more stocks to the watch list
- Monitor paper trades for a few weeks
- Create your own strategies!

## Support
- Alpaca Docs: https://docs.alpaca.markets
- Community: https://alpaca.markets/community

Good luck! 📊

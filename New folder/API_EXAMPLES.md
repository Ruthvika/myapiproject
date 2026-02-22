# API Examples - Alpaca Trading Bot

These are example curl commands to test and interact with your trading bot.

## Prerequisites
- Application running: `mvn spring-boot:run`
- API configured with your Alpaca credentials
- Base URL: `http://localhost:8080/api`

## Example Commands

### 1. Start the Trading Bot (Required First)
```bash
curl -X POST http://localhost:8080/api/trading-bot/start
```

### 2. Get Bot Status
```bash
curl http://localhost:8080/api/trading-bot/status
```

Example Response:
```json
{
  "isRunning": true,
  "watchList": ["AAPL", "GOOGL"]
}
```

### 3. Get Account Information
```bash
curl http://localhost:8080/api/trading-bot/account
```

Example Response:
```json
{
  "status": "ACTIVE",
  "buyingPower": 100000.0,
  "cash": 100000.0,
  "portfolioValue": 100000.0,
  "dayTradingBuyingPower": 100000.0
}
```

### 4. Get Current Positions
```bash
curl http://localhost:8080/api/trading-bot/positions
```

Example Response (when you own shares):
```json
[
  {
    "symbol": "AAPL",
    "qty": 10,
    "avg_fill_price": 150.25,
    "current_price": 155.50,
    "side": "long"
  }
]
```

### 5. View Watch List
```bash
curl http://localhost:8080/api/trading-bot/watch-list
```

Example Response:
```json
{
  "watchList": ["AAPL", "GOOGL"],
  "count": 2
}
```

### 6. Add Stock to Watch List
```bash
curl -X POST "http://localhost:8080/api/trading-bot/watch-list?symbol=TSLA"
curl -X POST "http://localhost:8080/api/trading-bot/watch-list?symbol=MSFT"
```

### 7. Remove Stock from Watch List
```bash
curl -X DELETE "http://localhost:8080/api/trading-bot/watch-list?symbol=GOOGL"
```

### 8. Stop the Bot
```bash
curl -X POST http://localhost:8080/api/trading-bot/stop
```

## Using with Postman or Others

If you prefer a GUI, import these as Postman requests:

1. **Start Bot**
   - Method: POST
   - URL: `http://localhost:8080/api/trading-bot/start`

2. **Check Status**
   - Method: GET
   - URL: `http://localhost:8080/api/trading-bot/status`

3. **Get Account**
   - Method: GET
   - URL: `http://localhost:8080/api/trading-bot/account`

4. **Get Positions**
   - Method: GET
   - URL: `http://localhost:8080/api/trading-bot/positions`

5. **Get Watch List**
   - Method: GET
   - URL: `http://localhost:8080/api/trading-bot/watch-list`

6. **Add to Watch List**
   - Method: POST
   - URL: `http://localhost:8080/api/trading-bot/watch-list?symbol=STOCK_SYMBOL`

7. **Remove from Watch List**
   - Method: DELETE
   - URL: `http://localhost:8080/api/trading-bot/watch-list?symbol=STOCK_SYMBOL`

8. **Stop Bot**
   - Method: POST
   - URL: `http://localhost:8080/api/trading-bot/stop`

## Monitoring in Real-Time

### Watch Console Output
The bot prints all trading signals and executions to the console:

```
========== Trading Bot Cycle [2026-02-22 10:30:00] ==========
AAPL - Price: $150.25, Fast MA: $149.50, Slow MA: $148.75
[AAPL] BUYING 1
  Reason: Golden Cross: Fast MA (149.50) crossed above Slow MA (148.75)
  Price: $150.25
  Order ID: 12345abc
  Status: filled

GOOGL - Price: $140.50, Fast MA: $139.75, Slow MA: $141.25
GOOGL - HOLD - No action taken
```

### Check Logs
```bash
# If running in background, check logs:
tail -f logs/application.log  # Linux/Mac
Get-Content logs/application.log -Tail 20 -Wait  # PowerShell
```

## Testing Workflow

1. **Start the bot:**
   ```bash
   curl -X POST http://localhost:8080/api/trading-bot/start
   ```

2. **Wait for the next trading cycle** (default: hourly, but you can change to 1-minute for testing)

3. **Check for new positions:**
   ```bash
   curl http://localhost:8080/api/trading-bot/positions
   ```

4. **Add more stocks to watch:**
   ```bash
   curl -X POST "http://localhost:8080/api/trading-bot/watch-list?symbol=NVDA"
   ```

5. **Monitor the account balance:**
   ```bash
   curl http://localhost:8080/api/trading-bot/account
   ```

6. **Stop when done:**
   ```bash
   curl -X POST http://localhost:8080/api/trading-bot/stop
   ```

## Useful Tips

- **Paper Trading**: All trades in paper trading are simulated - no real money!
- **Watch the Console**: The most important info is printed to the console
- **Test with 1-minute intervals**: Change `@Scheduled(fixedDelay = 3600000)` to `60000` or `300000` for testing
- **Keep track of wins/losses**: Review positions and account balance regularly
- **Monitor multiple stocks**: Add different symbols to the watch list

## Troubleshooting

### Command Not Found?
Make sure to include the full URL: `http://localhost:8080/api/trading-bot/...`

### 500 Error?
- Check if the app is running: `mvn spring-boot:run`
- Check API credentials in `application.properties`
- Look at console output for detailed error message

### No Trades Executing?
- Verify bot is running: `curl http://localhost:8080/api/trading-bot/status`
- Check if you have enough data (need 50+ days of historical bars)
- Verify strategy is generating signals (check console logs)

## Next Steps

1. Run these commands to test your bot
2. Monitor the console output for trading signals
3. Check positions regularly
4. Experiment with different stocks
5. Eventually test with live trading (after several weeks of paper trading)

Happy Trading! 📊🚀

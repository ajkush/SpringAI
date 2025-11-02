# Quick Start Guide

## Start the App

```bash
./run-app.sh
```

Wait for: `Started WeatherApplication in X.XXX seconds`

## Use the App

### Web Interface (Best Experience)
```
http://localhost:8080
```

Features:
- Click on world map to get weather
- Search any city
- View raw data or AI summaries
- Activity and clothing recommendations

### API Docs
```
http://localhost:8080/swagger-ui.html
```

### Quick Test
```bash
curl http://localhost:8080/api/weather/London/raw
```

## Stop the App

Press `Ctrl+C`

## Troubleshooting

**Port 8080 busy?**
```bash
lsof -i :8080
kill -9 <PID>
```

**Need more details?** Read README.md

That's it! Enjoy your weather app.



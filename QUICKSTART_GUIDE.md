# Quick Start Guide

## Summary of Fixes Applied

The main issue preventing the application from working was **WebClient configuration**. The WebClient had no base URL configured, causing it to default to `localhost:80` instead of connecting to the OpenWeatherMap API at `https://api.openweathermap.org/data/2.5`.

### Files Modified:
1. **WebClientConfig.java** - Added base URL configuration for OpenWeatherMap API
2. **WeatherService.java** - Simplified URI paths since base URL is now in WebClient config

## Starting the Application

### Step 1: Set Environment Variables & Start

```bash
# Set your OpenWeatherMap API key (FREE - get from https://openweathermap.org/api)
export OPENWEATHER_API_KEY=your-openweathermap-api-key-here

# OPTIONAL: Set OpenAI API key for AI-powered summaries
# Note: OpenAI requires $5 minimum payment - no free tier available
# Get from: https://platform.openai.com/api-keys
export OPENAI_API_KEY=your-openai-api-key-here  # Optional - for AI features only

# Start the application  
./mvnw spring-boot:run
```

**OR use the provided script:**

```bash
./run-app.sh
```

### Step 2: Wait for Startup

Wait about 20-30 seconds for the application to fully start. You'll see a message like:
```
Started WeatherApplication in X.XXX seconds
```

### Step 3: Test the Endpoints

Open a NEW terminal window and run these tests:

```bash
# Test 1: Raw weather data (should work)
curl http://localhost:8080/api/weather/London/raw

# Test 2: AI-powered weather summary
curl http://localhost:8080/api/weather/Paris

# Test 3: Weather by coordinates  
curl "http://localhost:8080/api/weather/coordinates?lat=35.6762&lon=139.6503"

# Test 4: Activity analysis
curl -X POST http://localhost:8080/api/weather/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "location": "Tokyo",
    "userQuery": "Should I go running this morning?",
    "activityType": "running"
  }'
```

## What Works WITHOUT OpenAI (100% Free)

✓ **Weather Data Retrieval** - Uses free OpenWeatherMap API
✓ **Raw Weather Endpoint** - Returns complete weather data
✓ **Weather by Coordinates** - Click on map or use lat/lon
✓ **Interactive Web UI** - Full web interface with world map
✓ **Caching** - 10-minute cache to reduce API calls  
✓ **Error Handling** - Proper exception handling

## What Requires OpenAI API Key (Paid - $5 minimum)

⚠️ **AI Weather Summaries** - Natural language weather descriptions
⚠️ **Activity Recommendations** - "Should I go running?" type queries
⚠️ **Clothing Suggestions** - AI-powered outfit recommendations
⚠️ **Chat Endpoint** - Conversational weather interface

**Note:** The application works fully without OpenAI. You only need it if you want AI-powered features.

## Troubleshooting

### Port 8080 Already in Use

```bash
# Find and kill the process using port 8080
lsof -ti:8080 | xargs kill -9

# Then restart the application
./run-app.sh
```

### Application Won't Start

```bash
# Clean rebuild
./mvnw clean package -DskipTests

# Then start
./run-app.sh
```

### Check if Application is Running

```bash
# Check if port 8080 is listening
lsof -i:8080

# Quick health check
curl http://localhost:8080/api/weather/London/raw
```

## API Documentation

Once the application is running, access the interactive API documentation:

```
http://localhost:8080/swagger-ui.html
```

## Key Configuration Files

- **`application.properties`** - Contains all configuration including API keys
- **`WeatherApiProperties.java`** - Weather API configuration properties
- **`WebClientConfig.java`** - **FIXED** - Now properly configures base URL
- **`run-app.sh`** - Startup script with environment variables

## Environment Variables

### Required (Free):
```bash
OPENWEATHER_API_KEY=your-openweathermap-api-key-here
```
Get your free API key at: https://openweathermap.org/api



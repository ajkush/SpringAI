# API Keys Guide

## Overview

This application uses two different API services:

1. **OpenWeatherMap API** - Required (FREE)
2. **OpenAI API** - Optional (PAID)

## OpenWeatherMap API (Required - FREE)

### What It Does
Provides real-time weather data for 200,000+ cities worldwide.

### Cost
**100% FREE** - No credit card required. Free tier includes:
- 1,000 API calls per day
- 60 calls per minute
- Current weather data
- 5-day forecast

### How to Get Your API Key

1. Go to: https://openweathermap.org/api
2. Click "Get API Key" or "Sign Up"
3. Create a free account (no payment required)
4. Verify your email
5. Go to https://home.openweathermap.org/api_keys
6. Copy your API key (looks like: `4c9d59b81edf8f00e333ec9bafa15032`)

### How to Use It

Set the environment variable:
```bash
export OPENWEATHER_API_KEY=your-key-here
```

Or edit the `.env` file:
```bash
OPENWEATHER_API_KEY=your-key-here
```

### Activation Time
API keys can take **10-15 minutes** to activate after creation. If you get authentication errors immediately after signup, wait a bit and try again.

## OpenAI API (Optional - PAID)

### What It Does
Powers the AI features:
- Natural language weather summaries
- Activity recommendations ("Should I go running?")
- Clothing suggestions
- Conversational weather chat

### Cost
**PAID SERVICE** - OpenAI phased out free API credits in 2024.

**Minimum Requirements:**
- $5 minimum account balance required
- Pay-as-you-go pricing
- Typical costs: $0.01-0.03 per request (using GPT-4)
- Budget control available in dashboard

**Pricing Details:**
- GPT-4: ~$0.03 per 1K tokens input, ~$0.06 per 1K tokens output
- GPT-3.5-turbo: ~$0.0005 per 1K tokens (much cheaper)
- A typical weather summary uses ~500-1000 tokens

**More info:** https://openai.com/api/pricing/

### How to Get Your API Key

1. Go to: https://platform.openai.com/signup
2. Create an account
3. Go to: https://platform.openai.com/account/billing
4. **Add $5 minimum to your account** (required)
5. Go to: https://platform.openai.com/api-keys
6. Click "Create new secret key"
7. Copy your API key (starts with `sk-proj-...`)
8. **IMPORTANT:** Save it immediately - you can't view it again!

### How to Use It

Set the environment variable:
```bash
export OPENAI_API_KEY=sk-proj-your-key-here
```

Or edit the `.env` file:
```bash
OPENAI_API_KEY=sk-proj-your-key-here
```

### Do You Need OpenAI?

**NO!** The application works perfectly without it. You get:
- ✓ Full weather data
- ✓ All endpoints working
- ✓ Interactive web UI
- ✓ Swagger documentation

**Without OpenAI, you miss:**
- ✗ AI-generated weather summaries
- ✗ Natural language activity recommendations
- ✗ Conversational chat interface

## Setting Up Both Keys

### Option 1: Environment Variables (Recommended)

```bash
# Required
export OPENWEATHER_API_KEY=your-openweathermap-key

# Optional
export OPENAI_API_KEY=your-openai-key  # Only if you want AI features

# Start the app
./run-app.sh
```

### Option 2: Create .env File

Copy the example file:
```bash
cp .env.example .env
```

Edit `.env`:
```bash
# Required
OPENWEATHER_API_KEY=your-openweathermap-key

# Optional (comment out if not using)
# OPENAI_API_KEY=your-openai-key
```

Then source it:
```bash
source .env
./run-app.sh
```

### Option 3: Edit application.properties

Edit `src/main/resources/application.properties`:
```properties
# Required
weather.api.key=${OPENWEATHER_API_KEY:your-openweathermap-key}

# Optional
spring.ai.openai.api-key=${OPENAI_API_KEY:not-set}
```

**Note:** This hardcodes keys in your source code - not recommended for version control.

## Verifying Your Setup

### Test OpenWeatherMap API
```bash
curl "http://localhost:8080/api/weather/London/raw"
```

**Success:** Returns JSON with weather data
**Failure:** Error message about API authentication

### Test OpenAI API (if configured)
```bash
curl "http://localhost:8080/api/weather/Paris"
```

**Success:** Returns AI-generated weather summary
**Failure:** Returns raw data or error message

## Common Issues

### "Invalid API key" for OpenWeatherMap
- Wait 10-15 minutes after creating the key
- Check you copied the entire key
- Verify at: https://home.openweathermap.org/api_keys

### "Authentication failed" for OpenAI
- Ensure you've added $5 to your account
- Check the key starts with `sk-proj-` or `sk-`
- Verify you copied the entire key
- Check at: https://platform.openai.com/api-keys

### Application starts but all requests fail
- Environment variables not set properly
- Try: `echo $OPENWEATHER_API_KEY` to verify
- Restart application after setting variables

### OpenAI features not working
- Make sure you have $5+ account balance
- Check rate limits in OpenAI dashboard
- Try switching to GPT-3.5-turbo in code (cheaper)

## Security Best Practices

1. **Never commit API keys to Git**
   - Use `.env` files (already in `.gitignore`)
   - Use environment variables
   
2. **Rotate keys periodically**
   - OpenWeatherMap: Generate new key monthly
   - OpenAI: Rotate every 90 days

3. **Set usage limits**
   - OpenWeatherMap: Monitor daily call count
   - OpenAI: Set monthly budget limits in dashboard

4. **Use separate keys for dev/prod**
   - Create different keys for testing vs production

## Cost Comparison

### Running This App for a Month

**FREE Option (OpenWeatherMap only):**
- Cost: $0
- API calls: Up to 30,000/month (1,000/day)
- Features: Full weather data

**With OpenAI (assuming 100 requests/day):**
- OpenWeatherMap: $0 (free tier)
- OpenAI: ~$3-9/month (depending on model)
- Total: ~$3-9/month

**Recommendation:** Start with FREE version. Add OpenAI later if you really need AI features.

## Summary

| Feature | OpenWeatherMap | OpenAI |
|---------|---------------|---------|
| **Cost** | FREE | $5 minimum |
| **Required** | Yes | No |
| **Setup Time** | 5 minutes | 10 minutes + payment |
| **Activation** | 10-15 minutes | Immediate |
| **Free Trial** | Yes (forever) | No (since 2024) |
| **What You Get** | Weather data | AI summaries |

**Bottom Line:** You only need OpenWeatherMap to run this app. OpenAI is nice-to-have for fancy AI features.


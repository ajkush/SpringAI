# API Keys Guide

## Overview

This application uses two different API services:

1. **OpenWeatherMap API** - Required (FREE)

## OpenWeatherMap API (Required - FREE)

### What It Does
Provides real-time weather data for 200,000+ cities worldwide.

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

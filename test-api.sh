#!/bin/bash

echo "======================================"
echo "Weather AI Application - Test Script"
echo "======================================"
echo ""

# Check if the application is running
echo "1. Checking if application is running..."
if curl -s http://localhost:8080/api/weather/health > /dev/null; then
    echo "✓ Application is running"
else
    echo "✗ Application is not running. Please start it with: ./mvnw spring-boot:run"
    exit 1
fi
echo ""

# Test health endpoint
echo "2. Testing health endpoint..."
HEALTH=$(curl -s http://localhost:8080/api/weather/health)
echo "$HEALTH" | grep -q "UP" && echo "✓ Health check passed" || echo "✗ Health check failed"
echo ""

# Test raw weather endpoint
echo "3. Testing raw weather endpoint for London..."
RAW_WEATHER=$(curl -s http://localhost:8080/api/weather/London/raw)
if echo "$RAW_WEATHER" | grep -q "London"; then
    echo "✓ Raw weather endpoint works"
    echo "   Temperature: $(echo $RAW_WEATHER | grep -o '"temp":[0-9.]*' | cut -d: -f2)°C"
else
    echo "✗ Raw weather endpoint failed"
    echo "   Response: $RAW_WEATHER"
fi
echo ""

# Test AI-enhanced weather endpoint
echo "4. Testing AI-enhanced weather endpoint for Paris..."
AI_WEATHER=$(curl -s http://localhost:8080/api/weather/Paris)
if echo "$AI_WEATHER" | grep -q "Paris"; then
    echo "✓ AI-enhanced weather endpoint works"
    if echo "$AI_WEATHER" | grep -q "aiSummary"; then
        echo "✓ AI summary generated"
    else
        echo "⚠ AI summary may be missing (check OpenAI API key)"
    fi
else
    echo "✗ AI-enhanced weather endpoint failed"
    echo "   Response: $AI_WEATHER"
fi
echo ""

# Test coordinates endpoint
echo "5. Testing weather by coordinates (Tokyo: 35.6762, 139.6503)..."
COORD_WEATHER=$(curl -s "http://localhost:8080/api/weather/coordinates?lat=35.6762&lon=139.6503")
if echo "$COORD_WEATHER" | grep -q "Tokyo"; then
    echo "✓ Coordinates endpoint works"
else
    echo "✗ Coordinates endpoint failed"
fi
echo ""

# Test chat endpoint
echo "6. Testing chat endpoint..."
CHAT_RESPONSE=$(curl -s "http://localhost:8080/api/weather/chat?query=What+is+weather")
if echo "$CHAT_RESPONSE" | grep -q "answer"; then
    echo "✓ Chat endpoint works"
else
    echo "✗ Chat endpoint failed"
    echo "   Response: $CHAT_RESPONSE"
fi
echo ""

# Test analyze endpoint
echo "7. Testing analyze endpoint..."
ANALYZE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/weather/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "location": "New York",
    "userQuery": "Should I go hiking?",
    "activityType": "hiking",
    "includeHealthAlerts": true
  }')
if echo "$ANALYZE_RESPONSE" | grep -q "analysis"; then
    echo "✓ Analyze endpoint works"
else
    echo "✗ Analyze endpoint failed"
    echo "   Response: $ANALYZE_RESPONSE"
fi
echo ""

echo "======================================"
echo "Test Summary"
echo "======================================"
echo "All basic endpoints have been tested."
echo ""
echo "To view detailed API documentation:"
echo "  http://localhost:8080/swagger-ui.html"
echo ""
echo "To view API docs JSON:"
echo "  http://localhost:8080/api-docs"
echo ""


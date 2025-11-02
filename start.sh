#!/bin/bash

echo "========================================"
echo "  AI-Powered Weather Information System"
echo "========================================"
echo ""

# Check if API keys are set
if [ -z "$OPENWEATHER_API_KEY" ]; then
    echo "⚠️  WARNING: OPENWEATHER_API_KEY environment variable is not set"
    echo "   Set it with: export OPENWEATHER_API_KEY=your-key"
    echo ""
fi

if [ -z "$OPENAI_API_KEY" ]; then
    echo "⚠️  WARNING: OPENAI_API_KEY environment variable is not set"
    echo "   Set it with: export OPENAI_API_KEY=your-key"
    echo ""
fi

# Check if both keys are set
if [ -z "$OPENWEATHER_API_KEY" ] || [ -z "$OPENAI_API_KEY" ]; then
    echo "❌ Cannot start application without API keys"
    echo ""
    echo "To set up:"
    echo "  1. Get OpenWeatherMap API key from: https://openweathermap.org/api"
    echo "  2. Get OpenAI API key from: https://platform.openai.com/api-keys"
    echo "  3. Set environment variables:"
    echo "     export OPENWEATHER_API_KEY=your-openweathermap-key"
    echo "     export OPENAI_API_KEY=your-openai-key"
    echo ""
    echo "Or copy .env.example to .env and fill in your keys"
    echo ""
    exit 1
fi

echo "✅ API keys configured"
echo ""

# Check if JAR exists
if [ ! -f "target/weather-0.0.1-SNAPSHOT.jar" ]; then
    echo "📦 JAR file not found. Building application..."
    ./mvnw clean package -DskipTests
    if [ $? -ne 0 ]; then
        echo "❌ Build failed"
        exit 1
    fi
    echo "✅ Build successful"
    echo ""
fi

echo "🚀 Starting Weather AI Application..."
echo ""
echo "The application will be available at:"
echo "  - API:        http://localhost:8080/api/weather"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "  - Health:     http://localhost:8080/api/weather/health"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""
echo "========================================"
echo ""

# Run the application
java -jar target/weather-0.0.1-SNAPSHOT.jar


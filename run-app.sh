#!/bin/bash

# Weather App Startup Script
echo "=============================================="
echo "   Weather App - Starting Application"
echo "=============================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 21 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo "Java version detected: $JAVA_VERSION"

if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "Warning: Java 21 or higher is recommended"
fi

echo ""
echo "Building application..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed. Please check the error messages above."
    exit 1
fi

echo ""
echo "=============================================="
echo "   Application built successfully!"
echo "=============================================="
echo ""
echo "Starting Weather App..."
echo ""
echo "Access the application at:"
echo "  Web Interface: http://localhost:8080"
echo "  Swagger UI:    http://localhost:8080/swagger-ui.html"
echo ""
echo "Press Ctrl+C to stop the application"
echo "=============================================="
echo ""

# Run the application
java -jar target/weather-0.0.1-SNAPSHOT.jar


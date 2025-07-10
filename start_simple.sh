#!/bin/bash
# Simplified startup script without sudo

echo "🚀 Starting Vehicle E-commerce Application (No Sudo)..."

# Set environment variables
export CATALINA_HOME=/usr/share/tomcat10
export CATALINA_BASE=/var/lib/tomcat10
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Check if Tomcat is already running
if pgrep -f tomcat > /dev/null; then
    echo "✅ Tomcat is already running"
else
    echo "🔧 Starting Tomcat..."
    
    # Try to start Tomcat directly
    if [ -x "$CATALINA_HOME/bin/catalina.sh" ]; then
        echo "📍 Using catalina.sh from $CATALINA_HOME/bin/"
        $CATALINA_HOME/bin/catalina.sh start
        sleep 5
    else
        echo "❌ catalina.sh not found or not executable"
        exit 1
    fi
fi

# Wait for Tomcat to start
echo "⏳ Waiting for Tomcat to start..."
for i in {1..30}; do
    if curl -s http://localhost:8080 > /dev/null; then
        echo "✅ Tomcat is responding on port 8080"
        break
    fi
    echo "   Attempt $i/30..."
    sleep 2
done

# Test if our servlet is accessible
echo "🧪 Testing VehicleServlet..."
response=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=c&page=1")
if [ "$response" = "200" ]; then
    echo "✅ VehicleServlet is responding (HTTP $response)"
    
    # Test with actual output
    echo "📄 Testing servlet output..."
    curl -s "http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=c&page=1" | head -5
else
    echo "❌ VehicleServlet returned HTTP $response"
fi

echo ""
echo "🌐 Application URLs:"
echo "   - Main: http://localhost:8080/vehicle-ecommerce/"
echo "   - Cars: http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=c&page=1"
echo "   - Boats: http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=b&page=1"
echo "   - Motorcycles: http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=m&page=1"

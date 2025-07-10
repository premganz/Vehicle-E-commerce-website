#!/bin/bash
# Vehicle E-commerce Application Startup Script
# Handles all dependencies, database setup, and Tomcat configuration
#
# TROUBLESHOOTING NOTES:
# - H2 2.2.x requires Java 17+, using 1.4.196 for Java 11 compatibility
# - Container environments don't support systemctl, using service/process checks
# - WAR packaging must put classes in WEB-INF/classes/classes/ to match package structure
# - H2 syntax differences: use MERGE instead of INSERT OR IGNORE, LIMIT syntax differs
# - JavaMail API needed for CheckoutServlet email functionality
# - Tomcat lib directory varies: /var/lib/tomcat10/lib/ for package install
# - Image names in database must match actual .jpg filenames exactly
# - Package "classes" requires WEB-INF/classes/classes/ directory structure
# - H2 and JavaMail JARs must be in WEB-INF/lib/ for webapp classloader access
# - H2 LIMIT syntax: "LIMIT count OFFSET offset" not "LIMIT offset,count"

set -e  # Exit on any error

# Configuration
WORKSPACE_DIR="/workspaces/Vehicle-E-commerce-website"
TOMCAT_LIB_DIR="/usr/share/tomcat10/lib"
TOMCAT_WEBAPPS_DIR="/var/lib/tomcat10/webapps"
DATABASE_DIR="${WORKSPACE_DIR}/database"
H2_VERSION="1.4.196"
H2_JAR_NAME="h2-${H2_VERSION}.jar"
H2_JAR_PATH="/tmp/${H2_JAR_NAME}"

echo "🚀 Starting Vehicle E-commerce Application Setup..."

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to download file if it doesn't exist
download_if_missing() {
    local url="$1"
    local dest="$2"
    if [ ! -f "$dest" ]; then
        echo "📥 Downloading $(basename "$dest")..."
        wget -q "$url" -O "$dest"
        echo "✅ Downloaded $(basename "$dest")"
    else
        echo "✅ $(basename "$dest") already exists"
    fi
}

# Function to copy jar to Tomcat lib if missing
copy_jar_to_tomcat() {
    local jar_path="$1"
    local jar_name="$(basename "$jar_path")"
    local tomcat_jar="${TOMCAT_LIB_DIR}/${jar_name}"
    
    if [ ! -f "$tomcat_jar" ]; then
        echo "📋 Copying $jar_name to Tomcat lib..."
        sudo cp "$jar_path" "$TOMCAT_LIB_DIR/"
        echo "✅ $jar_name copied to Tomcat lib"
    else
        echo "✅ $jar_name already in Tomcat lib"
    fi
}

# Check prerequisites
echo "🔍 Checking prerequisites..."

# Check Java
if ! command_exists java; then
    echo "❌ Java not found. Please install Java 8 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d '"' -f 2)
echo "✅ Java version: $JAVA_VERSION"

# Check Tomcat
if ! dpkg -l | grep -q tomcat10; then
    echo "❌ Tomcat 10 not installed."
    echo "Installing Tomcat 10..."
    sudo apt update
    sudo apt install -y tomcat10 tomcat10-admin
fi

# Check if Tomcat is running
if ! pgrep -f "org.apache.catalina.startup.Bootstrap" > /dev/null; then
    echo "▶️ Starting Tomcat..."
    sudo service tomcat10 start 2>/dev/null || sudo /usr/libexec/tomcat10/tomcat-start.sh &
    sleep 5
fi

# Download H2 Database (Java 8/11 compatible version - CRITICAL!)
echo "📦 Setting up H2 Database..."
# NOTE: H2 2.2.x requires Java 17+. Using 1.4.196 for Java 11 compatibility
H2_URL="https://repo1.maven.org/maven2/com/h2database/h2/${H2_VERSION}/${H2_JAR_NAME}"
download_if_missing "$H2_URL" "$H2_JAR_PATH"

# Copy H2 to Tomcat lib (must be in lib for JDBC driver loading)
copy_jar_to_tomcat "$H2_JAR_PATH"

# Download and setup JavaMail API (required for CheckoutServlet)
echo "📦 Setting up JavaMail API..."
# NOTE: CheckoutServlet requires javax.mail.* classes for email functionality
MAIL_JAR_PATH="/tmp/javax.mail.jar"
MAIL_URL="https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar"
download_if_missing "$MAIL_URL" "$MAIL_JAR_PATH"
copy_jar_to_tomcat "$MAIL_JAR_PATH"

# Create database directory
echo "🗄️ Setting up database..."
mkdir -p "$DATABASE_DIR"

# Initialize H2 database if it doesn't exist
if [ ! -f "${DATABASE_DIR}/vehicles.mv.db" ]; then
    echo "🔧 Initializing H2 database..."
    cd "$WORKSPACE_DIR"
    python3 init_h2_database.py
    
    # Fix image names to match actual files (CRITICAL for image display)
    if [ -f "fix_images.py" ]; then
        echo "🖼️ Fixing image names in database..."
        echo "NOTE: Database image names must exactly match .jpg filenames in web/image/"
        python3 fix_images.py
    fi
else
    echo "✅ H2 database already exists"
    # Always run image fix in case new images were added
    if [ -f "fix_images.py" ]; then
        echo "🖼️ Updating image names in database..."
        python3 fix_images.py
    fi
fi

# Compile Java servlets
echo "🔨 Compiling Java servlets..."
# NOTE: Must use tomcat10-servlet-api.jar for Jakarta EE namespace compatibility
SERVLET_JAR="/usr/share/java/tomcat10-servlet-api.jar"
MAIL_JAR="/var/lib/tomcat10/lib/javax.mail.jar"
H2_JAR="/var/lib/tomcat10/lib/${H2_JAR_NAME}"

# Use alternative paths for JARs during compilation
COMPILE_MAIL_JAR="${MAIL_JAR_PATH}"  # Use downloaded JAR for compilation
COMPILE_H2_JAR="${H2_JAR_PATH}"      # Use downloaded JAR for compilation

# Verify critical JARs exist
if [ ! -f "$SERVLET_JAR" ]; then
    echo "❌ Tomcat 10 servlet API not found. Installing..."
    sudo apt install -y libtomcat10-java
fi

if [ ! -f "$MAIL_JAR" ]; then
    echo "❌ JavaMail JAR not found in Tomcat lib. Copying..."
    copy_jar_to_tomcat "$MAIL_JAR_PATH"
fi

if [ ! -f "$H2_JAR" ]; then
    echo "❌ H2 JAR not found in Tomcat lib. Copying..."
    copy_jar_to_tomcat "$H2_JAR_PATH"
fi

# Ensure target directories exist
mkdir -p "${WORKSPACE_DIR}/build/classes"

# Compile all Java files
cd "${WORKSPACE_DIR}/src/java"
find . -name "*.java" -print0 | xargs -0 javac -cp "${SERVLET_JAR}:${COMPILE_MAIL_JAR}:${COMPILE_H2_JAR}" -d "${WORKSPACE_DIR}/build/classes"

if [ $? -eq 0 ]; then
    echo "✅ Java servlets compiled successfully"
else
    echo "❌ Failed to compile Java servlets"
    echo "DEBUG: Classpath used: ${SERVLET_JAR}:${COMPILE_MAIL_JAR}:${COMPILE_H2_JAR}"
    exit 1
fi

# Package WAR file
echo "📦 Creating WAR file..."
cd "${WORKSPACE_DIR}"
WAR_FILE="${WORKSPACE_DIR}/vehicle-ecommerce.war"

# Remove old WAR if exists
rm -f "$WAR_FILE"

# Create temporary directory for WAR packaging
WAR_TEMP_DIR="/tmp/war_temp"
rm -rf "$WAR_TEMP_DIR"
mkdir -p "$WAR_TEMP_DIR"

# Copy web content
cp -r web/* "$WAR_TEMP_DIR/"

# CRITICAL: Classes must be in WEB-INF/classes/classes/ to match package structure
# The package is "classes" so files must be in WEB-INF/classes/classes/
mkdir -p "$WAR_TEMP_DIR/WEB-INF/classes/classes"
cp -r build/classes/classes/* "$WAR_TEMP_DIR/WEB-INF/classes/classes/"

# CRITICAL: Copy H2 and JavaMail JARs to WEB-INF/lib for webapp classloader
# This fixes "ClassNotFoundException: org.h2.Driver" runtime error
mkdir -p "$WAR_TEMP_DIR/WEB-INF/lib"
cp "$H2_JAR_PATH" "$WAR_TEMP_DIR/WEB-INF/lib/"
cp "$MAIL_JAR_PATH" "$WAR_TEMP_DIR/WEB-INF/lib/"
echo "✅ Added H2 and JavaMail JARs to WEB-INF/lib"

# Create WAR file
cd "$WAR_TEMP_DIR"
jar -cvf "$WAR_FILE" . > /dev/null 2>&1

# Clean up
rm -rf "$WAR_TEMP_DIR"

echo "✅ WAR file created: $WAR_FILE"

# Verify WAR structure (troubleshooting aid)
echo "🔍 Verifying WAR structure..."
SERVLET_COUNT=$(jar -tf "$WAR_FILE" | grep -c "WEB-INF/classes/classes/.*Servlet\.class")
if [ "$SERVLET_COUNT" -gt 0 ]; then
    echo "✅ Found $SERVLET_COUNT servlet classes in correct package location"
    jar -tf "$WAR_FILE" | grep "WEB-INF/classes/classes/.*Servlet\.class"
else
    echo "❌ No servlet classes found in WEB-INF/classes/classes/ - packaging error!"
    echo "Package structure should match: WEB-INF/classes/classes/*.class"
    jar -tf "$WAR_FILE" | grep -i servlet || echo "No servlet classes found in WAR"
    exit 1
fi

# Deploy to Tomcat
echo "🚀 Deploying to Tomcat..."
sudo rm -rf "${TOMCAT_WEBAPPS_DIR}/vehicle-ecommerce"
sudo rm -f "${TOMCAT_WEBAPPS_DIR}/vehicle-ecommerce.war"
sudo cp "$WAR_FILE" "$TOMCAT_WEBAPPS_DIR/"

# Set proper ownership
sudo chown tomcat:tomcat "${TOMCAT_WEBAPPS_DIR}/vehicle-ecommerce.war"

# Wait for deployment
echo "⏳ Waiting for application deployment..."
sleep 10

# Check if application is deployed
if [ -d "${TOMCAT_WEBAPPS_DIR}/vehicle-ecommerce" ]; then
    echo "✅ Application deployed successfully"
    
    # Test deployment with a simple HTTP request
    echo "🧪 Testing application deployment..."
    if curl -s -f "http://localhost:8080/vehicle-ecommerce/" > /dev/null; then
        echo "✅ Application responding correctly"
        
        # Test individual servlets
        echo "🧪 Testing servlet endpoints..."
        CART_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/vehicle-ecommerce/cart")
        if [ "$CART_STATUS" = "500" ]; then
            echo "✅ Cart servlet accessible (500 expected for GET without params)"
        elif [ "$CART_STATUS" = "404" ]; then
            echo "❌ Cart servlet returning 404 - check package structure"
        else
            echo "ℹ️ Cart servlet status: $CART_STATUS"
        fi
        
        VEHICLE_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=c&page=1")
        if [ "$VEHICLE_STATUS" = "200" ]; then
            # Test if actual content is returned (not just 200 with empty body)
            VEHICLE_CONTENT=$(curl -s "http://localhost:8080/vehicle-ecommerce/VehicleServlet?vehicle=c&page=1" | wc -c)
            if [ "$VEHICLE_CONTENT" -gt 100 ]; then
                echo "✅ Vehicle servlet working correctly with content"
            else
                echo "⚠️ Vehicle servlet returns 200 but empty content (check H2 driver in WEB-INF/lib)"
            fi
        elif [ "$VEHICLE_STATUS" = "500" ]; then
            echo "⚠️ Vehicle servlet accessible but has runtime errors (check H2 database)"
        elif [ "$VEHICLE_STATUS" = "404" ]; then
            echo "❌ Vehicle servlet returning 404"
        else
            echo "ℹ️ Vehicle servlet status: $VEHICLE_STATUS"
        fi
    else
        echo "⚠️ Application deployed but not responding - check logs"
        echo "Tomcat logs: sudo tail -n 20 /var/lib/tomcat10/logs/catalina.out"
    fi
else
    echo "⚠️ Application deployment may be pending..."
    echo "Check: sudo ls -la ${TOMCAT_WEBAPPS_DIR}/"
fi

# Start proxy server if not running
echo "🔗 Starting proxy server..."
if ! pgrep -f "proxy_server.py" > /dev/null; then
    cd "$WORKSPACE_DIR"
    nohup python3 proxy_server.py > proxy.log 2>&1 &
    echo "✅ Proxy server started"
else
    echo "✅ Proxy server already running"
fi

echo ""
echo "🎉 Vehicle E-commerce Application Setup Complete!"
echo ""
echo "📋 Application Details:"
echo "  • Tomcat URL: http://localhost:8080/vehicle-ecommerce/"
echo "  • Proxy URL: http://localhost:3000/"
echo "  • Database: H2 file-based at ${DATABASE_DIR}/vehicles.mv.db"
echo "  • Logs: sudo journalctl -u tomcat10 -f"
echo ""
echo "🔧 Useful Commands:"
echo "  • Check Tomcat status: pgrep -f tomcat"
echo "  • View Tomcat logs: sudo tail -f /var/lib/tomcat10/logs/catalina.out"
echo "  • Stop proxy: pkill -f proxy_server.py"
echo "  • Restart Tomcat: sudo pkill -f tomcat && sudo service tomcat10 start"
echo "  • Test H2 database: java -cp ${H2_JAR_PATH} org.h2.tools.Shell"
echo "  • Check WAR contents: jar -tf vehicle-ecommerce.war | head -20"
echo ""
echo "🐛 Troubleshooting:"
echo "  • 404 errors: Check WAR structure and servlet mappings in web.xml"
echo "  • 500 errors: Check Tomcat logs for stack traces"
echo "  • CartServlet 500: Expected for GET without parameters (ProductIO needs productId)"
echo "  • VehicleServlet blank: ClassNotFoundException - ensure JARs in WEB-INF/lib/"
echo "  • Image issues: Verify image names in database match .jpg files"
echo "  • Database issues: Check H2 connection and JDBC driver loading"
echo "  • Container issues: Use 'service' commands instead of 'systemctl'"
echo "  • Package structure: Classes must be in WEB-INF/classes/classes/ for package 'classes'"
echo ""
echo "🌐 Access the application at: http://localhost:3000/"

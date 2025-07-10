# Vehicle E-commerce Website

A legacy Java servlet-based vehicle e-commerce website modernized to run in Codespace environments using H2 database and Jakarta EE.

## Original Project Description

A school senior project that requires the usage of java servlets and the understanding on how web.xml works in order to construct an eccomerce website solely for vehicles. In addition, it requires the usage of SQL Workbench to store all of our data and display them onto our website by using jdbc connection. By implementing the jdbc connection, displaying vehicles, prices, and images, users can see the numerous options of vehicles available for purchase. There is a shopping card in addition that allows users to update the quantity of vehicles that they want, and they are able to remove certain products if requested. Also, there is a checkout section in which it will send a confirmation email to the user's email about their order confirmation.

## Modernization Overview

This project has been successfully modernized to work in modern cloud environments with:
- **Database Migration:** MySQL → H2 file-based database for cloud compatibility
- **Namespace Update:** javax.servlet → jakarta.servlet for Tomcat 10
- **Cloud Deployment:** Codespace-ready with automated setup scripts
- **Error Handling:** Improved debugging and logging
- **Image Management:** Automated image synchronization

## Features

- **Vehicle listings** for cars, boats, and motorcycles
- **Search and filtering** by vehicle type
- **Sorting** by name and price  
- **Pagination** for large datasets
- **Shopping cart** functionality
- **Image gallery** with vehicle photos
- **H2 file-based database** for persistence
- **Email notifications** for order confirmations

## Live Demo

**Main Application URL (Codespace):**
```
https://[CODESPACE_NAME]-8080.app.github.dev/vehicle-ecommerce/
```

**Direct Servlet Examples:**
- Cars: `https://[CODESPACE_NAME]-8080.app.github.dev/vehicle-ecommerce/VehicleServlet?vehicle=c&page=1`
- Boats: `https://[CODESPACE_NAME]-8080.app.github.dev/vehicle-ecommerce/VehicleServlet?vehicle=b&page=1`
- Motorcycles: `https://[CODESPACE_NAME]-8080.app.github.dev/vehicle-ecommerce/VehicleServlet?vehicle=m&page=1`

## Architecture

- **Frontend:** JSP pages with HTML/CSS
- **Backend:** Jakarta EE Servlets (migrated from javax.servlet)
- **Database:** H2 file-based database (replaced MySQL)
- **Server:** Apache Tomcat 10
- **Build:** Manual compilation with Maven-style structure

## Project Structure

```
/workspaces/Vehicle-E-commerce-website/
├── src/java/classes/           # Java servlet source code
│   ├── VehicleServlet.java     # Main vehicle listing servlet
│   ├── CartServlet.java        # Shopping cart servlet
│   ├── CheckoutServlet.java    # Checkout processing
│   ├── ProductIO.java          # Product data access
│   └── *.java                  # Supporting classes
├── web/                        # Web application content
│   ├── WEB-INF/
│   │   ├── web.xml            # Servlet configuration
│   │   └── products.txt       # Product data file
│   ├── image/                 # Vehicle images (.jpg files)
│   ├── styles/                # CSS stylesheets
│   ├── index.jsp             # Main landing page
│   └── *.jsp                 # Other JSP pages
├── database/                  # H2 database files
│   ├── vehicles.mv.db        # Main database file
│   └── vehicles.trace.db     # Database trace logs
├── build/classes/classes/     # Compiled Java classes
├── vehicle-ecommerce.war     # Deployable WAR file
├── startup_tomcat.sh         # Automated setup script
├── init_h2_database.py       # Database initialization
├── fix_images.py             # Image name synchronization
└── proxy_server.py           # Optional proxy for port 3000
```

## Setup Instructions

### Prerequisites

1. **Java 11+** (OpenJDK recommended)
2. **Apache Tomcat 10** (Jakarta EE compatible)
3. **Python 3** (for database scripts)

### Required JARs

The following JAR files must be available in both Tomcat's lib directory and the WAR file's WEB-INF/lib:

1. **H2 Database Driver** (version 1.4.196 - Java 8/11 compatible)
   - Download: `https://repo1.maven.org/maven2/com/h2database/h2/1.4.196/h2-1.4.196.jar`
   - Location: `/var/lib/tomcat10/lib/h2-1.4.196.jar`

2. **JavaMail API** (for checkout email functionality)
   - Download: `https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar`
   - Location: `/var/lib/tomcat10/lib/javax.mail.jar`

### Quick Setup (Codespace)

1. **Clone the repository:**
   ```bash
   git clone [repository-url]
   cd Vehicle-E-commerce-website
   ```

2. **Run the automated setup script:**
   ```bash
   chmod +x startup_tomcat.sh
   ./startup_tomcat.sh
   ```

3. **Access the application:**
   - Replace `[CODESPACE_NAME]` with your actual codespace name
   - Visit: `https://[CODESPACE_NAME]-8080.app.github.dev/vehicle-ecommerce/`

### Manual Setup

#### 1. Install Dependencies

```bash
# Install Tomcat 10 and Java
sudo apt update
sudo apt install -y openjdk-11-jdk tomcat10 libtomcat10-java

# Download required JARs
wget -O /tmp/h2-1.4.196.jar "https://repo1.maven.org/maven2/com/h2database/h2/1.4.196/h2-1.4.196.jar"
wget -O /tmp/javax.mail.jar "https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar"

# Copy JARs to Tomcat lib (requires sudo)
sudo cp /tmp/h2-1.4.196.jar /var/lib/tomcat10/lib/
sudo cp /tmp/javax.mail.jar /var/lib/tomcat10/lib/
```

#### 2. Initialize Database

```bash
# Initialize H2 database with vehicle data
python3 init_h2_database.py

# Fix image names to match actual files
python3 fix_images.py
```

#### 3. Compile Servlets

```bash
# Create build directory
mkdir -p build/classes/classes

# Compile Java servlets
cd src/java
find . -name "*.java" -print0 | xargs -0 javac \
  -cp "/usr/share/java/tomcat10-servlet-api.jar:/tmp/javax.mail.jar:/tmp/h2-1.4.196.jar" \
  -d "../../build/classes"
```

#### 4. Package WAR File

```bash
# Create WAR structure
mkdir -p /tmp/war_temp
cp -r web/* /tmp/war_temp/

# Copy compiled classes (note the double 'classes' directory)
mkdir -p /tmp/war_temp/WEB-INF/classes/classes
cp -r build/classes/classes/* /tmp/war_temp/WEB-INF/classes/classes/

# Copy JARs to WAR
mkdir -p /tmp/war_temp/WEB-INF/lib
cp /tmp/h2-1.4.196.jar /tmp/war_temp/WEB-INF/lib/
cp /tmp/javax.mail.jar /tmp/war_temp/WEB-INF/lib/

# Create WAR file
cd /tmp/war_temp
jar -cvf ../../workspaces/Vehicle-E-commerce-website/vehicle-ecommerce.war .
```

#### 5. Deploy to Tomcat

**Option A: System Tomcat (requires sudo)**
```bash
sudo cp vehicle-ecommerce.war /var/lib/tomcat10/webapps/
sudo systemctl restart tomcat10
```

**Option B: User Tomcat (recommended for Codespace)**
```bash
# Create local Tomcat environment
export CATALINA_HOME=/usr/share/tomcat10
export CATALINA_BASE=/tmp/tomcat
mkdir -p /tmp/tomcat/{logs,temp,work,webapps,conf}

# Create minimal configuration
cat > /tmp/tomcat/conf/server.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<Server port="8005" shutdown="SHUTDOWN">
  <Service name="Catalina">
    <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
    <Engine name="Catalina" defaultHost="localhost">
      <Host name="localhost" appBase="webapps" unpackWARs="true" autoDeploy="true" />
    </Engine>
  </Service>
</Server>
EOF

cat > /tmp/tomcat/conf/web.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
</web-app>
EOF

# Deploy WAR and start Tomcat
cp vehicle-ecommerce.war /tmp/tomcat/webapps/
/usr/share/tomcat10/bin/startup.sh
```

## Database Schema

The H2 database contains a `vehicle` table with the following structure:

```sql
CREATE TABLE vehicle (
    idvehicle INT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10,2),
    type VARCHAR(50),  -- 'car', 'boat', or 'motorcycle'
    image VARCHAR(255) -- matches .jpg filename in web/image/
);
```

## Troubleshooting

### Common Issues

1. **ClassNotFoundException: org.h2.Driver**
   - Ensure H2 JAR is in both `/var/lib/tomcat10/lib/` AND `WEB-INF/lib/` of the WAR

2. **SQL Syntax Errors**
   - H2 uses different LIMIT syntax: `LIMIT count OFFSET offset` (not MySQL's `LIMIT offset,count`)

3. **Images Not Displaying**
   - Run `python3 fix_images.py` to sync database image names with actual files
   - Check that image files exist in `web/image/` directory

4. **Empty Servlet Response**
   - Check Tomcat logs: `/tmp/tomcat/logs/catalina.out`
   - Verify servlet classes are in `WEB-INF/classes/classes/` (note double 'classes')

5. **Permission Denied Errors**
   - Use the user Tomcat setup instead of system Tomcat
   - Ensure all files have proper permissions

### Log Locations

- **Tomcat Logs:** `/tmp/tomcat/logs/catalina.out`
- **Application Logs:** Check servlet System.out.println statements in catalina.out
- **Database Logs:** `database/vehicles.trace.db`

## Key Changes from Original

1. **Namespace Migration:** `javax.servlet.*` → `jakarta.servlet.*`
2. **Database Migration:** MySQL → H2 file-based database
3. **SQL Syntax Updates:** MySQL → H2 compatible queries
4. **Dependency Management:** Added H2 and JavaMail JARs to deployment
5. **Error Handling:** Added proper exception logging
6. **Image Synchronization:** Automated image name matching

## Development Notes

- **Package Structure:** All classes are in the `classes` package, requiring `WEB-INF/classes/classes/` structure
- **H2 Database:** Uses AUTO_SERVER mode for concurrent access
- **Servlet Context:** Parameter `k=10` controls pagination size
- **Static Files:** Images and CSS served directly by Tomcat
- **Session Management:** Uses HTTP sessions for cart functionality

## Contributing

1. Make changes to source files in `src/java/classes/`
2. Recompile with the compilation command above
3. Redeploy the WAR file
4. Test all servlet endpoints
5. Update this README if setup changes

## License

This is a educational project for learning Java servlets and web development.

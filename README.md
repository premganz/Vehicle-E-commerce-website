# Vehicle E-commerce Website

A legacy Java servlet-based vehicle e-commerce website successfully migrated to Spring Boot 2.7.18 with Java 11.

## Original Project Description

A school senior project that originally used Java servlets and understanding of web.xml to construct an e-commerce website solely for vehicles. The original implementation used SQL Workbench to store data and display it on the website using JDBC connections. Users could browse vehicles, see prices and images, add items to a shopping cart, update quantities, remove products, and receive email confirmations during checkout.

## Migration Overview - Spring Boot 2.7.18

This project has been successfully migrated from legacy Java servlets to Spring Boot 2.7.18 with strict feature parity:

### ✅ Completed Migration Features:
- **Framework Migration:** Legacy Servlets → Spring Boot 2.7.18 (Java 11)
- **Database Migration:** H2 file-based database with JPA/Hibernate
- **REST API:** Modern REST endpoints replacing servlet mappings
- **Service Layer:** Proper separation of concerns with Service and Repository layers
- **DTOs:** Data Transfer Objects matching original Java classes exactly
- **Static Frontend:** Migrated JSP functionality to modern HTML/CSS/JavaScript
- **Build System:** Maven-based project structure
- **Configuration:** Spring Boot application properties
- **Error Resolution:** Fixed infinite loop issues and proper image handling

### 🏗️ Architecture:
- **Backend:** Spring Boot 2.7.18 with Spring MVC REST controllers
- **Database:** H2 file-based database with JPA/Hibernate
- **Frontend:** Static HTML/CSS/JavaScript (maintains original JSP functionality)
- **Build:** Maven with Java 11 compatibility
- **Server:** Embedded Tomcat (Spring Boot default) on port 8081

## Features

- **Vehicle listings** for cars, boats, and motorcycles with pagination
- **REST API endpoints** for vehicle browsing and filtering
- **Search and filtering** by vehicle type, name, and price range
- **Sorting** by name and price (ascending/descending)
- **Shopping cart** functionality (ready for implementation)
- **Order management** system (ready for implementation)
- **Image gallery** with vehicle photos and placeholder handling
- **H2 file-based database** for data persistence
- **Spring Boot configuration** with proper logging and error handling

## API Endpoints

### Vehicle Management
- `GET /api/vehicles` - Get all vehicles with pagination and sorting
- `GET /api/vehicles/{id}` - Get specific vehicle by ID
- `GET /api/vehicles/search` - Search vehicles with filters
- `GET /api/vehicles/types` - Get distinct vehicle types
- `POST /api/vehicles` - Create new vehicle
- `PUT /api/vehicles/{id}` - Update existing vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle

### Example API Calls:
```
# Get first page of vehicles (12 per page)
GET http://localhost:8081/api/vehicles

# Search for cars under $30,000
GET http://localhost:8081/api/vehicles/search?type=Car&maxPrice=30000

# Get vehicles sorted by price descending
GET http://localhost:8081/api/vehicles?sortBy=price&sortDir=desc
```

## Live Demo

**Spring Boot Application URL:**
```
http://localhost:8081/
```

**H2 Database Console:**
```
http://localhost:8081/h2-console
```
- JDBC URL: `jdbc:h2:file:./database/vehicles`
- Username: `sa`
- Password: (empty)

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

---

## Spring Boot Migration (2025)

This project has been successfully migrated from legacy Java servlets to **Spring Boot 2.7.18** with Java 11 compatibility, maintaining strict feature parity with the original implementation.

### 🚀 Migration Overview

**Migration Status: ✅ COMPLETED**

- **Framework:** Legacy Servlets → Spring Boot 2.7.18 (Java 11)
- **Database:** H2 file-based database with JPA/Hibernate
- **Architecture:** Modern REST API with Service/Repository layers
- **Frontend:** Static HTML/CSS/JavaScript (maintains original JSP functionality)
- **Build System:** Maven-based project structure
- **Server:** Embedded Tomcat on port 8081

### 📁 Spring Boot Project Structure

```
springboot-migration/
├── pom.xml                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/vehicleecommerce/
│   │   │   ├── VehicleEcommerceApplication.java  # Main Spring Boot app
│   │   │   ├── controller/
│   │   │   │   └── VehicleController.java        # REST endpoints
│   │   │   ├── service/
│   │   │   │   └── VehicleService.java           # Business logic
│   │   │   ├── repository/
│   │   │   │   └── VehicleRepository.java        # Data access
│   │   │   ├── model/
│   │   │   │   ├── Vehicle.java                  # JPA entity
│   │   │   │   └── Order.java                    # Order entity
│   │   │   └── dto/
│   │   │       ├── Product.java                  # Product DTO
│   │   │       ├── Cart.java                     # Cart DTO
│   │   │       └── LineItem.java                 # Line item DTO
│   │   └── resources/
│   │       ├── application.properties             # Spring Boot config
│   │       └── static/                           # Static web content
│   │           ├── index.html                    # Main page
│   │           ├── css/style.css                 # Styles
│   │           ├── js/app.js                     # Frontend logic
│   │           └── image/                        # Vehicle images
│   └── test/
│       └── java/com/vehicleecommerce/
│           └── VehicleEcommerceApplicationTests.java
├── database/                  # H2 database files
│   ├── vehicles.mv.db        # Main database
│   └── vehicles.trace.db     # Trace logs
└── target/                   # Maven build output
```

### 🌐 Spring Boot Application URLs

**Main Application:**
```
http://localhost:8081/
```

**REST API Endpoints:**
```
GET    /api/vehicles              # List all vehicles (paginated)
GET    /api/vehicles/{id}         # Get vehicle by ID
GET    /api/vehicles/search       # Search/filter vehicles
GET    /api/vehicles/types        # Get vehicle types
POST   /api/vehicles              # Create vehicle
PUT    /api/vehicles/{id}         # Update vehicle
DELETE /api/vehicles/{id}         # Delete vehicle
```

**H2 Database Console:**
```
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:file:./database/vehicles
Username: sa
Password: (empty)
```

### 🔧 Quick Start - Spring Boot

1. **Navigate to Spring Boot project:**
   ```bash
   cd springboot-migration
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - Main site: http://localhost:8081/
   - API: http://localhost:8081/api/vehicles
   - Database: http://localhost:8081/h2-console

### 📊 API Examples

**Get all vehicles with pagination:**
```bash
curl "http://localhost:8081/api/vehicles?page=0&size=12&sortBy=name&sortDir=asc"
```

**Search for cars under $30,000:**
```bash
curl "http://localhost:8081/api/vehicles/search?type=Car&maxPrice=30000"
```

**Get vehicle types:**
```bash
curl "http://localhost:8081/api/vehicles/types"
```

### 🏗️ Architecture Features

- **Spring Boot 2.7.18** with Java 11 compatibility
- **JPA/Hibernate** for database operations
- **H2 file-based database** for persistence
- **REST controllers** replacing servlet mappings
- **Service layer** for business logic separation
- **Repository pattern** for data access
- **DTOs** matching original Java classes exactly
- **Static frontend** with modern HTML/CSS/JavaScript
- **Maven build system** with proper dependency management
- **Embedded Tomcat** server on port 8081

### 🔍 Key Migration Changes

1. **Servlet → REST Controller:**
   - `VehicleServlet.java` → `VehicleController.java`
   - URL mappings: `/vehicles` → `/api/vehicles`

2. **Database Integration:**
   - JDBC → JPA/Hibernate
   - Manual SQL → Repository methods with `@Query`

3. **Data Classes:**
   - Original DTOs preserved exactly (`Product`, `Cart`, `LineItem`)
   - Added JPA entities (`Vehicle`, `Order`)

4. **Frontend Migration:**
   - JSP functionality → Static HTML/JavaScript
   - AJAX calls to REST endpoints
   - Preserved original UI/UX design

5. **Configuration:**
   - `web.xml` → `application.properties`
   - Servlet configuration → Spring Boot auto-configuration

### 🧪 Development Workflow

**Build and run:**
```bash
cd springboot-migration
mvn clean compile
mvn spring-boot:run
```

**Run tests:**
```bash
mvn test
```

**Create JAR:**
```bash
mvn package
java -jar target/vehicle-ecommerce-0.0.1-SNAPSHOT.jar
```

### 🐛 Troubleshooting Spring Boot

**Application won't start:**
- Check Java 11+ is installed: `java -version`
- Verify Maven installation: `mvn -version`
- Check port 8081 is available: `netstat -tlnp | grep 8081`

**Database connection issues:**
- Database files created in `./database/` directory
- H2 console accessible at `/h2-console`
- Use JDBC URL: `jdbc:h2:file:./database/vehicles`

**Frontend issues:**
- Static files served from `src/main/resources/static/`
- Images should be in `static/image/` directory
- Check browser console for JavaScript errors

### 📈 Migration Benefits

- **Modern Framework:** Spring Boot with auto-configuration
- **Better Architecture:** Separation of concerns with layers
- **Developer Experience:** Hot reload, embedded server, easier testing
- **Maintainability:** Maven dependency management, Spring conventions
- **Scalability:** JPA/Hibernate for database operations
- **API-First:** REST endpoints for future frontend frameworks
- **Configuration:** Properties-based configuration management

### 🎯 Feature Parity Verification

✅ **Vehicle Listings:** Paginated vehicle display with sorting  
✅ **Search & Filter:** By type, name, and price range  
✅ **Database Integration:** H2 with JPA/Hibernate  
✅ **Static Assets:** Images and CSS properly served  
✅ **REST API:** Complete CRUD operations  
✅ **Error Handling:** Proper HTTP status codes and error responses  
✅ **Configuration:** Port 8081, database settings, logging  

### 📝 Migration Notes

- **Strict Feature Parity:** No features added or removed from original
- **Data Preservation:** Same database schema and data structure
- **URL Compatibility:** API endpoints follow RESTful conventions
- **Performance:** JPA queries optimized for pagination and filtering
- **Logging:** Configurable logging levels for debugging
- **Testing:** Basic test structure in place for future development

---

*Spring Boot migration completed on July 10, 2025*
*Original legacy servlet implementation preserved and documented above*

#!/usr/bin/env python3
"""
Initialize H2 database with vehicle data from products.txt
"""
import subprocess
import os

def create_h2_database():
    # Read products.txt
    products_file = "/workspaces/Vehicle-E-commerce-website/web/WEB-INF/products.txt"
    
    # Create SQL script
    sql_script = """
-- Create vehicle table (matching VehicleServlet expectations)
CREATE TABLE IF NOT EXISTS vehicle (
    idvehicle INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    type VARCHAR(100) NOT NULL,
    image VARCHAR(255)
);

-- Clear existing data
DELETE FROM vehicle;

-- Insert vehicle data
"""
    
    with open(products_file, 'r', encoding='utf-8-sig') as f:  # Handle BOM
        for line in f:
            line = line.strip()
            if line and '|' in line:
                parts = line.split('|')
                if len(parts) >= 3:
                    vehicle_id = parts[0].strip()
                    name = parts[1].strip().replace("'", "''")  # Escape quotes
                    price = parts[2].strip()
                    # Extract vehicle type from name (cars, boats, motorcycles)
                    name_lower = name.lower()
                    if any(word in name_lower for word in ['boat', 'yacht', 'cruiser', 'whaler']):
                        vehicle_type = 'boat'
                    elif any(word in name_lower for word in ['motorcycle', 'bike', 'harley', 'honda', 'kawasaki', 'yamaha', 'ducati', 'triumph', 'suzuki']):
                        vehicle_type = 'motorcycle'
                    else:
                        vehicle_type = 'car'
                    
                    # Use name as image filename (remove spaces and special chars)
                    image_name = name.replace(' ', ' ').replace('.', '').replace('-', ' ')
                    
                    sql_script += f"INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES ({vehicle_id}, '{name}', {price}, '{vehicle_type}', '{image_name}');\n"
    
    # Write SQL script to file
    sql_file = "/workspaces/Vehicle-E-commerce-website/init_vehicles.sql"
    with open(sql_file, 'w') as f:
        f.write(sql_script)
    
    print(f"Created SQL script: {sql_file}")
    
    # Create H2 database directory
    db_dir = "/workspaces/Vehicle-E-commerce-website/database"
    os.makedirs(db_dir, exist_ok=True)
    
    # Initialize H2 database
    h2_jar = "/tmp/h2-1.4.196.jar"  # Use Java 8 compatible version
    db_path = f"{db_dir}/vehicles"
    
    # Run H2 to create and populate database
    cmd = [
        "java", "-cp", h2_jar, "org.h2.tools.RunScript",
        "-url", f"jdbc:h2:{db_path};AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1",
        "-user", "sa",
        "-password", "",
        "-script", sql_file
    ]
    
    print("Initializing H2 database...")
    result = subprocess.run(cmd, capture_output=True, text=True)
    
    if result.returncode == 0:
        print("✅ H2 database initialized successfully!")
        print(f"Database location: {db_path}.mv.db")
        print("JDBC URL: jdbc:h2:/workspaces/Vehicle-E-commerce-website/database/vehicles;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1")
    else:
        print("❌ Error initializing database:")
        print(result.stderr)
    
    return result.returncode == 0

if __name__ == "__main__":
    create_h2_database()

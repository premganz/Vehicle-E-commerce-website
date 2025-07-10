#!/usr/bin/env python3
"""
Fix image names in H2 database to match actual image files
"""
import subprocess
import os

def fix_image_names():
    # Mapping of database image names to actual file names
    image_mapping = {
        'Aprilla Tuono V4 1100 Factory ABS': 'Aprilla Tuono V4 1100 Factory ABS',
        'Bavaria Cruiser 46': 'Bavaria Cruiser 46boat',
        'Harley Davidson Street Bob 114': 'Harley-Davidson Street Bob 114',
        'Honda CR V': 'Honda CR-V',
        'Kawasaki Versys X 300': 'Kawasaki Versys-X 300',
        'MV Agusta Brutale': 'MV Agusta Brutale 800',
        'Moto Guzzi MXG 21 Flying Fortress': 'Moto Guzzi MXG-21 Flying Fortress',
        'Suzuki GSX R1000R': 'Suzuki GSX-R1000R',
        'Triumph Street Cut': 'Triump Street Cut',
        'Yamaha FZ 10': 'Yamaha FZ-10'
    }
    
    # Create SQL update statements
    sql_updates = []
    for db_name, file_name in image_mapping.items():
        sql_updates.append(f"UPDATE vehicle SET image = '{file_name}' WHERE image = '{db_name}';")
    
    # Add missing images that should be in database (use MERGE for H2)
    missing_vehicles = [
        "MERGE INTO vehicle (idvehicle, name, price, type, image) VALUES (100, 'Cabin Cruisers', 25000.00, 'boat', 'Cabin Cruisers');",
        "MERGE INTO vehicle (idvehicle, name, price, type, image) VALUES (101, 'Sailboats', 15000.00, 'boat', 'Sailboats');",
        "MERGE INTO vehicle (idvehicle, name, price, type, image) VALUES (102, 'Yachts', 100000.00, 'boat', 'Yachts');"
    ]
    
    # Create SQL script
    sql_script = "-- Fix image names to match actual files\n"
    sql_script += "\n".join(sql_updates)
    sql_script += "\n\n-- Add missing vehicles\n"
    sql_script += "\n".join(missing_vehicles)
    
    # Write SQL script to file
    sql_file = "/workspaces/Vehicle-E-commerce-website/fix_images.sql"
    with open(sql_file, 'w') as f:
        f.write(sql_script)
    
    print(f"Created SQL script: {sql_file}")
    
    # Run H2 to update database
    h2_jar = "/tmp/h2-1.4.196.jar"
    db_path = "/workspaces/Vehicle-E-commerce-website/database/vehicles"
    
    cmd = [
        "java", "-cp", h2_jar, "org.h2.tools.RunScript",
        "-url", f"jdbc:h2:{db_path};AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1",
        "-user", "sa",
        "-password", "",
        "-script", sql_file
    ]
    
    print("Updating database...")
    result = subprocess.run(cmd, capture_output=True, text=True)
    
    if result.returncode == 0:
        print("✅ Database updated successfully!")
    else:
        print("❌ Error updating database:")
        print(result.stderr)
    
    return result.returncode == 0

if __name__ == "__main__":
    fix_image_names()

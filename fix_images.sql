-- Fix image names to match actual files
UPDATE vehicle SET image = 'Aprilla Tuono V4 1100 Factory ABS' WHERE image = 'Aprilla Tuono V4 1100 Factory ABS';
UPDATE vehicle SET image = 'Bavaria Cruiser 46boat' WHERE image = 'Bavaria Cruiser 46';
UPDATE vehicle SET image = 'Harley-Davidson Street Bob 114' WHERE image = 'Harley Davidson Street Bob 114';
UPDATE vehicle SET image = 'Honda CR-V' WHERE image = 'Honda CR V';
UPDATE vehicle SET image = 'Kawasaki Versys-X 300' WHERE image = 'Kawasaki Versys X 300';
UPDATE vehicle SET image = 'MV Agusta Brutale 800' WHERE image = 'MV Agusta Brutale';
UPDATE vehicle SET image = 'Moto Guzzi MXG-21 Flying Fortress' WHERE image = 'Moto Guzzi MXG 21 Flying Fortress';
UPDATE vehicle SET image = 'Suzuki GSX-R1000R' WHERE image = 'Suzuki GSX R1000R';
UPDATE vehicle SET image = 'Triump Street Cut' WHERE image = 'Triumph Street Cut';
UPDATE vehicle SET image = 'Yamaha FZ-10' WHERE image = 'Yamaha FZ 10';

-- Add missing vehicles
MERGE INTO vehicle (idvehicle, name, price, type, image) VALUES (100, 'Cabin Cruisers', 25000.00, 'boat', 'Cabin Cruisers');
MERGE INTO vehicle (idvehicle, name, price, type, image) VALUES (101, 'Sailboats', 15000.00, 'boat', 'Sailboats');
MERGE INTO vehicle (idvehicle, name, price, type, image) VALUES (102, 'Yachts', 100000.00, 'boat', 'Yachts');
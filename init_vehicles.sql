
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
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (1, 'Wakeboard Boats', 50000, 'boat', 'Wakeboard Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (2, 'Pontoon Boats', 7000, 'boat', 'Pontoon Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (3, 'Fishing Boats', 5000, 'boat', 'Fishing Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (4, 'Bowriders', 15000, 'car', 'Bowriders');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (5, 'High Performance Boats', 50000, 'boat', 'High Performance Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (6, 'Pontoon Boats', 7000, 'boat', 'Pontoon Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (7, 'Fishing Boats', 5000, 'boat', 'Fishing Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (8, 'Bowriders', 15000, 'car', 'Bowriders');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (9, 'Personal Watercraft', 5000, 'car', 'Personal Watercraft');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (10, 'Bass Boats', 10000, 'boat', 'Bass Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (11, 'Inflatable Boats', 10000, 'boat', 'Inflatable Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (12, 'Deck boats', 100000, 'boat', 'Deck boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (13, 'Recreational Boats', 50000, 'boat', 'Recreational Boats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (14, 'Towboats', 10000, 'boat', 'Towboats');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (15, 'Boston Whaler 270 Dauntless boat', 40000, 'boat', 'Boston Whaler 270 Dauntless boat');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (16, 'Bavaria Cruiser 46', 50000, 'boat', 'Bavaria Cruiser 46');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (17, 'Lagoon 380 boat', 30000, 'boat', 'Lagoon 380 boat');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (18, 'Honda Civic', 20000, 'motorcycle', 'Honda Civic');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (19, 'Tesla X', 40000, 'car', 'Tesla X');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (20, 'Tesla Y', 50000, 'car', 'Tesla Y');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (21, 'Tesla Roadster', 200000, 'car', 'Tesla Roadster');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (22, 'Toyota Camry', 30000, 'car', 'Toyota Camry');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (23, 'Subaru Impreza', 30000, 'car', 'Subaru Impreza');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (24, 'Ford Explorer', 35000, 'car', 'Ford Explorer');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (25, 'Ford Focus', 30000, 'car', 'Ford Focus');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (26, 'Chevrolet Impala', 25000, 'car', 'Chevrolet Impala');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (27, 'Toyota RAV4', 40000, 'car', 'Toyota RAV4');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (28, 'Chevrolet Malibu', 35000, 'car', 'Chevrolet Malibu');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (29, 'Acura TL', 36000, 'car', 'Acura TL');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (30, 'Dodge Charger', 27000, 'car', 'Dodge Charger');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (31, 'BMW 3', 30000, 'car', 'BMW 3');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (32, 'Toyota Highlander', 43000, 'car', 'Toyota Highlander');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (33, 'Nissan Maxima', 32000, 'car', 'Nissan Maxima');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (34, 'Honda CR-V', 23000, 'motorcycle', 'Honda CR V');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (35, 'Suzuki GSX-R1000R', 14000, 'motorcycle', 'Suzuki GSX R1000R');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (36, 'Triumph Bonneville Bobber', 10000, 'motorcycle', 'Triumph Bonneville Bobber');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (37, 'Honda CBR1000RR SP', 12000, 'motorcycle', 'Honda CBR1000RR SP');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (38, 'Yamaha FZ-10', 9000, 'motorcycle', 'Yamaha FZ 10');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (39, 'Honda Rebel 500', 9000, 'motorcycle', 'Honda Rebel 500');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (40, 'Harley-Davidson Street Bob 114', 15000, 'motorcycle', 'Harley Davidson Street Bob 114');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (41, 'Ducati Supersport S', 16000, 'motorcycle', 'Ducati Supersport S');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (42, 'KTM 1090 Adventure R', 16000, 'car', 'KTM 1090 Adventure R');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (43, 'Aprilla Tuono V4 1100 Factory ABS', 13000, 'car', 'Aprilla Tuono V4 1100 Factory ABS');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (44, 'Indian Chieftain Elite', 9000, 'car', 'Indian Chieftain Elite');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (45, 'Ducati Scrambler Desert Sled', 14000, 'motorcycle', 'Ducati Scrambler Desert Sled');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (46, 'Kawasaki Versys-X 300', 12000, 'motorcycle', 'Kawasaki Versys X 300');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (47, 'Triumph Street Triple RS', 17000, 'motorcycle', 'Triumph Street Triple RS');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (48, 'Moto Guzzi MXG-21 Flying Fortress', 15000, 'car', 'Moto Guzzi MXG 21 Flying Fortress');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (49, 'Kawasaki Z900', 14000, 'motorcycle', 'Kawasaki Z900');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (50, 'Triumph Street Cut', 15000, 'motorcycle', 'Triumph Street Cut');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (51, 'MV Agusta Brutale', 800, 'car', 'MV Agusta Brutale');
INSERT INTO vehicle (idvehicle, name, price, type, image) VALUES (﻿1, 'Wakeboard Boats', 50000, 'boat', 'Wakeboard Boats');

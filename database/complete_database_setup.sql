-- Complete Trip Planner Database Setup
-- Copy and paste this entire script into phpMyAdmin SQL tab

-- Create database
CREATE DATABASE IF NOT EXISTS trip_planner;
USE trip_planner;

-- Create hotels table
CREATE TABLE IF NOT EXISTS hotels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL
);

-- Create tourist_spots table
CREATE TABLE IF NOT EXISTS tourist_spots (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL
);

-- Create food_spots table
CREATE TABLE IF NOT EXISTS food_spots (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL
);

-- Insert sample hotels data (Reduced prices for affordable 3-day stays)
INSERT INTO hotels (name, destination, price_per_night) VALUES
('Hotel Paradise', 'Munnar', 800.00),
('Green Valley Resort', 'Munnar', 1000.00),
('Tea Garden Lodge', 'Munnar', 900.00),
('Hill View Hotel', 'Munnar', 700.00),
('Spice Mountain Resort', 'Munnar', 1200.00),

('City Center Hotel', 'Bangalore', 900.00),
('Tech Hub Inn', 'Bangalore', 850.00),
('Garden City Resort', 'Bangalore', 1100.00),
('Business Hotel', 'Bangalore', 750.00),
('Silicon Valley Lodge', 'Bangalore', 1000.00),
('Royal Orchid', 'Bangalore', 1300.00),

('Beach View Resort', 'Pondicherry', 1000.00),
('French Quarter Hotel', 'Pondicherry', 1200.00),
('Seaside Villa', 'Pondicherry', 1100.00),
('Colonial Inn', 'Pondicherry', 950.00),
('Ocean Breeze Resort', 'Pondicherry', 1300.00);

-- Insert tourist spots data
INSERT INTO tourist_spots (name, destination) VALUES
('Tea Gardens', 'Munnar'),
('Eravikulam National Park', 'Munnar'),
('Mattupetty Dam', 'Munnar'),
('Top Station', 'Munnar'),
('Anamudi Peak', 'Munnar'),
('Kundala Lake', 'Munnar'),
('Rose Garden', 'Munnar'),

('Lalbagh Botanical Garden', 'Bangalore'),
('Bangalore Palace', 'Bangalore'),
('Cubbon Park', 'Bangalore'),
('Vidhana Soudha', 'Bangalore'),
('ISKCON Temple', 'Bangalore'),
('UB City Mall', 'Bangalore'),
('Nandi Hills', 'Bangalore'),
('Tipu Sultan Palace', 'Bangalore'),

('Promenade Beach', 'Pondicherry'),
('Auroville', 'Pondicherry'),
('French Quarter', 'Pondicherry'),
('Paradise Beach', 'Pondicherry'),
('Sri Aurobindo Ashram', 'Pondicherry'),
('Pondicherry Museum', 'Pondicherry'),
('Rock Beach', 'Pondicherry'),
('Botanical Garden', 'Pondicherry');

-- Insert food spots data
INSERT INTO food_spots (name, destination) VALUES
('Spice Garden Restaurant', 'Munnar'),
('Hill View Cafe', 'Munnar'),
('Tea House Restaurant', 'Munnar'),
('Mountain Spice', 'Munnar'),
('Cardamom County', 'Munnar'),
('Copper Castle', 'Munnar'),

('MTR Restaurant', 'Bangalore'),
('Koshy\'s Restaurant', 'Bangalore'),
('Vidyarthi Bhavan', 'Bangalore'),
('Cafe Coffee Day', 'Bangalore'),
('Toit Brewpub', 'Bangalore'),
('The Only Place', 'Bangalore'),
('Mavalli Tiffin Room', 'Bangalore'),
('Corner House Ice Cream', 'Bangalore'),

('Cafe des Arts', 'Pondicherry'),
('Villa Shanti', 'Pondicherry'),
('Le Dupleix', 'Pondicherry'),
('Surguru Restaurant', 'Pondicherry'),
('Baker Street Bistro', 'Pondicherry'),
('Carte Blanche', 'Pondicherry'),
('Tanto Pizzeria', 'Pondicherry');

-- Display success message and data counts
SELECT 'Database Setup Complete!' as Status;
SELECT 'Hotels' as Table_Name, COUNT(*) as Record_Count FROM hotels
UNION ALL
SELECT 'Tourist Spots', COUNT(*) FROM tourist_spots  
UNION ALL
SELECT 'Food Spots', COUNT(*) FROM food_spots;










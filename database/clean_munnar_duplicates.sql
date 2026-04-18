-- Clean Duplicate Hotels from Munnar
-- Run this script in phpMyAdmin SQL tab

USE trip_planner;

-- Step 1: View current duplicates in Munnar
SELECT name, destination, price_per_night, COUNT(*) as duplicate_count 
FROM hotels 
WHERE destination = 'Munnar'
GROUP BY name, destination, price_per_night
HAVING COUNT(*) > 1;

-- Step 2: Delete duplicate hotels, keeping only the first occurrence (lowest ID)
DELETE h1 FROM hotels h1
INNER JOIN hotels h2 
WHERE h1.id > h2.id 
AND h1.name = h2.name 
AND h1.destination = h2.destination
AND h1.destination = 'Munnar';

-- Step 3: Verify the cleanup - show remaining Munnar hotels
SELECT id, name, destination, price_per_night 
FROM hotels 
WHERE destination = 'Munnar' 
ORDER BY name;

-- Step 4: Count total hotels in Munnar after cleanup
SELECT COUNT(*) as total_munnar_hotels 
FROM hotels 
WHERE destination = 'Munnar';

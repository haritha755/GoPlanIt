# MySQL Database Setup for Trip Planner

## Method 1: XAMPP + phpMyAdmin (EASIEST)

### Step 1: Install XAMPP
1. Download from: https://www.apachefriends.org/download.html
2. Run installer as Administrator
3. Install to default location (C:\xampp)
4. Start XAMPP Control Panel

### Step 2: Start MySQL
1. In XAMPP Control Panel, click "Start" next to MySQL
2. Wait for it to show "Running" in green
3. Click "Admin" next to MySQL (opens phpMyAdmin)

### Step 3: Create Database
1. In phpMyAdmin, click "SQL" tab
2. Copy and paste this SQL code:

```sql
CREATE DATABASE IF NOT EXISTS trip_planner;
USE trip_planner;

CREATE TABLE IF NOT EXISTS hotels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS tourist_spots (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS food_spots (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL
);

INSERT INTO hotels (name, destination, price_per_night) VALUES
('Hotel Paradise', 'Munnar', 2500.00),
('Green Valley Resort', 'Munnar', 3200.00),
('Tea Garden Lodge', 'Munnar', 2800.00),
('City Center Hotel', 'Bangalore', 3000.00),
('Tech Hub Inn', 'Bangalore', 2800.00),
('Garden City Resort', 'Bangalore', 3500.00),
('Beach View Resort', 'Pondicherry', 3500.00),
('French Quarter Hotel', 'Pondicherry', 4000.00),
('Seaside Villa', 'Pondicherry', 3800.00);

INSERT INTO tourist_spots (name, destination) VALUES
('Tea Gardens', 'Munnar'),
('Eravikulam National Park', 'Munnar'),
('Mattupetty Dam', 'Munnar'),
('Lalbagh Botanical Garden', 'Bangalore'),
('Bangalore Palace', 'Bangalore'),
('Cubbon Park', 'Bangalore'),
('Promenade Beach', 'Pondicherry'),
('Auroville', 'Pondicherry'),
('French Quarter', 'Pondicherry');

INSERT INTO food_spots (name, destination) VALUES
('Spice Garden Restaurant', 'Munnar'),
('Hill View Cafe', 'Munnar'),
('Tea House Restaurant', 'Munnar'),
('MTR Restaurant', 'Bangalore'),
('Koshy\'s Restaurant', 'Bangalore'),
('Vidyarthi Bhavan', 'Bangalore'),
('Cafe des Arts', 'Pondicherry'),
('Villa Shanti', 'Pondicherry'),
('Le Dupleix', 'Pondicherry');
```

3. Click "Go" to execute

### Step 4: Test Database
Click "Browse" next to each table to verify data was inserted.

## Method 2: MySQL Workbench (Alternative)

1. Download MySQL Installer from: https://dev.mysql.com/downloads/installer/
2. Install MySQL Server + Workbench
3. Open MySQL Workbench
4. Connect to local instance
5. Execute the same SQL code above

## Common XAMPP MySQL Settings:
- Host: localhost
- Port: 3306
- Username: root
- Password: (usually empty)










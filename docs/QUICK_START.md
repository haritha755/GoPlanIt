# 🚀 Quick Start Guide - Trip Planner App

## ✅ Database Issue FIXED!

The database now loads automatically when you start the application!

## How to Run (3 Easy Steps)

### Step 1: Make Sure MySQL is Running
- Open MySQL Workbench or check MySQL service is running
- Default port: 3306

### Step 2: Compile the Application
Double-click `compile.bat` or run:
```
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" DatabaseInitializer.java DatabaseConnection.java TripPlannerApp.java TripResultsWindow.java
```

### Step 3: Run the Application
Double-click `run.bat` or run:
```
java -cp ".;lib/mysql-connector-j-9.4.0.jar" TripPlannerApp
```

## What You'll See

On first run:
```
Checking database setup...
✓ Database 'trip_planner' is ready
✓ Hotels table created
✓ Tourist spots table created
✓ Food spots table created
Populating database with sample data...
✓ Sample data populated
Database initialization complete!
```

Then the Trip Planner GUI will open!

## Test the Application

1. Enter a destination: **Munnar**, **Bangalore**, or **Pondicherry**
2. Select dates (format: DD/MM/YYYY)
3. Choose a budget
4. Click "Search Trips"
5. Select hotels, tourist spots, and food spots
6. Click "Create My Perfect Plan"

## If You Have Issues

### Issue: "MySQL JDBC Driver not found"
**Solution:** Make sure `lib/mysql-connector-j-9.4.0.jar` exists

### Issue: "Access denied for user 'root'"
**Solution:** Update password in `DatabaseConnection.java` (line 8)

### Issue: "Can't connect to MySQL server"
**Solution:** Start MySQL service

## Database Credentials

Current settings in `DatabaseConnection.java`:
- **Host:** localhost:3306
- **Database:** trip_planner
- **Username:** root
- **Password:** harithaishu

Change these if your MySQL setup is different!

## Need More Help?

See `DATABASE_FIX_README.md` for detailed troubleshooting.

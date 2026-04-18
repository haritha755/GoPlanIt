@echo off
echo Compiling Trip Planner Application...
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" DatabaseInitializer.java DatabaseConnection.java TripPlannerApp.java TripResultsWindow.java
if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)
pause

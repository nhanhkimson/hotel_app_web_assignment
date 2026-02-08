#!/bin/bash

# Hotel Management System - Database Data Population Script
# This script runs the SQL files to populate the database

echo "=========================================="
echo "Hotel Management System - Data Population"
echo "=========================================="
echo ""

# Database connection details
DB_HOST="localhost"
DB_PORT="5431"
DB_NAME="hotel_sys"
DB_USER="postgres"

# Check if psql is available
if ! command -v psql &> /dev/null; then
    echo "Error: psql command not found. Please install PostgreSQL client tools."
    exit 1
fi

echo "Step 1: Running complete-data.sql (Hotels, Room Types, Roles, Guests)..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f src/main/resources/complete-data.sql

if [ $? -eq 0 ]; then
    echo "✓ Step 1 completed successfully!"
else
    echo "✗ Step 1 failed. Please check the error messages above."
    exit 1
fi

echo ""
echo "Step 2: Running data-generator.sql (Rooms, Employees, Bookings, Bills)..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f src/main/resources/data-generator.sql

if [ $? -eq 0 ]; then
    echo "✓ Step 2 completed successfully!"
else
    echo "✗ Step 2 failed. Please check the error messages above."
    exit 1
fi

echo ""
echo "=========================================="
echo "Data population completed!"
echo "=========================================="
echo ""
echo "Verifying data..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "
SELECT 
    'Hotels' as table_name, COUNT(*) as record_count FROM hotel
UNION ALL
SELECT 'Room Types', COUNT(*) FROM room_type
UNION ALL
SELECT 'Rooms', COUNT(*) FROM room
UNION ALL
SELECT 'Roles', COUNT(*) FROM role
UNION ALL
SELECT 'Employees', COUNT(*) FROM employee
UNION ALL
SELECT 'Guests', COUNT(*) FROM guest
UNION ALL
SELECT 'Bookings', COUNT(*) FROM booking
UNION ALL
SELECT 'Bills', COUNT(*) FROM bill;
"

echo ""
echo "Done! Your database is now populated with sample data."



# Hotel Management System - Data Setup Guide

This guide explains how to populate the database with 30 realistic records for each table.

## Files Created

1. **`data.sql`** - Static data for Hotels, Room Types, Roles, and Guests (30 records each)
2. **`data-generator.sql`** - Dynamic SQL script using DO blocks to generate Rooms, Employees, Bookings, and Bills with proper relationships
3. **`init-data.sql`** - Complete initialization script (recommended for manual execution)

## Setup Methods

### Method 1: Manual SQL Execution (Recommended)

1. Connect to your PostgreSQL database:
```bash
psql -h localhost -p 5431 -U postgres -d hotel_sys
```

2. Enable UUID extension (if not already enabled):
```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

3. Run the initialization script:
```sql
\i src/main/resources/init-data.sql
```

4. Run the data generator script:
```sql
\i src/main/resources/data-generator.sql
```

### Method 2: Using Spring Boot (Limited)

Spring Boot's `data.sql` initialization has limitations with complex SQL (DO blocks, functions). For the best results, use Method 1.

If you want to try Spring Boot initialization:
1. Place `data.sql` in `src/main/resources/`
2. Update `application.properties`:
```properties
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
```

Note: The `data-generator.sql` file with DO blocks should be run manually as Spring Boot may not execute it properly.

### Method 3: Using Database Migration Tool (Flyway/Liquibase)

1. Create a migration file in your migration tool
2. Copy the contents of `init-data.sql` and `data-generator.sql`
3. Run the migration

## Data Overview

After setup, you'll have:

- **30 Hotels** - Located in major cities worldwide
- **30 Room Types** - From economy to luxury suites
- **30 Roles** - Various hotel staff positions
- **30 Guests** - International guests with realistic data
- **30 Rooms** - Distributed across hotels with proper relationships
- **30 Employees** - Assigned to hotels and roles
- **30 Bookings** - With realistic dates and guest information
- **30 Bills** - Linked to bookings with payment details

## Verification

After running the scripts, verify the data:

```sql
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
```

All tables should show 30 records (or close to it for Rooms, Employees, Bookings, and Bills which are generated dynamically).

## Sample Queries

### View all bookings with details:
```sql
SELECT 
    b.booking_id,
    h.hotel_name,
    g.first_name || ' ' || g.last_name as guest_name,
    r.room_no,
    rt.room_type,
    b.arrival_date,
    b.departure_date
FROM booking b
JOIN hotel h ON b.hotel_code = h.hotel_code
JOIN guest g ON b.guest_id = g.guest_id
JOIN room r ON b.room_id = r.room_id
JOIN room_type rt ON r.room_type_id = rt.room_type_id
ORDER BY b.booking_date DESC
LIMIT 10;
```

### View bills with totals:
```sql
SELECT 
    bill.invoice_no,
    g.first_name || ' ' || g.last_name as guest_name,
    h.hotel_name,
    (bill.room_charge + bill.room_service + bill.restaurant_charges +
     bill.bar_charges + bill.misc_charges) as total_amount,
    bill.payment_mode,
    bill.payment_date
FROM bill
JOIN booking b ON bill.booking_id = b.booking_id
JOIN guest g ON bill.guest_id = g.guest_id
JOIN hotel h ON b.hotel_code = h.hotel_code
ORDER BY bill.payment_date DESC NULLS LAST
LIMIT 10;
```

## Troubleshooting

### UUID Extension Error
If you get an error about `uuid_generate_v4()`, make sure the extension is enabled:
```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

### Foreign Key Violations
If you get foreign key errors, make sure to run the scripts in order:
1. First: Hotels, Room Types, Roles, Guests
2. Then: Rooms, Employees
3. Finally: Bookings, Bills

### Duplicate Key Errors
If you're re-running the scripts, you may need to clear existing data first. Uncomment the DELETE statements in `init-data.sql` or manually delete records.

## Notes

- All data is realistic but fictional
- Phone numbers, emails, and addresses are sample data
- Dates are relative to the current date
- Prices are in USD (convert as needed)
- The data generator uses random selection, so exact counts may vary slightly



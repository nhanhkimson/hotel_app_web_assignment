# How to Run Database Data Scripts

## Quick Start

### Option 1: Using the Shell Script (Easiest)

```bash
cd /Users/apple/Desktop/hotel_mangement
./run-data-script.sh
```

The script will:
1. Run `complete-data.sql` (Hotels, Room Types, Roles, Guests - 30 each)
2. Run `data-generator.sql` (Rooms, Employees, Bookings, Bills - 30 each)
3. Verify the data was inserted correctly

### Option 2: Manual Execution via psql

1. **Connect to your database:**
```bash
psql -h localhost -p 5431 -U postgres -d hotel_sys
```

2. **Run the first script (static data):**
```sql
\i src/main/resources/complete-data.sql
```

3. **Run the second script (generated data with relationships):**
```sql
\i src/main/resources/data-generator.sql
```

4. **Verify the data:**
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

### Option 3: Using pgAdmin or DBeaver

1. Open your database client (pgAdmin, DBeaver, etc.)
2. Connect to `hotel_sys` database
3. Open and execute `src/main/resources/complete-data.sql`
4. Open and execute `src/main/resources/data-generator.sql`

## What Gets Created

- **30 Hotels** - Major cities worldwide
- **30 Room Types** - From economy to luxury
- **30 Roles** - Hotel staff positions
- **30 Guests** - International guests
- **30 Rooms** - Distributed across hotels
- **30 Employees** - Assigned to hotels
- **30 Bookings** - With realistic dates
- **30 Bills** - Linked to bookings

## Troubleshooting

### Error: "uuid-ossp extension does not exist"
```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

### Error: "relation does not exist"
Make sure your Spring Boot application has run at least once to create the tables, or run the schema creation manually.

### Error: "duplicate key value"
The data already exists. If you want to reset:
```sql
TRUNCATE TABLE bill CASCADE;
TRUNCATE TABLE booking CASCADE;
TRUNCATE TABLE guest CASCADE;
TRUNCATE TABLE employee CASCADE;
TRUNCATE TABLE room CASCADE;
TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE room_type CASCADE;
TRUNCATE TABLE hotel CASCADE;
```
Then run the scripts again.

### Connection Issues
- Make sure PostgreSQL is running
- Check the port (5431) and host (localhost) in `application.properties`
- Verify database name: `hotel_sys`
- Check username/password: `postgres` / `123`

## Files

- `complete-data.sql` - Static data (Hotels, Room Types, Roles, Guests)
- `data-generator.sql` - Dynamic data with relationships (Rooms, Employees, Bookings, Bills)
- `run-data-script.sh` - Automated script to run everything


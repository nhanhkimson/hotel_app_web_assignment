-- Hotel Management System - Complete Data Generator
-- This script generates 30 records for each table with proper relationships
-- Run this after the basic data.sql has been executed

-- ========================================
-- Generate ROOMS (30 records distributed across hotels)
-- ========================================
DO $$
DECLARE
    hotel_rec RECORD;
    room_type_rec RECORD;
    room_counter INTEGER := 1;
    room_numbers TEXT[] := ARRAY['101', '102', '103', '201', '202', '203', '301', '302', '303', '401', '402', '501', '502', '601', '602'];
    occupancies TEXT[] := ARRAY['Available', 'Occupied', 'Maintenance', 'Reserved'];
    i INTEGER;
    selected_room_type_id UUID;
    selected_occupancy TEXT;
BEGIN
    FOR hotel_rec IN SELECT hotel_code, hotel_name FROM hotel LIMIT 30 LOOP
        -- Create 1-2 rooms per hotel to reach 30 total
        FOR i IN 1..1 LOOP
            -- Select a random room type
            SELECT room_type_id INTO selected_room_type_id 
            FROM room_type 
            ORDER BY RANDOM() 
            LIMIT 1;
            
            -- Select random occupancy
            selected_occupancy := occupancies[1 + floor(random() * array_length(occupancies, 1))::int];
            
            INSERT INTO room (room_id, room_no, hotel_code, room_type_id, occupancy)
            VALUES (
                uuid_generate_v4(),
                room_numbers[room_counter % array_length(room_numbers, 1) + 1],
                hotel_rec.hotel_code,
                selected_room_type_id,
                selected_occupancy
            );
            
            room_counter := room_counter + 1;
            EXIT WHEN room_counter > 30;
        END LOOP;
        EXIT WHEN room_counter > 30;
    END LOOP;
END $$;

-- ========================================
-- Generate EMPLOYEES (30 records)
-- ========================================
DO $$
DECLARE
    hotel_rec RECORD;
    role_rec RECORD;
    first_names TEXT[] := ARRAY['John', 'Sarah', 'Michael', 'Emma', 'David', 'Lisa', 'James', 'Maria', 'Robert', 'Jennifer', 'William', 'Patricia', 'Richard', 'Linda', 'Joseph', 'Elizabeth', 'Thomas', 'Barbara', 'Charles', 'Susan', 'Christopher', 'Jessica', 'Daniel', 'Sarah', 'Matthew', 'Karen', 'Anthony', 'Nancy', 'Mark', 'Betty'];
    last_names TEXT[] := ARRAY['Smith', 'Johnson', 'Williams', 'Brown', 'Jones', 'Garcia', 'Miller', 'Davis', 'Rodriguez', 'Martinez', 'Hernandez', 'Lopez', 'Wilson', 'Anderson', 'Thomas', 'Taylor', 'Moore', 'Jackson', 'Martin', 'Lee', 'Thompson', 'White', 'Harris', 'Sanchez', 'Clark', 'Ramirez', 'Lewis', 'Robinson', 'Walker', 'Young'];
    genders TEXT[] := ARRAY['Male', 'Female'];
    phones TEXT[] := ARRAY['+1-212-555-', '+44-20-7946-', '+33-1-42-86-', '+1-305-555-', '+44-20-7234-', '+33-1-42-65-', '+81-3-1234-', '+61-2-9876-', '+971-4-123-', '+49-30-9876-'];
    domains TEXT[] := ARRAY['@grandplaza.com', '@seaside.co.uk', '@mountainview.fr', '@citycenter.com', '@oceanbreeze.com', '@royalheritage.co.uk', '@champselysees.fr', '@tokyoskyline.jp', '@sydneyharbour.au', '@dubaimarina.ae'];
    emp_counter INTEGER := 1;
    selected_hotel_code UUID;
    selected_role_id UUID;
    selected_first TEXT;
    selected_last TEXT;
    selected_gender TEXT;
    selected_phone TEXT;
    selected_domain TEXT;
    selected_salary DECIMAL;
    dob DATE;
BEGIN
    FOR i IN 1..30 LOOP
        -- Select random hotel
        SELECT hotel_code INTO selected_hotel_code 
        FROM hotel 
        ORDER BY RANDOM() 
        LIMIT 1;
        
        -- Select random role
        SELECT role_id INTO selected_role_id 
        FROM role 
        ORDER BY RANDOM() 
        LIMIT 1;
        
        -- Select random name components
        selected_first := first_names[1 + floor(random() * array_length(first_names, 1))::int];
        selected_last := last_names[1 + floor(random() * array_length(last_names, 1))::int];
        selected_gender := genders[1 + floor(random() * array_length(genders, 1))::int];
        selected_phone := phones[1 + floor(random() * array_length(phones, 1))::int] || LPAD((1000 + i)::TEXT, 4, '0');
        selected_domain := domains[1 + floor(random() * array_length(domains, 1))::int];
        
        -- Generate DOB (age 25-55)
        dob := CURRENT_DATE - INTERVAL '1 year' * (25 + floor(random() * 30)::int);
        
        -- Salary based on role (simplified)
        selected_salary := 30000 + (random() * 70000);
        
        INSERT INTO employee (employee_id, hotel_code, role_id, first_name, last_name, dob, gender, phone_no, email, password, salary)
        VALUES (
            uuid_generate_v4(),
            selected_hotel_code,
            selected_role_id,
            selected_first,
            selected_last,
            dob,
            selected_gender,
            selected_phone,
            LOWER(selected_first || '.' || selected_last || selected_domain),
            'hashed_password_' || i,
            selected_salary
        );
    END LOOP;
END $$;

-- ========================================
-- Generate BOOKINGS (30 records)
-- ========================================
DO $$
DECLARE
    hotel_rec RECORD;
    guest_rec RECORD;
    room_rec RECORD;
    booking_counter INTEGER := 1;
    num_adults_arr INTEGER[] := ARRAY[1, 2, 2, 2, 3, 4];
    num_children_arr INTEGER[] := ARRAY[0, 0, 1, 2, 0, 1, 2];
    special_reqs TEXT[] := ARRAY['Late check-in', 'Early check-out', 'Baby cot needed', 'Extra towels', 'Quiet room', 'High floor preferred', 'Near elevator', 'City view requested', 'Non-smoking room', 'Adjoining rooms', 'Anniversary celebration', 'Business trip', 'Honeymoon', NULL];
    booking_date DATE;
    arrival_date DATE;
    departure_date DATE;
    selected_hotel_code UUID;
    selected_guest_id UUID;
    selected_room_id UUID;
    selected_num_adults INTEGER;
    selected_num_children INTEGER;
    selected_special_req TEXT;
BEGIN
    FOR i IN 1..30 LOOP
        -- Select random hotel
        SELECT hotel_code INTO selected_hotel_code 
        FROM hotel 
        ORDER BY RANDOM() 
        LIMIT 1;
        
        -- Select random guest
        SELECT guest_id INTO selected_guest_id 
        FROM guest 
        ORDER BY RANDOM() 
        LIMIT 1;
        
        -- Select random room from the selected hotel
        SELECT room_id INTO selected_room_id 
        FROM room 
        WHERE hotel_code = selected_hotel_code 
        ORDER BY RANDOM() 
        LIMIT 1;
        
        -- Generate dates (bookings in the past 30 days to future 90 days)
        booking_date := CURRENT_DATE - INTERVAL '1 day' * floor(random() * 30)::int;
        arrival_date := booking_date + INTERVAL '1 day' * (1 + floor(random() * 30)::int);
        departure_date := arrival_date + INTERVAL '1 day' * (1 + floor(random() * 7)::int);
        
        selected_num_adults := num_adults_arr[1 + floor(random() * array_length(num_adults_arr, 1))::int];
        selected_num_children := num_children_arr[1 + floor(random() * array_length(num_children_arr, 1))::int];
        selected_special_req := special_reqs[1 + floor(random() * array_length(special_reqs, 1))::int];
        
        INSERT INTO booking (booking_id, hotel_code, guest_id, room_id, booking_date, booking_time, arrival_date, departure_date, est_arrival_time, est_departure_time, num_adults, num_children, special_req)
        VALUES (
            uuid_generate_v4(),
            selected_hotel_code,
            selected_guest_id,
            selected_room_id,
            booking_date,
            (TIME '08:00:00' + (random() * INTERVAL '10 hours'))::TIME,
            arrival_date,
            departure_date,
            (TIME '14:00:00' + (random() * INTERVAL '4 hours'))::TIME,
            (TIME '10:00:00' + (random() * INTERVAL '2 hours'))::TIME,
            selected_num_adults,
            selected_num_children,
            selected_special_req
        );
    END LOOP;
END $$;

-- ========================================
-- Generate BILLS (30 records)
-- ========================================
DO $$
DECLARE
    booking_rec RECORD;
    payment_modes TEXT[] := ARRAY['Credit Card', 'Debit Card', 'Cash', 'Cheque', 'Bank Transfer'];
    bill_counter INTEGER := 1;
    selected_booking_id UUID;
    selected_guest_id UUID;
    room_charge DECIMAL;
    room_service DECIMAL;
    restaurant_charges DECIMAL;
    bar_charges DECIMAL;
    misc_charges DECIMAL;
    if_late_checkout BOOLEAN;
    payment_date DATE;
    selected_payment_mode TEXT;
    credit_card_no TEXT;
    expire_date DATE;
    cheque_no TEXT;
BEGIN
    FOR booking_rec IN SELECT booking_id, guest_id, arrival_date, departure_date 
                       FROM booking 
                       ORDER BY RANDOM() 
                       LIMIT 30 LOOP
        
        -- Calculate room charge based on nights (simplified: $100-300 per night)
        room_charge := (booking_rec.departure_date - booking_rec.arrival_date) * (100 + random() * 200);
        room_service := 50 + (random() * 200);
        restaurant_charges := 100 + (random() * 300);
        bar_charges := 30 + (random() * 150);
        misc_charges := 20 + (random() * 100);
        if_late_checkout := (random() < 0.2); -- 20% chance
        
        -- Payment date (usually on or after departure)
        payment_date := booking_rec.departure_date + INTERVAL '1 day' * floor(random() * 3)::int;
        
        selected_payment_mode := payment_modes[1 + floor(random() * array_length(payment_modes, 1))::int];
        
        IF selected_payment_mode IN ('Credit Card', 'Debit Card') THEN
            credit_card_no := '****-****-****-' || LPAD((1000 + bill_counter)::TEXT, 4, '0');
            expire_date := CURRENT_DATE + INTERVAL '1 year' * (1 + floor(random() * 3)::int);
            cheque_no := NULL;
        ELSIF selected_payment_mode = 'Cheque' THEN
            cheque_no := 'CHQ' || LPAD((10000 + bill_counter)::TEXT, 6, '0');
            credit_card_no := NULL;
            expire_date := NULL;
        ELSE
            credit_card_no := NULL;
            expire_date := NULL;
            cheque_no := NULL;
        END IF;
        
        INSERT INTO bill (invoice_no, booking_id, guest_id, room_charge, room_service, restaurant_charges, bar_charges, misc_charges, if_late_checkout, payment_date, payment_mode, credit_card_no, expire_date, cheque_no)
        VALUES (
            uuid_generate_v4(),
            booking_rec.booking_id,
            booking_rec.guest_id,
            ROUND(room_charge::numeric, 2),
            ROUND(room_service::numeric, 2),
            ROUND(restaurant_charges::numeric, 2),
            ROUND(bar_charges::numeric, 2),
            ROUND(misc_charges::numeric, 2),
            if_late_checkout,
            payment_date,
            selected_payment_mode,
            credit_card_no,
            expire_date,
            cheque_no
        );
        
        bill_counter := bill_counter + 1;
    END LOOP;
END $$;

-- ========================================
-- Update guest booking_id references
-- ========================================
-- Update guest booking_id references (update first 30 matching records)
UPDATE guest g
SET booking_id = b.booking_id
FROM (
    SELECT DISTINCT ON (guest_id) booking_id, guest_id
    FROM booking
    ORDER BY guest_id, booking_id
    LIMIT 30
) b
WHERE g.guest_id = b.guest_id
AND g.booking_id IS NULL;

-- ========================================
-- Verification Query
-- ========================================
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


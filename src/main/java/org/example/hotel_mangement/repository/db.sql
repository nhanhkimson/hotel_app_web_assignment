-- Hotel Management System - Sample Data Insert Script with UUID for PostgreSQL

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Insert Hotels
INSERT INTO hotel (hotel_code, hotel_name, address, postcode, city, country, num_rooms, phone_no, star_rating) VALUES
                                                                                                                   (uuid_generate_v4(), 'Grand Plaza Hotel', '123 Main Street', '10001', 'New York', 'USA', 150, '+1-212-555-0100', 5),
                                                                                                                   (uuid_generate_v4(), 'Seaside Resort', '456 Beach Road', 'SW1A 1AA', 'London', 'UK', 200, '+44-20-7946-0958', 4),
                                                                                                                   (uuid_generate_v4(), 'Mountain View Lodge', '789 Alpine Way', '75001', 'Paris', 'France', 80, '+33-1-42-86-82-00', 4),
                                                                                                                   (uuid_generate_v4(), 'City Center Inn', '321 Downtown Ave', '10002', 'New York', 'USA', 100, '+1-212-555-0200', 3);

-- 2. Insert Room Types
INSERT INTO room_type (room_type_id, room_type, room_price, default_room_price, room_img, room_desc) VALUES
                                                                                                         (uuid_generate_v4(), 'Single', 100.00, 100.00, 'single_room.jpg', 'Cozy single room with queen bed, perfect for solo travelers'),
                                                                                                         (uuid_generate_v4(), 'Double', 150.00, 150.00, 'double_room.jpg', 'Spacious double room with two queen beds'),
                                                                                                         (uuid_generate_v4(), 'Suite', 300.00, 300.00, 'suite_room.jpg', 'Luxurious suite with separate living area and king bed'),
                                                                                                         (uuid_generate_v4(), 'Deluxe', 250.00, 250.00, 'deluxe_room.jpg', 'Deluxe room with premium amenities and city view'),
                                                                                                         (uuid_generate_v4(), 'Presidential', 500.00, 500.00, 'presidential_suite.jpg', 'Top floor presidential suite with panoramic views');

-- 3. Insert Rooms
-- Get hotel and room type IDs for reference
WITH hotel_ids AS (
    SELECT hotel_code, hotel_name FROM hotel
),
     room_type_ids AS (
         SELECT room_type_id, room_type FROM room_type
     )
INSERT INTO room (room_id, room_no, hotel_code, room_type_id, occupancy)
SELECT
    uuid_generate_v4(),
    room_data.room_no,
    h.hotel_code,
    rt.room_type_id,
    room_data.occupancy
FROM (VALUES
          -- Grand Plaza Hotel rooms
          ('101', 'Grand Plaza Hotel', 'Single', 'Available'),
          ('102', 'Grand Plaza Hotel', 'Double', 'Available'),
          ('103', 'Grand Plaza Hotel', 'Suite', 'Available'),
          ('201', 'Grand Plaza Hotel', 'Deluxe', 'Occupied'),
          ('301', 'Grand Plaza Hotel', 'Presidential', 'Available'),
          -- Seaside Resort rooms
          ('101', 'Seaside Resort', 'Single', 'Available'),
          ('102', 'Seaside Resort', 'Double', 'Available'),
          ('201', 'Seaside Resort', 'Suite', 'Occupied'),
          ('202', 'Seaside Resort', 'Deluxe', 'Available'),
          -- Mountain View Lodge rooms
          ('101', 'Mountain View Lodge', 'Single', 'Available'),
          ('102', 'Mountain View Lodge', 'Double', 'Occupied'),
          ('201', 'Mountain View Lodge', 'Suite', 'Available'),
          -- City Center Inn rooms
          ('101', 'City Center Inn', 'Single', 'Available'),
          ('102', 'City Center Inn', 'Double', 'Available')
     ) AS room_data(room_no, hotel_name, room_type_name, occupancy)
         JOIN hotel_ids h ON h.hotel_name = room_data.hotel_name
         JOIN room_type_ids rt ON rt.room_type = room_data.room_type_name;

-- 4. Insert Roles
INSERT INTO role (role_id, role_title, role_desc) VALUES
                                                      (uuid_generate_v4(), 'Manager', 'Hotel manager responsible for overall operations'),
                                                      (uuid_generate_v4(), 'Receptionist', 'Front desk staff handling check-ins and bookings'),
                                                      (uuid_generate_v4(), 'Housekeeping', 'Room cleaning and maintenance staff'),
                                                      (uuid_generate_v4(), 'Chef', 'Restaurant kitchen staff'),
                                                      (uuid_generate_v4(), 'Security', 'Hotel security personnel');

-- 5. Insert Employees
WITH hotel_ids AS (
    SELECT hotel_code, hotel_name FROM hotel
),
     role_ids AS (
         SELECT role_id, role_title FROM role
     )
INSERT INTO employee (employee_id, hotel_code, role_id, first_name, last_name, dob, gender, phone_no, email, password, salary)
SELECT
    uuid_generate_v4(),
    h.hotel_code,
    r.role_id,
    emp_data.first_name,
    emp_data.last_name,
    emp_data.dob,
    emp_data.gender,
    emp_data.phone_no,
    emp_data.email,
    emp_data.password,
    emp_data.salary
FROM (VALUES
          ('Grand Plaza Hotel', 'Manager', 'John', 'Smith', '1985-03-15', 'Male', '+1-212-555-1001', 'john.smith@grandplaza.com', 'hashed_password_1', 75000.00),
          ('Grand Plaza Hotel', 'Receptionist', 'Sarah', 'Johnson', '1990-07-22', 'Female', '+1-212-555-1002', 'sarah.johnson@grandplaza.com', 'hashed_password_2', 45000.00),
          ('Grand Plaza Hotel', 'Housekeeping', 'Michael', 'Brown', '1988-11-10', 'Male', '+1-212-555-1003', 'michael.brown@grandplaza.com', 'hashed_password_3', 35000.00),
          ('Seaside Resort', 'Manager', 'Emma', 'Wilson', '1982-05-18', 'Female', '+44-20-7946-1001', 'emma.wilson@seaside.co.uk', 'hashed_password_4', 70000.00),
          ('Seaside Resort', 'Receptionist', 'James', 'Davis', '1992-09-25', 'Male', '+44-20-7946-1002', 'james.davis@seaside.co.uk', 'hashed_password_5', 42000.00),
          ('Mountain View Lodge', 'Manager', 'Sophie', 'Martin', '1987-02-14', 'Female', '+33-1-42-86-1001', 'sophie.martin@mountainview.fr', 'hashed_password_6', 68000.00),
          ('City Center Inn', 'Receptionist', 'Robert', 'Taylor', '1991-06-30', 'Male', '+1-212-555-2001', 'robert.taylor@citycenter.com', 'hashed_password_7', 43000.00)
     ) AS emp_data(hotel_name, role_title, first_name, last_name, dob, gender, phone_no, email, password, salary)
         JOIN hotel_ids h ON h.hotel_name = emp_data.hotel_name
         JOIN role_ids r ON r.role_title = emp_data.role_title;

-- 6. Insert Guests
INSERT INTO guest (guest_id, booking_id, guest_title, first_name, last_name, dob, gender, phone_no, email, password, passport_no, address, postcode, city, country) VALUES
                                                                                                                                                                        (uuid_generate_v4(), NULL, 'Mr', 'David', 'Anderson', '1980-04-12', 'Male', '+1-555-0101', 'david.anderson@email.com', 'guest_pass_1', 'P12345678', '100 Oak Street', '90001', 'Los Angeles', 'USA'),
                                                                                                                                                                        (uuid_generate_v4(), NULL, 'Ms', 'Lisa', 'Martinez', '1985-08-20', 'Female', '+1-555-0102', 'lisa.martinez@email.com', 'guest_pass_2', 'P23456789', '200 Pine Avenue', '60601', 'Chicago', 'USA'),
                                                                                                                                                                        (uuid_generate_v4(), NULL, 'Mrs', 'Jennifer', 'Garcia', '1978-12-05', 'Female', '+1-555-0103', 'jennifer.garcia@email.com', 'guest_pass_3', 'P34567890', '300 Maple Drive', '33101', 'Miami', 'USA'),
                                                                                                                                                                        (uuid_generate_v4(), NULL, 'Mr', 'William', 'Lee', '1983-06-18', 'Male', '+44-7700-900123', 'william.lee@email.co.uk', 'guest_pass_4', 'GB45678901', '50 King Street', 'SW1A 2AA', 'London', 'UK'),
                                                                                                                                                                        (uuid_generate_v4(), NULL, 'Dr', 'Maria', 'Rodriguez', '1975-03-25', 'Female', '+33-6-12-34-56-78', 'maria.rodriguez@email.fr', 'guest_pass_5', 'FR56789012', '25 Rue de la Paix', '75002', 'Paris', 'France');

-- 7. Insert Bookings
WITH hotel_data AS (
    SELECT hotel_code, hotel_name FROM hotel
),
     guest_data AS (
         SELECT guest_id, email FROM guest
     ),
     room_data AS (
         SELECT r.room_id, r.room_no, h.hotel_name
         FROM room r
                  JOIN hotel h ON r.hotel_code = h.hotel_code
     )
INSERT INTO booking (booking_id, hotel_code, guest_id, room_id, booking_date, booking_time, arrival_date, departure_date, est_arrival_time, est_departure_time, num_adults, num_children, special_req)
SELECT
    uuid_generate_v4(),
    h.hotel_code,
    g.guest_id,
    r.room_id,
    booking_data.booking_date,
    booking_data.booking_time,
    booking_data.arrival_date,
    booking_data.departure_date,
    booking_data.est_arrival_time,
    booking_data.est_departure_time,
    booking_data.num_adults,
    booking_data.num_children,
    booking_data.special_req
FROM (VALUES
          ('Grand Plaza Hotel', '201', 'david.anderson@email.com', '2024-12-01', '10:30:00', '2024-12-15', '2024-12-20', '14:00:00', '11:00:00', 2, 0, 'Late check-in requested'),
          ('Seaside Resort', '201', 'lisa.martinez@email.com', '2024-12-02', '09:15:00', '2024-12-18', '2024-12-22', '15:00:00', '10:00:00', 2, 1, 'Baby cot needed'),
          ('Mountain View Lodge', '102', 'jennifer.garcia@email.com', '2024-12-03', '14:20:00', '2024-12-10', '2024-12-14', '16:00:00', '11:00:00', 2, 2, 'Adjoining rooms preferred'),
          ('Grand Plaza Hotel', '103', 'william.lee@email.co.uk', '2024-12-04', '11:45:00', '2024-12-20', '2024-12-25', '13:00:00', '12:00:00', 2, 0, 'Anniversary celebration - champagne in room'),
          ('City Center Inn', '101', 'maria.rodriguez@email.fr', '2024-12-05', '16:30:00', '2024-12-12', '2024-12-15', '18:00:00', '10:00:00', 1, 0, 'Quiet room away from elevator')
     ) AS booking_data(hotel_name, room_no, guest_email, booking_date, booking_time, arrival_date, departure_date, est_arrival_time, est_departure_time, num_adults, num_children, special_req)
         JOIN hotel_data h ON h.hotel_name = booking_data.hotel_name
         JOIN guest_data g ON g.email = booking_data.guest_email
         JOIN room_data r ON r.hotel_name = booking_data.hotel_name AND r.room_no = booking_data.room_no;

-- 8. Update guest table with booking_id references
UPDATE guest g
SET booking_id = b.booking_id
FROM booking b
WHERE g.guest_id = b.guest_id;

-- 9. Insert Bills
WITH booking_data AS (
    SELECT b.booking_id, b.guest_id, g.email
    FROM booking b
             JOIN guest g ON b.guest_id = g.guest_id
)
INSERT INTO bill (invoice_no, booking_id, guest_id, room_charge, room_service, restaurant_charges, bar_charges, misc_charges, if_late_checkout, payment_date, payment_mode, credit_card_no, expire_date, cheque_no)
SELECT
    uuid_generate_v4(),
    bd.booking_id,
    bd.guest_id,
    bill_data.room_charge,
    bill_data.room_service,
    bill_data.restaurant_charges,
    bill_data.bar_charges,
    bill_data.misc_charges,
    bill_data.if_late_checkout,
    bill_data.payment_date,
    bill_data.payment_mode,
    bill_data.credit_card_no,
    bill_data.expire_date,
    bill_data.cheque_no
FROM (VALUES
          ('david.anderson@email.com', 1250.00, 150.00, 300.00, 100.00, 50.00, false, '2024-12-20', 'Credit Card', '************1234', '2026-12-31', NULL),
          ('lisa.martinez@email.com', 1200.00, 200.00, 250.00, 80.00, 30.00, false, '2024-12-22', 'Credit Card', '************5678', '2027-06-30', NULL),
          ('jennifer.garcia@email.com', 600.00, 100.00, 180.00, 60.00, 20.00, true, '2024-12-14', 'Cash', NULL, NULL, NULL),
          ('william.lee@email.co.uk', 1500.00, 300.00, 400.00, 150.00, 100.00, false, NULL, NULL, NULL, NULL, NULL),
          ('maria.rodriguez@email.fr', 300.00, 50.00, 100.00, 30.00, 10.00, false, NULL, NULL, NULL, NULL, NULL)
     ) AS bill_data(guest_email, room_charge, room_service, restaurant_charges, bar_charges, misc_charges, if_late_checkout, payment_date, payment_mode, credit_card_no, expire_date, cheque_no)
         JOIN booking_data bd ON bd.email = bill_data.guest_email;

-- ========================================
-- Verification Queries
-- ========================================

-- Check all tables have data
SELECT 'hotel' as table_name, COUNT(*) as row_count FROM hotel
UNION ALL
SELECT 'room_type', COUNT(*) FROM room_type
UNION ALL
SELECT 'room', COUNT(*) FROM room
UNION ALL
SELECT 'role', COUNT(*) FROM role
UNION ALL
SELECT 'employee', COUNT(*) FROM employee
UNION ALL
SELECT 'guest', COUNT(*) FROM guest
UNION ALL
SELECT 'booking', COUNT(*) FROM booking
UNION ALL
SELECT 'bill', COUNT(*) FROM bill;

-- View sample bookings with details
SELECT
    b.booking_id,
    h.hotel_name,
    g.first_name || ' ' || g.last_name as guest_name,
    r.room_no,
    rt.room_type,
    b.arrival_date,
    b.departure_date,
    b.num_adults,
    b.num_children
FROM booking b
         JOIN hotel h ON b.hotel_code = h.hotel_code
         JOIN guest g ON b.guest_id = g.guest_id
         JOIN room r ON b.room_id = r.room_id
         JOIN room_type rt ON r.room_type_id = rt.room_type_id
ORDER BY b.booking_date;

-- View bills with totals
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
ORDER BY bill.payment_date NULLS LAST;
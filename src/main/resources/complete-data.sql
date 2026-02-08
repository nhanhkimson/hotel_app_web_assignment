-- Hotel Management System - Complete Data Script
-- Run this script to populate all tables with 30 records each
-- Usage: psql -h localhost -p 5431 -U postgres -d hotel_sys -f complete-data.sql

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Clear existing data (optional - uncomment if you want to reset)
-- TRUNCATE TABLE bill CASCADE;
-- TRUNCATE TABLE booking CASCADE;
-- TRUNCATE TABLE guest CASCADE;
-- TRUNCATE TABLE employee CASCADE;
-- TRUNCATE TABLE room CASCADE;
-- TRUNCATE TABLE role CASCADE;
-- TRUNCATE TABLE room_type CASCADE;
-- TRUNCATE TABLE hotel CASCADE;

-- ========================================
-- 1. HOTELS (30 records)
-- ========================================
INSERT INTO hotel (hotel_code, hotel_name, address, postcode, city, country, num_rooms, phone_no, star_rating) VALUES
(uuid_generate_v4(), 'Grand Plaza Hotel', '123 Main Street', '10001', 'New York', 'USA', 150, '+1-212-555-0100', 5),
(uuid_generate_v4(), 'Seaside Resort', '456 Beach Road', 'SW1A 1AA', 'London', 'UK', 200, '+44-20-7946-0958', 4),
(uuid_generate_v4(), 'Mountain View Lodge', '789 Alpine Way', '75001', 'Paris', 'France', 80, '+33-1-42-86-82-00', 4),
(uuid_generate_v4(), 'City Center Inn', '321 Downtown Ave', '10002', 'New York', 'USA', 100, '+1-212-555-0200', 3),
(uuid_generate_v4(), 'Ocean Breeze Resort', '789 Coastal Highway', '33139', 'Miami', 'USA', 180, '+1-305-555-0300', 5),
(uuid_generate_v4(), 'Royal Heritage Hotel', '12 Buckingham Palace Road', 'SW1W 0QP', 'London', 'UK', 120, '+44-20-7234-5678', 5),
(uuid_generate_v4(), 'Champs Elysees Hotel', '45 Avenue des Champs-Élysées', '75008', 'Paris', 'France', 95, '+33-1-42-65-78-90', 4),
(uuid_generate_v4(), 'Tokyo Skyline Hotel', '1-2-3 Shibuya', '150-0002', 'Tokyo', 'Japan', 220, '+81-3-1234-5678', 5),
(uuid_generate_v4(), 'Sydney Harbour View', '88 Harbour Street', '2000', 'Sydney', 'Australia', 160, '+61-2-9876-5432', 4),
(uuid_generate_v4(), 'Dubai Marina Hotel', 'Dubai Marina Walk', '00000', 'Dubai', 'UAE', 300, '+971-4-123-4567', 5),
(uuid_generate_v4(), 'Berlin Central Hotel', 'Friedrichstraße 123', '10117', 'Berlin', 'Germany', 110, '+49-30-9876-5432', 4),
(uuid_generate_v4(), 'Rome Colosseum Inn', 'Via dei Fori Imperiali 1', '00186', 'Rome', 'Italy', 85, '+39-06-1234-5678', 3),
(uuid_generate_v4(), 'Barcelona Beach Resort', 'Passeig de Gràcia 92', '08008', 'Barcelona', 'Spain', 140, '+34-93-123-4567', 4),
(uuid_generate_v4(), 'Amsterdam Canal Hotel', 'Prinsengracht 1053', '1017 JE', 'Amsterdam', 'Netherlands', 75, '+31-20-123-4567', 4),
(uuid_generate_v4(), 'Vienna Imperial Hotel', 'Kärntner Ring 16', '1010', 'Vienna', 'Austria', 130, '+43-1-234-5678', 5),
(uuid_generate_v4(), 'Stockholm Nordic Hotel', 'Sveavägen 44', '111 34', 'Stockholm', 'Sweden', 90, '+46-8-123-4567', 4),
(uuid_generate_v4(), 'Zurich Business Hotel', 'Bahnhofstrasse 31', '8001', 'Zurich', 'Switzerland', 105, '+41-44-123-4567', 4),
(uuid_generate_v4(), 'Prague Castle View', 'Nerudova 211', '118 00', 'Prague', 'Czech Republic', 70, '+420-2-1234-5678', 3),
(uuid_generate_v4(), 'Athens Acropolis Hotel', 'Leoforos Syngrou 2', '117 42', 'Athens', 'Greece', 95, '+30-21-1234-5678', 4),
(uuid_generate_v4(), 'Lisbon Ocean View', 'Avenida da Liberdade 185', '1269-050', 'Lisbon', 'Portugal', 115, '+351-21-123-4567', 4),
(uuid_generate_v4(), 'Copenhagen Harbor Hotel', 'Nyhavn 71', '1051', 'Copenhagen', 'Denmark', 88, '+45-33-12-34-56', 4),
(uuid_generate_v4(), 'Oslo Fjord Resort', 'Karl Johans gate 33', '0162', 'Oslo', 'Norway', 125, '+47-22-12-34-56', 4),
(uuid_generate_v4(), 'Helsinki Design Hotel', 'Pohjoisesplanadi 29', '00100', 'Helsinki', 'Finland', 98, '+358-9-123-4567', 4),
(uuid_generate_v4(), 'Warsaw Central Plaza', 'Aleje Jerozolimskie 65/79', '00-697', 'Warsaw', 'Poland', 145, '+48-22-123-4567', 4),
(uuid_generate_v4(), 'Budapest Danube Hotel', 'Rákóczi út 90', '1072', 'Budapest', 'Hungary', 135, '+36-1-234-5678', 4),
(uuid_generate_v4(), 'Bucharest Grand Hotel', 'Calea Victoriei 63', '010065', 'Bucharest', 'Romania', 155, '+40-21-123-4567', 4),
(uuid_generate_v4(), 'Sofia City Center', 'Vitosha Boulevard 1', '1000', 'Sofia', 'Bulgaria', 120, '+359-2-123-4567', 3),
(uuid_generate_v4(), 'Zagreb Palace Hotel', 'Trg bana Jelačića 1', '10000', 'Zagreb', 'Croatia', 100, '+385-1-234-5678', 4),
(uuid_generate_v4(), 'Belgrade River View', 'Knez Mihailova 56', '11000', 'Belgrade', 'Serbia', 110, '+381-11-123-4567', 3),
(uuid_generate_v4(), 'Istanbul Bosphorus Hotel', 'Taksim Square 1', '34437', 'Istanbul', 'Turkey', 250, '+90-212-123-4567', 5);

-- ========================================
-- 2. ROOM TYPES (30 records)
-- ========================================
INSERT INTO room_type (room_type_id, room_type, room_price, default_room_price, room_img, room_desc) VALUES
(uuid_generate_v4(), 'Single Standard', 80.00, 80.00, 'single_standard.jpg', 'Cozy single room with queen bed, perfect for solo travelers'),
(uuid_generate_v4(), 'Double Standard', 120.00, 120.00, 'double_standard.jpg', 'Comfortable double room with two queen beds'),
(uuid_generate_v4(), 'Twin Room', 130.00, 130.00, 'twin_room.jpg', 'Two single beds, ideal for friends or colleagues'),
(uuid_generate_v4(), 'Deluxe Single', 150.00, 150.00, 'deluxe_single.jpg', 'Spacious single room with premium amenities'),
(uuid_generate_v4(), 'Deluxe Double', 180.00, 180.00, 'deluxe_double.jpg', 'Luxurious double room with city view'),
(uuid_generate_v4(), 'Junior Suite', 250.00, 250.00, 'junior_suite.jpg', 'Suite with separate sitting area and king bed'),
(uuid_generate_v4(), 'Executive Suite', 350.00, 350.00, 'executive_suite.jpg', 'Business suite with work desk and premium amenities'),
(uuid_generate_v4(), 'Family Room', 200.00, 200.00, 'family_room.jpg', 'Spacious room accommodating up to 4 guests'),
(uuid_generate_v4(), 'Presidential Suite', 500.00, 500.00, 'presidential_suite.jpg', 'Top floor suite with panoramic views and butler service'),
(uuid_generate_v4(), 'Honeymoon Suite', 400.00, 400.00, 'honeymoon_suite.jpg', 'Romantic suite with jacuzzi and champagne service'),
(uuid_generate_v4(), 'Ocean View Room', 220.00, 220.00, 'ocean_view.jpg', 'Room with stunning ocean views'),
(uuid_generate_v4(), 'Garden View Room', 140.00, 140.00, 'garden_view.jpg', 'Peaceful room overlooking hotel gardens'),
(uuid_generate_v4(), 'City View Room', 160.00, 160.00, 'city_view.jpg', 'Room with panoramic city skyline views'),
(uuid_generate_v4(), 'Corner Suite', 280.00, 280.00, 'corner_suite.jpg', 'Corner suite with extra windows and space'),
(uuid_generate_v4(), 'Accessible Room', 130.00, 130.00, 'accessible_room.jpg', 'Wheelchair accessible room with adapted facilities'),
(uuid_generate_v4(), 'Pet Friendly Room', 145.00, 145.00, 'pet_friendly.jpg', 'Room designed for guests traveling with pets'),
(uuid_generate_v4(), 'Smoking Room', 110.00, 110.00, 'smoking_room.jpg', 'Designated smoking room with ventilation'),
(uuid_generate_v4(), 'Non-Smoking Deluxe', 170.00, 170.00, 'non_smoking_deluxe.jpg', 'Premium non-smoking room with air purifier'),
(uuid_generate_v4(), 'Studio Apartment', 190.00, 190.00, 'studio_apartment.jpg', 'Self-contained studio with kitchenette'),
(uuid_generate_v4(), 'Penthouse Suite', 600.00, 600.00, 'penthouse_suite.jpg', 'Luxury penthouse with private terrace'),
(uuid_generate_v4(), 'Business Class Room', 165.00, 165.00, 'business_class.jpg', 'Room with dedicated workspace and high-speed internet'),
(uuid_generate_v4(), 'Spa Suite', 320.00, 320.00, 'spa_suite.jpg', 'Suite with in-room spa facilities'),
(uuid_generate_v4(), 'Connecting Rooms', 240.00, 240.00, 'connecting_rooms.jpg', 'Two connecting rooms perfect for families'),
(uuid_generate_v4(), 'Economy Single', 65.00, 65.00, 'economy_single.jpg', 'Budget-friendly single room with essential amenities'),
(uuid_generate_v4(), 'Economy Double', 95.00, 95.00, 'economy_double.jpg', 'Affordable double room for budget travelers'),
(uuid_generate_v4(), 'Luxury Villa', 800.00, 800.00, 'luxury_villa.jpg', 'Private villa with pool and garden'),
(uuid_generate_v4(), 'Beachfront Suite', 450.00, 450.00, 'beachfront_suite.jpg', 'Suite with direct beach access'),
(uuid_generate_v4(), 'Mountain View Suite', 380.00, 380.00, 'mountain_view_suite.jpg', 'Suite with breathtaking mountain vistas'),
(uuid_generate_v4(), 'Historic Room', 175.00, 175.00, 'historic_room.jpg', 'Room in historic building with period features'),
(uuid_generate_v4(), 'Modern Loft', 210.00, 210.00, 'modern_loft.jpg', 'Contemporary loft-style room with high ceilings'),
(uuid_generate_v4(), 'Boutique Room', 195.00, 195.00, 'boutique_room.jpg', 'Uniquely designed boutique-style accommodation');

-- ========================================
-- 3. ROLES (30 records)
-- ========================================
INSERT INTO role (role_id, role_title, role_desc) VALUES
(uuid_generate_v4(), 'General Manager', 'Oversees all hotel operations and staff'),
(uuid_generate_v4(), 'Front Desk Manager', 'Manages reception and guest services'),
(uuid_generate_v4(), 'Receptionist', 'Handles check-ins, check-outs, and guest inquiries'),
(uuid_generate_v4(), 'Concierge', 'Assists guests with local information and reservations'),
(uuid_generate_v4(), 'Housekeeping Manager', 'Supervises cleaning and maintenance staff'),
(uuid_generate_v4(), 'Housekeeper', 'Cleans and maintains guest rooms'),
(uuid_generate_v4(), 'Executive Chef', 'Leads kitchen operations and menu planning'),
(uuid_generate_v4(), 'Sous Chef', 'Assists executive chef in kitchen management'),
(uuid_generate_v4(), 'Line Cook', 'Prepares food items in restaurant kitchen'),
(uuid_generate_v4(), 'Pastry Chef', 'Creates desserts and baked goods'),
(uuid_generate_v4(), 'Restaurant Manager', 'Manages restaurant operations and staff'),
(uuid_generate_v4(), 'Server', 'Serves food and beverages to guests'),
(uuid_generate_v4(), 'Bartender', 'Prepares and serves drinks at bar'),
(uuid_generate_v4(), 'Security Manager', 'Oversees hotel security operations'),
(uuid_generate_v4(), 'Security Guard', 'Monitors hotel premises and ensures safety'),
(uuid_generate_v4(), 'Maintenance Manager', 'Supervises facility maintenance and repairs'),
(uuid_generate_v4(), 'Maintenance Technician', 'Performs repairs and maintenance tasks'),
(uuid_generate_v4(), 'Event Coordinator', 'Plans and organizes hotel events'),
(uuid_generate_v4(), 'Spa Manager', 'Manages spa and wellness services'),
(uuid_generate_v4(), 'Spa Therapist', 'Provides massage and wellness treatments'),
(uuid_generate_v4(), 'Fitness Instructor', 'Leads fitness classes and training'),
(uuid_generate_v4(), 'Pool Attendant', 'Monitors pool area and ensures safety'),
(uuid_generate_v4(), 'Bellhop', 'Assists guests with luggage'),
(uuid_generate_v4(), 'Valet', 'Parks and retrieves guest vehicles'),
(uuid_generate_v4(), 'Night Auditor', 'Handles overnight front desk operations'),
(uuid_generate_v4(), 'Reservations Agent', 'Manages room bookings and reservations'),
(uuid_generate_v4(), 'Guest Relations Manager', 'Handles guest complaints and special requests'),
(uuid_generate_v4(), 'Sales Manager', 'Develops and maintains client relationships'),
(uuid_generate_v4(), 'Marketing Coordinator', 'Manages hotel marketing and promotions'),
(uuid_generate_v4(), 'Accountant', 'Handles financial records and reporting'),
(uuid_generate_v4(), 'HR Manager', 'Manages human resources and recruitment');

-- ========================================
-- 4. GUESTS (30 records)
-- ========================================
INSERT INTO guest (guest_id, booking_id, guest_title, first_name, last_name, dob, gender, phone_no, email, password, passport_no, address, postcode, city, country) VALUES
(uuid_generate_v4(), NULL, 'Mr', 'David', 'Anderson', '1980-04-12', 'Male', '+1-555-0101', 'david.anderson@email.com', 'guest_pass_1', 'P12345678', '100 Oak Street', '90001', 'Los Angeles', 'USA'),
(uuid_generate_v4(), NULL, 'Ms', 'Lisa', 'Martinez', '1985-08-20', 'Female', '+1-555-0102', 'lisa.martinez@email.com', 'guest_pass_2', 'P23456789', '200 Pine Avenue', '60601', 'Chicago', 'USA'),
(uuid_generate_v4(), NULL, 'Mrs', 'Jennifer', 'Garcia', '1978-12-05', 'Female', '+1-555-0103', 'jennifer.garcia@email.com', 'guest_pass_3', 'P34567890', '300 Maple Drive', '33101', 'Miami', 'USA'),
(uuid_generate_v4(), NULL, 'Mr', 'William', 'Lee', '1983-06-18', 'Male', '+44-7700-900123', 'william.lee@email.co.uk', 'guest_pass_4', 'GB45678901', '50 King Street', 'SW1A 2AA', 'London', 'UK'),
(uuid_generate_v4(), NULL, 'Dr', 'Maria', 'Rodriguez', '1975-03-25', 'Female', '+33-6-12-34-56-78', 'maria.rodriguez@email.fr', 'guest_pass_5', 'FR56789012', '25 Rue de la Paix', '75002', 'Paris', 'France'),
(uuid_generate_v4(), NULL, 'Mr', 'James', 'Wilson', '1990-07-15', 'Male', '+1-555-0106', 'james.wilson@email.com', 'guest_pass_6', 'P67890123', '400 Elm Street', '77001', 'Houston', 'USA'),
(uuid_generate_v4(), NULL, 'Ms', 'Sarah', 'Brown', '1987-11-22', 'Female', '+1-555-0107', 'sarah.brown@email.com', 'guest_pass_7', 'P78901234', '500 Cedar Lane', '98101', 'Seattle', 'USA'),
(uuid_generate_v4(), NULL, 'Mr', 'Michael', 'Davis', '1982-02-28', 'Male', '+44-7700-900456', 'michael.davis@email.co.uk', 'guest_pass_8', 'GB89012345', '75 Victoria Road', 'SW1V 1AA', 'London', 'UK'),
(uuid_generate_v4(), NULL, 'Mrs', 'Emma', 'Taylor', '1989-09-10', 'Female', '+33-6-12-34-56-89', 'emma.taylor@email.fr', 'guest_pass_9', 'FR90123456', '30 Boulevard Haussmann', '75009', 'Paris', 'France'),
(uuid_generate_v4(), NULL, 'Mr', 'Robert', 'Miller', '1976-05-03', 'Male', '+49-30-9876-5432', 'robert.miller@email.de', 'guest_pass_10', 'DE01234567', 'Unter den Linden 1', '10117', 'Berlin', 'Germany'),
(uuid_generate_v4(), NULL, 'Ms', 'Olivia', 'Moore', '1992-01-18', 'Female', '+39-06-1234-5678', 'olivia.moore@email.it', 'guest_pass_11', 'IT12345678', 'Via del Corso 100', '00186', 'Rome', 'Italy'),
(uuid_generate_v4(), NULL, 'Mr', 'Daniel', 'Jackson', '1984-08-25', 'Male', '+34-93-123-4567', 'daniel.jackson@email.es', 'guest_pass_12', 'ES23456789', 'Passeig de Gràcia 50', '08008', 'Barcelona', 'Spain'),
(uuid_generate_v4(), NULL, 'Dr', 'Sophia', 'White', '1979-12-30', 'Female', '+31-20-123-4567', 'sophia.white@email.nl', 'guest_pass_13', 'NL34567890', 'Damrak 1', '1012 LG', 'Amsterdam', 'Netherlands'),
(uuid_generate_v4(), NULL, 'Mr', 'Matthew', 'Harris', '1986-04-07', 'Male', '+43-1-234-5678', 'matthew.harris@email.at', 'guest_pass_14', 'AT45678901', 'Kärntner Ring 10', '1010', 'Vienna', 'Austria'),
(uuid_generate_v4(), NULL, 'Ms', 'Isabella', 'Clark', '1991-10-14', 'Female', '+46-8-123-4567', 'isabella.clark@email.se', 'guest_pass_15', 'SE56789012', 'Drottninggatan 50', '111 35', 'Stockholm', 'Sweden'),
(uuid_generate_v4(), NULL, 'Mr', 'Christopher', 'Lewis', '1981-06-21', 'Male', '+41-44-123-4567', 'christopher.lewis@email.ch', 'guest_pass_16', 'CH67890123', 'Bahnhofstrasse 20', '8001', 'Zurich', 'Switzerland'),
(uuid_generate_v4(), NULL, 'Mrs', 'Ava', 'Walker', '1988-03-08', 'Female', '+420-2-1234-5678', 'ava.walker@email.cz', 'guest_pass_17', 'CZ78901234', 'Wenceslas Square 1', '110 00', 'Prague', 'Czech Republic'),
(uuid_generate_v4(), NULL, 'Mr', 'Andrew', 'Hall', '1983-11-15', 'Male', '+30-21-1234-5678', 'andrew.hall@email.gr', 'guest_pass_18', 'GR89012345', 'Syntagma Square 1', '105 63', 'Athens', 'Greece'),
(uuid_generate_v4(), NULL, 'Ms', 'Mia', 'Allen', '1993-07-22', 'Female', '+351-21-123-4567', 'mia.allen@email.pt', 'guest_pass_19', 'PT90123456', 'Avenida da Liberdade 100', '1269-046', 'Lisbon', 'Portugal'),
(uuid_generate_v4(), NULL, 'Mr', 'Joseph', 'Young', '1977-02-28', 'Male', '+45-33-12-34-56', 'joseph.young@email.dk', 'guest_pass_20', 'DK01234567', 'Strøget 1', '1200', 'Copenhagen', 'Denmark'),
(uuid_generate_v4(), NULL, 'Dr', 'Charlotte', 'King', '1985-09-05', 'Female', '+47-22-12-34-56', 'charlotte.king@email.no', 'guest_pass_21', 'NO12345678', 'Karl Johans gate 20', '0162', 'Oslo', 'Norway'),
(uuid_generate_v4(), NULL, 'Mr', 'Benjamin', 'Wright', '1990-05-12', 'Male', '+358-9-123-4567', 'benjamin.wright@email.fi', 'guest_pass_22', 'FI23456789', 'Esplanadi 20', '00130', 'Helsinki', 'Finland'),
(uuid_generate_v4(), NULL, 'Ms', 'Amelia', 'Lopez', '1987-01-19', 'Female', '+48-22-123-4567', 'amelia.lopez@email.pl', 'guest_pass_23', 'PL34567890', 'Nowy Świat 50', '00-042', 'Warsaw', 'Poland'),
(uuid_generate_v4(), NULL, 'Mr', 'Samuel', 'Hill', '1982-08-26', 'Male', '+36-1-234-5678', 'samuel.hill@email.hu', 'guest_pass_24', 'HU45678901', 'Andrássy út 1', '1061', 'Budapest', 'Hungary'),
(uuid_generate_v4(), NULL, 'Mrs', 'Harper', 'Scott', '1989-04-02', 'Female', '+40-21-123-4567', 'harper.scott@email.ro', 'guest_pass_25', 'RO56789012', 'Calea Victoriei 50', '010065', 'Bucharest', 'Romania'),
(uuid_generate_v4(), NULL, 'Mr', 'Henry', 'Green', '1984-10-09', 'Male', '+359-2-123-4567', 'henry.green@email.bg', 'guest_pass_26', 'BG67890123', 'Vitosha Boulevard 50', '1000', 'Sofia', 'Bulgaria'),
(uuid_generate_v4(), NULL, 'Ms', 'Evelyn', 'Adams', '1991-06-16', 'Female', '+385-1-234-5678', 'evelyn.adams@email.hr', 'guest_pass_27', 'HR78901234', 'Ilica 1', '10000', 'Zagreb', 'Croatia'),
(uuid_generate_v4(), NULL, 'Mr', 'Alexander', 'Baker', '1978-12-23', 'Male', '+381-11-123-4567', 'alexander.baker@email.rs', 'guest_pass_28', 'RS89012345', 'Knez Mihailova 30', '11000', 'Belgrade', 'Serbia'),
(uuid_generate_v4(), NULL, 'Dr', 'Emily', 'Nelson', '1986-08-30', 'Female', '+90-212-123-4567', 'emily.nelson@email.tr', 'guest_pass_29', 'TR90123456', 'Istiklal Caddesi 100', '34430', 'Istanbul', 'Turkey'),
(uuid_generate_v4(), NULL, 'Mr', 'Daniel', 'Carter', '1983-03-06', 'Male', '+81-3-1234-5678', 'daniel.carter@email.jp', 'guest_pass_30', 'JP01234567', 'Shibuya 1-2-3', '150-0002', 'Tokyo', 'Japan');

-- Note: For Rooms, Employees, Bookings, and Bills, run the data-generator.sql script
-- which uses PostgreSQL DO blocks to generate data with proper relationships

-- Verification
SELECT 'Hotels' as table_name, COUNT(*) as record_count FROM hotel
UNION ALL
SELECT 'Room Types', COUNT(*) FROM room_type
UNION ALL
SELECT 'Roles', COUNT(*) FROM role
UNION ALL
SELECT 'Guests', COUNT(*) FROM guest;



package org.example.hotel_mangement.service;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.BookingDTO;
import org.example.hotel_mangement.model.request.BookingRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

public interface BookingService {
    PayloadResponse<BookingDTO> findAll(int page, int size, String search, String sortBy, String sortDir);
    BookingDTO findBookingById(UUID id);
    BookingDTO saveBooking(BookingRequest bookingRequest);
    BookingDTO updateBooking(UUID id, BookingRequest bookingRequest);
    BookingDTO deleteBooking(UUID id);
}


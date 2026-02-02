package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.BookingDTO;
import org.example.hotel_mangement.model.request.BookingRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

import java.util.UUID;

public interface BookingService {
    PayloadResponse<BookingDTO> findAll(int page, int size);
    BookingDTO findBookingById(UUID id);
    BookingDTO saveBooking(BookingRequest bookingRequest);
    BookingDTO updateBooking(UUID id, BookingRequest bookingRequest);
    BookingDTO deleteBooking(UUID id);
}


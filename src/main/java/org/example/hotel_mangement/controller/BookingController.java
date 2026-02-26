package org.example.hotel_mangement.controller;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.BookingDTO;
import org.example.hotel_mangement.model.request.BookingRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<BookingDTO>>> getBookings(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        ApiResponse<PayloadResponse<BookingDTO>> bookingsApiResponse = ApiResponse.<PayloadResponse<BookingDTO>>builder()
                .message("Get bookings")
                .status(HttpStatus.OK)
                .payload(bookingService.findAll(page, size, search, sortBy, sortDir))
                .build();
        return ResponseEntity.ok(bookingsApiResponse);
    }

    @GetMapping("{booking-id}")
    public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(@PathVariable("booking-id") UUID id) {
        ApiResponse<BookingDTO> bookingDtoApiResponse = ApiResponse.<BookingDTO>builder()
                .message("Get booking by id")
                .status(HttpStatus.OK)
                .payload(bookingService.findBookingById(id))
                .build();
        return ResponseEntity.ok(bookingDtoApiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@RequestBody BookingRequest bookingRequest) {
        ApiResponse<BookingDTO> bookingDtoApiResponse = ApiResponse.<BookingDTO>builder()
                .message("Add booking success.")
                .status(HttpStatus.CREATED)
                .payload(bookingService.saveBooking(bookingRequest))
                .build();
        return ResponseEntity.ok(bookingDtoApiResponse);
    }

    @PutMapping("{booking-id}")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(@PathVariable("booking-id") UUID id, @RequestBody BookingRequest bookingRequest) {
        ApiResponse<BookingDTO> bookingDtoApiResponse = ApiResponse.<BookingDTO>builder()
                .message("Update booking success.")
                .status(HttpStatus.OK)
                .payload(bookingService.updateBooking(id, bookingRequest))
                .build();
        return ResponseEntity.ok(bookingDtoApiResponse);
    }

    @DeleteMapping("{booking-id}")
    public ResponseEntity<ApiResponse<BookingDTO>> deleteBooking(@PathVariable("booking-id") UUID id) {
        ApiResponse<BookingDTO> bookingDtoApiResponse = ApiResponse.<BookingDTO>builder()
                .message("Deleted booking success.")
                .status(HttpStatus.OK)
                .payload(bookingService.deleteBooking(id))
                .build();
        return ResponseEntity.ok(bookingDtoApiResponse);
    }
}


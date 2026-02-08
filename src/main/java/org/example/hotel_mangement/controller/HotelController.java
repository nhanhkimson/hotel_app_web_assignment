package org.example.hotel_mangement.controller;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.HotelDTO;
import org.example.hotel_mangement.model.request.HotelRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.HotelService;
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
@RequestMapping("api/v1/hotel")
public class HotelController {
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<HotelDTO>>> getHotels(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "hotelName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        ApiResponse<PayloadResponse<HotelDTO>> hotelsApiResponse = ApiResponse.<PayloadResponse<HotelDTO>>builder()
                .message("Get hotels")
                .status(HttpStatus.OK)
                .payload(hotelService.findAll(page, size, search, sortBy, sortDir))
                .build();
        return ResponseEntity.ok(hotelsApiResponse);
    }

    @GetMapping("{hotel-id}")
    public ResponseEntity<ApiResponse<HotelDTO>> getHotelById(@PathVariable("hotel-id") UUID id) {
        ApiResponse<HotelDTO> hotelDtoApiResponse = ApiResponse.<HotelDTO>builder()
                .message("Get hotel by id")
                .status(HttpStatus.OK)
                .payload(hotelService.findHotelById(id))
                .build();
        return ResponseEntity.ok(hotelDtoApiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HotelDTO>> createHotel(@RequestBody HotelRequest hotelRequest) {
        ApiResponse<HotelDTO> hotelDtoApiResponse = ApiResponse.<HotelDTO>builder()
                .message("Add hotel success.")
                .status(HttpStatus.CREATED)
                .payload(hotelService.saveHotel(hotelRequest))
                .build();
        return ResponseEntity.ok(hotelDtoApiResponse);
    }

    @PutMapping("{hotel-id}")
    public ResponseEntity<ApiResponse<HotelDTO>> updateHotel(@PathVariable("hotel-id") UUID id, @RequestBody HotelRequest hotelRequest) {
        ApiResponse<HotelDTO> hotelDtoApiResponse = ApiResponse.<HotelDTO>builder()
                .message("Update hotel success.")
                .status(HttpStatus.OK)
                .payload(hotelService.updateHotel(id, hotelRequest))
                .build();
        return ResponseEntity.ok(hotelDtoApiResponse);
    }

    @DeleteMapping("{hotel-id}")
    public ResponseEntity<ApiResponse<HotelDTO>> deleteHotel(@PathVariable("hotel-id") UUID id) {
        ApiResponse<HotelDTO> hotelDtoApiResponse = ApiResponse.<HotelDTO>builder()
                .message("Deleted hotel success.")
                .status(HttpStatus.OK)
                .payload(hotelService.deleteHotel(id))
                .build();
        return ResponseEntity.ok(hotelDtoApiResponse);
    }
}


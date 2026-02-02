package org.example.hotel_mangement.controller;

import java.util.List;
import java.util.UUID;

import org.example.hotel_mangement.model.dto.GuestDTO;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/guest")
class GuestController {
    private final GuestService guestService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuestDTO>>> getAllGuest() {
        ApiResponse<List<GuestDTO>> apiResponse = ApiResponse.<List<GuestDTO>>builder()
                .message("Get all guest success.")
                .payload(guestService.getAllGuest())
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{guest-id}")
    public ResponseEntity<ApiResponse<GuestDTO>> getGuestById(@PathVariable("guest-id") UUID id) {
        ApiResponse<GuestDTO> guestApiResponse = ApiResponse.<GuestDTO>builder()
                .message("Get guest by id success.")
                .payload(guestService.getGuestById(id))
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(guestApiResponse);
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PayloadResponse<GuestDTO>>> createGuest(@RequestBody GuestDTO guestDTO) {
        ApiResponse<PayloadResponse<GuestDTO>> guestApiResponse = ApiResponse.<PayloadResponse<GuestDTO>>builder()
                .message("Create guest success.")
                .status(HttpStatus.CREATED)
                .payload(null)
                .build();
        return ResponseEntity.ok(guestApiResponse);
    }
}

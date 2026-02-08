package org.example.hotel_mangement.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.model.dto.RoomTypeDto;
import org.example.hotel_mangement.model.request.RoomTypeRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<RoomTypeDto>>> getRoomTypes(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "roomType") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
            ) {
        ApiResponse<PayloadResponse<RoomTypeDto>> roomTypesApiResponse = ApiResponse.<PayloadResponse<RoomTypeDto>>builder()
                .message("Get room types")
                .status(HttpStatus.OK)
                .payload(roomTypeService.findAll(page, size, search, sortBy, sortDir))
                .build();
        return ResponseEntity.ok(roomTypesApiResponse);
    }

    @GetMapping("{roomType-id}")
    public ResponseEntity<ApiResponse<RoomTypeDto>> getRoomTypes(@PathVariable("roomType-id") UUID id) {
        ApiResponse<RoomTypeDto> roomTypeDtoApiResponse = ApiResponse.<RoomTypeDto>builder()
                .message("Get room types")
                .status(HttpStatus.OK)
                .payload(roomTypeService.findRoomTypeById(id))
                .build();
        return ResponseEntity.ok(roomTypeDtoApiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomTypeDto>> createRoomType(@RequestBody RoomTypeRequest roomTypeRequest) {
        ApiResponse<RoomTypeDto> roomTypeDtoApiResponse = ApiResponse.<RoomTypeDto>builder()
                .message("Add room type success.")
                .status(HttpStatus.CREATED)
                .payload(roomTypeService.saveRoomType(roomTypeRequest))
                .build();
        return ResponseEntity.ok(roomTypeDtoApiResponse);
    }

    @PutMapping("{roomType-id}")
    public ResponseEntity<ApiResponse<RoomTypeDto>> updateRoomType(@PathVariable("roomType-id") UUID id ,@RequestBody RoomTypeRequest roomTypeRequest) {
        ApiResponse<RoomTypeDto> roomTypeDtoApiResponse = ApiResponse.<RoomTypeDto>builder()
                .message("Update room type success.")
                .status(HttpStatus.OK)
                .payload(roomTypeService.updateRoomType(id ,roomTypeRequest))
                .build();
        return ResponseEntity.ok(roomTypeDtoApiResponse);
    }

    @DeleteMapping("{roomType-id}")
    public ResponseEntity<ApiResponse<RoomTypeDto>> deleteRoomType(@PathVariable("roomType-id") UUID id) {
        ApiResponse<RoomTypeDto> roomTypeDtoApiResponse = ApiResponse.<RoomTypeDto>builder()
                .message("Deleted room type success.")
                .status(HttpStatus.OK)
                .payload(roomTypeService.deleteRoomType(id))
                .build();
        return ResponseEntity.ok(roomTypeDtoApiResponse);
    }

}

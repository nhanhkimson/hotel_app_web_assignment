package org.example.hotel_mangement.controller;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.RoomDTO;
import org.example.hotel_mangement.model.request.RoomRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.RoomService;
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
@RequestMapping("api/v1/room")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<RoomDTO>>> getRooms(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        ApiResponse<PayloadResponse<RoomDTO>> roomsApiResponse = ApiResponse.<PayloadResponse<RoomDTO>>builder()
                .message("Get rooms")
                .status(HttpStatus.OK)
                .payload(roomService.findAll(page, size, search, sortBy, sortDir))
                .build();
        return ResponseEntity.ok(roomsApiResponse);
    }

    @GetMapping("{room-id}")
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomById(@PathVariable("room-id") UUID id) {
        ApiResponse<RoomDTO> roomDtoApiResponse = ApiResponse.<RoomDTO>builder()
                .message("Get room by id")
                .status(HttpStatus.OK)
                .payload(roomService.findRoomById(id))
                .build();
        return ResponseEntity.ok(roomDtoApiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomDTO>> createRoom(@RequestBody RoomRequest roomRequest) {
        ApiResponse<RoomDTO> roomDtoApiResponse = ApiResponse.<RoomDTO>builder()
                .message("Add room success.")
                .status(HttpStatus.CREATED)
                .payload(roomService.saveRoom(roomRequest))
                .build();
        return ResponseEntity.ok(roomDtoApiResponse);
    }

    @PutMapping("{room-id}")
    public ResponseEntity<ApiResponse<RoomDTO>> updateRoom(@PathVariable("room-id") UUID id, @RequestBody RoomRequest roomRequest) {
        ApiResponse<RoomDTO> roomDtoApiResponse = ApiResponse.<RoomDTO>builder()
                .message("Update room success.")
                .status(HttpStatus.OK)
                .payload(roomService.updateRoom(id, roomRequest))
                .build();
        return ResponseEntity.ok(roomDtoApiResponse);
    }

    @DeleteMapping("{room-id}")
    public ResponseEntity<ApiResponse<RoomDTO>> deleteRoom(@PathVariable("room-id") UUID id) {
        ApiResponse<RoomDTO> roomDtoApiResponse = ApiResponse.<RoomDTO>builder()
                .message("Deleted room success.")
                .status(HttpStatus.OK)
                .payload(roomService.deleteRoom(id))
                .build();
        return ResponseEntity.ok(roomDtoApiResponse);
    }
}
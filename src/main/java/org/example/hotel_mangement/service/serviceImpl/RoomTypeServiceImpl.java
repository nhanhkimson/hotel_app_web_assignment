package org.example.hotel_mangement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.exception.NotFoundException;
import org.example.hotel_mangement.model.dto.RoomTypeDto;
import org.example.hotel_mangement.model.entity.RoomType;
import org.example.hotel_mangement.model.request.RoomTypeRequest;
import org.example.hotel_mangement.model.response.PaginationResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.RoomTypeRepository;
import org.example.hotel_mangement.service.RoomTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    @Override
    public PayloadResponse<RoomTypeDto> findAll(int page, int size, String search, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        
        Page<RoomType> roomTypes;
        if (search != null && !search.trim().isEmpty()) {
            roomTypes = roomTypeRepository.searchRoomTypes(search.trim(), pageable);
        } else {
            roomTypes = roomTypeRepository.findAll(pageable);
        }

        List<RoomTypeDto> roomTypeDto = new ArrayList<>();
        if (!roomTypes.isEmpty()) {
            for (RoomType roomType : roomTypes) {
                roomTypeDto.add(toDTO(roomType));
            }
        }

        return PayloadResponse.<RoomTypeDto>builder()
                .items(roomTypeDto)
                .pagination(PaginationResponse.fromPage(roomTypes, page, size))
                .build();
    }
    
    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String fieldName = mapSortField(sortBy);
        return Sort.by(direction, fieldName);
    }
    
    private String mapSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) return "createdAt";
        switch (sortBy) {
            case "createdAt": return "createdAt";
            case "roomType": return "roomType";
            case "roomPrice": return "roomPrice";
            case "roomDesc": return "roomDesc";
            default: return "createdAt";
        }
    }

    @Override
    public RoomTypeDto findRoomTypeById(UUID id) {
        RoomType roomType = getRoomTypeById(id);
        return toDTO(roomType);
    }

    public RoomType getRoomTypeById(UUID id) {
        return roomTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Room type not found."));
    }

    @Override
    public RoomTypeDto saveRoomType(RoomTypeRequest roomTypeRequest) {
        RoomType roomType = new RoomType();
        return toDTO(saveOrUpdateRoomType(roomType, roomTypeRequest));
    }
    @Override
    public RoomTypeDto updateRoomType(UUID id, RoomTypeRequest roomTypeRequest) {
        RoomType roomType = getRoomTypeById(id);
        return toDTO(saveOrUpdateRoomType(roomType, roomTypeRequest));
    }

    @Override
    public RoomTypeDto deleteRoomType(UUID id) {
        RoomType roomType = getRoomTypeById(id);
        roomTypeRepository.delete(roomType);
        return toDTO(roomType);
    }

    private RoomType saveOrUpdateRoomType(RoomType roomType ,RoomTypeRequest roomTypeRequest) {
        roomType.setRoomType(roomTypeRequest.getRoomType());
        roomType.setDefaultRoomPrice(roomTypeRequest.getDefaultRoomPrice());
        roomType.setRoomDesc(roomTypeRequest.getRoomDesc());
        roomType.setRoomType(roomTypeRequest.getRoomType());
        roomType.setRoomImg(roomTypeRequest.getRoomImg());
        roomType.setRoomPrice(roomTypeRequest.getRoomPrice());
        return roomTypeRepository.save(roomType);
    }

    private RoomTypeDto toDTO(RoomType roomType) {
        return RoomTypeDto.builder()
                .roomTypeId(roomType.getRoomTypeId())
                .defaultRoomPrice(roomType.getDefaultRoomPrice())
                .roomDesc(roomType.getRoomDesc())
                .roomType(roomType.getRoomType())
                .roomImg(roomType.getRoomImg())
                .roomPrice(roomType.getRoomPrice())
                .roomTypeId(roomType.getRoomTypeId())
                .build();
    }
}

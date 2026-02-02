package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.HotelDTO;
import org.example.hotel_mangement.model.request.HotelRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

import java.util.UUID;

public interface HotelService {
    PayloadResponse<HotelDTO> findAll(int page, int size);
    HotelDTO findHotelById(UUID id);
    HotelDTO saveHotel(HotelRequest hotelRequest);
    HotelDTO updateHotel(UUID id, HotelRequest hotelRequest);
    HotelDTO deleteHotel(UUID id);
}


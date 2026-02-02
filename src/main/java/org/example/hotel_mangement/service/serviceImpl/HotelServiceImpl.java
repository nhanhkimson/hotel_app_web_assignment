package org.example.hotel_mangement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.exception.NotFoundException;
import org.example.hotel_mangement.model.dto.HotelDTO;
import org.example.hotel_mangement.model.entity.Hotel;
import org.example.hotel_mangement.model.request.HotelRequest;
import org.example.hotel_mangement.model.response.PaginationResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.HotelRepository;
import org.example.hotel_mangement.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    public PayloadResponse<HotelDTO> findAll(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Hotel> hotels = hotelRepository.findAll(pageable);

        List<HotelDTO> hotelDTOs = new ArrayList<>();
        if (hotels.isEmpty()) {
            throw new NotFoundException("Hotel not enough with page " + page + ".");
        }
        for (Hotel hotel : hotels) {
            hotelDTOs.add(toDTO(hotel));
        }

        return PayloadResponse.<HotelDTO>builder()
                .items(hotelDTOs)
                .pagination(PaginationResponse.fromPage(hotels, page, size))
                .build();
    }

    @Override
    public HotelDTO findHotelById(UUID id) {
        Hotel hotel = getHotelById(id);
        return toDTO(hotel);
    }

    public Hotel getHotelById(UUID id) {
        return hotelRepository.findById(id).orElseThrow(() -> new NotFoundException("Hotel not found."));
    }

    @Override
    public HotelDTO saveHotel(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        return toDTO(saveOrUpdateHotel(hotel, hotelRequest));
    }

    @Override
    public HotelDTO updateHotel(UUID id, HotelRequest hotelRequest) {
        Hotel hotel = getHotelById(id);
        return toDTO(saveOrUpdateHotel(hotel, hotelRequest));
    }

    @Override
    public HotelDTO deleteHotel(UUID id) {
        Hotel hotel = getHotelById(id);
        hotelRepository.delete(hotel);
        return toDTO(hotel);
    }

    private Hotel saveOrUpdateHotel(Hotel hotel, HotelRequest hotelRequest) {
        hotel.setHotelName(hotelRequest.getHotelName());
        hotel.setAddress(hotelRequest.getAddress());
        hotel.setPostcode(hotelRequest.getPostcode());
        hotel.setCity(hotelRequest.getCity());
        hotel.setCountry(hotelRequest.getCountry());
        hotel.setNumRooms(hotelRequest.getNumRooms());
        hotel.setPhoneNo(hotelRequest.getPhoneNo());
        hotel.setStarRating(hotelRequest.getStarRating());
        return hotelRepository.save(hotel);
    }

    private HotelDTO toDTO(Hotel hotel) {
        return HotelDTO.builder()
                .hotelCode(hotel.getHotelCode())
                .hotelName(hotel.getHotelName())
                .address(hotel.getAddress())
                .postcode(hotel.getPostcode())
                .city(hotel.getCity())
                .country(hotel.getCountry())
                .numRooms(hotel.getNumRooms())
                .phoneNo(hotel.getPhoneNo())
                .starRating(hotel.getStarRating())
                .build();
    }
}


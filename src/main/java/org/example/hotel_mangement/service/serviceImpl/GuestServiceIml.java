package org.example.hotel_mangement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.model.dto.GuestDTO;
import org.example.hotel_mangement.model.entity.Guest;
import org.example.hotel_mangement.repository.GuestRepository;
import org.example.hotel_mangement.service.GuestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestServiceIml implements GuestService {
    private final GuestRepository guestRepository;
    
    @Override
    public List<GuestDTO> getAllGuest() {
        List<GuestDTO> guestDTOs = new ArrayList<>();
        List<Guest> guests = guestRepository.findAllByOrderByCreatedAtDesc();
        for (Guest guest : guests) {
            guestDTOs.add(guestToDTO(guest));
        }
        return guestDTOs;
    }

    @Override
    public GuestDTO getGuestById(UUID id) {
        Guest guest = guestRepository.findById(id).orElse(null);
        return guest != null ? guestToDTO(guest) : null;
    }
    
    private GuestDTO guestToDTO(Guest guest) {
        return GuestDTO.builder()
                .guestId(guest.getGuestId())
                .bookingId(guest.getBooking() != null ? guest.getBooking().getBookingId() : null)
                .guestTitle(guest.getGuestTitle())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .dob(guest.getDob())
                .gender(guest.getGender())
                .phoneNo(guest.getPhoneNo())
                .email(guest.getEmail())
                .passportNo(guest.getPassportNo())
                .address(guest.getAddress())
                .postcode(guest.getPostcode())
                .city(guest.getCity())
                .country(guest.getCountry())
                .build();
    }
}

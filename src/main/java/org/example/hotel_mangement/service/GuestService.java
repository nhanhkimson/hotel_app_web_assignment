package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.GuestDTO;

import java.util.List;
import java.util.UUID;

public interface GuestService {
    List<GuestDTO> getAllGuest();

    GuestDTO getGuestById(UUID id);
}

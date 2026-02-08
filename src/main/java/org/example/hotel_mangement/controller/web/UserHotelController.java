package org.example.hotel_mangement.controller.web;

import java.util.List;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Hotel;
import org.example.hotel_mangement.model.entity.HotelImage;
import org.example.hotel_mangement.model.entity.Room;
import org.example.hotel_mangement.repository.HotelImageRepository;
import org.example.hotel_mangement.repository.HotelRepository;
import org.example.hotel_mangement.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user/hotels")
@RequiredArgsConstructor
public class UserHotelController {
    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;
    private final RoomRepository roomRepository;

    @GetMapping
    public String listHotels(
            @RequestParam(required = false) String search,
            Model model
    ) {
        List<Hotel> hotels;
        if (search != null && !search.trim().isEmpty()) {
            hotels = hotelRepository.findAll().stream()
                    .filter(hotel -> 
                        hotel.getHotelName().toLowerCase().contains(search.toLowerCase()) ||
                        (hotel.getCity() != null && hotel.getCity().toLowerCase().contains(search.toLowerCase())) ||
                        (hotel.getCountry() != null && hotel.getCountry().toLowerCase().contains(search.toLowerCase()))
                    )
                    .toList();
        } else {
            hotels = hotelRepository.findAll();
        }
        
        // Get images for all hotels
        java.util.Map<UUID, List<HotelImage>> hotelImagesMap = new java.util.HashMap<>();
        for (Hotel hotel : hotels) {
            List<HotelImage> images = hotelImageRepository.findByHotel_HotelCode(hotel.getHotelCode());
            hotelImagesMap.put(hotel.getHotelCode(), images);
        }
        
        model.addAttribute("hotels", hotels);
        model.addAttribute("hotelImagesMap", hotelImagesMap);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("pageTitle", "Browse Hotels - Hotel Management");
        return "user/hotels/list";
    }

    @GetMapping("/{id}")
    public String showHotelDetail(@PathVariable("id") UUID id, Model model) {
        try {
            Hotel hotel = hotelRepository.findById(id)
                    .orElse(null);
            
            if (hotel == null) {
                return "redirect:/user/hotels?error=notfound";
            }
            
            List<HotelImage> images = hotelImageRepository.findByHotel_HotelCode(id);
            List<Room> rooms = roomRepository.findByHotel_HotelCode(id);
            
            model.addAttribute("hotel", hotel);
            model.addAttribute("images", images != null ? images : java.util.Collections.emptyList());
            model.addAttribute("rooms", rooms != null ? rooms : java.util.Collections.emptyList());
            model.addAttribute("pageTitle", (hotel.getHotelName() != null ? hotel.getHotelName() : "Hotel") + " - Hotel Details");
            return "user/hotels/detail";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/hotels?error=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}


package org.example.hotel_mangement.controller.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.example.hotel_mangement.model.dto.RoomDTO;
import org.example.hotel_mangement.model.request.RoomRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.HotelService;
import org.example.hotel_mangement.service.RoomService;
import org.example.hotel_mangement.service.RoomTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/rooms")
@RequiredArgsConstructor
public class RoomWebController {
    private final RoomService roomService;
    private final HotelService hotelService;
    private final RoomTypeService roomTypeService;

    @GetMapping
    public String listRooms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        // If search is provided and not empty, always start from page 1
        int actualPage = (search != null && !search.trim().isEmpty()) ? 1 : page;
        
        PayloadResponse<RoomDTO> response = roomService.findAll(actualPage, size, search, sortBy, sortDir);
        model.addAttribute("rooms", response.getItems());
        model.addAttribute("pagination", response.getPagination());
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("activePage", "rooms");
        model.addAttribute("pageTitle", "Rooms - Hotel Management");
        
        // If search was provided and page was not 1, redirect to page 1 with search
        if (search != null && !search.trim().isEmpty() && page != 1) {
            return "redirect:/web/rooms?page=1&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8) 
                   + "&sortBy=" + sortBy + "&sortDir=" + sortDir;
        }
        
        return "web/rooms/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new RoomRequest());
        model.addAttribute("hotels", hotelService.findAll(1, 100, null, "hotelName", "asc").getItems());
        model.addAttribute("roomTypes", roomTypeService.findAll(1, 100, null, "roomType", "asc").getItems());
        model.addAttribute("activePage", "rooms");
        model.addAttribute("pageTitle", "Add Room - Hotel Management");
        return "web/rooms/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") UUID id, Model model) {
        RoomDTO room = roomService.findRoomById(id);
        RoomRequest request = new RoomRequest();
        request.setRoomNo(room.getRoomNo());
        request.setHotelCode(room.getHotelCode());
        request.setRoomTypeId(room.getRoomTypeId());
        request.setOccupancy(room.getOccupancy());
        model.addAttribute("room", request);
        model.addAttribute("roomId", id);
        model.addAttribute("hotels", hotelService.findAll(1, 100, null, "hotelName", "asc").getItems());
        model.addAttribute("roomTypes", roomTypeService.findAll(1, 100, null, "roomType", "asc").getItems());
        model.addAttribute("activePage", "rooms");
        model.addAttribute("pageTitle", "Edit Room - Hotel Management");
        return "web/rooms/form";
    }

    @PostMapping
    public String createRoom(@ModelAttribute RoomRequest roomRequest, RedirectAttributes redirectAttributes) {
        try {
            roomService.saveRoom(roomRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating room: " + e.getMessage());
        }
        return "redirect:/web/rooms?page=1&sortBy=roomNo&sortDir=asc";
    }

    @PostMapping("/{id}")
    public String updateRoom(
            @PathVariable("id") UUID id,
            @ModelAttribute RoomRequest roomRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            roomService.updateRoom(id, roomRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room: " + e.getMessage());
        }
        return "redirect:/web/rooms?page=1&sortBy=roomNo&sortDir=asc";
    }

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting room: " + e.getMessage());
        }
        return "redirect:/web/rooms?page=1&sortBy=roomNo&sortDir=asc";
    }
}

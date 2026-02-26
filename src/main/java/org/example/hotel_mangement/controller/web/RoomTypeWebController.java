package org.example.hotel_mangement.controller.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.example.hotel_mangement.model.dto.RoomTypeDto;
import org.example.hotel_mangement.model.request.RoomTypeRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;
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
@RequestMapping("/web/room-types")
@RequiredArgsConstructor
public class RoomTypeWebController {
    private final RoomTypeService roomTypeService;

    @GetMapping
    public String listRoomTypes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        // If search is provided and not empty, always start from page 1
        int actualPage = (search != null && !search.trim().isEmpty()) ? 1 : page;
        
        PayloadResponse<RoomTypeDto> response = roomTypeService.findAll(actualPage, size, search, sortBy, sortDir);
        model.addAttribute("roomTypes", response.getItems());
        model.addAttribute("pagination", response.getPagination());
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("activePage", "room-types");
        model.addAttribute("pageTitle", "Room Types - Hotel Management");
        
        // If search was provided and page was not 1, redirect to page 1 with search
        if (search != null && !search.trim().isEmpty() && page != 1) {
            return "redirect:/web/room-types?page=1&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8) 
                   + "&sortBy=" + sortBy + "&sortDir=" + sortDir;
        }
        
        return "web/room-types/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("roomType", new RoomTypeRequest());
        model.addAttribute("activePage", "room-types");
        model.addAttribute("pageTitle", "Add Room Type - Hotel Management");
        return "web/room-types/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") UUID id, Model model) {
        RoomTypeDto roomType = roomTypeService.findRoomTypeById(id);
        RoomTypeRequest request = new RoomTypeRequest();
        request.setRoomType(roomType.getRoomType());
        request.setRoomPrice(roomType.getRoomPrice());
        request.setDefaultRoomPrice(roomType.getDefaultRoomPrice());
        request.setRoomImg(roomType.getRoomImg());
        request.setRoomDesc(roomType.getRoomDesc());
        model.addAttribute("roomType", request);
        model.addAttribute("roomTypeId", id);
        model.addAttribute("activePage", "room-types");
        model.addAttribute("pageTitle", "Edit Room Type - Hotel Management");
        return "web/room-types/form";
    }

    @PostMapping
    public String createRoomType(@ModelAttribute RoomTypeRequest roomTypeRequest, RedirectAttributes redirectAttributes) {
        try {
            roomTypeService.saveRoomType(roomTypeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room type created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating room type: " + e.getMessage());
        }
        return "redirect:/web/room-types?page=1&sortBy=roomType&sortDir=asc";
    }

    @PostMapping("/{id}")
    public String updateRoomType(
            @PathVariable("id") UUID id,
            @ModelAttribute RoomTypeRequest roomTypeRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            roomTypeService.updateRoomType(id, roomTypeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Room type updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room type: " + e.getMessage());
        }
        return "redirect:/web/room-types?page=1&sortBy=roomType&sortDir=asc";
    }

    @PostMapping("/{id}/delete")
    public String deleteRoomType(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            roomTypeService.deleteRoomType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room type deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting room type: " + e.getMessage());
        }
        return "redirect:/web/room-types?page=1&sortBy=roomType&sortDir=asc";
    }
}

package org.example.hotel_mangement.controller.web;

import org.example.hotel_mangement.service.GuestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/guests")
@RequiredArgsConstructor
public class GuestWebController {
    private final GuestService guestService;

    @GetMapping
    public String listGuests(Model model) {
        model.addAttribute("guests", guestService.getAllGuest());
        model.addAttribute("activePage", "guests");
        model.addAttribute("pageTitle", "Guests - Hotel Management");
        return "web/guests/list";
    }
}

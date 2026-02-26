package org.example.hotel_mangement.controller.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.example.hotel_mangement.model.dto.BillDTO;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.BillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/bills")
@RequiredArgsConstructor
public class BillWebController {
    private final BillService billService;

    @GetMapping
    public String listBills(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        // If search is provided and not empty, always start from page 1
        int actualPage = (search != null && !search.trim().isEmpty()) ? 1 : page;
        
        PayloadResponse<BillDTO> response = billService.findAll(actualPage, size, search, sortBy, sortDir);
        model.addAttribute("bills", response.getItems());
        model.addAttribute("pagination", response.getPagination());
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("activePage", "bills");
        model.addAttribute("pageTitle", "Bills - Hotel Management");
        
        // If search was provided and page was not 1, redirect to page 1 with search
        if (search != null && !search.trim().isEmpty() && page != 1) {
            return "redirect:/web/bills?page=1&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8) 
                   + "&sortBy=" + sortBy + "&sortDir=" + sortDir;
        }
        
        return "web/bills/list";
    }
}


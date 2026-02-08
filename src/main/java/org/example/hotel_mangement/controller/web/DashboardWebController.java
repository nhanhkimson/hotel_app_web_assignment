package org.example.hotel_mangement.controller.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.example.hotel_mangement.model.dto.BillDTO;
import org.example.hotel_mangement.model.dto.BookingDTO;
import org.example.hotel_mangement.service.BillService;
import org.example.hotel_mangement.service.BookingService;
import org.example.hotel_mangement.service.HotelService;
import org.example.hotel_mangement.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class DashboardWebController {
    private final HotelService hotelService;
    private final RoomService roomService;
    private final BookingService bookingService;
    private final BillService billService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        try {
            // Get counts for dashboard cards with error handling
            long hotelsCount = 0;
            long roomsCount = 0;
            long bookingsCount = 0;
            long billsCount = 0;
            
            try {
                var hotelsResponse = hotelService.findAll(1, 1, null, "hotelName", "asc");
                hotelsCount = hotelsResponse != null && hotelsResponse.getPagination() != null 
                    ? hotelsResponse.getPagination().getTotalElements() : 0;
            } catch (Exception e) {
                hotelsCount = 0;
            }
            
            try {
                var roomsResponse = roomService.findAll(1, 1, null, "roomNo", "asc");
                roomsCount = roomsResponse != null && roomsResponse.getPagination() != null 
                    ? roomsResponse.getPagination().getTotalElements() : 0;
            } catch (Exception e) {
                roomsCount = 0;
            }
            
            try {
                var bookingsResponse = bookingService.findAll(1, 1, null, "bookingDate", "desc");
                bookingsCount = bookingsResponse != null && bookingsResponse.getPagination() != null 
                    ? bookingsResponse.getPagination().getTotalElements() : 0;
            } catch (Exception e) {
                bookingsCount = 0;
            }
            
            try {
                var billsResponse = billService.findAll(1, 1, null, "paymentDate", "desc");
                billsCount = billsResponse != null && billsResponse.getPagination() != null 
                    ? billsResponse.getPagination().getTotalElements() : 0;
            } catch (Exception e) {
                billsCount = 0;
            }

            // Get recent data with error handling
            List<BookingDTO> recentBookings = Collections.emptyList();
            List<BillDTO> recentBills = Collections.emptyList();
            
            try {
                var bookingsResponse = bookingService.findAll(1, 5, null, "bookingDate", "desc");
                recentBookings = bookingsResponse != null && bookingsResponse.getItems() != null 
                    ? bookingsResponse.getItems() : Collections.emptyList();
            } catch (Exception e) {
                recentBookings = Collections.emptyList();
            }
            
            try {
                var billsResponse = billService.findAll(1, 5, null, "paymentDate", "desc");
                recentBills = billsResponse != null && billsResponse.getItems() != null 
                    ? billsResponse.getItems() : Collections.emptyList();
            } catch (Exception e) {
                recentBills = Collections.emptyList();
            }
            
            // Current date
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));

            model.addAttribute("hotelsCount", hotelsCount);
            model.addAttribute("roomsCount", roomsCount);
            model.addAttribute("bookingsCount", bookingsCount);
            model.addAttribute("billsCount", billsCount);
            model.addAttribute("recentBookings", recentBookings);
            model.addAttribute("recentBills", recentBills);
            model.addAttribute("currentDate", currentDate);
            model.addAttribute("activePage", "dashboard");
            model.addAttribute("pageTitle", "Dashboard - Hotel Management");
            return "web/dashboard";
        } catch (Exception e) {
            // Fallback values if anything goes wrong
            model.addAttribute("hotelsCount", 0);
            model.addAttribute("roomsCount", 0);
            model.addAttribute("bookingsCount", 0);
            model.addAttribute("billsCount", 0);
            model.addAttribute("recentBookings", Collections.<BookingDTO>emptyList());
            model.addAttribute("recentBills", Collections.<BillDTO>emptyList());
            model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
            model.addAttribute("activePage", "dashboard");
            model.addAttribute("pageTitle", "Dashboard - Hotel Management");
            return "web/dashboard";
        }
    }
}


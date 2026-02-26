package org.example.hotel_mangement.controller.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.example.hotel_mangement.model.dto.HotelDTO;
import org.example.hotel_mangement.model.entity.Hotel;
import org.example.hotel_mangement.model.entity.HotelImage;
import org.example.hotel_mangement.model.request.HotelRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.HotelImageRepository;
import org.example.hotel_mangement.repository.HotelRepository;
import org.example.hotel_mangement.service.HotelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/hotels")
@RequiredArgsConstructor
public class HotelWebController {
    private final HotelService hotelService;
    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @GetMapping
    public String listHotels(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        // If search is provided and not empty, always start from page 1
        // This ensures search results always show from the beginning
        int actualPage = (search != null && !search.trim().isEmpty()) ? 1 : page;
        
        PayloadResponse<HotelDTO> response = hotelService.findAll(actualPage, size, search, sortBy, sortDir);
        model.addAttribute("hotels", response.getItems());
        model.addAttribute("pagination", response.getPagination());
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "Hotels - Hotel Management");
        
        // If search was provided and page was not 1, redirect to page 1 with search
        if (search != null && !search.trim().isEmpty() && page != 1) {
            return "redirect:/web/hotels?page=1&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8) 
                   + "&sortBy=" + sortBy + "&sortDir=" + sortDir;
        }
        
        return "web/hotels/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new HotelRequest());
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "Add Hotel - Hotel Management");
        return "web/hotels/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") UUID id, Model model) {
        HotelDTO hotel = hotelService.findHotelById(id);
        HotelRequest request = new HotelRequest();
        request.setHotelName(hotel.getHotelName());
        request.setAddress(hotel.getAddress());
        request.setPostcode(hotel.getPostcode());
        request.setCity(hotel.getCity());
        request.setCountry(hotel.getCountry());
        request.setNumRooms(hotel.getNumRooms());
        request.setPhoneNo(hotel.getPhoneNo());
        request.setStarRating(hotel.getStarRating());
        
        // Get existing images
        List<HotelImage> images = hotelImageRepository.findByHotel_HotelCode(id);
        model.addAttribute("hotel", request);
        model.addAttribute("hotelId", id);
        model.addAttribute("images", images);
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "Edit Hotel - Hotel Management");
        return "web/hotels/form";
    }

    @PostMapping
    public String createHotel(
            @ModelAttribute HotelRequest hotelRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            RedirectAttributes redirectAttributes
    ) {
        try {
            HotelDTO savedHotel = hotelService.saveHotel(hotelRequest);
            
            // Handle image uploads
            if (images != null && images.length > 0) {
                Hotel hotel = hotelRepository.findById(savedHotel.getHotelCode())
                        .orElseThrow(() -> new RuntimeException("Hotel not found"));
                saveHotelImages(hotel, images);
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Hotel created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating hotel: " + e.getMessage());
        }
        return "redirect:/web/hotels?page=1&sortBy=createdAt&sortDir=desc";
    }

    @PostMapping("/{id}")
    public String updateHotel(
            @PathVariable("id") UUID id,
            @ModelAttribute HotelRequest hotelRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            RedirectAttributes redirectAttributes
    ) {
        try {
            hotelService.updateHotel(id, hotelRequest);
            
            // Handle image uploads
            if (images != null && images.length > 0) {
                Hotel hotel = hotelRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Hotel not found"));
                saveHotelImages(hotel, images);
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Hotel updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating hotel: " + e.getMessage());
        }
        return "redirect:/web/hotels?page=1&sortBy=createdAt&sortDir=desc";
    }
    
    private void saveHotelImages(Hotel hotel, MultipartFile[] images) throws IOException {
        // Create date-based directory structure: YYYY/MM/DD for easier maintenance
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.format("%02d", today.getMonthValue());
        String day = String.format("%02d", today.getDayOfMonth());
        
        // Create upload directory: uploads/YYYY/MM/DD/
        Path uploadPath = Paths.get(uploadDir, year, month, day);
        Files.createDirectories(uploadPath);

        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                String originalFilename = image.getOriginalFilename();
                String fileExtension = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : "";
                
                // Create unique filename: hotel_{hotelCode}_{uuid}.ext
                String filename = String.format("hotel_%s_%s%s", 
                    hotel.getHotelCode().toString().substring(0, 8), 
                    UUID.randomUUID().toString().substring(0, 8), 
                    fileExtension);
                Path filePath = uploadPath.resolve(filename);

                // Save file
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Save image reference to database (store relative path: YYYY/MM/DD/filename)
                String relativePath = String.format("%s/%s/%s/%s", year, month, day, filename);
                HotelImage hotelImage = new HotelImage();
                hotelImage.setHotel(hotel);
                hotelImage.setFilePath(relativePath);
                hotelImage.setFileName(originalFilename);
                hotelImage.setFileSize(image.getSize());
                hotelImage.setContentType(image.getContentType());
                hotelImage.setUploadDate(new Date());
                hotelImageRepository.save(hotelImage);
            }
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteHotel(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            hotelService.deleteHotel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Hotel deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting hotel: " + e.getMessage());
        }
        return "redirect:/web/hotels?page=1&sortBy=createdAt&sortDir=desc";
    }
}


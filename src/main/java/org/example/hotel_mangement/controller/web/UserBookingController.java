package org.example.hotel_mangement.controller.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Booking;
import org.example.hotel_mangement.model.entity.BookingImage;
import org.example.hotel_mangement.model.entity.Guest;
import org.example.hotel_mangement.model.entity.Hotel;
import org.example.hotel_mangement.model.entity.HotelImage;
import org.example.hotel_mangement.model.entity.Room;
import org.example.hotel_mangement.repository.BookingImageRepository;
import org.example.hotel_mangement.repository.BookingRepository;
import org.example.hotel_mangement.repository.GuestRepository;
import org.example.hotel_mangement.repository.HotelImageRepository;
import org.example.hotel_mangement.repository.HotelRepository;
import org.example.hotel_mangement.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user/booking")
@RequiredArgsConstructor
public class UserBookingController {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final BookingImageRepository bookingImageRepository;
    private final HotelImageRepository hotelImageRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @GetMapping
    public String showBookingForm(
            @RequestParam(required = false) UUID hotelId,
            Model model
    ) {
        // Get all hotels and rooms for dropdowns (newest first), only active (null = active for existing data)
        List<Hotel> hotels = hotelRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .filter(h -> h.getActive() == null || h.getActive())
                .toList();
        model.addAttribute("hotels", hotels);
        model.addAttribute("rooms", roomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .filter(r -> r.getActive() == null || r.getActive())
                .toList());
        
        // Pre-select hotel if provided
        if (hotelId != null) {
            model.addAttribute("selectedHotelId", hotelId);
            // Filter rooms for selected hotel (only active)
            List<Room> hotelRooms = roomRepository.findByHotel_HotelCode(hotelId)
                    .stream()
                    .filter(r -> r.getActive() == null || r.getActive())
                    .toList();
            model.addAttribute("hotelRooms", hotelRooms);
        }
        
        // Get images for all hotels
        java.util.Map<UUID, List<HotelImage>> hotelImagesMap = new java.util.HashMap<>();
        for (Hotel hotel : hotels) {
            List<HotelImage> images = hotelImageRepository.findByHotel_HotelCode(hotel.getHotelCode());
            hotelImagesMap.put(hotel.getHotelCode(), images);
        }
        model.addAttribute("hotelImagesMap", hotelImagesMap);
        
        model.addAttribute("activePage", "user-booking");
        model.addAttribute("pageTitle", "Book a Room - Hotel Management");
        return "user/booking-form";
    }

    @PostMapping
    public String createBooking(
            @RequestParam UUID hotelCode,
            @RequestParam UUID roomId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phoneNo,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam java.sql.Date arrivalDate,
            @RequestParam java.sql.Date departureDate,
            @RequestParam(required = false) Integer numAdults,
            @RequestParam(required = false) Integer numChildren,
            @RequestParam(required = false) String specialReq,
            @RequestParam("images") MultipartFile[] images,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Find or create guest
            Guest guest = guestRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Guest newGuest = new Guest();
                        newGuest.setFirstName(firstName);
                        newGuest.setLastName(lastName);
                        newGuest.setEmail(email);
                        newGuest.setPhoneNo(phoneNo);
                        newGuest.setAddress(address);
                        newGuest.setCity(city);
                        newGuest.setCountry(country);
                        return guestRepository.save(newGuest);
                    });

            // Get hotel and room
            Hotel hotel = hotelRepository.findById(hotelCode)
                    .orElseThrow(() -> new RuntimeException("Hotel not found"));
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            // Create booking
            Booking booking = new Booking();
            booking.setHotel(hotel);
            booking.setGuest(guest);
            booking.setRoom(room);
            booking.setArrivalDate(new Date(arrivalDate.getTime()));
            booking.setDepartureDate(new Date(departureDate.getTime()));
            booking.setBookingDate(new Date());
            booking.setNumAdults(numAdults != null ? numAdults : 1);
            booking.setNumChildren(numChildren != null ? numChildren : 0);
            booking.setSpecialReq(specialReq);
            
            Booking savedBooking = bookingRepository.save(booking);

            // Handle image uploads
            if (images != null && images.length > 0) {
                saveBookingImages(savedBooking, images);
            }

            redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
            return "redirect:/user/booking?success=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating booking: " + e.getMessage());
            return "redirect:/user/booking?error=true";
        }
    }

    private void saveBookingImages(Booking booking, MultipartFile[] images) throws IOException {
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
                
                // Create unique filename: booking_{bookingId}_{uuid}.ext
                String filename = String.format("booking_%s_%s%s", 
                    booking.getBookingId().toString().substring(0, 8), 
                    UUID.randomUUID().toString().substring(0, 8), 
                    fileExtension);
                Path filePath = uploadPath.resolve(filename);

                // Save file
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Save image reference to database (store relative path: YYYY/MM/DD/filename)
                String relativePath = String.format("%s/%s/%s/%s", year, month, day, filename);
                BookingImage bookingImage = new BookingImage();
                bookingImage.setBooking(booking);
                bookingImage.setFilePath(relativePath);
                bookingImage.setFileName(originalFilename);
                bookingImage.setFileSize(image.getSize());
                bookingImage.setContentType(image.getContentType());
                bookingImage.setUploadDate(new Date());
                bookingImageRepository.save(bookingImage);
            }
        }
    }

    @GetMapping("/images")
    public String viewBookingImages(@RequestParam UUID bookingId, Model model) {
        List<BookingImage> images = bookingImageRepository.findByBooking_BookingId(bookingId);
        model.addAttribute("images", images);
        model.addAttribute("bookingId", bookingId);
        return "user/booking-images";
    }
}


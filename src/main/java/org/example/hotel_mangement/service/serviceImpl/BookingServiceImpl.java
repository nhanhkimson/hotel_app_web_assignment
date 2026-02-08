package org.example.hotel_mangement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.exception.NotFoundException;
import org.example.hotel_mangement.model.dto.BookingDTO;
import org.example.hotel_mangement.model.entity.Booking;
import org.example.hotel_mangement.model.entity.Guest;
import org.example.hotel_mangement.model.entity.Hotel;
import org.example.hotel_mangement.model.entity.Room;
import org.example.hotel_mangement.model.request.BookingRequest;
import org.example.hotel_mangement.model.response.PaginationResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.BookingRepository;
import org.example.hotel_mangement.repository.GuestRepository;
import org.example.hotel_mangement.repository.HotelRepository;
import org.example.hotel_mangement.repository.RoomRepository;
import org.example.hotel_mangement.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    @Override
    public PayloadResponse<BookingDTO> findAll(int page, int size, String search, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Booking> bookings;
        if (search != null && !search.trim().isEmpty()) {
            bookings = bookingRepository.searchBookings(search.trim(), pageable);
        } else {
            bookings = bookingRepository.findAllWithRelations(pageable);
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();
        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                bookingDTOs.add(toDTO(booking));
            }
        }

        return PayloadResponse.<BookingDTO>builder()
                .items(bookingDTOs)
                .pagination(PaginationResponse.fromPage(bookings, page, size))
                .build();
    }
    
    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String fieldName = mapSortField(sortBy);
        return Sort.by(direction, fieldName);
    }
    
    private String mapSortField(String sortBy) {
        switch (sortBy) {
            case "bookingDate": return "bookingDate";
            case "arrivalDate": return "arrivalDate";
            case "departureDate": return "departureDate";
            case "guestName": return "guest.firstName";
            case "hotelName": return "hotel.hotelName";
            default: return "bookingDate";
        }
    }

    @Override
    public BookingDTO findBookingById(UUID id) {
        Booking booking = getBookingById(id);
        return toDTO(booking);
    }

    public Booking getBookingById(UUID id) {
        return bookingRepository.findByBookingId(id).orElseThrow(() -> new NotFoundException("Booking not found."));
    }

    @Override
    public BookingDTO saveBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        return toDTO(saveOrUpdateBooking(booking, bookingRequest));
    }

    @Override
    public BookingDTO updateBooking(UUID id, BookingRequest bookingRequest) {
        Booking booking = getBookingById(id);
        return toDTO(saveOrUpdateBooking(booking, bookingRequest));
    }

    @Override
    public BookingDTO deleteBooking(UUID id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
        return toDTO(booking);
    }

    private Booking saveOrUpdateBooking(Booking booking, BookingRequest bookingRequest) {
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelCode())
                .orElseThrow(() -> new NotFoundException("Hotel not found."));
        Guest guest = guestRepository.findById(bookingRequest.getGuestId())
                .orElseThrow(() -> new NotFoundException("Guest not found."));
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found."));

        booking.setHotel(hotel);
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setBookingTime(bookingRequest.getBookingTime());
        booking.setArrivalDate(bookingRequest.getArrivalDate());
        booking.setDepartureDate(bookingRequest.getDepartureDate());
        booking.setEstArrivalTime(bookingRequest.getEstArrivalTime());
        booking.setEstDepartureTime(bookingRequest.getEstDepartureTime());
        booking.setNumAdults(bookingRequest.getNumAdults());
        booking.setNumChildren(bookingRequest.getNumChildren());
        booking.setSpecialReq(bookingRequest.getSpecialReq());
        return bookingRepository.save(booking);
    }

    private BookingDTO toDTO(Booking booking) {
        return BookingDTO.builder()
                .bookingId(booking.getBookingId())
                .hotelCode(booking.getHotel() != null ? booking.getHotel().getHotelCode() : null)
                .hotelName(booking.getHotel() != null ? booking.getHotel().getHotelName() : null)
                .guestId(booking.getGuest() != null ? booking.getGuest().getGuestId() : null)
                .guestName(booking.getGuest() != null ? 
                    (booking.getGuest().getFirstName() + " " + booking.getGuest().getLastName()) : null)
                .roomId(booking.getRoom() != null ? booking.getRoom().getRoomId() : null)
                .roomNo(booking.getRoom() != null ? booking.getRoom().getRoomNo() : null)
                .bookingDate(booking.getBookingDate())
                .bookingTime(booking.getBookingTime())
                .arrivalDate(booking.getArrivalDate())
                .departureDate(booking.getDepartureDate())
                .estArrivalTime(booking.getEstArrivalTime())
                .estDepartureTime(booking.getEstDepartureTime())
                .numAdults(booking.getNumAdults())
                .numChildren(booking.getNumChildren())
                .specialReq(booking.getSpecialReq())
                .build();
    }
}


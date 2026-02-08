package org.example.hotel_mangement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.exception.NotFoundException;
import org.example.hotel_mangement.model.dto.BillDTO;
import org.example.hotel_mangement.model.entity.Bill;
import org.example.hotel_mangement.model.entity.Booking;
import org.example.hotel_mangement.model.entity.Guest;
import org.example.hotel_mangement.model.request.BillRequest;
import org.example.hotel_mangement.model.response.PaginationResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.BillRepository;
import org.example.hotel_mangement.repository.BookingRepository;
import org.example.hotel_mangement.repository.GuestRepository;
import org.example.hotel_mangement.service.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    @Override
    public PayloadResponse<BillDTO> findAll(int page, int size, String search, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Bill> bills;
        if (search != null && !search.trim().isEmpty()) {
            bills = billRepository.searchBills(search.trim(), pageable);
        } else {
            bills = billRepository.findAllWithRelations(pageable);
        }

        List<BillDTO> billDTOs = new ArrayList<>();
        if (!bills.isEmpty()) {
            for (Bill bill : bills) {
                billDTOs.add(toDTO(bill));
            }
        }

        return PayloadResponse.<BillDTO>builder()
                .items(billDTOs)
                .pagination(PaginationResponse.fromPage(bills, page, size))
                .build();
    }
    
    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String fieldName = mapSortField(sortBy);
        return Sort.by(direction, fieldName);
    }
    
    private String mapSortField(String sortBy) {
        switch (sortBy) {
            case "paymentDate": return "paymentDate";
            case "paymentMode": return "paymentMode";
            case "guestName": return "guest.firstName";
            default: return "paymentDate";
        }
    }

    @Override
    public BillDTO findBillById(UUID id) {
        Bill bill = getBillById(id);
        return toDTO(bill);
    }

    public Bill getBillById(UUID id) {
        return billRepository.findByInvoiceNo(id).orElseThrow(() -> new NotFoundException("Bill not found."));
    }

    @Override
    public BillDTO saveBill(BillRequest billRequest) {
        Bill bill = new Bill();
        return toDTO(saveOrUpdateBill(bill, billRequest));
    }

    @Override
    public BillDTO updateBill(UUID id, BillRequest billRequest) {
        Bill bill = getBillById(id);
        return toDTO(saveOrUpdateBill(bill, billRequest));
    }

    @Override
    public BillDTO deleteBill(UUID id) {
        Bill bill = getBillById(id);
        billRepository.delete(bill);
        return toDTO(bill);
    }

    private Bill saveOrUpdateBill(Bill bill, BillRequest billRequest) {
        Booking booking = bookingRepository.findById(billRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException("Booking not found."));
        Guest guest = guestRepository.findById(billRequest.getGuestId())
                .orElseThrow(() -> new NotFoundException("Guest not found."));

        bill.setBooking(booking);
        bill.setGuest(guest);
        bill.setRoomCharge(billRequest.getRoomCharge());
        bill.setRoomService(billRequest.getRoomService());
        bill.setRestaurantCharges(billRequest.getRestaurantCharges());
        bill.setBarCharges(billRequest.getBarCharges());
        bill.setMiscCharges(billRequest.getMiscCharges());
        bill.setIfLateCheckout(billRequest.getIfLateCheckout());
        bill.setPaymentDate(billRequest.getPaymentDate());
        bill.setPaymentMode(billRequest.getPaymentMode());
        bill.setCreditCardNo(billRequest.getCreditCardNo());
        bill.setExpireDate(billRequest.getExpireDate());
        bill.setChequeNo(billRequest.getChequeNo());
        return billRepository.save(bill);
    }

    private BillDTO toDTO(Bill bill) {
        return BillDTO.builder()
                .invoiceNo(bill.getInvoiceNo())
                .bookingId(bill.getBooking() != null ? bill.getBooking().getBookingId() : null)
                .guestId(bill.getGuest() != null ? bill.getGuest().getGuestId() : null)
                .guestName(bill.getGuest() != null ? 
                    (bill.getGuest().getFirstName() + " " + bill.getGuest().getLastName()) : null)
                .roomCharge(bill.getRoomCharge())
                .roomService(bill.getRoomService())
                .restaurantCharges(bill.getRestaurantCharges())
                .barCharges(bill.getBarCharges())
                .miscCharges(bill.getMiscCharges())
                .ifLateCheckout(bill.getIfLateCheckout())
                .paymentDate(bill.getPaymentDate())
                .paymentMode(bill.getPaymentMode())
                .creditCardNo(bill.getCreditCardNo())
                .expireDate(bill.getExpireDate())
                .chequeNo(bill.getChequeNo())
                .build();
    }
}


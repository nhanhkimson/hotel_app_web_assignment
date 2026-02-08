package org.example.hotel_mangement.service;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.BillDTO;
import org.example.hotel_mangement.model.request.BillRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

public interface BillService {
    PayloadResponse<BillDTO> findAll(int page, int size, String search, String sortBy, String sortDir);
    BillDTO findBillById(UUID id);
    BillDTO saveBill(BillRequest billRequest);
    BillDTO updateBill(UUID id, BillRequest billRequest);
    BillDTO deleteBill(UUID id);
}


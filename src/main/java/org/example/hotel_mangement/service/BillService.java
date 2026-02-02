package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.BillDTO;
import org.example.hotel_mangement.model.request.BillRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

import java.util.UUID;

public interface BillService {
    PayloadResponse<BillDTO> findAll(int page, int size);
    BillDTO findBillById(UUID id);
    BillDTO saveBill(BillRequest billRequest);
    BillDTO updateBill(UUID id, BillRequest billRequest);
    BillDTO deleteBill(UUID id);
}


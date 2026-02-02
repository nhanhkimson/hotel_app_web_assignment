package org.example.hotel_mangement.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.model.dto.BillDTO;
import org.example.hotel_mangement.model.request.BillRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bill")
public class BillController {
    private final BillService billService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<BillDTO>>> getBills(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size
    ) {
        ApiResponse<PayloadResponse<BillDTO>> billsApiResponse = ApiResponse.<PayloadResponse<BillDTO>>builder()
                .message("Get bills")
                .status(HttpStatus.OK)
                .payload(billService.findAll(page, size))
                .build();
        return ResponseEntity.ok(billsApiResponse);
    }

    @GetMapping("{bill-id}")
    public ResponseEntity<ApiResponse<BillDTO>> getBillById(@PathVariable("bill-id") UUID id) {
        ApiResponse<BillDTO> billDtoApiResponse = ApiResponse.<BillDTO>builder()
                .message("Get bill by id")
                .status(HttpStatus.OK)
                .payload(billService.findBillById(id))
                .build();
        return ResponseEntity.ok(billDtoApiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BillDTO>> createBill(@RequestBody BillRequest billRequest) {
        ApiResponse<BillDTO> billDtoApiResponse = ApiResponse.<BillDTO>builder()
                .message("Add bill success.")
                .status(HttpStatus.CREATED)
                .payload(billService.saveBill(billRequest))
                .build();
        return ResponseEntity.ok(billDtoApiResponse);
    }

    @PutMapping("{bill-id}")
    public ResponseEntity<ApiResponse<BillDTO>> updateBill(@PathVariable("bill-id") UUID id, @RequestBody BillRequest billRequest) {
        ApiResponse<BillDTO> billDtoApiResponse = ApiResponse.<BillDTO>builder()
                .message("Update bill success.")
                .status(HttpStatus.OK)
                .payload(billService.updateBill(id, billRequest))
                .build();
        return ResponseEntity.ok(billDtoApiResponse);
    }

    @DeleteMapping("{bill-id}")
    public ResponseEntity<ApiResponse<BillDTO>> deleteBill(@PathVariable("bill-id") UUID id) {
        ApiResponse<BillDTO> billDtoApiResponse = ApiResponse.<BillDTO>builder()
                .message("Deleted bill success.")
                .status(HttpStatus.OK)
                .payload(billService.deleteBill(id))
                .build();
        return ResponseEntity.ok(billDtoApiResponse);
    }
}


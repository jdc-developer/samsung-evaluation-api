package com.ibm.controller;

import com.ibm.dto.CurrencyDTO;
import com.ibm.dto.DocumentDTO;
import com.ibm.dto.QuotationDTO;
import com.ibm.service.SamsungAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/v1/samsung")
public class SamsungAPIController {

    private final SamsungAPIService samsungAPIService;

    public SamsungAPIController(SamsungAPIService samsungAPIService) {
        this.samsungAPIService = samsungAPIService;
    }

    @GetMapping("/list-currencies")
    public ResponseEntity<List<CurrencyDTO>> listCurrencies() {
        return ResponseEntity.ok(samsungAPIService.listCurrencies());
    }

    @GetMapping("/list-quotations")
    public ResponseEntity<List<QuotationDTO>> listQuotations() {
        return ResponseEntity.ok(samsungAPIService.listQuotations());
    }

    @GetMapping("/list-documents")
    public ResponseEntity<List<DocumentDTO>> listDocuments(@RequestParam(value = "documentNumber", defaultValue = "") String documentNumber,
                                                           @RequestParam(value = "currencyCode", defaultValue = "") String currencyCode,
                                                           @RequestParam(value = "documentDateFrom", defaultValue = "") String documentDateFrom,
                                                           @RequestParam(value = "documentDateTo", defaultValue = "") String documentDateTo) {
        return ResponseEntity.ok(samsungAPIService.listDocuments(documentNumber, currencyCode, documentDateFrom, documentDateTo));
    }
}

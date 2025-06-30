package com.middleware.third_party_service.controller;


import com.middleware.third_party_service.dto.*;
import com.middleware.third_party_service.service.ThirdPartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/identity")
public class IdentityController {
    private final ThirdPartyService thirdPartyService;

    @PostMapping(value = "/nin-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<KycServiceResponse>> ninDetails(@RequestBody @Valid KycServiceRequest request) {
        return ResponseEntity.ok(new APIResponse<>("Success", "000", thirdPartyService.getNinDetailsForKYC(request)));
    }

    @PostMapping(value = "/bvn-details", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<KycServiceResponse>> bvnDetails(@RequestBody @Valid KycServiceRequest request) {
        return ResponseEntity.ok(new APIResponse<>("Success", "000", thirdPartyService.getBvnDetailsForKYC(request)));
    }

    @GetMapping(value = "/bank-enquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<BankResponse>> createAccount() {
        return ResponseEntity.ok(new APIResponse<>("Success", "000", thirdPartyService.getBankList()));
    }


    @GetMapping(value = "/get-billers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<BillersResponse>> getBiller() {
        return ResponseEntity.ok(new APIResponse<>("Success", "000", thirdPartyService.getBiller()));
    }

    @GetMapping(value = "/get-biller-products/{billerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<BillerResponse>> getBillerProducts(@PathVariable(name = "billerId")String billerId) {
        return ResponseEntity.ok(new APIResponse<>("Success", "000", thirdPartyService.getBillerProductsDetails(billerId)));
    }
}

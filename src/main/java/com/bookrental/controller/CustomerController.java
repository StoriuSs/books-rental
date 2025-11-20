package com.bookrental.controller;

import com.bookrental.dto.ApiResponse;
import com.bookrental.dto.customer.CustomerRequest;
import com.bookrental.dto.customer.CustomerResponse;
import com.bookrental.dto.customer.CustomerSearchCriteria;
import com.bookrental.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getCustomers(
            CustomerSearchCriteria criteria,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CustomerResponse> result = customerService.getCustomers(criteria, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Customers retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable UUID id) {
        CustomerResponse result = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Customer retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> addCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse result = customerService.addCustomer(customerRequest);
        return new ResponseEntity<>(ApiResponse.created(result, "Customer created successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse result = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(ApiResponse.success(result, "Customer updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully"));
    }
}

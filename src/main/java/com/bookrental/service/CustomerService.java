package com.bookrental.service;

import com.bookrental.dto.customer.CustomerRequest;
import com.bookrental.dto.customer.CustomerResponse;
import com.bookrental.dto.customer.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CustomerService {
    Page<CustomerResponse> getCustomers(CustomerSearchCriteria criteria, Pageable pageable);
    CustomerResponse getCustomerById(UUID id);
    CustomerResponse addCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest);
    void deleteCustomer(UUID id);
}

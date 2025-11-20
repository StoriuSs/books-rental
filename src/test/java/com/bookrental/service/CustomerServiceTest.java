package com.bookrental.service;

import com.bookrental.dto.customer.CustomerRequest;
import com.bookrental.dto.customer.CustomerResponse;
import com.bookrental.entity.Customer;
import com.bookrental.exception.ResourceNotFoundException;
import com.bookrental.repository.CustomerRepository;
import com.bookrental.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.bookrental.dto.customer.CustomerSearchCriteria;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequest customerRequest;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");
        customer.setPhone("0123456789");
        customer.setIdCard("123456789");
        customer.setAddress("Hanoi");

        customerRequest = new CustomerRequest();
        customerRequest.setName("Updated Customer");
        customerRequest.setPhone("0987654321");
        customerRequest.setIdCard("987654321");
        customerRequest.setAddress("HCM");
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerById(customerId);

        assertThat(response.getId()).isEqualTo(customerId);
        assertThat(response.getName()).isEqualTo("Test Customer");
    }

    @Test
    void getCustomerById_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    void addCustomer_Success() {
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setId(customerId);
            return savedCustomer;
        });

        CustomerResponse response = customerService.addCustomer(customerRequest);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(customerRequest.getName());
        assertThat(response.getPhone()).isEqualTo(customerRequest.getPhone());
    }

    @Test
    void updateCustomer_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse response = customerService.updateCustomer(customerId, customerRequest);

        assertThat(response.getName()).isEqualTo("Updated Customer");
        assertThat(response.getAddress()).isEqualTo("HCM");
    }

    @Test
    void deleteCustomer_Success() {
        when(customerRepository.existsById(customerId)).thenReturn(true);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void deleteCustomer_NotFound() {
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(customerId));
    }

    @Test
    void getCustomers_Success() {
        // Mock Page
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);

        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setKeyword("Test");
        Pageable pageable = PageRequest.of(0, 10);

        Page<CustomerResponse> result = customerService.getCustomers(criteria, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Customer");
    }
}

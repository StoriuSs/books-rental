package com.bookrental.service.impl;

import com.bookrental.dto.customer.CustomerRequest;
import com.bookrental.dto.customer.CustomerResponse;
import com.bookrental.dto.customer.CustomerSearchCriteria;
import com.bookrental.entity.Customer;
import com.bookrental.exception.ResourceNotFoundException;
import com.bookrental.repository.CustomerRepository;
import com.bookrental.service.CustomerService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomers(CustomerSearchCriteria criteria, Pageable pageable) {
        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria != null) {
                if (StringUtils.hasText(criteria.getKeyword())) {
                    String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
                    Predicate nameLike = cb.like(cb.lower(root.get("name")), keyword);
                    Predicate idCardLike = cb.like(root.get("idCard"), keyword);
                    Predicate phoneLike = cb.like(root.get("phone"), keyword);
                    predicates.add(cb.or(nameLike, idCardLike, phoneLike));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return customerRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        mapRequestToEntity(customerRequest, customer);
        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        mapRequestToEntity(customerRequest, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // Helper methods for mapping
    private CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setIdCard(customer.getIdCard());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        return response;
    }

    private void mapRequestToEntity(CustomerRequest request, Customer customer) {
        customer.setName(request.getName());
        customer.setIdCard(request.getIdCard());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
    }
}

package com.bookrental.dto.customer;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class CustomerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "ID Card is required")
    private String idCard;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;
}

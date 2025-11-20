package com.bookrental.dto.customer;

import lombok.Data;
import java.util.UUID;

@Data
public class CustomerResponse {
    private UUID id;
    private String name;
    private String idCard;
    private String phone;
    private String address;
}

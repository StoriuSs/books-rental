package com.bookrental.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CustomerDTO {
    private UUID id;
    private String name;
    private String idCard;
    private String phone;
    private String address;
}

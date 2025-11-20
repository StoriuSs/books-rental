package com.bookrental.dto.book;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BookResponse {
    private UUID id;
    private String title;
    private String author;
    private String publisher;
    private Integer publishYear;
    private BigDecimal pricePerDay;
    private Integer quantity;
    private Integer availableCopies;
    private BigDecimal replacementCost;
}

package com.bookrental.dto.book;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class BookRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Publisher is required")
    private String publisher;

    @NotNull(message = "Publish year is required")
    private Integer publishYear;

    @NotNull(message = "Price per day is required")
    @Positive(message = "Price must be positive")
    private BigDecimal pricePerDay;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Replacement cost is required")
    @Positive(message = "Replacement cost must be positive")
    private BigDecimal replacementCost;
}

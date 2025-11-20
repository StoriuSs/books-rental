package com.bookrental.dto.book;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookSearchCriteria {
    private String keyword;
    private Integer minPublishYear;
    private Integer maxPublishYear;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}

package com.bookrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String author;

    private String publisher;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    private Integer quantity;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "replacement_cost")
    private BigDecimal replacementCost;
}

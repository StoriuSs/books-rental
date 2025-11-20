package com.bookrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "borrow_items")
public class BorrowItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private BorrowReceipt receipt;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_condition_before")
    private BookCondition bookConditionBefore;

    public enum BorrowStatus {
        BORROWED, RETURNED
    }

    public enum BookCondition {
        GOOD, DAMAGED, HEAVILY_DAMAGED
    }
}

package com.bookrental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "return_items")
public class ReturnItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "borrow_item_id")
    private BorrowItem borrowItem;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "rent_days")
    private Integer rentDays;

    @Column(name = "rent_fee")
    private BigDecimal rentFee;

    @Column(name = "penalty_fee")
    private BigDecimal penaltyFee;

    @Column(name = "final_amount")
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_condition_after")
    private BorrowItem.BookCondition bookConditionAfter;
}

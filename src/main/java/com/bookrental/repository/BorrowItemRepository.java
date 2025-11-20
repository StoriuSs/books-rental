package com.bookrental.repository;

import com.bookrental.entity.BorrowItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, UUID> {
    // SELECT * FROM borrow_items 
    // JOIN borrow_receipts ON borrow_items.receipt_id = borrow_receipts.id 
    // WHERE borrow_receipts.customer_id = ?1 AND borrow_items.status = ?2;
    List<BorrowItem> findByReceiptCustomerIdAndStatus(UUID customerId, BorrowItem.BorrowStatus status);
}

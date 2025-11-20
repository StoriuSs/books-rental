package com.bookrental.repository;

import com.bookrental.entity.BorrowItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, UUID> {
    List<BorrowItem> findByReceiptCustomerIdAndStatus(UUID customerId, BorrowItem.BorrowStatus status);
}

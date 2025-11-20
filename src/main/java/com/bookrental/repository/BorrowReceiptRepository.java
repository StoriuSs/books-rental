package com.bookrental.repository;

import com.bookrental.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, UUID> {
}

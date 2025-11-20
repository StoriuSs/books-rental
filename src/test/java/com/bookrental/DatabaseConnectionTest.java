package com.bookrental;

import com.bookrental.entity.Book;
import com.bookrental.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class DatabaseConnectionTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testCreateAndReadBook() {
        // 1. Create a new book
        Book book = new Book();
        book.setTitle("Test Book Connection");
        book.setAuthor("Tester");
        book.setPublisher("Tech Publisher");
        book.setPublishYear(2024);
        book.setPricePerDay(new BigDecimal("5000"));
        book.setQuantity(10);
        book.setAvailableCopies(10);
        book.setReplacementCost(new BigDecimal("100000"));

        Book savedBook = bookRepository.save(book);

        // 2. Verify it has an ID
        assertThat(savedBook.getId()).isNotNull();

        // 3. Read it back
        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Test Book Connection");

        System.out.println(">>> TEST SUCCESS: Book saved with ID: " + savedBook.getId());
    }
}

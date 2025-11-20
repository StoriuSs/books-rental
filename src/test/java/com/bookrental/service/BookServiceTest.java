package com.bookrental.service;

import com.bookrental.dto.book.BookRequest;
import com.bookrental.dto.book.BookResponse;
import com.bookrental.entity.Book;
import com.bookrental.exception.ResourceNotFoundException;
import com.bookrental.repository.BookRepository;
import com.bookrental.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.bookrental.dto.book.BookSearchCriteria;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookRequest bookRequest;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setQuantity(10);
        book.setAvailableCopies(10);
        book.setPricePerDay(new BigDecimal("5000"));

        bookRequest = new BookRequest();
        bookRequest.setTitle("Updated Book");
        bookRequest.setAuthor("Updated Author");
        bookRequest.setPublisher("Updated Publisher");
        bookRequest.setPublishYear(2024);
        bookRequest.setPricePerDay(new BigDecimal("6000"));
        bookRequest.setQuantity(15); // Increase quantity
        bookRequest.setReplacementCost(new BigDecimal("100000"));
    }

    @Test
    void getBookById_Success() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(bookId);

        assertThat(response.getId()).isEqualTo(bookId);
        assertThat(response.getTitle()).isEqualTo("Test Book");
    }

    @Test
    void getBookById_NotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void addBook_Success() {
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(bookId);
            return savedBook;
        });

        BookResponse response = bookService.addBook(bookRequest);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(bookRequest.getTitle());
        assertThat(response.getAvailableCopies()).isEqualTo(bookRequest.getQuantity()); // Available copies = Quantity for new book
    }

    @Test
    void updateBook_Success_IncreaseQuantity() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Initial: Quantity 10, Available 10. Request: Quantity 15.
        // Diff = 5. New Available = 10 + 5 = 15.
        BookResponse response = bookService.updateBook(bookId, bookRequest);

        assertThat(response.getQuantity()).isEqualTo(15);
        assertThat(response.getAvailableCopies()).isEqualTo(15);
    }

    @Test
    void updateBook_Success_DecreaseQuantity() {
        bookRequest.setQuantity(5); // Decrease from 10 to 5
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Initial: Quantity 10, Available 10. Request: Quantity 5.
        // Diff = -5. New Available = 10 - 5 = 5.
        BookResponse response = bookService.updateBook(bookId, bookRequest);

        assertThat(response.getQuantity()).isEqualTo(5);
        assertThat(response.getAvailableCopies()).isEqualTo(5);
    }

    @Test
    void updateBook_Fail_NegativeAvailableCopies() {
        book.setAvailableCopies(2); // Only 2 available
        bookRequest.setQuantity(5); // Decrease total from 10 to 5 (Diff = -5)
        // New Available = 2 - 5 = -3 -> Should throw exception

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(bookId, bookRequest));
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.existsById(bookId)).thenReturn(true);
        
        bookService.deleteBook(bookId);
        
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBook_NotFound() {
        when(bookRepository.existsById(bookId)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(bookId));
    }

    @Test
    void getBooks_Success() {
        // Mock Page
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);

        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setKeyword("Test");
        Pageable pageable = PageRequest.of(0, 10);

        Page<BookResponse> result = bookService.getBooks(criteria, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Book");
    }
}

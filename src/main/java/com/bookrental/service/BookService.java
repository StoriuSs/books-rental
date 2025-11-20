package com.bookrental.service;

import com.bookrental.dto.book.BookRequest;
import com.bookrental.dto.book.BookResponse;
import com.bookrental.dto.book.BookSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface BookService {
    Page<BookResponse> getBooks(BookSearchCriteria criteria, Pageable pageable);
    BookResponse getBookById(UUID id);
    BookResponse addBook(BookRequest bookRequest);
    BookResponse updateBook(UUID id, BookRequest bookRequest);
    void deleteBook(UUID id);
}

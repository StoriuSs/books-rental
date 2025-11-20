package com.bookrental.controller;

import com.bookrental.dto.ApiResponse;
import com.bookrental.dto.book.BookRequest;
import com.bookrental.dto.book.BookResponse;
import com.bookrental.dto.book.BookSearchCriteria;
import com.bookrental.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getBooks(
            BookSearchCriteria criteria,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<BookResponse> result = bookService.getBooks(criteria, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Books retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable UUID id) {
        BookResponse result = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Book retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> addBook(@Valid @RequestBody BookRequest bookRequest) {
        BookResponse result = bookService.addBook(bookRequest);
        return new ResponseEntity<>(ApiResponse.created(result, "Book created successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@PathVariable UUID id, @Valid @RequestBody BookRequest bookRequest) {
        BookResponse result = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(ApiResponse.success(result, "Book updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Book deleted successfully"));
    }
}

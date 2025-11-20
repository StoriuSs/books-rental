package com.bookrental.service.impl;

import com.bookrental.dto.book.BookRequest;
import com.bookrental.dto.book.BookResponse;
import com.bookrental.dto.book.BookSearchCriteria;
import com.bookrental.entity.Book;
import com.bookrental.exception.ResourceNotFoundException;
import com.bookrental.repository.BookRepository;
import com.bookrental.service.BookService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getBooks(BookSearchCriteria criteria, Pageable pageable) {
        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria != null) {
                if (StringUtils.hasText(criteria.getKeyword())) {
                    String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
                    Predicate titleLike = cb.like(cb.lower(root.get("title")), keyword);
                    Predicate authorLike = cb.like(cb.lower(root.get("author")), keyword);
                    Predicate publisherLike = cb.like(cb.lower(root.get("publisher")), keyword);
                    predicates.add(cb.or(titleLike, authorLike, publisherLike));
                }

                if (criteria.getMinPublishYear() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("publishYear"), criteria.getMinPublishYear()));
                }
                if (criteria.getMaxPublishYear() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("publishYear"), criteria.getMaxPublishYear()));
                }
                if (criteria.getMinPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerDay"), criteria.getMinPrice()));
                }
                if (criteria.getMaxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("pricePerDay"), criteria.getMaxPrice()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return bookRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    @Override
    public BookResponse addBook(BookRequest bookRequest) {
        Book book = new Book();
        mapRequestToEntity(bookRequest, book);
        // New book: available copies = quantity
        book.setAvailableCopies(bookRequest.getQuantity());
        
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(UUID id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        // Calculate difference in quantity to adjust available copies
        int quantityDiff = bookRequest.getQuantity() - book.getQuantity();
        
        mapRequestToEntity(bookRequest, book);
        
        // Adjust available copies based on quantity change
        book.setAvailableCopies(book.getAvailableCopies() + quantityDiff);
        
        if (book.getAvailableCopies() < 0) {
             book.setAvailableCopies(0);
             throw new IllegalArgumentException("Available copies cannot be negative");
        }

        Book updatedBook = bookRepository.save(book);
        return mapToResponse(updatedBook);
    }

    @Override
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    // Helper methods for mapping
    private BookResponse mapToResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setPublisher(book.getPublisher());
        response.setPublishYear(book.getPublishYear());
        response.setPricePerDay(book.getPricePerDay());
        response.setQuantity(book.getQuantity());
        response.setAvailableCopies(book.getAvailableCopies());
        response.setReplacementCost(book.getReplacementCost());
        return response;
    }

    private void mapRequestToEntity(BookRequest request, Book book) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublishYear(request.getPublishYear());
        book.setPricePerDay(request.getPricePerDay());
        book.setQuantity(request.getQuantity());
        book.setReplacementCost(request.getReplacementCost());
    }
}

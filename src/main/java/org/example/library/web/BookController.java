package org.example.library.web;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.library.models.dtos.BookDTO;
import org.example.library.models.entities.Book;
import org.example.library.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        try {
            bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok("Book updated successfully!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        System.out.println("Fetching book with ID: " + id);
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity.ok(books);
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable("title") String title) {
        BookDTO bookDTO = bookService.findByTittle(title);
        if (bookDTO == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity.ok(bookDTO);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createBook(@RequestBody BookDTO bookDTO,
                                             UriComponentsBuilder uriComponentsBuilder) {
        try {
            Long newBookId = bookService.createBook(bookDTO);
            return ResponseEntity.ok(newBookId);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBookById(@PathVariable("id") Long bookId) {
        bookService.deleteBookById(bookId);

        return ResponseEntity.noContent().build();
    }
    }
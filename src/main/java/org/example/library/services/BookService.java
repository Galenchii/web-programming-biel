package org.example.library.services;
import org.example.library.models.dtos.BookDTO;
import org.example.library.models.entities.Author;
import org.example.library.models.entities.Book;
import org.example.library.models.entities.Genre;
import org.example.library.models.enums.EGenre;
import org.example.library.repositories.AuthorRepository;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    public Long createBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        Optional<Author> author = authorRepository.findByName(bookDTO.getAuthorName());

        if (bookRepository.findByTitle(bookDTO.getTitle()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A book with this title already exists.");
        }

        List<Genre> genres = new ArrayList<>();

        for (String genreName : bookDTO.getGenres()) {
            Optional<Genre> genre = genreRepository.findByGenreType(EGenre.valueOf(genreName));
            genre.ifPresent(genres::add);
        }
        if (author.isPresent()) {
            author.get().getBooks().add(book);
            book.setAuthor(author.get());
            book.setGenre(genres);
            bookRepository.save(book);

            return book.getId();
        }

        return null;
    }

    public void updateBook(Long bookId, BookDTO bookDTO) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (bookDTO.getTitle() != null) {
                book.setTitle(bookDTO.getTitle());
            }
            if (bookDTO.getPrice() != null) {
                book.setPrice(bookDTO.getPrice());
            }
            if (bookDTO.getGenres() != null && !bookDTO.getGenres().isEmpty()) {
                List<Genre> genres = new ArrayList<>();
                for (String genreName : bookDTO.getGenres()) {
                    Optional<Genre> genre = genreRepository.findByGenreType(EGenre.valueOf(genreName));
                    genre.ifPresent(genres::add);
                }
                book.setGenres(genres);
            }
            if (bookDTO.getAuthorName() != null) {
                Optional<Author> author = authorRepository.findByName(bookDTO.getAuthorName());
                author.ifPresent(book::setAuthor);
            }
            bookRepository.save(book);
        }
    }

    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(book -> {
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);

                    List<String> genres = new ArrayList<>();

                    for (Genre genre : book.getGenre()) {
                        genres.add(genre.getGenreType().name());
                    }
                    bookDTO.setGenres(genres);
                    return bookDTO;

                })
                .collect(Collectors.toList());
    }

    public BookDTO findByTittle(String title) {
        Optional<Book> book = bookRepository.findByTitle(title);

        if (book.isPresent()) {
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            for (Genre genre : book.get().getGenre()) {
                bookDTO.getGenres().add(genre.getGenreType().toString());
            }
            return bookDTO;
        }

        return null;
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}

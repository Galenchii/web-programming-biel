package org.example.library.services;

import org.example.library.models.dtos.AuthorDTO;
import org.example.library.models.entities.Author;
import org.example.library.repositories.AuthorRepository;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository
                .findAll()
                .stream()
                .map(author -> modelMapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());
    }

    public Long createAuthor(AuthorDTO authorDTO) {
        if (authorRepository.findByName(authorDTO.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Author with this name already exists");
        }
        Author author = modelMapper.map(authorDTO, Author.class);
        author.setBooks(new ArrayList<>());
        authorRepository.save(author);
      return author.getId();
    }
}

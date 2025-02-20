package org.example.library.web;
import jakarta.validation.Valid;
import org.example.library.models.dtos.AuthorDTO;
import org.example.library.services.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/api/authors")
public class AuthorController
{
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        try {
            Long authorId = authorService.createAuthor(authorDTO);
            return ResponseEntity.ok(authorId);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(){
        List<AuthorDTO> authorDTOS = authorService.getAllAuthors();
        if(authorDTOS.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(authorDTOS);
        }
    }
}
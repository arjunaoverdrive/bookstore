package org.arjunaoverdrive.bookstore.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.arjunaoverdrive.bookstore.mapper.BookMapper;
import org.arjunaoverdrive.bookstore.service.BookService;
import org.arjunaoverdrive.bookstore.web.dto.BookFilter;
import org.arjunaoverdrive.bookstore.web.dto.BookListResponse;
import org.arjunaoverdrive.bookstore.web.dto.BookResponse;
import org.arjunaoverdrive.bookstore.web.dto.UpsertBookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/filter")
    public ResponseEntity<BookResponse> findByTitleAndAuthor(@Valid BookFilter bookFilter){
        return ResponseEntity.ok().body(bookMapper.toDto(bookService.findByTitleAndAuthor(bookFilter)));
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<BookListResponse> findByCategory(@PathVariable(name = "name") String categoryName){
        return ResponseEntity.ok().body(bookMapper.toListResponse(bookService.findByCategory(categoryName)));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid UpsertBookRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toDto(
                bookService.createBook(bookMapper.toBook(request))
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id, @RequestBody @Valid UpsertBookRequest request){
        return ResponseEntity.accepted().body(bookMapper.toDto(
                bookService.updateBook(bookMapper.toBook(id, request))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable UUID id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

}

package com.books.application.book.book.controller;

import com.books.application.book.book.dto.*;
import com.books.application.book.book.service.BookService;
import com.books.application.book.book.statics.constants.BookRestApi;
import com.books.security.authentication.filter.JwtUser;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Member Books Controller")
@RestController
@RequestMapping(value = {"${rest.member}"})
@Validated
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(description = "count books")
    @GetMapping(path = BookRestApi.BOOKS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid BookFilter filter,
                                         BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException, SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        filter.putUserId(userContextDto.getId());
        return new ResponseEntity<>(bookService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "get all books")
    @GetMapping(path = BookRestApi.BOOKS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookOut>> getAll(@Valid BookPageableFilter pageableFilter,
                                                BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        pageableFilter.putUserId(userContextDto.getId());
        return new ResponseEntity<>(bookService.getAll(pageableFilter), HttpStatus.OK);
    }

    @Operation(description = "get book by id")
    @GetMapping(path = BookRestApi.BOOKS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookOut> getById(@PathVariable(name = "id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();

        return new ResponseEntity<>(bookService.getById(userContextDto.getId(), id), HttpStatus.OK);
    }

    @Operation(description = "get book's file")
    @GetMapping(BookRestApi.BOOKS_FILE)
    public ResponseEntity<Resource> getBookFile(@PathVariable(name = "id") int id, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        BookFileOut model = bookService.getFile(contextDto.getId(), id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(model.getContentType()))
                .body(model.getResource());
    }

    @Operation(description = "create book")
    @PostMapping(path = BookRestApi.BOOKS, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void create(@ModelAttribute @Valid BookIn model, BindingResult bindingResult,
                       HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        bookService.create(userContextDto.getId(), model);
    }

    @Operation(description = "update book")
    @PutMapping(path = BookRestApi.BOOKS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable(name = "id") int id, @RequestBody @Valid BookUpdateIn model,
                       BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        bookService.update(id, userContextDto.getId(), model);
    }

    @Operation(description = "update book file")
    @PutMapping(path = BookRestApi.BOOKS_FILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateFile(@PathVariable(name = "id") int id, @RequestBody @Valid @NotNull MultipartFile file,
                           BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        bookService.updateFile(id, userContextDto.getId(), file);
    }

    @Operation(description = "delete book")
    @DeleteMapping(path = BookRestApi.BOOKS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        bookService.delete(id, userContextDto.getId());
    }
}

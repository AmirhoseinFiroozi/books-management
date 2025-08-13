package com.books.domain.book.shelf.controller;

import com.books.domain.book.shelf.dto.BookShelfFilter;
import com.books.domain.book.shelf.dto.BookShelfIn;
import com.books.domain.book.shelf.dto.BookShelfOut;
import com.books.domain.book.shelf.dto.BookShelfPageableFilter;
import com.books.domain.book.shelf.service.BookShelfService;
import com.books.domain.book.shelf.statics.constants.BookShelfRestApi;
import com.books.security.authentication.filter.JwtUser;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member Book Shelves Controller")
@RestController
@RequestMapping(value = {"${rest.member}"})
@Validated
public class BookShelfController {
    private final BookShelfService bookShelfService;

    @Autowired
    public BookShelfController(BookShelfService bookShelfService) {
        this.bookShelfService = bookShelfService;
    }

    @Operation(description = "count book shelves")
    @GetMapping(path = BookShelfRestApi.BOOK_SHELVES_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid BookShelfFilter filter,
                                         BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException, SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        filter.putUserId(userContextDto.getId());
        return new ResponseEntity<>(bookShelfService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "get all book shelves")
    @GetMapping(path = BookShelfRestApi.BOOK_SHELVES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookShelfOut>> getAll(@Valid BookShelfPageableFilter pageableFilter,
                                                     BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        pageableFilter.putUserId(userContextDto.getId());
        return new ResponseEntity<>(bookShelfService.getAll(pageableFilter), HttpStatus.OK);
    }

    @Operation(description = "create book shelf")
    @PostMapping(path = BookShelfRestApi.BOOK_SHELVES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookShelfOut> create(@RequestBody @Valid BookShelfIn model, BindingResult bindingResult,
                                               HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(bookShelfService.create(userContextDto.getId(), model), HttpStatus.OK);
    }

    @Operation(description = "update book shelf")
    @PutMapping(path = BookShelfRestApi.BOOK_SHELVES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookShelfOut> update(@PathVariable(name = "id") int id, @RequestBody @Valid BookShelfIn model,
                                               BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(bookShelfService.update(id, userContextDto.getId(), model), HttpStatus.OK);
    }

    @Operation(description = "delete book shelf")
    @DeleteMapping(path = BookShelfRestApi.BOOK_SHELVES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto userContextDto = JwtUser.getAuthenticatedUser();
        bookShelfService.delete(id, userContextDto.getId());
    }
}

package com.books.search.controller;

import com.books.search.dto.BookSearchFileOut;
import com.books.search.dto.BookSearchOut;
import com.books.search.dto.BookSearchPageableFilter;
import com.books.search.service.BookSearchService;
import com.books.search.statics.constants.BookSearchRestApi;
import com.books.utility.system.exception.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Public Books Search Controller")
@RestController
@RequestMapping(value = {"${rest.public}"})
@Validated
public class BookSearchController {
    private final BookSearchService searchService;

    @Autowired
    public BookSearchController(BookSearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(description = "count search results")
    @GetMapping(path = BookSearchRestApi.SEARCH_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> searchCount(@PathVariable(name = "clause") String clause, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(searchService.searchCount(clause), HttpStatus.OK);
    }

    @Operation(description = "search by clause")
    @GetMapping(path = BookSearchRestApi.SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookSearchOut>> search(@PathVariable(name = "clause") String clause, @Valid BookSearchPageableFilter pageableFilter,
                                                      BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(searchService.search(clause, pageableFilter), HttpStatus.OK);
    }

    @Operation(description = "download book's file")
    @GetMapping(BookSearchRestApi.BOOKS_ID)
    public ResponseEntity<Resource> downloadBookFile(@PathVariable(name = "id") int id, HttpServletRequest request) throws SystemException {
        BookSearchFileOut model = searchService.downloadBookFile(id);
        if (model.getResource() == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, model.getContentDisposition())
                .contentType(MediaType.parseMediaType(model.getContentType()))
                .body(model.getResource());
    }
}

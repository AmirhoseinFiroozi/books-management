package com.books.book.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
public class BookFileOut {
    private String contentType;
    private Resource resource;
}

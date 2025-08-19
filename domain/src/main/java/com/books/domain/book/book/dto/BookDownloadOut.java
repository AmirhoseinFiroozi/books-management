package com.books.domain.book.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDownloadOut {
    private String contentDisposition;
    private String contentType;
    private Resource resource;
}

package com.books.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class BookIn {
    @NotNull
    private String name;
    @NotNull
    private MultipartFile file;
}

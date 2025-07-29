package com.books.book.book.dto;

import jakarta.validation.constraints.Min;
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
    @Min(1)
    private Integer bookShelfId;
    @NotNull
    private MultipartFile file;
}

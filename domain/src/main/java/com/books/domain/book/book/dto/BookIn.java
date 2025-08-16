package com.books.domain.book.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class BookIn {
    @NotBlank
    private String name;
    @NotNull
    @Min(1)
    private Integer bookShelfId;
    @NotNull
    private Boolean published;
    @NotNull
    private MultipartFile file;

    public void setBookShelfId(String bookShelfId) {
        this.bookShelfId = Integer.valueOf(bookShelfId);
    }

    public void setPublished(String published) {
        this.published = Boolean.parseBoolean(published);
    }
}

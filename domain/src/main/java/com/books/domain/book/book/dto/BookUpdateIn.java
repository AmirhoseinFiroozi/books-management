package com.books.domain.book.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookUpdateIn {
    @NotBlank
    private String name;
    @NotNull
    private Boolean published;
    @NotNull
    @Min(1)
    private Integer bookShelfId;
}

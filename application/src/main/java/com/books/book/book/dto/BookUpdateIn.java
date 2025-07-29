package com.books.book.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookUpdateIn {
    @NotNull
    private String name;
    @NotNull
    @Min(1)
    private Integer bookShelfId;
}

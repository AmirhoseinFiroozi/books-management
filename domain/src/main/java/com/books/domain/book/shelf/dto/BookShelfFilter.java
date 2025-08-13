package com.books.domain.book.shelf.dto;

import com.books.utility.commons.repository.interfaces.FilterBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BookShelfFilter implements FilterBase {
    private Integer id;
    private String name;
    private LocalDate createdMin;
    private LocalDate createdMax;
    @Schema(hidden = true)
    @Setter(AccessLevel.PRIVATE)
    private Integer userId;

    public void putUserId(Integer userId) {
        this.userId = userId;
    }
}

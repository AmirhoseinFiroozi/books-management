package com.books.book.book.dto;

import com.books.utility.commons.repository.interfaces.FilterBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookFilter implements FilterBase {
    private Long id;
    private String name;
    private LocalDateTime createdMin;
    private LocalDateTime createdMax;
    @Schema(hidden = true)
    @Setter(AccessLevel.PRIVATE)
    private Integer userId;
    @Schema(hidden = true)
    @Setter(AccessLevel.PRIVATE)
    private Integer bookShelfId;

    public void putUserId(Integer userId) {
        this.userId = userId;
    }

    public void putBookShelfId(Integer bookShelfId) {
        this.bookShelfId = bookShelfId;
    }
}

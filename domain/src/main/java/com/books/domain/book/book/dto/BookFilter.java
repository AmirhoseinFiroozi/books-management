package com.books.domain.book.book.dto;

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
    private LocalDateTime createdMin;
    private LocalDateTime createdMax;
    private Boolean published;
    @Schema(hidden = true)
    @Setter(AccessLevel.PRIVATE)
    private Integer userId;
    private Integer bookShelfId;

    public void putUserId(Integer userId) {
        this.userId = userId;
    }
}

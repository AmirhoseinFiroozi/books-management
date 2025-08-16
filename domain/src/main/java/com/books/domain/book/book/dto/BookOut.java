package com.books.domain.book.book.dto;

import com.books.domain.book.book.entity.BookEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookOut {
    private int id;
    private String name;
    private Boolean published;
    private LocalDateTime created;
    private Integer userId;

    public BookOut(BookEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.published = entity.isPublished();
        this.created = entity.getCreated();
        this.userId = entity.getUserId();
    }
}

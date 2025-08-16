package com.books.search.dto;

import com.books.utility.commons.repository.dto.SortOption;
import com.books.utility.commons.repository.interfaces.FilterBase;
import com.books.utility.commons.repository.statics.SortType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookSearchPageableFilter implements FilterBase {
    @Min(1)
    private int page;
    @Min(1)
    private int size;
    private boolean export;
    private List<SortOption> sort;

    public BookSearchPageableFilter() {
        this.page = 1;
        this.size = 10;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}

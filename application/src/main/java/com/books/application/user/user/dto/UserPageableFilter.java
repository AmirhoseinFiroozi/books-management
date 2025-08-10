package com.books.application.user.user.dto;

import com.books.utility.commons.repository.dto.SortOption;
import com.books.utility.commons.repository.statics.SortType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserPageableFilter extends UserFilter {
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer size;
    private List<SortOption> sort;
    private boolean export;

    public UserPageableFilter() {
        this.page = 1;
        this.size = 50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}

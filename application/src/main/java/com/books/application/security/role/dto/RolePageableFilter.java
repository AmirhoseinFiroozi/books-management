package com.books.application.security.role.dto;

import com.books.utility.commons.repository.dto.SortOption;
import com.books.utility.commons.repository.statics.SortType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RolePageableFilter extends RoleFilter {
    @Min(1)
    private int page;
    @Min(1)
    private int size;
    private List<SortOption> sort;
    private boolean export;

    public RolePageableFilter() {
        this.page = 1;
        this.size = 50;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}

package com.books.user.account.dto;

import com.books.company.entity.CompanyEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyInfo {
    private long id;
    private String name;

    public CompanyInfo(CompanyEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}

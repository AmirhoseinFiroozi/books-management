package com.books.domain.user.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserRoleRealmIn {
    private Integer roleId;
    private Integer realmId;

    @Override
    public String toString() {
        return "{" +
                "\"roleId\":" + roleId +
                ", \"realmId\":" + realmId +
                "}";
    }
}

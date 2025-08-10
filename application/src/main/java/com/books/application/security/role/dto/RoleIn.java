package com.books.application.security.role.dto;

import com.books.application.security.role.statics.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.books.utility.support.NormalizeEngine.normalizePersianString;

@Getter
@Setter
@NoArgsConstructor
public class RoleIn {
    @NotBlank
    private String name;
    private boolean show = true;
    @NotNull
    private RoleType type;
    private List<Integer> permissionIds;

    public void setName(String name) {
        this.name = normalizePersianString(name);
    }
}

package com.books.user.account.dto;

import com.books.company.entity.CompanyEntity;
import com.books.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountInfoOut {
    private int id;
    private String fullName;
    private String displayName;
    private String image;
    private String phoneNumber;
    private String email;
    private boolean phoneNumberConfirmed;
    private boolean emailConfirmed;
    private boolean hasPassword;
    private List<Long> companyIds;
    private List<CompanyInfo> companyInfo;

    public AccountInfoOut(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.fullName = userEntity.getFullName();
        this.displayName = userEntity.getDisplayName();
        this.image = userEntity.getImage();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.email = userEntity.getEmail();
        this.phoneNumberConfirmed = userEntity.isPhoneNumberConfirmed();
        this.emailConfirmed = userEntity.isEmailConfirmed();
        this.hasPassword = userEntity.getHashedPassword() != null && !userEntity.getHashedPassword().isEmpty();
        if (Hibernate.isInitialized(userEntity.getCompanies()) && !CollectionUtils.isEmpty(userEntity.getCompanies())) {
            this.companyIds = userEntity.getCompanies().stream().map(CompanyEntity::getId).toList();
            this.companyInfo = userEntity.getCompanies().stream().map(CompanyInfo::new).toList();
        }
    }
}

package com.books.application.user.user.service;

import com.books.application.user.in.rolerealm.entity.SecurityUserRoleRealmEntity;
import com.books.application.user.user.dto.*;
import com.books.application.user.user.entity.UserEntity;
import com.books.application.user.user.repository.BaseUserDao;
import com.books.database.service.AbstractService;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportCriteriaJoinCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<UserEntity, BaseUserDao> {
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(BaseUserDao dao, ModelMapper modelMapper) {
        super(dao);
        this.modelMapper = modelMapper;
    }

    public UserEntity getByUsernameOrPhoneNumber(String username) {
        return getDao().getByUsernameOrPhoneNumber(username);
    }

    public boolean existsByUsernameOrPhoneNumber(String username, String phoneNumber) {
        return getDao().existsByUsernameOrPhoneNumber(username, phoneNumber);
    }

    public UserEntity getByUsernameOrPhoneNumber(String username, String phoneNumber) {
        return getDao().getByUsernameOrPhoneNumber(username, phoneNumber);
    }

    public void updateAccessFailedCount(int id) {
        getDao().updateAccessFailedCount(id);
    }

    public int count(UserFilter filter) {
        return this.countEntity(filter(filter));
    }

    public List<UserInfoOut> getAllInfo(UserPageableFilter filter) {
        List<UserEntity> users = this.getAllEntities(filter(filter), null);
        return users.stream().map(UserInfoOut::new).toList();
    }

    public UserOut getById(int id) throws SystemException {
        String[] include = {"roleRealms.role,realmEntity", "roleRealms.role.permissions"};
        return new UserOut(getEntityById(id, include));
    }

    public UserOut update(int id, UserIn model) throws SystemException {
        UserEntity existUser = getByUsernameOrPhoneNumber(model.getUsername(), model.getPhoneNumber());
        UserEntity entity = getEntityById(id, null);
        if (existUser != null && !Objects.equals(existUser.getId(), entity.getId())) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "username: " + model.getUsername() + " or , phoneNumber: " + model.getPhoneNumber(), 30021);
        }
        modelMapper.map(model, entity);
        updateEntity(entity);
        return new UserOut(entity);
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateUserRoleRealms(int id, List<UserRoleRealmIn> model) throws SystemException {
        String[] include = {"roleRealms"};
        UserEntity userEntity = getEntityById(id, include);
        setUserRoles(userEntity, model);
        updateEntity(userEntity);
    }

    public void delete(int id) throws SystemException {
        if (id != -1) {
            UserEntity userEntity = getEntityById(id, null);
            userEntity.setDeleted(LocalDateTime.now());
            updateEntity(userEntity);
        }
    }

    private void setUserRoles(UserEntity userEntity, List<UserRoleRealmIn> model) {
        Iterator<SecurityUserRoleRealmEntity> roleRealmIterator = userEntity.getRoleRealms().iterator();
        while (roleRealmIterator.hasNext()) {
            SecurityUserRoleRealmEntity eachRoleRealmEntity = roleRealmIterator.next();
            boolean deleteEntity = true;
            Iterator<UserRoleRealmIn> modelIterator = model.iterator();
            while (modelIterator.hasNext()) {
                UserRoleRealmIn eachModel = modelIterator.next();
                if ((eachRoleRealmEntity.getRoleId() == eachModel.getRoleId()) &&
                        (eachRoleRealmEntity.getRealmId() == eachModel.getRealmId())) {
                    deleteEntity = false;
                    modelIterator.remove();
                }
            }
            if (deleteEntity) {
                roleRealmIterator.remove();
            }
        }

        Set<SecurityUserRoleRealmEntity> newRoleRealmEntity = model.stream()
                .map(e -> new SecurityUserRoleRealmEntity(e.getRoleId(), e.getRealmId(), userEntity.getId()))
                .collect(Collectors.toSet());
        userEntity.getRoleRealms().addAll(newRoleRealmEntity);
    }

    public ReportFilter filter(UserFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof UserPageableFilter userPageableFilter) {
            reportOption.setPageNumber(userPageableFilter.getPage());
            reportOption.setPageSize(userPageableFilter.getSize());
            reportOption.setSortOptions(userPageableFilter.getSort());
            reportOption.setExport(userPageableFilter.isExport());
        }
        reportOption.setDistinct(true);

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addNullCondition("deleted");
        reportCondition.addMinNumberCondition("id", 0);

        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("username", filter.getUsername());
        reportCondition.addLikeCondition("phoneNumber", filter.getPhoneNumber());
        reportCondition.addCaseInsensitiveLikeCondition("email", filter.getEmail());
        reportCondition.addEqualCondition("suspended", filter.getSuspended());
        reportCondition.addMinTimeCondition("created", filter.getCreatedMin());
        reportCondition.addMaxTimeCondition("created", filter.getCreatedMax());
        reportCondition.addMinTimeCondition("lockExpired", filter.getLockExpireMin());
        reportCondition.addMaxTimeCondition("lockExpired", filter.getLockExpireMax());
        if (filter.getLock() != null) {
            if (filter.getLock()) {
                reportCondition.addMinTimeCondition("lockExpired", LocalDateTime.now());
            } else {
                ReportCondition orCondition = new ReportCondition();
                orCondition.addMaxTimeCondition("lockExpired", LocalDateTime.now());
                orCondition.addNullCondition("lockExpired");
                reportCondition.setOrCondition(orCondition);
            }
        }

        if (filter.getRoleId() != null || filter.getType() != null) {
            ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("roleRealms", JoinType.INNER);
            joinCondition.addEqualCondition("roleId", filter.getRoleId());
            if (filter.getType() != null) {
                ReportCriteriaJoinCondition roleJoinCondition = new ReportCriteriaJoinCondition("role", JoinType.INNER);
                roleJoinCondition.addEqualCondition("type", filter.getType());
                joinCondition.addJoinCondition(roleJoinCondition);
            }
            reportCondition.addJoinCondition(joinCondition);
        }
        return new ReportFilter(reportCondition, reportOption);
    }

}
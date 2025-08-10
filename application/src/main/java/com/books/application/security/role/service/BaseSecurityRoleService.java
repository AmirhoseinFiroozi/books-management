package com.books.application.security.role.service;

import com.books.application.security.permission.entity.SecurityPermissionEntity;
import com.books.application.security.permission.service.BasePermissionService;
import com.books.application.security.role.dto.RoleFilter;
import com.books.application.security.role.dto.RoleIn;
import com.books.application.security.role.dto.RoleOut;
import com.books.application.security.role.dto.RolePageableFilter;
import com.books.application.security.role.entity.SecurityRoleEntity;
import com.books.application.security.role.repository.BaseRoleDao;
import com.books.database.service.AbstractService;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 16.11.21
 */
@Service
public class BaseSecurityRoleService extends AbstractService<SecurityRoleEntity, BaseRoleDao> {
    private final ModelMapper modelMapper;
    private final BasePermissionService permissionService;

    @Autowired
    public BaseSecurityRoleService(BaseRoleDao dao, ModelMapper modelMapper, BasePermissionService permissionService) {
        super(dao);
        this.modelMapper = modelMapper;
        this.permissionService = permissionService;
    }

    public int count(RoleFilter filter) {
        return countEntity(filter(filter));
    }

    public List<RoleOut> getAll(RolePageableFilter pageableFilter) {
        return getAllEntities(filter(pageableFilter), null).stream().map(RoleOut::new).toList();
    }

    public RoleOut create(RoleIn model) {
        SecurityRoleEntity entity = modelMapper.map(model, SecurityRoleEntity.class);
        List<SecurityPermissionEntity> permissions = permissionService.getEntitiesByIds(model.getPermissionIds());
        entity.setPermissions(new HashSet<>(permissions));
        createEntity(entity);
        return new RoleOut(entity);
    }

    public RoleOut update(int id, RoleIn model) throws SystemException {
        SecurityRoleEntity entity = getEntityById(id, null);
        modelMapper.map(model, entity);
        List<SecurityPermissionEntity> permissions = permissionService.getEntitiesByIds(model.getPermissionIds());
        entity.setPermissions(new HashSet<>(permissions));
        updateEntity(entity);
        return new RoleOut(entity);
    }

    public void delete(int id) throws SystemException {
        if (getDao().hasUser(id)) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "a user with the specified role exits", 100032);
        }
        if (getDao().hasPermission(id)) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "a permission with the specified role exits", 100032);
        }
        deleteById(id);
    }

    private ReportFilter filter(RoleFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof RolePageableFilter pageableFilter) {
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setSortOptions(pageableFilter.getSort());
            reportOption.setExport(pageableFilter.isExport());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("name", filter.getName());
        reportCondition.addEqualCondition("show", filter.getShow());
        reportCondition.addEqualCondition("type", filter.getType());

        return new ReportFilter(reportCondition, reportOption);
    }
}

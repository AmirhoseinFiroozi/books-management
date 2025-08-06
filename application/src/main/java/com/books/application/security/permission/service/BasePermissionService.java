package com.books.application.security.permission.service;

import com.books.application.security.permission.dto.PermissionFilter;
import com.books.application.security.permission.dto.PermissionOut;
import com.books.application.security.permission.dto.PermissionPageableFilter;
import com.books.application.security.permission.entity.SecurityPermissionEntity;
import com.books.database.repository.Dao;
import com.books.database.service.AbstractService;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 16.11.21
 */
@Service
public class BasePermissionService extends AbstractService<SecurityPermissionEntity, Dao<SecurityPermissionEntity>> {

    public int count(PermissionFilter filter) {
        return countEntity(filter(filter));
    }

    public List<PermissionOut> getAll(PermissionPageableFilter pageableFilter) {
        return getAllEntities(filter(pageableFilter), null).stream().map(PermissionOut::new).toList();
    }

    private ReportFilter filter(PermissionFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof PermissionPageableFilter pageableFilter) {
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setSortOptions(pageableFilter.getSort());
            reportOption.setExport(pageableFilter.isExport());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("parentIdFk", filter.getParentIdFk());
        reportCondition.addEqualCondition("nodeType", filter.getNodeType());
        reportCondition.addEqualCondition("traversal", filter.getTraversal());
        reportCondition.addLikeCondition("name", filter.getName());
        reportCondition.addEqualCondition("type", filter.getType());

        return new ReportFilter(reportCondition, reportOption);
    }
}

package com.books.domain.book.shelf.service;

import com.books.database.service.AbstractService;
import com.books.domain.book.shelf.dto.BookShelfFilter;
import com.books.domain.book.shelf.dto.BookShelfIn;
import com.books.domain.book.shelf.dto.BookShelfOut;
import com.books.domain.book.shelf.dto.BookShelfPageableFilter;
import com.books.domain.book.shelf.entity.BookShelfEntity;
import com.books.domain.book.shelf.repository.BookShelfDao;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportCriteriaJoinCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookShelfService extends AbstractService<BookShelfEntity, BookShelfDao> {

    @Autowired
    public BookShelfService(BookShelfDao dao) {
        super(dao);
    }

    public int count(BookShelfFilter filter) {
        return this.countEntity(filter(filter));
    }

    public List<BookShelfOut> getAll(BookShelfPageableFilter pageableFilter) {
        return this.getAllEntities(filter(pageableFilter), null).stream().map(BookShelfOut::new).toList();
    }

    public BookShelfOut create(int userId, BookShelfIn model) {
        BookShelfEntity entity = new BookShelfEntity();
        entity.setName(model.getName());
        entity.setUserId(userId);
        this.createEntity(entity);
        return new BookShelfOut(entity);
    }

    public BookShelfOut update(int id, int userId, BookShelfIn model) throws SystemException {
        BookShelfEntity entity = getEntityById(userId, id);
        entity.setName(model.getName());
        this.updateEntity(entity);
        return new BookShelfOut(entity);
    }

    public void delete(int id, int userId) throws SystemException {
        getEntityById(userId, id);
        deleteById(id);
    }

    private BookShelfEntity getEntityById(int userId, int id) throws SystemException {
        return this.getDao().getById(userId, id).orElseThrow(
                () -> new SystemException(SystemError.DATA_NOT_FOUND, "book shelf doesn't exists", 100020));
    }

    public void bookShelfExistsByUserId(int userId, int id) throws SystemException {
        if (!this.getDao().bookShelfExistsByUserId(userId, id)) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "book shelf doesn't exists", 100021);
        }
    }

    public void deleteEmptyBookShelf(int id) {
        this.getDao().deleteEmptyBookShelf(id);
    }

    private ReportFilter filter(BookShelfFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof BookShelfPageableFilter pageableFilter) {
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setSortOptions(pageableFilter.getSort());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("name", filter.getName());
        reportCondition.addMinTimeCondition("created", filter.getCreatedMin());
        reportCondition.addMaxTimeCondition("created", filter.getCreatedMax());
        reportCondition.addEqualCondition("userId", filter.getUserId());
        ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("user", JoinType.INNER);
        reportCondition.addJoinCondition(joinCondition);

        return new ReportFilter(reportCondition, reportOption);
    }
}

package com.books.book.service;

import com.books.book.dto.*;
import com.books.book.entity.BookEntity;
import com.books.book.respository.BookDao;
import com.books.database.service.AbstractService;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BookService extends AbstractService<BookEntity, BookDao> {
    private final ApplicationProperties applicationProperties;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookDao dao, ApplicationProperties applicationProperties, ModelMapper modelMapper) {
        super(dao);
        this.applicationProperties = applicationProperties;
        this.modelMapper = modelMapper;
    }

    public int count(BookFilter filter) {
        return this.countEntity(filter(filter));
    }

    public List<BookOut> getAll(BookPageableFilter filter) {
        return this.getAllEntities(filter(filter), null).stream()
                .map(BookOut::new).toList();
    }

    public BookOut getById(int userId, int id) throws SystemException {
        return new BookOut(getEntityById(userId, id));
    }

    public BookFileOut getFile(int userId, int id) throws SystemException {
        BookEntity entity = getEntityById(userId, id);
        Path path = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + entity.getFile());
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(path);
                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }
                return new BookFileOut(contentType, resource);
            }
        } catch (Exception e) {
            throw new SystemException(SystemError.FILE_NOT_FOUND, "file is malformed", 3020);
        }
        throw new SystemException(SystemError.FILE_NOT_FOUND, "file does not exists", 3022);
    }

    public void createBook(BookIn model) {
        BookEntity entity = new BookEntity();
        entity.setName(model.getName());
        model.getFile().transferTo();
    }

    private BookEntity getEntityById(int userId, int id) throws SystemException {
        return this.getDao().getById(userId, id).orElseThrow(
                () -> new SystemException(SystemError.DATA_NOT_FOUND, "book doesn't exists", 100006));
    }

    private ReportFilter filter(BookFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof BookPageableFilter pageableFilter) {
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

        return new ReportFilter(reportCondition, reportOption);
    }
}

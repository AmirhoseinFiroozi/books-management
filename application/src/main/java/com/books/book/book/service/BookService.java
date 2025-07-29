package com.books.book.book.service;

import com.books.book.book.dto.*;
import com.books.book.book.entity.BookEntity;
import com.books.book.book.respository.BookDao;
import com.books.book.shelf.service.BookShelfService;
import com.books.database.service.AbstractService;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class BookService extends AbstractService<BookEntity, BookDao> {
    private final ApplicationProperties applicationProperties;
    private final BookShelfService bookShelfService;

    @Autowired
    public BookService(BookDao dao, ApplicationProperties applicationProperties, BookShelfService bookShelfService) {
        super(dao);
        this.applicationProperties = applicationProperties;
        this.bookShelfService = bookShelfService;
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

    public void create(int userId, BookIn model) throws SystemException {
        bookShelfService.bookShelfExistsByUserId(userId, model.getBookShelfId());
        BookEntity entity = new BookEntity();
        entity.setName(model.getName());
        entity.setFile(createFile(model.getFile()));
        entity.setUserId(userId);
        entity.setBookShelfId(model.getBookShelfId());
        this.createEntity(entity);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(int id, int userId, BookUpdateIn model) throws SystemException {
        bookShelfService.bookShelfExistsByUserId(userId, model.getBookShelfId());
        BookEntity entity = getEntityById(userId, id);
        entity.setName(model.getName());
        int oldBookShelfId = entity.getBookShelfId();
        entity.setBookShelfId(model.getBookShelfId());
        if (oldBookShelfId != model.getBookShelfId()) {
            bookShelfService.deleteEmptyBookShelf(oldBookShelfId);
        }
        this.updateEntity(entity);
    }

    public void updateFile(int id, int userId, MultipartFile file) throws SystemException {
        BookEntity entity = getEntityById(userId, id);
        Path staledFilePath = Paths.get(entity.getFile());
        entity.setFile(createFile(file));
        deleteFile(staledFilePath);
        this.updateEntity(entity);
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(int id, int userId) throws SystemException {
        BookEntity entity = getEntityById(userId, id);
        Path staledFilePath = Paths.get(entity.getFile());
        deleteFile(staledFilePath);
        bookShelfService.deleteEmptyBookShelf(entity.getBookShelfId());
        this.deleteEntity(entity);
    }

    private void deleteFile(Path staledFilePath) throws SystemException {
        try {
            Files.delete(staledFilePath);
        } catch (IOException e) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "couldn't remove file: " + staledFilePath, 100010);
        }
    }

    private String createFile(MultipartFile file) throws SystemException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        File destination = new File(applicationProperties.getFileCrud().getBaseFilePath() + fileName);

        boolean directoryMade = destination.getParentFile().mkdirs();
        if (!directoryMade) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "couldn't create directory: " + destination.getParentFile()
                    , 100009);
        }
        try {
            file.transferTo(destination);
        } catch (IOException e) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "couldn't transfer the file", 100008);
        }
        return destination.getAbsolutePath();
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
        reportCondition.addEqualCondition("bookShelfId", filter.getBookShelfId());

        return new ReportFilter(reportCondition, reportOption);
    }
}

package com.books.search.service;

import com.books.domain.book.book.entity.BookEntity;
import com.books.domain.book.book.service.BookService;
import com.books.search.dto.BookSearchFileOut;
import com.books.search.dto.BookSearchOut;
import com.books.search.dto.BookSearchPageableFilter;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BookSearchService {
    private static final String[] INCLUDE = new String[]{"user"};
    private final BookService bookService;
    private final ApplicationProperties applicationProperties;

    public BookSearchService(BookService bookService, ApplicationProperties applicationProperties) {
        this.bookService = bookService;
        this.applicationProperties = applicationProperties;
    }

    public List<BookSearchOut> search(String clause, BookSearchPageableFilter pageableFilter) {
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addCaseInsensitiveLikeCondition("name", clause);
        reportCondition.addEqualCondition("published", true);

        ReportOption reportOption = new ReportOption();
        reportOption.setPageSize(pageableFilter.getSize());
        reportOption.setPageNumber(pageableFilter.getPage());
        reportOption.setSortOptions(pageableFilter.getSort());
        reportOption.setExport(pageableFilter.isExport());

        return bookService.getAllEntities(new ReportFilter(reportCondition, reportOption), INCLUDE).stream()
                .map(BookSearchOut::new).toList();
    }

    public int searchCount(String clause) {
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addCaseInsensitiveLikeCondition("name", clause);
        reportCondition.addEqualCondition("published", true);

        return bookService.countEntity(new ReportFilter(reportCondition, new ReportOption()));
    }

    public BookSearchFileOut downloadBookFile(int id) throws SystemException {
        BookEntity entity = bookService.getEntityById(id, null);
        if (!entity.isPublished()) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "book doesn't exists", 100042);
        }
        Path path = Paths.get(entity.getFile());
        try {
            Resource resource = new FileSystemResource(path);
            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(path);
                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }
                ContentDisposition contentDisposition = ContentDisposition
                        .attachment()
                        .filename(entity.getName()).build();

                return new BookSearchFileOut(contentDisposition.toString(), contentType, resource);
            }
        } catch (Exception ignored) {
        }
        return new BookSearchFileOut();
    }
}

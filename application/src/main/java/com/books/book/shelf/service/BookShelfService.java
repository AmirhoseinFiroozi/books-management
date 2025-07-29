package com.books.book.shelf.service;

import com.books.book.shelf.entity.BookShelfEntity;
import com.books.book.shelf.repository.BookShelfDao;
import com.books.database.service.AbstractService;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookShelfService extends AbstractService<BookShelfEntity, BookShelfDao> {

    @Autowired
    public BookShelfService(BookShelfDao dao) {
        super(dao);
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
}

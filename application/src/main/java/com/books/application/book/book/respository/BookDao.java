package com.books.application.book.book.respository;

import com.books.application.book.book.entity.BookEntity;
import com.books.database.repository.Dao;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookDao extends Dao<BookEntity> {
    public Optional<BookEntity> getById(int userId, int id) {
        Query query = this.getEntityManager().createQuery("SELECT entity FROM BookEntity entity " +
                "where entity.id = :id AND entity.userId = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("id", id);
        List<BookEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }
}

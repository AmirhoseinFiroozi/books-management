package com.books.book.shelf.repository;

import com.books.book.shelf.entity.BookShelfEntity;
import com.books.database.repository.Dao;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookShelfDao extends Dao<BookShelfEntity> {
    public Optional<BookShelfEntity> getById(int userId, int id) {
        Query query = this.getEntityManager().createQuery("SELECT entity FROM BookShelfEntity entity " +
                "where entity.id = :id AND entity.userId = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("id", id);
        List<BookShelfEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    public boolean bookShelfExistsByUserId(int userId, int id) {
        Query query = this.getEntityManager().createQuery("SELECT 1 FROM BookShelfEntity entity " +
                "where entity.id = :id AND entity.userId = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("id", id);
        List<Integer> result = super.queryHql(query, map);
        return !result.isEmpty();
    }

    public void deleteEmptyBookShelf(int id) {
        String sql = "DELETE FROM book_shelf WHERE id_pk = :id\n" +
                "AND NOT EXISTS (SELECT 1 FROM book WHERE book_shelf_id_fk = :id)";
        org.hibernate.query.Query<BookShelfEntity> query = getSession().createNativeQuery(sql, BookShelfEntity.class);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        super.updateSql(query, map);
    }
}

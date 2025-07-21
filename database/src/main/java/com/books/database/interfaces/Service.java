package com.books.database.interfaces;

import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.system.exception.SystemException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Todo T must be restricted to baseEntity.
 *
 * @author imax
 */
public interface Service<T> {

    List<T> getAllEntities(ReportFilter reportFilter, String[] include);

    List<T> getAllEntities(ReportFilter reportFilter, Integer firstResult, Integer maxResult, String[] include);

    List<T> getAllEntitiesLimit(ReportFilter reportFilter, String[] include);

    <R> List<R> getAllEntitiesGroupBy(ReportFilter reportFilter, List<String> groupBy, List<String> selects, Class<R> output);

    <R> List<R> getAllEntitiesGroupBy(ReportFilter reportFilter, List<String> groupBy, Class<R> output);

    int countEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, List<String> selects);

    <R> int countEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, Class<R> source);

    List<T> getAllEntitiesWithoutMaxLimit(ReportFilter reportFilter, String[] include);

    List<T> getAllEntitiesWithFilter(ReportFilter reportFilter, String[] include);

    List<T> getAllEntitiesJoin(ReportFilter reportFilter, String[] include);

    int countEntity(ReportFilter reportFilter);

    T getEntityById(int id, String[] includes) throws SystemException;

    T getEntityById(Object id, String[] includes) throws SystemException;

    Optional<T> optionalById(Object id, String[] includes) throws SystemException;

    T getEntityById(int id, String[] includes, int errorCode) throws SystemException;

    T getEntityById(Object id, String[] includes, int errorCode) throws SystemException;

    T createEntity(T entity);

    T updateEntity(T entity);

    T mergeEntity(T entity);

    void detach(T entity);

    void flush();

    void createOrUpdateEntity(T entity);

    void saveOrUpdateEntityCollection(Collection<T> collection);

    void saveOrUpdateEntity(T entity);

    boolean deleteById(int id);

    boolean deleteById(Object id);

    void deleteEntity(T entity);

    List<T> getEntitiesByIds(List<Integer> ids);
}

package com.books.security.repository;

import com.books.database.repository.Dao;
import com.books.security.dto.SecurityAccessRule;
import com.books.utility.commons.dto.UserContextDto;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;


/**
 * @author unascribed
 * @author imax
 */
@Repository
public class RestBaseDao extends Dao<UserContextDto> {

    public List<SecurityAccessRule> listRestsWithPermissions() {
        String sql =
                "select security_rest.http_method as \"httpMethod\"," +
                        " security_rest.url," +
                        " array_to_string(array_agg(distinct security_permission_rest.permission_id_fk), ',') as access" +
                        " from map.security_rest security_rest" +
                        " left join map.security_permission_rest security_permission_rest" +
                        " on security_rest.id_pk = security_permission_rest.rest_id_fk" +
                        " group by security_rest.id_pk";
        Query<SecurityAccessRule> query = getSession().createNativeQuery(sql, SecurityAccessRule.class);
        List<SecurityAccessRule> result = querySql(query, new HashMap<>(), SecurityAccessRule.class);
        return result.isEmpty() ? null : result;
    }
}
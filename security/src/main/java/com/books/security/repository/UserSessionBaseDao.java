package com.books.security.repository;

import com.books.database.repository.Dao;
import com.books.utility.commons.dto.UserContextDto;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author unascribed
 * @author imax
 */
@Repository
public class UserSessionBaseDao extends Dao<UserContextDto> {

    public UserContextDto getSessionWithUser(int id) {
        String sql = "select user_session.id_pk as \"sessionId\"," +
                " users.id_pk as \"id\"," +
                " users.suspended," +
                " users.lock_expired as \"lockExpired\"," +
                " users.username as \"username\"," +
                " array_to_string(array_agg(distinct security_role_permission.permission_id_fk), ',') as \"permissionIds\"" +
                " from user_session user_session " +
                " inner join users users " +
                " on user_session.user_id_fk = users.id_pk" +
                " left join security_user_role_realm user_role_realm" +
                " on users.id_pk = user_role_realm.user_id_fk" +
                " left join security_role_permission security_role_permission" +
                " on  user_role_realm.role_id_fk = security_role_permission.role_id_fk" +
                " where user_session.id_pk =:id and users.deleted is null" +
                " group by user_session.id_pk,users.id_pk";

        Query<UserContextDto> query = getSession().createNativeQuery(sql, UserContextDto.class);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        List<UserContextDto> result = querySql(query, parameters, UserContextDto.class);
        return result.isEmpty() ? null : result.getFirst();
    }

    public UserContextDto getSimpleSessionWithUser(int id) {
        String sql = "select user_session.id_pk as \"sessionId\"," +
                " users.id_pk as \"id\"," +
                " users.suspended," +
                " users.lock_expired as \"lockExpired\"," +
                " users.username as \"username\"," +
                " '' as \"permissionIds\"" +
                " from user_session user_session " +
                " inner join users users " +
                " on user_session.user_id_fk = users.id_pk" +
                " where user_session.id_pk =:id and users.deleted is null";

        Query<UserContextDto> query = getSession().createNativeQuery(sql, UserContextDto.class);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        List<UserContextDto> result = querySql(query, parameters, UserContextDto.class);
        return result.isEmpty() ? null : result.getFirst();
    }
}

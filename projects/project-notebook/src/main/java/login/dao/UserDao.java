package login.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * User data access object.
 * @author mac
 * */
@Mapper
@Repository
public interface UserDao {

    String TABLE_NAME = "tnb_user";
    String INSERT_FIELDS = "";
    String SELECT_FIELDS = "";
}

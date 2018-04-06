package itcast.research.repository.user;

import itcast.research.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:14
 * Description: 用户数据库操作接口
 */
public interface IUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
}

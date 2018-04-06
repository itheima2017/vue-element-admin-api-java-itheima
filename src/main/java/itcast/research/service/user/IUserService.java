package itcast.research.service.user;

import itcast.research.entity.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:49
 * Description: 用户服务接口
 */
public interface IUserService {
    /**
     * 分页获取用户列表
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param id       用户ID
     * @param username 用户名称
     * @param sort     排序
     * @return 用户列表
     * @throws Exception
     */
    Page<User> findAllByPage(int page, int pageSize, Long id, String username, String sort) throws Exception;

    /**
     * 获取简单用户列表
     *
     * @param username 用户名称
     * @param sort     排序
     * @return 简单用户列表
     * @throws Exception
     */
    List<User> findAllBySimple(String username, String sort) throws Exception;

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @throws Exception
     */
    void save(User user) throws Exception;

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 用户信息
     * @throws Exception
     */
    User update(User user) throws Exception;

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @throws Exception
     */
    void del(Long id) throws Exception;

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     * @throws Exception
     */
    User findById(Long id) throws Exception;

    /**
     * 通过邮箱获取用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);
}

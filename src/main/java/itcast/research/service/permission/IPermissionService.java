package itcast.research.service.permission;

import itcast.research.entity.permission.Permission;

import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:50
 * Description: 权限服务接口
 */
public interface IPermissionService {
    /**
     * 添加权限
     *
     * @param permission 权限信息
     * @throws Exception
     */
    void save(Permission permission) throws Exception;
    /**
     * 获取权限列表
     *
     * @param sort 排序规则
     * @return 权限列表
     * @throws Exception
     */
    List<Permission> findAll(String sort) throws Exception;

    /**
     * 获取需校验API权限列表
     *
     * @return API权限列表
     * @throws Exception
     */
    List<Permission> findAllByCheckApi();

    /**
     * 获取全部API权限列表
     *
     * @return
     * @throws Exception
     */
    List<Permission> findAllByApi() throws Exception;

    /**
     * 更新权限
     *
     * @param permission 权限信息
     * @throws Exception
     */
    Permission update(Permission permission) throws Exception;

    /**
     * 获取权限详情
     *
     * @param id 权限ID
     * @return 权限详情
     * @throws Exception
     */
    Permission findOne(Long id) throws Exception;

    /**
     * 删除权限
     *
     * @param id 权限ID
     * @throws Exception
     */
    void del(Long id) throws Exception;

    /**
     * 批量添加API权限列表
     *
     * @throws Exception
     */
    void saveApiPermissions(List<Permission> permissionList) throws Exception;
}

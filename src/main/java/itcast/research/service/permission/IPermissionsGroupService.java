package itcast.research.service.permission;

import itcast.research.entity.permission.PermissionGroup;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 10:34
 * Description: 权限组服务接口
 */
public interface IPermissionsGroupService {
    /**
     * 分页获取权限组列表
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param sort     排序条件
     * @return 权限组列表
     * @throws Exception
     */
    Page<PermissionGroup> findPermissionGroupByPage(int page, int pageSize, String title, String sort) throws Exception;

    /**
     * 获取全部权限组
     *
     * @return 全部权限组
     * @throws Exception
     */
    List<PermissionGroup> findAll() throws Exception;

    /**
     * 添加权限组
     *
     * @param permissionGroup 权限组信息
     * @throws Exception
     */
    void save(PermissionGroup permissionGroup) throws Exception;

    /**
     * 更新权限组
     *
     * @param permissionGroup 权限组信息
     * @return 权限组信息
     * @throws Exception
     */
    PermissionGroup update(PermissionGroup permissionGroup) throws Exception;

    /**
     * 更新API权限组
     *
     * @param permissionGroup 权限组信息
     * @return 权限组信息
     * @throws Exception
     */
    PermissionGroup updateApi(PermissionGroup permissionGroup) throws Exception;

    /**
     * 删除权限组
     *
     * @param id 权限组ID
     * @throws Exception
     */
    void del(Long id) throws Exception;

    /**
     * 获取权限组详情
     *
     * @param id 权限组ID
     * @return 权限组详情
     * @throws Exception
     */
    PermissionGroup findOne(Long id) throws Exception;

    /**
     * 根据用户ID获取权限组信息
     *
     * @param uid 用户ID
     * @return 权限组信息
     * @throws Exception
     */
    PermissionGroup findOneByUid(Long uid) throws Exception;
}

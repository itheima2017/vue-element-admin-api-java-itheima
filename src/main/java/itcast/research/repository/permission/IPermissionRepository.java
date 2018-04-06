package itcast.research.repository.permission;

import itcast.research.entity.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:15
 * Description: 权限数据库操作接口
 */
public interface IPermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
}

package itcast.research.repository.permission;

import itcast.research.entity.permission.PermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 10:19
 * Description: 权限组数据库操作接口
 */
public interface IPermissionGroupRepository extends JpaRepository<PermissionGroup, Long>, JpaSpecificationExecutor<PermissionGroup> {
}

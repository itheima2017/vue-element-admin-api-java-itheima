package itcast.research.repository.permission;

import itcast.research.entity.permission.PermissionApiExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:17
 * Description: API权限数据库操作接口
 */
public interface IPermissionApiExtendRepository extends JpaRepository<PermissionApiExtend, Long>, JpaSpecificationExecutor<PermissionApiExtend> {
}

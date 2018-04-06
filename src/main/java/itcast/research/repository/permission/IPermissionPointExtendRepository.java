package itcast.research.repository.permission;

import itcast.research.entity.permission.PermissionPointExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:19
 * Description: 点权限数据库操作接口
 */
public interface IPermissionPointExtendRepository extends JpaRepository<PermissionPointExtend, Long>, JpaSpecificationExecutor<PermissionPointExtend> {
}

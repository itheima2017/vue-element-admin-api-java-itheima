package itcast.research.repository.permission;

import itcast.research.entity.permission.PermissionMenuExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:18
 * Description: 菜单权限数据库接口
 */
public interface IPermissionMenuExtendRepository extends JpaRepository<PermissionMenuExtend, Long>, JpaSpecificationExecutor<PermissionMenuExtend> {
}

package itcast.research.repository.other;

import itcast.research.entity.other.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 14:25
 * Description: 日志数据库操作接口
 */
public interface ILogRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {
}

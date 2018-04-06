package itcast.research.service.other;

import itcast.research.entity.other.Log;
import org.springframework.data.domain.Page;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 14:26
 * Description: 日志服务接口
 */
public interface ILogService {
    /**
     * 添加日志
     *
     * @param log 日志信息
     */
    void save(Log log) throws Exception;

    /**
     * 分页获取日志列表
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param userId   用户ID
     * @param keyword  关键词
     * @param sort     排序
     * @return 日志列表
     * @throws Exception
     */
    Page<Log> findAllByPage(int page, int pageSize, Long userId, String keyword, String sort) throws Exception;
}

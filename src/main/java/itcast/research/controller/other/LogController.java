package itcast.research.controller.other;

import itcast.research.entity.other.Log;
import itcast.research.entity.user.User;
import itcast.research.exception.CommonException;
import itcast.research.service.other.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 16:36
 * Description: 日志控制器
 */
@RestController
public class LogController {
    @Autowired
    private ILogService logService;

    @PreAuthorize("hasAuthority('GET|/base/logs')")
    @RequestMapping(name = "check分页获取日志列表", value = "/base/logs", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findAllByPage(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize, @RequestParam(name = "user_id", required = false) Long userId, @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "sort", required = false) String sort) throws Exception {
        Page<Log> logPage = logService.findAllByPage(page, pageSize, userId, keyword, sort);
        if (logPage == null) {
            throw new CommonException("获取日志失败！");
        }
        //处理返回信息
        Map<String, Object> map = new HashMap<>();
        map.put("counts", logPage.getTotalElements());
        map.put("pagesize", pageSize);
        map.put("pages", logPage.getTotalPages());
        map.put("page", page);
        //处理用户数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (Log log : logPage.getContent()) {
            list.add(formatOutLog(log));
        }
        map.put("list", list);
        return Mono.just(map);
    }

    /**
     * 格式化日志输出信息
     *
     * @param log 日志信息
     * @return 日志输出信息
     */
    private Map<String, Object> formatOutLog(Log log) {
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("id", log.getId() == null ? 0 : log.getId());
        logMap.put("method", log.getMethod() == null ? "" : log.getMethod());
        logMap.put("request_body", log.getRequestBody() == null ? "" : log.getRequestBody());
        logMap.put("url", log.getUrl() == null ? "" : log.getUrl());
        logMap.put("operation_type", log.getOperationResult() == null ? false : log.getOperationResult());
        if (log.getUser() != null) {
            User user = log.getUser();
            logMap.put("user_name", user.getUsername() == null ? "" : user.getUsername());
            logMap.put("user_id", user.getId() == null ? 0 : user.getId());
        }
        return logMap;
    }
}

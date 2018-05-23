package itcast.research.handler;

import itcast.research.entity.other.Log;
import itcast.research.entity.user.User;
import itcast.research.service.other.ILogService;
import itcast.research.service.user.IUserService;
import itcast.research.util.DBLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 15:16
 * Description: 控制器切面
 */
@Aspect
@Component
@Slf4j
public class ControllerInterceptor {
    @Autowired
    private IUserService userService;
    @Autowired
    private ILogService logService;

    /**
     * 匹配controller包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(* itcast.research.controller..*.*(..))")
    public void executeService() {

    }

    /**
     * 方法执行成功通知
     *
     * @throws Throwable
     */
    @AfterReturning(value = "executeService()")
    public void doAfter(JoinPoint jp) {
        saveLog(jp, true);
    }

    /**
     * 方法执行失败通知
     */
    @AfterThrowing(value = "executeService()")
    public void afterThrowing(JoinPoint jp) {
        saveLog(jp, false);
    }

    /**
     * 保存日志
     * @param jp 方法信息
     * @param isSuc 操作结果标记
     */
    public void saveLog(JoinPoint jp, boolean isSuc) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String body = Arrays.toString(jp.getArgs());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        Log log = new Log();
        log.setUser(user);
        log.setMethod(method);
        log.setUrl(uri);
        log.setOperationDate(new Date());
        log.setRequestBody(body);
        log.setOperationResult(isSuc);
        DBLogUtil.getInstance().setLogService(logService).write(log);
    }
}

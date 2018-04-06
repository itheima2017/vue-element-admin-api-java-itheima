package itcast.research.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 9:34
 * Description: 统一异常处理类
 */
@RestControllerAdvice
public class ExceptionHandlers {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(Exception.class)
    public String serverExceptionHandler(HttpServletResponse httpServletResponse, Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        ObjectMapper om = new ObjectMapper();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        int errCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (ex.getClass() == AccessDeniedException.class) {
            errCode = HttpStatus.FORBIDDEN.value();
        }
        httpServletResponse.setStatus(errCode);
        try {
            return om.writeValueAsString(new ReturnValue(errCode, ex.getMessage()));
        } catch (JsonProcessingException e) {
            return "{\"err_code\":500,\"message\":\"信息处理错误！\"}";
        }
    }
}

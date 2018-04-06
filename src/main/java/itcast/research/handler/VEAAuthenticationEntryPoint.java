package itcast.research.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import itcast.research.exception.ReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/29 14:47
 * Description:
 */
@Component
@Slf4j
public class VEAAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public VEAAuthenticationEntryPoint() {
        super("/base/frame/login");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("未登录跳转...");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ReturnValue rv = new ReturnValue();
        rv.setErr_code(401);
        rv.setMessage("用户未登录，请登录");
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), rv);
    }
}

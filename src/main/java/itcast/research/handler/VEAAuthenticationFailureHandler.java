package itcast.research.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import itcast.research.exception.ReturnValue;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/29 10:40
 * Description:
 */
@Component
public class VEAAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ReturnValue rv = new ReturnValue();
        rv.setErr_code(401);
        rv.setMessage("用户登录失败,用户名或密码错误");
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), rv);
    }
}

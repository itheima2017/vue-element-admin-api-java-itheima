package itcast.research.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/29 14:39
 * Description:
 */
@Component
public class VEALogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "注销成功！");
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), map);
    }
}

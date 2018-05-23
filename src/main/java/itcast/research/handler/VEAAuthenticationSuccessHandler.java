package itcast.research.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import itcast.research.config.JWTConstants;
import itcast.research.entity.other.Log;
import itcast.research.service.other.ILogService;
import itcast.research.service.user.IUserService;
import itcast.research.util.DBLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/29 10:57
 * Description:
 */
@Component
@Slf4j
public class VEAAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private IUserService userService;
    @Autowired
    private ILogService logService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功..." + authentication.getPrincipal());
        String username = String.valueOf(authentication.getPrincipal());
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, JWTConstants.SECRET)
                .compact();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), map);
        List<String> list=new ArrayList<>();
        Log log = new Log();
        log.setOperationDate(new Date());
        log.setUrl("/base/frame/login");
        log.setMethod(RequestMethod.POST.name());
        log.setOperationResult(true);
        log.setUser(userService.findByEmail(username));
        DBLogUtil.getInstance().setLogService(logService).write(log);
    }
}

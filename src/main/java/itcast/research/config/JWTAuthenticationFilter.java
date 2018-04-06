package itcast.research.config;

import itcast.research.service.user.impl.UserDetailsServiceImpl;
import itcast.research.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/28 17:40
 * Description: jwt校验过滤器
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private UserDetailsServiceImpl userDetailsService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTConstants.AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(JWTConstants.AUTHORIZATION_PRE)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = JwtUtil.getAuthentication(request, userDetailsService);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }
}

package itcast.research.util;

import io.jsonwebtoken.Jwts;
import itcast.research.config.JWTConstants;
import itcast.research.service.user.impl.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 11:58
 * Description: jwt工具
 */
public class JwtUtil {

    public static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, UserDetailsServiceImpl userDetailsService) {
        String token = request.getHeader(JWTConstants.AUTHORIZATION_HEADER);
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(JWTConstants.SECRET)
                    .parseClaimsJws(token.replace(JWTConstants.AUTHORIZATION_PRE, ""))
                    .getBody()
                    .getSubject();
            if (user != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user);
                return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            }
        }
        return null;
    }
}

package itcast.research.config;

import itcast.research.handler.VEAAuthenticationEntryPoint;
import itcast.research.handler.VEAAuthenticationFailureHandler;
import itcast.research.handler.VEAAuthenticationSuccessHandler;
import itcast.research.handler.VEALogoutSuccessHandler;
import itcast.research.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/28 17:41
 * Description: 安全配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private VEAAuthenticationFailureHandler veaAuthenticationFailureHandler;
    @Autowired
    private VEAAuthenticationSuccessHandler veaAuthenticationSuccessHandler;
    @Autowired
    private VEALogoutSuccessHandler veaLogoutSuccessHandler;
    @Autowired
    private VEAAuthenticationEntryPoint veaAuthenticationEntryPoint;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/base/frame/login", "/login").permitAll()
                .anyRequest().authenticated()  // 所有请求需要身份认证
                .and()
                .formLogin().loginProcessingUrl("/base/frame/login").successHandler(veaAuthenticationSuccessHandler).failureHandler(veaAuthenticationFailureHandler).permitAll()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userDetailsService))
                .logout().logoutUrl("/base/frame/logout").logoutSuccessHandler(veaLogoutSuccessHandler).permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(veaAuthenticationEntryPoint);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.authenticationProvider(customAuthenticationProvider);
    }
}

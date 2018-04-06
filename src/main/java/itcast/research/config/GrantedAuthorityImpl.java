package itcast.research.config;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/29 9:12
 * Description: 存储权限和角色实现
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}

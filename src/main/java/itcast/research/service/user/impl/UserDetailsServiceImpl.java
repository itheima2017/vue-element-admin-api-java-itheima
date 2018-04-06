package itcast.research.service.user.impl;

import itcast.research.config.GrantedAuthorityImpl;
import itcast.research.entity.permission.Permission;
import itcast.research.entity.permission.PermissionApiExtend;
import itcast.research.entity.permission.PermissionGroup;
import itcast.research.entity.user.User;
import itcast.research.service.user.IUserService;
import itcast.research.util.EUserStatus;
import itcast.research.util.permission.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/28 17:54
 * Description:
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user != null && user.getStatus() != EUserStatus.DISABLE.getCode()) {
            PermissionGroup permissionGroup = user.getPermissionGroup();
            if (permissionGroup != null) {
                Set<Permission> permissionSet = permissionGroup.getPermissions();
                if (permissionSet != null) {
                    for (Permission permission : permissionSet) {
                        if (PermissionConstants.PERMISSION_TYPE_API == permission.getType()) {
                            //此为API权限
                            PermissionApiExtend permissionApiExtend = permission.getPermissionApiExtend();
                            GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(permissionApiExtend.getApiMethod() + "|" + permissionApiExtend.getApiUrl());
                            authorities.add(grantedAuthority);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }
        return null;
    }
}

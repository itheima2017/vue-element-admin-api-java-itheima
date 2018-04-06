package itcast.research.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import itcast.research.entity.permission.Permission;
import itcast.research.entity.permission.PermissionGroup;
import itcast.research.entity.user.User;
import itcast.research.exception.CommonException;
import itcast.research.service.user.IUserService;
import itcast.research.util.DateUtil;
import itcast.research.util.VEAStringUtil;
import itcast.research.util.permission.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 14:29
 * Description: 用户控制器
 */
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 分页获取用户列表
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param id       用户ID
     * @param username 用户名称
     * @param sort     排序条件
     * @return 用户列表
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/users')")
    @RequestMapping(name = "check分页获取用户列表", value = "/base/users", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findAllByPage(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize, @RequestParam(name = "id", required = false) Long id, @RequestParam(name = "username", required = false) String username, @RequestParam(name = "sort", required = false) String sort) throws Exception {
        Page<User> userPage = userService.findAllByPage(page, pageSize, id, username, sort);
        if (userPage == null) {
            throw new CommonException("查找用户失败！");
        }
        //处理返回信息
        Map<String, Object> map = new HashMap<>();
        map.put("counts", userPage.getTotalElements());
        map.put("pagesize", pageSize);
        map.put("pages", userPage.getTotalPages());
        map.put("page", page);
        //处理用户数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (User user : userPage.getContent()) {
            list.add(formatOutUser(user));
        }
        map.put("list", list);
        return Mono.just(map);
    }

    /**
     * 获取用户简单列表
     *
     * @param username 用户名称
     * @param sort     排序
     * @return 用户简单列表
     * @throws Exception
     */
    @RequestMapping(name = "获取用户简单列表", value = "/base/users/simple", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Flux<Map<String, Object>> findAllBySimple(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "sort", required = false) String sort) throws Exception {
        List<User> userList = userService.findAllBySimple(username, sort);
        List<Map<String, Object>> results = new ArrayList<>();
        for (User user : userList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            results.add(map);
        }
        return Flux.fromIterable(results);
    }

    /**
     * 添加用户
     *
     * @param strBody 用户信息
     * @return 用户信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('POST|/base/users')")
    @RequestMapping(name = "check添加用户", value = "/base/users", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> save(@RequestBody String strBody) throws Exception {
        User user = formatInUser(strBody);
        if (VEAStringUtil.isBlank(user.getPassword()) || VEAStringUtil.isBlank(user.getUsername()) || VEAStringUtil.isBlank(user.getEmail())) {
            throw new CommonException("用户名或密码或邮箱不能为空！");
        }
        userService.save(user);
        return Mono.just(formatOutUser(user));
    }

    /**
     * 更新用户信息
     *
     * @param strBody 用户信息
     * @return 用户信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('PUT|/base/users/{id}')")
    @RequestMapping(name = "check更新用户信息", value = "/base/users/{id}", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> update(@RequestBody String strBody) throws Exception {
        User user = formatInUser(strBody);
        User resultUser = userService.update(user);
        return Mono.just(formatOutUser(resultUser));
    }

    /**
     * 用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/users/{id}')")
    @RequestMapping(name = "check根据ID获取用户详情", value = "/base/users/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findOne(@PathVariable(name = "id") Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误！");
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new CommonException("用户不存在！");
        }
        return Mono.just(formatOutUser(user));
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('DELETE|/base/users/{id}')")
    @RequestMapping(name = "check删除用户", value = "/base/users/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> delete(@PathVariable(name = "id") Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误！");
        }
        userService.del(id);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        return Mono.just(resultMap);
    }

    /**
     * 获取用户信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(name = "获取用户信息", value = "/base/frame/profile", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> profile() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CommonException("获取用户信息失败！");
        }
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getUsername() == null ? "" : user.getUsername());
        map.put("avatar", user.getAvatar() == null ? "" : user.getAvatar());
        map.put("introduction", user.getIntroduction() == null ? "" : user.getIntroduction());
        Map<String,Object> rolesMap=new HashMap<>();
        List<String> menus = new ArrayList<>();
        List<String> points = new ArrayList<>();
        if (user.getPermissionGroup() != null && user.getPermissionGroup().getPermissions() != null) {
            Set<Permission> permissionSet = user.getPermissionGroup().getPermissions();
            for (Permission permission : permissionSet) {
                if (permission.getType() == PermissionConstants.PERMISSION_TYPE_MENU) {
                    menus.add(permission.getPermissionMenuExtend().getCode());
                } else if (permission.getType() == PermissionConstants.PERMISSION_TYPE_POINT) {
                    menus.add(permission.getPermissionPointExtend().getCode());
                }
            }
        }
        rolesMap.put("menus", menus);
        rolesMap.put("points", points);
        map.put("roles",rolesMap);
        return Mono.just(map);
    }

    /**
     * 格式化用户输入信息
     *
     * @param strBody 用户信息
     * @return 用户信息
     * @throws Exception
     */
    private User formatInUser(String strBody) throws Exception {
        if (VEAStringUtil.isBlank(strBody)) {
            throw new CommonException("参数错误！");
        }
        User user = new User();
        JSONObject jsonBody = JSON.parseObject(strBody);
        //处理ID信息
        if (jsonBody.containsKey("id")) {
            user.setId(jsonBody.getLong("id"));
        }
        //处理Email信息
        if (jsonBody.containsKey("email")) {
            user.setEmail(jsonBody.getString("email"));
        }
        //处理密码信息
        if (jsonBody.containsKey("password")) {
            user.setPassword(jsonBody.getString("password"));
        }
        //处理电话信息
        if (jsonBody.containsKey("phone")) {
            user.setPhone(jsonBody.getString("phone"));
        }
        //处理用户名称信息
        if (jsonBody.containsKey("username")) {
            user.setUsername(jsonBody.getString("username"));
        }
        //处理头像信息
        if (jsonBody.containsKey("avatar")) {
            user.setAvatar(jsonBody.getString("avatar"));
        }
        //处理用户介绍
        if (jsonBody.containsKey("introduction")) {
            user.setIntroduction(jsonBody.getString("introduction"));
        }
        //处理权限组信息
        if (jsonBody.containsKey("permission_group_id")) {
            PermissionGroup group = new PermissionGroup();
            group.setId(jsonBody.getLong("permission_group_id"));
            user.setPermissionGroup(group);
        }
        return user;
    }

    /**
     * 格式化用户输出信息
     *
     * @param user 用户信息
     * @return 用户输出信息
     */
    private Map<String, Object> formatOutUser(User user) throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId() == null ? 0 : user.getId());
        userMap.put("email", user.getEmail() == null ? "" : user.getEmail());
        userMap.put("phone", user.getPhone() == null ? "" : user.getPhone());
        userMap.put("username", user.getUsername() == null ? "" : user.getUsername());
        userMap.put("avatar", user.getAvatar() == null ? "" : user.getAvatar());
        userMap.put("introduction", user.getIntroduction() == null ? "" : user.getIntroduction());
        userMap.put("create_time", DateUtil.parseDate2String(user.getCreateTime()));
        userMap.put("last_update_time", DateUtil.parseDate2String(user.getLastUpdateTime()));
        userMap.put("permission_group_id", user.getPermissionGroup() == null ? 0 : user.getPermissionGroup().getId() == null ? 0 : user.getPermissionGroup().getId());
        userMap.put("permission_group_title", user.getPermissionGroup() == null ? "" : user.getPermissionGroup().getName() == null ? "" : user.getPermissionGroup().getName());
        userMap.put("is_deleted", user.getStatus());
        return userMap;
    }
}

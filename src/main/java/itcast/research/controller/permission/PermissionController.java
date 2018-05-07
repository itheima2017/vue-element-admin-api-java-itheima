package itcast.research.controller.permission;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import itcast.research.entity.permission.Permission;
import itcast.research.entity.permission.PermissionMenuExtend;
import itcast.research.entity.permission.PermissionPointExtend;
import itcast.research.exception.CommonException;
import itcast.research.service.permission.IPermissionService;
import itcast.research.util.VEAStringUtil;
import itcast.research.util.permission.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 9:09
 * Description: 权限控制器
 */
@RestController
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    /**
     * 获取全部权限
     *
     * @param sort 排序条件
     * @return 权限列表
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/menus')")
    @RequestMapping(name = "check获取全部权限", value = "/base/menus", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Flux<Map<String, Object>> findAll(@RequestParam(name = "sort", required = false) String sort) throws Exception {
        List<Permission> permissionList = permissionService.findAll(sort);
        return Flux.fromIterable(getNode(permissionList));
    }

    /**
     * 添加权限
     *
     * @param strBody 权限信息
     * @return 权限信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('POST|/base/menus')")
    @RequestMapping(name = "check添加权限", value = "/base/menus", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> save(@RequestBody String strBody) throws Exception {
        if (StringUtils.isEmpty(strBody)) {
            throw new CommonException("参数错误！");
        }
        Permission permission = formatInPermission(strBody);
        permissionService.save(permission);
        return Mono.just(formatOutPermission(permission));
    }

    /**
     * 更新权限信息
     *
     * @param strBody 权限信息
     * @return 权限信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('PUT|/base/menus/{id}')")
    @RequestMapping(name = "check更新权限信息", value = "/base/menus/{id}", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> update(@RequestBody String strBody) throws Exception {
        if (StringUtils.isEmpty(strBody)) {
            throw new CommonException("参数错误！");
        }
        Permission permission = formatInPermission(strBody);
        Permission result = permissionService.update(permission);
        return Mono.just(formatOutPermission(result));
    }

    /**
     * 删除权限
     *
     * @param id 权限ID
     * @return 返回信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('DELETE|/base/menus/{id}')")
    @RequestMapping(name = "check删除权限", value = "/base/menus/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> delete(@PathVariable(name = "id") Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误");
        }
        permissionService.del(id);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        return Mono.just(resultMap);
    }

    /**
     * 权限详情
     *
     * @param id 权限ID
     * @return 权限详情信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/menus/{id}')")
    @RequestMapping(name = "check根据ID获取权限详情", value = "/base/menus/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findOne(@PathVariable(name = "id") Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误");
        }
        Permission permission = permissionService.findOne(id);
        if (permission == null) {
            throw new CommonException("权限不存在！");
        }
        return Mono.just(formatOutPermission(permission));
    }

    /**
     * 格式化输入权限信息
     *
     * @param strBody 原始权限信息
     * @return 格式化后权限信息
     * @throws Exception
     */
    private Permission formatInPermission(String strBody) throws Exception {
        Permission permission = new Permission();
        JSONObject jsonBody = JSON.parseObject(strBody);
        //填充父权限信息
        if (jsonBody.containsKey("pid") && jsonBody.getLong("pid") > 0) {
            Long pid = jsonBody.getLong("pid");
            Permission parentPermission = new Permission();
            parentPermission.setId(pid);
            permission.setParentPermission(parentPermission);
        }
        //填充权限ID
        if (jsonBody.containsKey("id")) {
            permission.setId(jsonBody.getLong("id"));
        }
        //填充权限名称
        if (jsonBody.containsKey("title")) {
            permission.setName(jsonBody.getString("title"));
        }
        //区分扩展信息类型
        boolean isPoint = jsonBody.getBoolean("is_point");
        if (!isPoint) {
            //填充菜单扩展信息
            permission.setType(PermissionConstants.PERMISSION_TYPE_MENU);
            PermissionMenuExtend menuExtend = new PermissionMenuExtend();
            menuExtend.setCode(jsonBody.getString("code"));
            permission.setPermissionMenuExtend(menuExtend);
        } else {
            //填充权限点扩展信息
            permission.setType(PermissionConstants.PERMISSION_TYPE_POINT);
            PermissionPointExtend pointExtend = new PermissionPointExtend();
            pointExtend.setCode(jsonBody.getString("code"));
            permission.setPermissionPointExtend(pointExtend);
        }
        return permission;
    }

    /**
     * 格式化输出权限信息
     *
     * @param permission 原始权限信息
     * @return 格式化后权限信息
     * @throws Exception
     */
    private Map<String, Object> formatOutPermission(Permission permission) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", permission.getId());
        if (permission.getParentPermission() != null) {
            map.put("pid", permission.getParentPermission().getId());
        }
        boolean isPoint = permission.getType().equals(PermissionConstants.PERMISSION_TYPE_POINT);
        map.put("is_point", isPoint ? true : false);
        String code = isPoint ? permission.getPermissionPointExtend().getCode() : permission.getPermissionMenuExtend().getCode();
        map.put("code", VEAStringUtil.isBlank(code) ? "" : code);
        map.put("title", permission.getName());
        return map;
    }

    /**
     * 递归处理返回数据
     *
     * @param permissionList 权限列表
     * @return 返回数据
     * @throws Exception
     */
    private List<Map<String, Object>> getNode(List<Permission> permissionList) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < permissionList.size(); i++) {
            Permission permission = permissionList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", permission.getId());
            map.put("pid", permission.getParentPermission() == null ? 0 : permission.getParentPermission().getId());
            map.put("title", permission.getName());
            if (permission.getType().equals(PermissionConstants.PERMISSION_TYPE_MENU)) {
                PermissionMenuExtend menuExtend = permission.getPermissionMenuExtend();
                map.put("code", menuExtend.getCode() == null ? "" : menuExtend.getCode());
                map.put("is_point", false);
            } else if (permission.getType().equals(PermissionConstants.PERMISSION_TYPE_POINT)) {
                PermissionPointExtend pointExtend = permission.getPermissionPointExtend();
                map.put("code", pointExtend.getCode());
                map.put("is_point", true);
            }
            if (permission.getChildrenPermission() != null && permission.getChildrenPermission().size() > 0) {
                Set<Permission> permissionSet = permission.getChildrenPermission();
                List<Permission> subMenuList = new ArrayList<>();
                List<Permission> subPointList = new ArrayList<>();
                for (Permission per : permissionSet) {
                    if (per.getType().equals(PermissionConstants.PERMISSION_TYPE_MENU)) {
                        subMenuList.add(per);
                    } else {
                        subPointList.add(per);
                    }
                }
                //对菜单权限列表进行排序 根据排序字段正序
                if (subMenuList.size() > 0) {
                    map.put("childs", getNode(subMenuList));
                }
                if (subPointList.size() > 0) {
                    map.put("points", getNode(subPointList));
                }
            }
            result.add(map);
        }
        return result;
    }
}

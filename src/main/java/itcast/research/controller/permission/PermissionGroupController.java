package itcast.research.controller.permission;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import itcast.research.entity.permission.Permission;
import itcast.research.entity.permission.PermissionApiExtend;
import itcast.research.entity.permission.PermissionGroup;
import itcast.research.exception.CommonException;
import itcast.research.service.permission.IPermissionService;
import itcast.research.service.permission.IPermissionsGroupService;
import itcast.research.util.DateUtil;
import itcast.research.util.permission.PermissionConstants;
import itcast.research.util.VEAStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 11:09
 * Description: 权限组控制器
 */
@RestController
public class PermissionGroupController {
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IPermissionsGroupService permissionsGroupService;

    /**
     * 分页获取权限组列表
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param sort     排序条件
     * @return 权限组列表
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/permissions')")
    @RequestMapping(name = "check分页获取权限组列表", value = "/base/permissions", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findPermissionGroupByPage(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize, @RequestParam(name = "title", required = false) String title, @RequestParam(name = "sort", required = false) String sort) throws Exception {
        //获取数据
        Page<PermissionGroup> permissionGroupPage = permissionsGroupService.findPermissionGroupByPage(page, pageSize, title, sort);
        if (permissionGroupPage == null) {
            throw new CommonException("查询失败！");
        }
        //处理返回信息
        Map<String, Object> map = new HashMap<>();
        map.put("counts", permissionGroupPage.getTotalElements());
        map.put("pagesize", pageSize);
        map.put("pages", permissionGroupPage.getTotalPages());
        map.put("page", page);
        //处理权限组数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (PermissionGroup permissionGroup : permissionGroupPage.getContent()) {
            list.add(formatOutPermissionGroup(permissionGroup));
        }
        map.put("list", list);
        return Mono.just(map);
    }

    /**
     * 添加权限组
     *
     * @param strBody 添加信息
     * @return 权限组信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('POST|/base/permissions')")
    @RequestMapping(name = "check添加权限组", value = "/base/permissions", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> save(@RequestBody String strBody) throws Exception {
        PermissionGroup permissionGroup = formatInPermissionGroup(strBody);
        permissionsGroupService.save(permissionGroup);
        return Mono.just(formatOutPermissionGroup(permissionGroup));
    }

    /**
     * 更新权限组信息
     *
     * @param strBody 更新信息
     * @return 权限组信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('PUT|/base/permissions/{id}')")
    @RequestMapping(name = "check更新权限组信息", value = "/base/permissions/{id}", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> update(@RequestBody String strBody) throws Exception {
        PermissionGroup permissionGroup = formatInPermissionGroup(strBody);
        PermissionGroup group = permissionsGroupService.update(permissionGroup);
        return Mono.just(formatOutPermissionGroup(group));
    }

    /**
     * 删除权限组
     *
     * @param id 权限组ID
     * @return 返回信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('DELETE|/base/permissions/{id}')")
    @RequestMapping(name = "check删除权限组", value = "/base/permissions/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> delete(@PathVariable(name = "id") Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误！");
        }
        permissionsGroupService.del(id);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        return Mono.just(resultMap);
    }

    /**
     * 获取权限组详情
     *
     * @param id 权限组ID
     * @return 权限组详情
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/permissions/{id}')")
    @RequestMapping(name = "check根据ID获取权限组信息", value = "/base/permissions/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findOne(@PathVariable(name = "id") Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误！");
        }
        PermissionGroup permissionGroup = permissionsGroupService.findOne(id);
        if (permissionGroup == null) {
            throw new CommonException("权限组不存在！");
        }
        Map<String, Object> map = formatOutPermissionGroup(permissionGroup);
        List<Long> ids = new ArrayList<>();
        if (permissionGroup.getPermissions() != null) {
            for (Permission permission : permissionGroup.getPermissions()) {
                if (permission.getType() != PermissionConstants.PERMISSION_TYPE_API) {
                    ids.add(permission.getId());
                }
            }
        }
        map.put("permissions", ids);
        return Mono.just(map);
    }

    /**
     * 获取接口权限列表
     *
     * @return 接口权限列表
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/permissions/apis/')")
    @RequestMapping(name = "check获取接口权限列表", value = "/base/permissionGroups/apis/", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Flux<Map<String, Object>> findAllApiPermission() throws Exception {
        List<Permission> apiPermissionList = permissionService.findAllByCheckApi();
        List<Map<String, Object>> results = new ArrayList<>();
        for (Permission permission : apiPermissionList) {
            Map<String, Object> map = new HashMap<>();
            PermissionApiExtend apiExtend = permission.getPermissionApiExtend();
            map.put("pid", 0);
            map.put("id", permission.getId());
            map.put("title", permission.getName());
            map.put("method", apiExtend.getApiMethod());
            map.put("url", apiExtend.getApiUrl());
            results.add(map);
        }
        return Flux.fromIterable(results);
    }

    /**
     * 获取接口权限组信息
     *
     * @param id 权限组ID
     * @return 接口权限组信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('GET|/base/permissions/{id}/apis')")
    @RequestMapping(name = "check获取接口权限组信息", value = "/base/permissionGroups/{id}/apis", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> findAllApiPermissionGroup(@PathVariable Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new CommonException("参数错误！");
        }
        PermissionGroup permissionGroup = permissionsGroupService.findOne(id);
        if (permissionGroup == null) {
            throw new CommonException("权限组不存在！");
        }
        Map<String, Object> map = formatOutPermissionGroup(permissionGroup);
        List<Long> ids = new ArrayList<>();
        if (permissionGroup.getPermissions() != null) {
            for (Permission permission : permissionGroup.getPermissions()) {
                if (permission.getType() == PermissionConstants.PERMISSION_TYPE_API) {
                    ids.add(permission.getId());
                }
            }
        }
        map.put("apis", ids);
        return Mono.just(map);
    }

    /**
     * 保存权限组授权API权限信息
     *
     * @param strBody 权限组信息
     * @return 权限组信息
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('PUT|/base/permissions/{id}/apis')")
    @RequestMapping(name = "check保存权限组授权API权限信息", value = "/base/permissionGroups/{id}/apis", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public Mono<Map<String, Object>> updateApis(@RequestBody String strBody) throws Exception {
        PermissionGroup permissionGroup = formatInPermissionGroup(strBody);
        Map<String, Object> map = formatOutPermissionGroup(permissionGroup);
        List<Long> ids = new ArrayList<>();
        if (permissionGroup.getPermissions() != null) {
            for (Permission permission : permissionGroup.getPermissions()) {
                if (permission.getType() == PermissionConstants.PERMISSION_TYPE_API) {
                    ids.add(permission.getId());
                }
            }
        }
        map.put("apis", ids);
        return Mono.just(map);
    }

    /**
     * 获取权限组简单列表
     *
     * @return 权限组简单列表
     * @throws Exception
     */
    @RequestMapping(name = "获取权限组简单列表", value = "/base/permissions/simple", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Flux<Map<String, Object>> findAllBySimple() throws Exception {
        List<PermissionGroup> permissionGroupList = permissionsGroupService.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (PermissionGroup permissionGroup : permissionGroupList) {
            Map<String, Object> map = formatOutPermissionGroup(permissionGroup);
            mapList.add(map);
        }
        return Flux.fromIterable(mapList);
    }

    /**
     * 格式化输出信息
     *
     * @param permissionGroup 权限组信息
     * @return 格式化后数据
     * @throws Exception
     */
    private Map<String, Object> formatOutPermissionGroup(PermissionGroup permissionGroup) throws Exception {
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("id", permissionGroup.getId());
        groupMap.put("title", permissionGroup.getName());
        groupMap.put("create_date", DateUtil.parseDate2String(permissionGroup.getCreateTime()));
        return groupMap;
    }

    /**
     * 格式化输入信息
     *
     * @param strBody 输入信息
     * @return 格式化后信息
     * @throws Exception
     */
    private PermissionGroup formatInPermissionGroup(String strBody) throws Exception {
        if (VEAStringUtil.isBlank(strBody)) {
            throw new CommonException("参数错误！");
        }
        PermissionGroup permissionGroup = new PermissionGroup();
        JSONObject jsonBody = JSON.parseObject(strBody);
        //处理权限组ID
        if (jsonBody.containsKey("id")) {
            permissionGroup.setId(jsonBody.getLong("id"));
        }
        //处理权限绑定
        if (jsonBody.containsKey("permissions")) {
            Set<Permission> permissionSet = new HashSet<>();
            JSONArray jsonArray = jsonBody.getJSONArray("permissions");
            for (int i = 0; i < jsonArray.size(); i++) {
                Long id = jsonArray.getLong(i);
                Permission permission = new Permission();
                permission.setId(id);
                permissionSet.add(permission);
            }
            permissionGroup.setPermissions(permissionSet);
        }
        //处理API权限绑定
        if (jsonBody.containsKey("apis")) {
            Set<Permission> permissionSet = new HashSet<>();
            JSONArray jsonArray = jsonBody.getJSONArray("apis");
            for (int i = 0; i < jsonArray.size(); i++) {
                Long id = jsonArray.getLong(i);
                Permission permission = new Permission();
                permission.setId(id);
                permissionSet.add(permission);
            }
            permissionGroup.setPermissions(permissionSet);
        } else {
            //处理权限组名称
            if (!jsonBody.containsKey("title") || VEAStringUtil.isBlank(jsonBody.getString("title"))) {
                throw new CommonException("名称不能为空！");
            }
            permissionGroup.setName(jsonBody.getString("title"));
        }
        return permissionGroup;
    }
}

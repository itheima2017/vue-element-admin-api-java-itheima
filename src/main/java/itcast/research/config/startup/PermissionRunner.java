package itcast.research.config.startup;

import itcast.research.entity.permission.Permission;
import itcast.research.entity.permission.PermissionApiExtend;
import itcast.research.service.permission.IPermissionService;
import itcast.research.service.permission.IPermissionsGroupService;
import itcast.research.util.permission.PermissionConstants;
import itcast.research.util.permission.PermissionUrlInfo;
import itcast.research.util.permission.PermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 9:42
 * Description: 权限信息启动类
 */
@Component
@Slf4j
public class PermissionRunner implements CommandLineRunner {
    @Autowired
    IPermissionService permissionsService;
    @Autowired
    IPermissionsGroupService permissionsGroupService;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Override
    public void run(String... strings) throws Exception {
        log.info("启动权限信息缓存!!!");
        List<PermissionUrlInfo> urlInfoList = PermissionUtil.getAllUrl(webApplicationContext);
        List<Permission> saveList = new ArrayList<>();
        for (PermissionUrlInfo info : urlInfoList) {
            Permission permission = new Permission();
            permission.setName(info.getName());
            permission.setType(PermissionConstants.PERMISSION_TYPE_API);
            PermissionApiExtend apiExtend = new PermissionApiExtend();
            apiExtend.setApiMethod(info.getMethod());
            apiExtend.setApiUrl(info.getUrl());
            permission.setPermissionApiExtend(apiExtend);
            if (permission.getName().startsWith("check")) {
                permission.setName(permission.getName().replace("check", ""));
                apiExtend.setApiLevel(PermissionConstants.API_LEVEL_CHECK);
            } else {
                apiExtend.setApiLevel(PermissionConstants.API_LEVEL_UNCHECK);
            }
            saveList.add(permission);
        }
        permissionsService.saveApiPermissions(saveList);
        //初始化权限缓存
        log.info("权限信息缓存成功!!!");
    }
}

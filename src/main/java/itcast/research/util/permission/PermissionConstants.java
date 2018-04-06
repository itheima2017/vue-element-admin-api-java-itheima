package itcast.research.util.permission;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:38
 * Description: 权限相关常量定义
 */
public class PermissionConstants {
    /**
     * 权限类型 1为菜单 2为功能 3为API
     */
    public static final int PERMISSION_TYPE_MENU = 1;
    public static final int PERMISSION_TYPE_POINT = 2;
    public static final int PERMISSION_TYPE_API = 3;
    /**
     * 权限等级，1为通用接口权限，2为需校验接口权限
     */
    public static final int API_LEVEL_UNCHECK = 1;
    public static final int API_LEVEL_CHECK = 2;

}

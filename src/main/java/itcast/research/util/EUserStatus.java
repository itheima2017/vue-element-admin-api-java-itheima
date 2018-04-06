package itcast.research.util;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:33
 * Description: 用户状态枚举
 */
public enum EUserStatus {
    NORMAL(0, "正常"), DISABLE(1, "禁用");
    private Integer code;
    private String value;

    EUserStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}

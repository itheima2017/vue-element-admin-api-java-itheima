package itcast.research.util.permission;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 9:44
 * Description: 链接信息
 */
@Component
@Getter
@Setter
public class PermissionUrlInfo implements Serializable {
    private static final long serialVersionUID = -7185672283656816025L;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 接口链接
     */
    private String url;
    /**
     * 接口名称
     */
    private String name;
}

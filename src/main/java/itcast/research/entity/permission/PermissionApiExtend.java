package itcast.research.entity.permission;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 10:51
 * Description: API权限实体类
 */
@Entity
@Table(name = "pe_permission_api_extend")
@Getter
@Setter
public class PermissionApiExtend implements Serializable {
    private static final long serialVersionUID = -1803315043290784820L;
    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键ID'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 链接
     */
    @Column(name = "api_url", columnDefinition = "varchar(255) COMMENT '链接'")
    private String apiUrl;
    /**
     * 请求类型
     */
    @Column(name = "api_method", columnDefinition = "varchar(255) COMMENT '请求类型'")
    private String apiMethod;
    /**
     * 权限等级，1为通用接口权限，2为需校验接口权限
     */
    @Column(name = "api_level", columnDefinition = "int COMMENT '权限等级，1为通用接口权限，2为需校验接口权限'")
    private Integer apiLevel;
    /**
     * 对应的权限ID
     */
    @OneToOne(targetEntity = Permission.class)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;
}

package itcast.research.entity.user;

import itcast.research.entity.other.Log;
import itcast.research.entity.permission.PermissionGroup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 10:02
 * Description: 用户实体类
 */
@Entity
@Table(name = "bs_user")
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 219178079011023526L;
    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 邮箱
     */
    @Column(name = "email", unique = true, columnDefinition = "varchar(255) COMMENT '邮箱'")
    private String email;

    /**
     * 手机号码
     */
    @Column(name = "phone", unique = true, columnDefinition = "varchar(255) COMMENT '手机号码'")
    private String phone;

    /**
     * 用户名称
     */
    @Column(name = "username", columnDefinition = "varchar(255) COMMENT '用户名称'")
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", columnDefinition = "text COMMENT '密码'")
    private String password;

    /**
     * 用户头像
     */
    @Column(name = "avatar", columnDefinition = "varchar(255) COMMENT '头像'")
    private String avatar;

    /**
     * 用户介绍
     */
    @Column(name = "introduction", columnDefinition = "text COMMENT '介绍'")
    private String introduction;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time", columnDefinition = "datetime COMMENT '最后修改时间'")
    private Date lastUpdateTime;

    /**
     * 账号状态 1为启用，2为禁用
     */
    @Column(name = "status", columnDefinition = "int default 0 COMMENT '账号状态 0为启用，1为禁用'")
    private Integer status;

    /**
     * 权限组
     */
    @ManyToOne(targetEntity = PermissionGroup.class)
    @JoinColumn(name = "permission_group_id", columnDefinition = "bigint COMMENT '权限组ID'")
    private PermissionGroup permissionGroup;

    /**
     * 日志
     */
    @OneToMany(targetEntity = Log.class, mappedBy = "user")
    private Set<Log> logs;
}

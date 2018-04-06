package itcast.research.entity.permission;

import itcast.research.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 10:56
 * Description: 权限组实体类
 */
@Entity
@Table(name = "pe_permission_group")
@Getter
@Setter
public class PermissionGroup implements Serializable {
    private static final long serialVersionUID = 2632779548833438649L;
    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键ID'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 权限组名称
     */
    @Column(name = "name", unique = true, columnDefinition = "varchar(255) COMMENT '权限名称'")
    private String name;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", columnDefinition = "datetime COMMENT '更新时间'")
    private Date updateTime;

    /**
     * 权限集合
     */
    @ManyToMany(targetEntity = Permission.class,fetch = FetchType.EAGER)
    @JoinTable(name = "a_permission_permission_group", joinColumns = {@JoinColumn(name = "pgid", columnDefinition = "bigint COMMENT '权限组ID'")}, inverseJoinColumns = {@JoinColumn(name = "pid", columnDefinition = "bigint COMMENT '权限ID'")})
    private Set<Permission> permissions;

    /**
     * 用户集合
     */
    @OneToMany(targetEntity = User.class, mappedBy = "permissionGroup")
    private Set<User> users;
}

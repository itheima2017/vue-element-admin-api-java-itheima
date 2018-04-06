package itcast.research.entity.permission;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 10:18
 * Description: 权限实体类
 */
@Entity
@Table(name = "pe_permission")
@Getter
@Setter
public class Permission implements Serializable {
    private static final long serialVersionUID = -4990810027542971546L;
    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 权限名称
     */
    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '权限名称'")
    private String name;
    /**
     * 权限类型 1为菜单 2为功能 3为API
     */
    @Column(name = "type", columnDefinition = "tinyint COMMENT '权限类型 1为菜单 2为功能 3为API'")
    private Integer type;

    /**
     * 权限描述
     */
    @Column(name = "description", columnDefinition = "text COMMENT '权限描述'")
    private String description;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     * 父权限
     */
    @ManyToOne
    @JoinColumn(name = "pid")
    private Permission parentPermission;

    /**
     * 菜单权限
     */
    @OneToOne(targetEntity = PermissionMenuExtend.class, cascade = {CascadeType.ALL})
    private PermissionMenuExtend permissionMenuExtend;

    /**
     * 点权限
     */
    @OneToOne(targetEntity = PermissionPointExtend.class, cascade = {CascadeType.ALL})
    private PermissionPointExtend permissionPointExtend;

    /**
     * API权限
     */
    @OneToOne(targetEntity = PermissionApiExtend.class, cascade = {CascadeType.ALL})
    private PermissionApiExtend permissionApiExtend;

    /**
     * 权限组集合
     */
    @ManyToMany(targetEntity = PermissionGroup.class, mappedBy = "permissions")
    private Set<PermissionGroup> permissionGroups;

    /**
     * 子权限集合
     */
    @OneToMany(mappedBy = "parentPermission", cascade = {CascadeType.ALL})
    private Set<Permission> childrenPermission;
}

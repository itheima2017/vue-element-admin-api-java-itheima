package itcast.research.entity.permission;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 10:42
 * Description: 点权限实体类
 */
@Entity
@Table(name = "pe_permission_point_extend")
@Getter
@Setter
public class PermissionPointExtend implements Serializable {
    private static final long serialVersionUID = -3050919427783987777L;
    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键ID'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 权限代码
     */
    @Column(name = "code",unique = true,columnDefinition = "text COMMENT '权限代码'")
    private String code;

    /**
     * 对应的权限ID
     */
    @OneToOne(targetEntity = Permission.class)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;
}

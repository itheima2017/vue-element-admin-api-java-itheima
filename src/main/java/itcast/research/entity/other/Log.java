package itcast.research.entity.other;

import itcast.research.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 14:12
 * Description: 日志实体类
 */
@Entity
@Table(name = "sys_log")
@Getter
@Setter
public class Log implements Serializable {
    private static final long serialVersionUID = -5605729959538820242L;

    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "bigint COMMENT '主键'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * url
     */
    @Column(name = "url", columnDefinition = "varchar(255) COMMENT 'url'")
    private String url;

    /**
     * method
     */
    @Column(name = "method", columnDefinition = "varchar(255) COMMENT 'method'")
    private String method;

    /**
     * 参数内容
     */
    @Column(name = "request_body", columnDefinition = "text COMMENT '参数内容'")
    private String requestBody;

    /**
     * 操作结果
     */
    @Column(name = "operation_result", columnDefinition = "tinyint(1) COMMENT '操作结果'")
    private Boolean operationResult;

    /**
     * 操作时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "operation_date", columnDefinition = "datetime COMMENT '操作时间'")
    private Date operationDate;

    /**
     * 操作人
     */
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", columnDefinition = "bigint COMMENT '操作人ID'")
    private User user;
}

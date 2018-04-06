package itcast.research.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 9:35
 * Description: 错误信息
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnValue implements Serializable {
    private static final long serialVersionUID = 8373410838940987727L;
    /**
     * 错误状态码
     */
    private Integer err_code;
    /**
     * 错误信息
     */
    private String message;
}

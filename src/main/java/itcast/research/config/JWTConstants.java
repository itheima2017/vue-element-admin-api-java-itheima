package itcast.research.config;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/29 9:47
 * Description: JWT常量
 */
public class JWTConstants {
    /**
     * 秘钥
     */
    public static final byte[] SECRET = "3d990d2276917dfac04467df11fff26d".getBytes();
    /**
     * token 前缀
     */
    public static final String AUTHORIZATION_PRE = "VEA-ADMIN ";
    /**
     * token header名称
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";
}

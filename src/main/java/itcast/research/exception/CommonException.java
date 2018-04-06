package itcast.research.exception;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 13:59
 * Description: 通用异常类
 */
public class CommonException extends Exception {
    public CommonException() {
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }
}

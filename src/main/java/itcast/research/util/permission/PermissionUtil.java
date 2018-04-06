package itcast.research.util.permission;

import itcast.research.util.VEAStringUtil;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 9:45
 * Description: 权限工具类
 */
public class PermissionUtil {
    /**
     * 获取所有的链接
     *
     * @return 所有链接
     */
    public static List<PermissionUrlInfo> getAllUrl(WebApplicationContext applicationContext) {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<PermissionUrlInfo> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()) {
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            Set<RequestMethod> methodSet = info.getMethodsCondition().getMethods();
            PermissionUrlInfo urlInfo = new PermissionUrlInfo();
            for (String url : patterns) {
                urlInfo.setUrl(url);
            }
            for (RequestMethod method : methodSet) {
                urlInfo.setMethod(method.name());
            }
            urlInfo.setName(info.getName());
            if (urlInfo.getMethod() != null && urlInfo.getName() != null) {
                urlList.add(urlInfo);
            }
        }
        return urlList;
    }

    /**
     * 格式化链接地址
     *
     * @param uri 原始链接地址
     * @return 格式化后链接地址
     */
    public static String formatUrl(String uri) {
        String[] subUris = uri.split("/", -1);
        StringBuilder sbResult = new StringBuilder();
        for (String str : subUris) {
            if (VEAStringUtil.isNumeric(str)) {
                str = "{id}";
            }
            sbResult.append("/" + str);
        }
        return sbResult.toString().replace("//", "/");
    }
}

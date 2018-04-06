package itcast.research.util;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 9:55
 * Description: 字符串工具
 */
public class VEAStringUtil {
    /**
     * 下划线转驼峰
     *
     * @param str 处理前字符串
     * @return 处理后字符串
     */
    public static String underLineToCamel(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        if (!str.contains("_")) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        String[] strs = str.split("_");
        for (String s : strs) {
            if (s.isEmpty()) {
                continue;
            }
            if (sb.length() == 0) {
                sb.append(s.toLowerCase());
            } else {
                sb.append(s.substring(0, 1).toUpperCase());
                sb.append(s.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否为空 空为假
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断是否为空 空为真
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 处理排序信息
     *
     * @param sort 排序信息
     * @return 排序
     */
    public static Sort getSort(String sort, Sort.Direction direction) {
        //处理分页条件与排序条件
        List<Sort.Order> listSorts = new ArrayList<Sort.Order>();
        if (VEAStringUtil.isNotBlank(sort)) {
            String[] str = sort.split(",");
            for (String s : str) {
                s = VEAStringUtil.underLineToCamel(s);
                Sort.Order so = null;
                if (!s.startsWith("-")) {
                    so = new Sort.Order(Sort.Direction.ASC, s);
                } else {
                    String s1 = s.substring(1);
                    so = new Sort.Order(Sort.Direction.DESC, s1);
                }
                listSorts.add(so);
            }
        } else {
            listSorts.add(new Sort.Order(direction, "id"));
        }
        return Sort.by(listSorts);
    }

    /**
     * 判断是否是数字
     *
     * @param cs
     * @return
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}

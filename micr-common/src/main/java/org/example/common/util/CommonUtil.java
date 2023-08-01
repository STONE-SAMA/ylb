package org.example.common.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class CommonUtil {

    /*处理pageNo*/
    public static int defaultPageNo(Integer pageNo) {
        int pNo = pageNo;
        if (pageNo == null || pageNo < 1) {
            pNo = 1;
        }
        return pNo;
    }

    /*处理pageSize*/
    public static int defaultPageSize(Integer pageSize) {
        int pSize = pageSize;
        if (pageSize == null || pageSize < 1) {
            pSize = 1;
        }
        return pSize;
    }

    //手机号脱敏
    public static String privacyPhone(String phone) {
        String result = "***********";
        if (phone != null && phone.trim().length() == 11) {
            result = phone.substring(0, 3) + "******" + phone.substring(9, 11);
        }
        return result;
    }

    //手机号合法判断
    public static boolean checkPhone(String phone) {
        String regex = "^((13[0-9])|(14([0-1]|[4-9]))|(15([0-3]|[5-9]))|(16(2|[5-7]))|(17[1-9])|(18[0-9])|(19[0|1|3])|(19[5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(phone).matches();
    }

    //比较BigDecimal  true:n1>=n2
    public static boolean ge(BigDecimal n1, BigDecimal n2) {
        if (n1 == null || n2 == null) {
            throw new RuntimeException("参数为null");
        }
        return n1.compareTo(n2) >= 0;
    }

}

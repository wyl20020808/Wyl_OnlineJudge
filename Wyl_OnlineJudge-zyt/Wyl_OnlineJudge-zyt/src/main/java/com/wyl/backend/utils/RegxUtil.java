package com.wyl.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtil {
    /**
     * 返回一个与正则表达式相匹配的String
     *
     * @param str 要匹配的String
     * @param regex 正则表达式
     * @return 与正则匹配的String
     */
    public static String getMatches(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean isFind = matcher.find();
        if (isFind)
            return matcher.group();
        else
            return null;
    }

    /**
     * 判断字符与正则表达式是否匹配
     * @param str 字符串
     * @param regex 正则表达式
     * @return 匹配结果
     */
    public static boolean isMatches(String str, String regex){
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(str);
        return m.matches();
    }
}

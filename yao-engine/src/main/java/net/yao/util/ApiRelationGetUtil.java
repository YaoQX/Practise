package net.yao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiRelationGetUtil {

    /**
     * 它会把字符串里的 {{token}} 这种占位符，从 ApiRelationContext 里取出真实值并替换掉。
     * @param parameter
     * @return
     */
    public static String getParameter(String parameter){
        String newParameter = parameter;
        System.out.println("-----");
        System.out.println(newParameter);
        //匹配{{}}
        Pattern pattern = Pattern.compile(ApiRegexpUtil.REGEXP);
        Matcher matcher = pattern.matcher(parameter);
        if(matcher.find()){
            //从匹配到的内容里，取出第 1 个“括号里的部分”
            String trueparameter = matcher.group(1);

            //去容器取关联参数
            String fetched = ApiRelationContext.get(trueparameter);
            if(fetched == null){
                //关联参数不存在
                throw new RuntimeException("Parameter cannot find");
            }
            newParameter  = parameter.replaceAll(ApiRegexpUtil.byName(trueparameter),fetched);
        }
        return newParameter;
    }

}

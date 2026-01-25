package net.yao.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SpringBeanUtil {

    /**
     *
     * @param <T> 目标对象类型
     * @param source 源对象
     * @param target 目标对象
     * @return 复制后目标对象
     */
    public static<T> T copyProperties(Object source, Class<T> target) {
        try {
            T t = target.getConstructor().newInstance();//空的目标对象
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     *
     * @param <T> 目标对象类型
     * @param sourceList 源对象列表
     * @param target 目标对象列表
     * @return 复制后目标对象列表
     */
    public static <T> List<T> copyProperties(List<?> sourceList, Class<T> target) {
        ArrayList<T> targetList = new ArrayList<>();
        sourceList.forEach(item -> targetList.add(copyProperties(item, target)));//循环调用上面方法
        return targetList;
    }
}

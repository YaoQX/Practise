package net.yao.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//基于 ThreadLocal 的“接口执行上下文存储器”，用于实现接口间数据传递和隔离
public class ApiRelationContext {
    //线程之间互不影响
    private static final ThreadLocal<Map<String,String>> THREAD_LOCAL = new ThreadLocal();

    public static Map<String,String> get(){
        return THREAD_LOCAL.get();
    }


    /**
     * 从线程本地存储的上下文中获取指定键的值
     * @param key 要获取的键名
     * @return 返回键对应的值，如果上下文未初始化或键不存在则返回 null
     */
    public static String get(String key){
        Map<String, String> map = THREAD_LOCAL.get();
        if(map == null){
            return null;
        }
        return map.get(key);
    }



    public static void set(String key,String value){
        if(get() == null){
            THREAD_LOCAL.set(new HashMap<>(16));
        }
        THREAD_LOCAL.get().put(key,value);
    }

    public static void remove(){
        String filePaths = get("filePaths");
        if(filePaths!=null){
            String[] split = filePaths.split(",");
            for(String pathStr : split){
                Path filePath = Paths.get(pathStr);
                try {
                    //把临时文件删除掉，避免垃圾文件残留。
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //它会把当前线程绑定的 Map 整个移除掉。
        THREAD_LOCAL.remove();
    }
}

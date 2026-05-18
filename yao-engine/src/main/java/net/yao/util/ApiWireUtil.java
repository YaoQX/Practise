package net.yao.util;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import io.micrometer.common.util.StringUtils;
import io.restassured.specification.RequestSpecification;
import net.sf.saxon.trans.SymbolicName;
import net.yao.dto.KeyValueDTO;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;
import net.yao.dto.api.RequestBodyDTO;
import net.yao.enums.ApiBodyTypeEnum;
import net.yao.service.common.FileService;
import org.checkerframework.checker.units.qual.K;

//将接口测试的参数（如 URL、Header、Query、Body）装配（Wire）到 RestAssured 的请求对象中。
@Slf4j
public class ApiWireUtil {


    public static void wireBase(RequestSpecification request,String base,String path){
        //拼接 BaseURL 和具体 Path。
        request.baseUri(base+path);
    }

    /**
     * 装配请求头 把headerList里的数据，变成真正的HTTP请求头
     */
    public static void wireHeader(RequestSpecification request, List<KeyValueDTO> headerList){
        if(headerList == null || headerList.isEmpty()){
            return;
        }
        HashMap<String,Object> map = new HashMap<>(headerList.size());
        for(KeyValueDTO header:headerList){
            //正则匹配是否为关联参数表达式 不是就返回原参数
            String value = ApiRelationGetUtil.getParameter(header.getValue());
            header.setValue(value);
            map.put(header.getKey(), value);

        }
        request.headers(map);
    }

    public static void wireQuery(RequestSpecification request, List<KeyValueDTO> queryList){
        if(queryList == null || queryList.isEmpty()){
            return;
        }
        HashMap<String,Object> map = new HashMap<>(queryList.size());
        for(KeyValueDTO query:queryList){
            //正则匹配是否为关联参数表达式
            String value = ApiRelationGetUtil.getParameter(query.getValue());
            query.setValue(value);
            map.put(query.getKey(), value);
        }
        request.params(map);
    }

    /**
     *递归遍历 JSON 请求体，把里面的 {{变量名}} 这种关联参数替换成真实值。
     */
    private static String traverseAndModify(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        traverseJsonObject(jsonObject);
        return JSON.toJSONString(jsonObject);
    }

    //遍历一个json里所有字段
    private static void traverseJsonObject(JSONObject jsonObject){
        if(jsonObject!=null){
            for(String key : jsonObject.keySet()){
                Object value = jsonObject.get(key);
                //如果 value 还是一个 JSON 对象，就继续递归
                if(value instanceof JSONObject){
                    traverseJsonObject((JSONObject) value);
                }else if(value instanceof JSONArray){
                    JSONArray jsonArray = (JSONArray) value;
                    for(int i = 0; i < jsonArray.size(); i++){
                        Object subValue = jsonArray.get(i);
                        if (subValue instanceof String subStringValue) {
                            String parameterizedValue = ApiRelationGetUtil.getParameter(subStringValue);
                            jsonArray.remove(subStringValue);
                            jsonArray.add(parameterizedValue);
                            // 仅在实际需要日志记录时，使用占位符记录
                            // log.info("Parameterized string: {} -> {}", subStringValue, parameterizedValue);
                        } else if (subValue instanceof JSONObject subJsonObject) {
                            traverseJsonObject(subJsonObject);
                        } else {

                            log.info("Unsupported type encountered: {}", subValue.getClass().getName());
                        }
                    }
                } else if (value instanceof String stringValue){
                    String parameterizedValue = ApiRelationGetUtil.getParameter(stringValue);
                    jsonObject.put(key, parameterizedValue);
                }else {
                    log.info("Unsupported type encountered: {}", value.getClass().getName());
                }
            }
        }
    }

    public static void wireBody(RequestSpecification request, RequestBodyDTO requestBody, List<KeyValueDTO> bodyList){
        if(StringUtils.isBlank(requestBody.getBody()) || requestBody.getBody().equals("[]")|| requestBody.getBody().equals("{}") ){
            return;
        }
        if(ApiBodyTypeEnum.JSON.name().equals(requestBody.getBodyType())){
            String modifiedJson = traverseAndModify(requestBody.getBody());
            requestBody.setBody(modifiedJson);
            request.body(modifiedJson);
        }else{
            ApiBodyTypeEnum bodyTypeEnum = ApiBodyTypeEnum.valueOf(requestBody.getBodyType());
            for(KeyValueDTO item :  bodyList)
            {
                switch(bodyTypeEnum){
                    case FORM_DATA,X_WWW_FORM_URLENCODED:
                        //解析表单数据
                        String parameter = ApiRelationGetUtil.getParameter(item.getValue());
                        item.setValue(parameter);
                        request.formParam(item.getKey(), item.getValue());
                        break;
                    case BINARY:
                        //解析二进制数据 上传
                        FileService fileService = SpringContextHolder.getBean(FileService.class);
                        String localTempFile = fileService.copyRemoteFileToLocalTempFile(item.getValue());
                        File file = new File(localTempFile);
                        //如果你的文件存在 MinIO 或远程文件服务里，不能直接把远程路径当成本地文件上传，所以要先下载/复制到本地临时文件。
                        //用于测试文件上传接口。
                        request.multiPart(file);
                        //资源清理放置路径
                        String filePaths = ApiRelationContext.get("filePaths");
                        if (filePaths != null) {
                            filePaths = filePaths + "," + file.getAbsolutePath();
                            ApiRelationContext.set("filePaths", filePaths);
                        } else {
                            ApiRelationContext.set("filePaths", file.getAbsolutePath());
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unsupported Request Type");
                }

            }
        }


    }
}

package net.yao.service.api.core;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import net.yao.dto.KeyValueDTO;
import net.yao.dto.api.ApiJsonAssertionDTO;
import net.yao.dto.api.ApiJsonRelationDTO;
import net.yao.dto.api.RequestBodyDTO;
import net.yao.enums.ApiBodyTypeEnum;
import net.yao.util.ApiWireUtil;
import net.yao.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

//一个“可执行的接口请求对象”，负责把配置数据转换成真正的HTTP请求
@Data
public class ApiRequest {

    private String base;

    private String path;

    private String assertion;

    private String relation;

    private String query;

    private String header;

    private RequestBodyDTO requestBody;

    private List<ApiJsonAssertionDTO> assertionList;

    private List<ApiJsonRelationDTO> relationList;

    private List<KeyValueDTO> queryList;

    private List<KeyValueDTO> headerList;

    private List<KeyValueDTO> bodyList;

    private RequestSpecification request = RestAssured.given();

    public ApiRequest(String base, String path, String assertion, String relation, String query, String header, String body, String bodyType) {
        this.base = base;
        this.path = path;
        this.assertion = assertion;
        this.relation = relation;
        this.query = query;
        this.header = header;
        this.requestBody = new RequestBodyDTO(body,bodyType);

        this.assertionList = StringUtils.isBlank(assertion) ? null : JsonUtil.json2List(assertion, ApiJsonAssertionDTO.class);
        this.relationList = StringUtils.isBlank(relation) ? null : JsonUtil.json2List(relation, ApiJsonRelationDTO.class);
        this.queryList = StringUtils.isBlank(query) ? null : JsonUtil.json2List(query, KeyValueDTO.class);
        this.headerList = StringUtils.isBlank(header) ? null : JsonUtil.json2List(header, KeyValueDTO.class);
        if(!ApiBodyTypeEnum.JSON.name().equals(bodyType)){
            this.bodyList = StringUtils.isBlank(body) ? null : JsonUtil.json2List(body, KeyValueDTO.class);
        }


    }

    public RequestSpecification createRequest(){
        //基础路径
        ApiWireUtil.wireBase(request,base,path);

        //请求头
        ApiWireUtil.wireHeader(request,headerList);

        //请求参数
        ApiWireUtil.wireQuery(request,queryList);

        //请求体
        ApiWireUtil.wireBody(request,requestBody,bodyList);


        return request;//
    }


}

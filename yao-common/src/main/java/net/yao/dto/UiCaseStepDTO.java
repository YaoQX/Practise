
package net.yao.dto;
import lombok.Data;
import java.util.Date;

/**
* UI测试用例步骤数据传输对象
 * 包含测试步骤的完整属性定义及预期结果信息
 */
@Data
public class UiCaseStepDTO {
    /**
     * 获取/设置ID
     */
    private Long id;

    /**
     * 关联项目ID
     * 多项目管理的外键标识
     */
    private Long projectId;

    /**
     * 所属测试用例ID
     * 用例管理表的外键关联
     */
    private Long caseId;

    /**
     * 步骤执行顺序号
     * 同一用例内的执行顺序标识
     */
    private Long num;

    /**
     * 步骤名称
     * 测试日志和报告的显示标识
     */
    private String name;

    /**
     * 操作类型
     * 点击/输入/验证等操作类型标识
     */
    private String operationType;

    /**
     * 元素定位类型
     * ID/CSS选择器/XPATH等定位方式
     */
    private String locationType;

    /**
     * 元素定位表达式* locationType对应的具体定位值
     */
    private String locationExpress;

    /**
     * 元素等待时间
     * 单位毫秒，元素检测超时时间
     */
    private Long elementWait;

    /**
     * 目标元素定位类型
     * 拖拽等复合操作使用
     */
    private String targetLocationType;

    /**
     * 目标元素定位表达式
     * targetLocationType对应的具体值
     */
    private String targetLocationExpress;

    /**
     * 目标元素等待时间
     * 单位毫秒，目标元素超时时间
     */
    private Long targetElementWait;

    /**
     * 操作值
     * 输入操作时的文本内容
     */
    private String value;

    /**
     * 预期结果键值
     * 验证项的标识符
     */
    private String expectKey;

    /**
     * expectKey对应的预期结果
     */
    private String expectValue;

    /**
     * 步骤描述信息
     * 操作内容的详细说明
     */
    private String description;

   /**
     * 错误继续执行标志
     * true时记录错误并继续执行
     */
    private Boolean isContinue;

    /**
     * 截图获取标志
     * true时操作前后进行截图
     */
    private Boolean isScreenshot;

    private Date gmtCreate;

    private Date gmtModified;
}

package net.yao.service.ui;

import net.yao.dto.UiOperationResultDTO;

public interface SeleniumAssertionOperationService {

    /**
     * 判断两个值是否相等。
     * @param value 实际值
     * @param expectValue 期望值
     * @return 返回比较结果，包含是否成功、消息和截获的异常等信息。
     */
    UiOperationResultDTO equalValue(Object value, Object expectValue);

    /**
     * 判断两个值是否不相等。
     * @param value 实际值
     * @param expectValue 期望值
     * @return 返回比较结果，包含是否成功、消息和截获的异常等信息。
     */
    UiOperationResultDTO notEqualValue(Object value, Object expectValue);

    /**
     * 判断实际值是否包含期望值。
     * @param value 实际值
     * @param expectValue 期望值
     * @return 返回比较结果，包含是否成功、消息和截获的异常等信息。
     */
    UiOperationResultDTO containValue(String value, String expectValue);

    /**
     * 判断实际值是否不包含期望值。
     * @param value 实际值
     * @param expectValue 期望值
     * @return 返回比较结果，包含是否成功、消息和截获的异常等信息。
     */
    UiOperationResultDTO notContainValue(String value, String expectValue);

    /**
     * 大于
     */
    UiOperationResultDTO greaterThan(Long value, Long expectValue);

    /**
     * 小于
     */
    UiOperationResultDTO lessThan(Long value, Long expectValue);

    /**
     * 存在
     * @param locationType 用什么方式定位元素。
     * @param locationExpress 表达式
     */
    UiOperationResultDTO existElement(String locationType, String locationExpress);

    /**
     * 不存在
     */
    UiOperationResultDTO absentElement(String locationType, String locationExpress);
    /**
     * 元素是否启用
     */
    UiOperationResultDTO enableElement(String locationType, String locationExpress);
    /**
     * 元素是否禁用
     */
    UiOperationResultDTO disableElement(String locationType, String locationExpress);

    /**
     * 元素可见
     */
    UiOperationResultDTO visibleElement(String locationType, String locationExpress);
    /**
     * 元素不可见
     */
    UiOperationResultDTO invisibleElement(String locationType, String locationExpress);
    /**
     * 元素可选
     */
    UiOperationResultDTO selectElement(String locationType, String locationExpress);
    /**
     * 元素不可选
     */
    UiOperationResultDTO unselectElement(String locationType, String locationExpress);

}

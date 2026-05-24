package net.yao.service.ui.impl;

import net.yao.dto.UiOperationResultDTO;
import net.yao.service.ui.SeleniumAssertionOperationService;
import net.yao.util.SeleniumFetchUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SeleniumAssertionOperationServiceImpl implements SeleniumAssertionOperationService {
    /**
     *比较两个对象值是否相等，并返回包含比较结果及值的操作结果DTO。
    *
    * @param value        实际值对象，用于与期望值进行比较
    * @param expectValue  期望值对象，用于与实际值进行比较
    * @return包含操作状态、实际值和期望值的UI操作结果DTO对象
    */
   public UiOperationResultDTO equalValue(Object value, Object expectValue) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder().operationState(Objects.equals(value, expectValue))
                .actualValue(value).expectValue(expectValue).build();
        return resultDTO;
    }

    /**
     * 比较两个对象值是否不相等，并返回包含比较结果及值的操作结果DTO。
     *
     * @param value        实际值对象，用于与期望值进行比较
     * @param expectValue  期望值对象，用于与实际值进行比较
     * @return             包含操作状态、实际值和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO notEqualValue(Object value, Object expectValue) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder().operationState(!Objects.equals(value, expectValue))
                .actualValue(value).expectValue(expectValue).build();
        return resultDTO;
    }

    /**
     * 检查字符串实际值是否包含期望值，并返回包含比较结果及值的操作结果DTO。
     *
     * @param value        实际值字符串，用于与期望值进行包含关系比较
     * @param expectValue  期望值字符串，用于检查是否被包含在实际值中
     * @return             包含操作状态、实际值和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO containValue(String value, String expectValue) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder().operationState(value.contains(expectValue))
                .actualValue(value).expectValue(expectValue).build();
        return resultDTO;
    }

    /**
     * 检查字符串实际值是否不包含期望值，并返回包含比较结果及值的操作结果DTO。
     *
     * @param value        实际值字符串，用于与期望值进行包含关系比较
     * @param expectValue  期望值字符串，用于检查是否未被包含在实际值中
     * @return             包含操作状态、实际值和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO notContainValue(String value, String expectValue) {
       UiOperationResultDTO resultDTO = UiOperationResultDTO.builder().operationState(!value.contains(expectValue))
                .actualValue(value).expectValue(expectValue).build();
        return resultDTO;
    }

    /**
     * 检查Long型实际值是否大于期望值，并返回包含比较结果及值的操作结果DTO。
     *
     * @param value        实际值Long对象，用于与期望值进行比较
     * @param expectValue  期望值Long对象，用于与实际值进行比较
     * @return             包含操作状态、实际值和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO greaterThan(Long value, Long expectValue) {
        UiOperationResultDTO resultDTO =UiOperationResultDTO.builder().operationState(value > expectValue)
                .actualValue(value).expectValue(expectValue).build();
        return resultDTO;
    }

    /**
     * 检查Long型实际值是否小于期望值，并返回包含比较结果及值的操作结果DTO。
     *
     * @param value        实际值Long对象，用于与期望值进行比较
     * @param expectValue  期望值Long对象，用于与实际值进行比较
     * @return             包含操作状态、实际值和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO lessThan(Long value, Long expectValue) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder().operationState(value<expectValue)
                .actualValue(value).expectValue(expectValue).build();
        return resultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否存在。
     * 元素存在时将操作状态设为true，不存在时设为false并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO existElement(String locationType, String locationExpress) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder()
                .expectValue("locationType="+locationType+",locationExpress="+locationExpress).build();
        try {
            SeleniumFetchUtil.findElement(locationType,locationExpress,0L);
            resultDTO.setOperationState(true);
        }catch (Exception e){
            resultDTO.setOperationState(false);
        }
        return resultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否不存在。
     * 反转existElement的结果，元素不存在时将操作状态设为true并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO absentElement(String locationType, String locationExpress) {
        UiOperationResultDTO uiOperationResultDTO = existElement(locationType, locationExpress);
        uiOperationResultDTO.setOperationState(!uiOperationResultDTO.getOperationState());
        return uiOperationResultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否启用。
     * 元素启用时将操作状态设为true，禁用时设为false并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO enableElement(String locationType,String locationExpress) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder()
                .expectValue("locationType="+locationType+",locationExpress="+locationExpress).build();
        try {
            boolean enabled = SeleniumFetchUtil.findElement(locationType, locationExpress, 0L).isEnabled();
            resultDTO.setOperationState(enabled);
        }catch (Exception e){
            resultDTO.setOperationState(false);
        }
        return resultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否禁用。
     * 反转enableElement的结果，元素禁用时将操作状态设为true并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO disableElement(String locationType, String locationExpress) {
        UiOperationResultDTO operationResultDTO = enableElement(locationType, locationExpress);
        operationResultDTO.setOperationState(!operationResultDTO.getOperationState());
        return operationResultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否可见。
     * 元素可见时将操作状态设为true，不可见时设为false并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO visibleElement(String locationType, String locationExpress) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder()
                .expectValue("locationType="+locationType+",locationExpress="+locationExpress).build();
        try {
            boolean displayed= SeleniumFetchUtil.findElement(locationType, locationExpress, 0L).isDisplayed();
            resultDTO.setOperationState(displayed);
        }catch (Exception e){
            resultDTO.setOperationState(false);
        }
        return resultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否不可见。
     * 反转visibleElement的结果，元素不可见时将操作状态设为true并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO invisibleElement(String locationType, String locationExpress) {
        UiOperationResultDTO operationResultDTO = visibleElement(locationType, locationExpress);
        operationResultDTO.setOperationState(!operationResultDTO.getOperationState());
        return operationResultDTO;
    }

    /*
     * 使用指定的定位类型和表达式验证元素是否被选中。
     * 元素被选中时将操作状态设为true，未被选中时设为false并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */
    public UiOperationResultDTO selectElement(String locationType, String locationExpress) {
        UiOperationResultDTO resultDTO = UiOperationResultDTO.builder()
                .expectValue("locationType="+locationType+",locationExpress="+locationExpress).build();
        try {
            boolean selected = SeleniumFetchUtil.findElement(locationType, locationExpress, 0L).isSelected();
            resultDTO.setOperationState(selected);
        }catch (Exception e){
            resultDTO.setOperationState(false);
        }
        return resultDTO;
    }

    /**
     * 使用指定的定位类型和表达式验证元素是否未被选中。
     * 反转selectElement的结果，元素未被选中时将操作状态设为true并返回结果。
     *
     * @param locationType      元素的定位类型 (例如: id, xpath, cssSelector)
     * @param locationExpress   元素的定位表达式
     * @return                  包含操作状态和期望值的UI操作结果DTO对象
     */

    public UiOperationResultDTO unselectElement(String locationType, String locationExpress) {
        UiOperationResultDTO operationResultDTO = selectElement(locationType, locationExpress);
        operationResultDTO.setOperationState(!operationResultDTO.getOperationState());
        return operationResultDTO;
    }
}

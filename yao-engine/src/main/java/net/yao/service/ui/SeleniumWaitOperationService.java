package net.yao.service.ui;

import net.yao.model.UiCaseStepDO;

public interface SeleniumWaitOperationService {
    /**
     * 隐藏等待，等待页面元素隐藏。
     * @param millSeconds 等待的毫秒数。
     */
    void waitHide(Long millSeconds);

    /**
     * 显示等待，等待页面元素显示。
     */
    void waitShow(UiCaseStepDO uiCaseStepDO);

    /**
     * 强制等待，等待指定的时间。
     * @param millSeconds 等待的毫秒数。
     */
    void waitForce(Long millSeconds);

}

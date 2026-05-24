package net.yao.service.ui;

public interface SeleniumBrowserOperationService {

    /**
     * 打开网页
     */
    void open(String url);

    /**
     * 关闭
     */
    void close();

    /**
     * 返回前一个页面
     */
    void back();

    /**
     * 前进一个页面
     */
    void forward();

    /**
     * 刷新当前页面
     */
    void refresh();

    /**
     * 最大窗口尺寸
     */
    void resizeMax();

    /**
     * 重新设定窗口大小
     */
    void resize(int width, int height);

    /**
     * 切换窗口页面-句柄
     */
    void switchByHandle(String handler);

    /**
     * 切换窗口页面-索引顺序
     */
    void switchByIndex(int index);
}

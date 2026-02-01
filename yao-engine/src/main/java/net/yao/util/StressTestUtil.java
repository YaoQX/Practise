package net.yao.util;

import java.io.File;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.util.JMeterUtils;


public class StressTestUtil {
    /**
     * 获取Jmeter的home⽬录，临时写法
     * 读取 src/main/resources 目录下的配置文件或模板文件。
     */
    public static String getJmeterHome() {
        try {
            return StressTestUtil.class.getClassLoader().getResource("jmeter").getPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取Jmeter的bin⽬录
     */
    public static String getJmeterHomeBin() {
        return getJmeterHome() + File.separator + "bin";
    }
    /**
     * 设置JMeter的属性, 可以不触发调⽤
     */
    public static void initJmeterProperties() {
        // 获取JMeter的安装⽬录
        String jmeterHome = getJmeterHome();
        String jmeterHomeBin = getJmeterHomeBin();
        // 加᫹jmeter.properties⽂件
        JMeterUtils.loadJMeterProperties(jmeterHomeBin + File.separator + "jmeter.properties");
        // 设置JMeter的安装⽬录
        JMeterUtils.setJMeterHome(jmeterHome);
        //免中⽂乱码
        JMeterUtils.setProperty("sampleresult.default.encoding","UTF-8");
        // 初始化本地环境
        JMeterUtils.initLocale();
        // 获取JMeter的属性⽂件
        //Properties jmeterProps = JMeterUtils.getJMeterProperties();
    }
    /**
     * 获取⾃定义jmeter压测引擎,封装⽅法，统一返回对象
     * @return
     */
    public static StandardJMeterEngine getJmeterEngine() {
        //初始化ᯈ置
        initJmeterProperties();
        StandardJMeterEngine standardJMeterEngine = new StandardJMeterEngine();
        return standardJMeterEngine;
    }
}

package net.yao.service.stress.core;


import net.yao.dto.KeyValueDTO;
import net.yao.dto.ReportDTO;
import net.yao.dto.common.ThreadGroupConfigDTO;
import net.yao.dto.stress.CSVDataFileDTO;
import net.yao.model.EnvironmentDO;
import net.yao.model.StressCaseDO;
import net.yao.service.common.FileService;
import net.yao.service.common.impl.KafkaSenderServiceImpl;
import net.yao.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.protocol.http.gui.HTTPArgumentsPanel;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 把在数据库里填写的压测配置，一键转成JMeter能跑的“代码版”脚本
 */
public class StressSimpleEngine extends BaseStressEngine{

    /**
     * 一套代码在不同地方运行不同配置
     */
    private  EnvironmentDO environmentDO;

    public StressSimpleEngine(EnvironmentDO environmentDO, StressCaseDO stressCaseDO, ReportDTO reportDTO, ApplicationContext applicationContext) {
        this.environmentDO = environmentDO;
        super.stressCaseDO= stressCaseDO;
        super.reportDTO = reportDTO;
        super.applicationContext= applicationContext;
    }
    @Override
    public void assembleTestPlan() {
        //获取压测结果收集器
        EngineSampleCollector engineSampleCollector = super.getEngineSampleCollector(applicationContext.getBean(KafkaSenderServiceImpl.class));

        //组装测试计划

        //创建hashTree
        ListedHashTree testHashTree = new ListedHashTree();

        //创建测试计划
        TestPlan testPlan = createTestPlan();

        //创建线程组
        ThreadGroup threadGroup = createTheadGroup();

        //请求头和参数等
        HeaderManager headerManager = createHeaderManager();

        //创建采样器
        HTTPSamplerProxy httpSamplerProxy = createHttpSamplerProxy();

        //创建断言列表
        List<ResponseAssertion> responseAssertionList = createResponseAssertionList();

        //创建参数化
        List<CSVDataSet> csvDataSetList = createCsvDataSetList();


        //组装到测试计划里面
        HashTree threadGroupHashTree = testHashTree.add(testPlan, threadGroup);

        //将http采样器添加到线程组下面
        threadGroupHashTree.add(httpSamplerProxy);

        //结果收集器添加到线程组下面
        threadGroupHashTree.add(engineSampleCollector);


        if(headerManager != null){
            threadGroupHashTree.add(headerManager);
        }
        if(responseAssertionList != null){
            threadGroupHashTree.add(responseAssertionList);
        }
        if(csvDataSetList != null){
            threadGroupHashTree.add(csvDataSetList);
        }

        super.setTestPlanHashTree(testHashTree);


    }

    private List<CSVDataSet> createCsvDataSetList() {

        if (StringUtils.isBlank(stressCaseDO.getRelation())) {
            return null;
        }


        FileService fileService = applicationContext.getBean(FileService.class);

        List<CSVDataFileDTO> csvDataFileDTOS = JsonUtil.json2List(stressCaseDO.getRelation(), CSVDataFileDTO.class);

        //定义一个list，存储CSVDataSet
        List<CSVDataSet> csvDataSetList = new ArrayList<>(csvDataFileDTOS.size());


        for(CSVDataFileDTO csvDataFileDTO : csvDataFileDTOS){

            CSVDataSet csvDataSet = new CSVDataSet();
            csvDataSet.setName(csvDataFileDTO.getName());
            csvDataSet.setProperty(TestElement.TEST_CLASS, CSVDataSet.class.getName());
            csvDataSet.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
            csvDataSet.setEnabled(true);

            csvDataSet.setProperty("delimiter", csvDataFileDTO.getDelimiter());
            csvDataSet.setProperty("quotedData", false);
            csvDataSet.setProperty("recycle", csvDataFileDTO.getRecycle());
            csvDataSet.setProperty("ignoreFirstLine", csvDataFileDTO.getIgnoreFirstLine());
            csvDataSet.setProperty("variableNames", csvDataFileDTO.getVariableNames());

            csvDataSet.setProperty("filename", fileService.copyRemoteFileToLocalTempFile(csvDataFileDTO.getRemoteFilePath()));
            csvDataSet.setProperty("fileEncoding", "UTF-8");
            csvDataSet.setProperty("stopThread", false);
            csvDataSet.setProperty("shareMode", "shareMode.all");
            csvDataSetList.add(csvDataSet);
        }

        return csvDataSetList;
    }

    private List<ResponseAssertion> createResponseAssertionList() {
    }

    /**
     * 创建http请求最核心的采样器
     * @return HTTPSamplerProxy
     */
    private HTTPSamplerProxy createHttpSamplerProxy() {
        // 设置HTTP请求的名称、协议、域名、端口、路径和方法
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        httpSampler.setEnabled(true);

        httpSampler.setName(stressCaseDO.getName());
        httpSampler.setProtocol(environmentDO.getProtocol());
        httpSampler.setDomain(environmentDO.getDomain());
        httpSampler.setPort(environmentDO.getPort());
        httpSampler.setProperty("HTTPSampler.path",stressCaseDO.getPath());
        httpSampler.setMethod(stressCaseDO.getMethod());

        httpSampler.setAutoRedirects(false);
        httpSampler.setUseKeepAlive(true);
        httpSampler.setFollowRedirects(true);
        httpSampler.setPostBodyRaw(true);

        //处理请求参数 GET请求
        if(HttpMethod.GET.name().equals(stressCaseDO.getMethod()) && StringUtils.isNotBlank(stressCaseDO.getQuery())){
            List<KeyValueDTO> keyValueList = JsonUtil.json2List(stressCaseDO.getQuery(), KeyValueDTO.class);
            for(KeyValueDTO keyValueDTO : keyValueList){
                httpSampler.addArgument(keyValueDTO.getKey(),keyValueDTO.getValue());
            }
        }else {
            // Post 请求
            Arguments arguments = createArguments();
            httpSampler.setArguments(arguments);
        }
        return httpSampler;
    }

    /**
     * 创建Post请求体
     * @return
     */
    private Arguments createArguments() {
        Arguments argumentManager = new Arguments();
        argumentManager.setProperty(TestElement.TEST_CLASS, Arguments.class.getName());
        argumentManager.setProperty(TestElement.GUI_CLASS, HTTPArgumentsPanel.class.getName());

        HTTPArgument httpArgument = new HTTPArgument();
        httpArgument.setValue(stressCaseDO.getBody());
        httpArgument.setAlwaysEncoded(false);
        argumentManager.addArgument(httpArgument);

        return argumentManager;
    }

    /**
     * 创建请求头 从数据库里取出来并序列化
     * @return HeadManager
     */
    private HeaderManager createHeaderManager() {
        if(StringUtils.isBlank(stressCaseDO.getHeader())){
            return null;
        }
        List<KeyValueDTO> requestHeaders = JsonUtil.json2List(stressCaseDO.getHeader(), KeyValueDTO.class);
        HeaderManager headerManager = new HeaderManager();
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        headerManager.setEnabled(true);
        headerManager.setName(stressCaseDO.getName()+" headers ");
        requestHeaders.forEach(keyValueConfig->{
            headerManager.add(new Header(keyValueConfig.getKey(),keyValueConfig.getValue()));
        });
        return headerManager;
    }

    /**
     * 创建线程组 也是从数据库里拿出
     * @return
     */
    private ThreadGroup createTheadGroup() {
        // 将线程组配置转换为DTO对象
        ThreadGroupConfigDTO configDTO = JsonUtil.json2Obj(stressCaseDO.getThreadGroupConfig(), ThreadGroupConfigDTO.class);
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

        // 设置线程组名称、线程数、预热时间，同一用户
        threadGroup.setName(configDTO.getThreadGroupName());
        threadGroup.setNumThreads(configDTO.getNumThreads());
        threadGroup.setRampUp(configDTO.getRampUp());
        threadGroup.setIsSameUserOnNextIteration(true);
        threadGroup.setScheduler(false);
        threadGroup.setEnabled(true);
        //错误之后 也要继续
        threadGroup.setProperty(new StringProperty(ThreadGroup.ON_SAMPLE_ERROR, "continue"));

        // 判断调度器是否启用
        if(configDTO.getSchedulerEnabled()){
            // 启动调度器
            threadGroup.setScheduler(true);
            // 持续时间秒
            threadGroup.setDuration(configDTO.getDuration());
            // 延迟时间
            threadGroup.setDelay(configDTO.getDelay());
        }

        // 创建循环控制器
        LoopController loopController = createLoopController(configDTO.getLoopCount());
        threadGroup.setSamplerController(loopController);

        return threadGroup;
    }

    /**
     * 创建循环控制器 决定了压测脚本到底要执行多少次
     * @param loopCount
     * @return
     */

    private LoopController createLoopController(Integer loopCount) {
        // 创建一个 LoopController 对象
        LoopController loopController = new LoopController();
        // 设置测试类的属性为 LoopController 类的名称
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        // 设置图形用户界面类的属性为 LoopController 类的名称
        loopController.setProperty(TestElement.GUI_CLASS, LoopController.class.getName());
        // 设置循环次数
        loopController.setLoops(loopCount);
        // 设置为第一次循环
        loopController.setFirst(true);
        // 启用 LoopController
        loopController.setEnabled(true);
        // 初始化 LoopController 不然次数可能会错
        loopController.initialize();
        // 返回 LoopController 对象
        return loopController;
    }

    /**
     * 创建测试计划
     * @return
     */
    private TestPlan createTestPlan() {
        TestPlan testPlan = new TestPlan(stressCaseDO.getName());
        //这是一个 TestPlan 类型的逻辑控制器
        testPlan.setProperty(TestElement.TEST_CLASS,TestPlan.class.getName());
        //如果这个对象被导入到 JMeter 界面，应该用 TestPlanGui 这个面板来展示
        testPlan.setProperty(TestElement.GUI_CLASS,TestPlan.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
        //独立运行每个线程组
        testPlan.setSerialized(true);
        //测试计划结束的时候，关闭所有线程
        testPlan.setTearDownOnShutdown(true);

        return testPlan;
    }
}

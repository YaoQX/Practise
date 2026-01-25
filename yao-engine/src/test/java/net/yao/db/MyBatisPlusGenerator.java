package net.yao.db;


import org.apache.ibatis.type.JdbcType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

import java.util.Collections;

public class MyBatisPlusGenerator {

    //根据数据库表结构，自动生成 Java 代码
    public static void main(String[] args) {
        String userName = "root";
        String password = "yaoqx";
        String serverInfo = "18.181.159.177:3306";
        String targetModuleNamePath = "/yao-engine";
        String dbName = "test_engine";
        String[] tables = {
                "project", "environment",
                "stress_case_module", "stress_case"
        };
        // 数据源配置
        FastAutoGenerator.create("jdbc:mysql://" + serverInfo + "/" + dbName + "?useUnicode = true & characterEncoding = utf8 & useSSL = false & serverTimezone = Asia/Shanghai & tinyInt1isBit = true", userName,
                        password)
                .globalConfig(builder -> {
                    builder.author("yaoqx,")        // 设置作者
                            .commentDate("yyyy-MM-dd")
                            .enableSpringdoc()
                            .disableOpenDir() //禁⽌打开输出⽬录
                            .dateType(DateType.ONLY_DATE)   //定义⽣成的实体类中⽇期类型 DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                            .outputDir(System.getProperty("user.dir") +
                                    targetModuleNamePath + "/src/main/java"); // 指定输出⽬录
                })
                .dataSourceConfig(builder -> {//Mysql下tinyint字段转换
                    builder.typeConvertHandler((globalConfig, typeRegistry,
                                                metaInfo) -> {
                        if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                            return DbColumnType.BOOLEAN;
                        }
                        return typeRegistry.getColumnType(metaInfo);
                    });
                })
                .templateConfig(builder -> {
                    //设置不⽣成controller和service

                    builder.disable(TemplateType.CONTROLLER, TemplateType.SERVICE, TemplateType.SERVICE_IMPL);
                })
                .packageConfig(builder -> {
                    builder.parent("net.yao") // ⽗包模块名
                            .entity("model")      //Entity 包名 默认值:entity 这里包名是model
                            .mapper("mapper")     //Mapper 包名 默认值:mapper
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + targetModuleNamePath +
                                            "/src/main/resources/mapper")); // 设置mapperXml⽣成路,默认存放在mapper的xml下
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要⽣成的表名 可变参数
                            .entityBuilder()// Entity策略配置
                            .enableFileOverride() // 开启⽣成Entity层⽂件覆盖
                            .idType(IdType.ASSIGN_ID)//主键策略  雪花算法⾃动⽣成的id 唯一的
                            .enableLombok() //开启lombok
                            .logicDeleteColumnName("deleted")// 说明逻辑删除是哪个字段
                            .enableTableFieldAnnotation()// 属性加上注解说明
                            .formatFileName("%sDO") //如果数据库表名是 stress_case，生成的类名就是 StressCaseDO（DO 代表 Data Object）。
                            .controllerBuilder()// Controller策略配置
                            .enableFileOverride() // 开启⽣成Controller层⽂件覆盖
                            .serviceBuilder()// Service策略配置
                            .enableFileOverride() // 开启⽣成Service层⽂件覆盖
                            .superServiceClass("")
                            .superServiceImplClass("")
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .mapperBuilder()// Mapper策略配置
                            .enableFileOverride() // 开启⽣成Mapper层⽂件覆盖
                            .formatMapperFileName("%sMapper")
                            .superClass(BaseMapper.class)
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatXmlFileName("%sMapper");
                })
                .execute();
    }
}

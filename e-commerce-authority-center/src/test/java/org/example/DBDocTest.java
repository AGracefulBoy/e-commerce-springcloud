package org.example;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数据库表文档生成
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DBDocTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void buildDBDoc(){
        DataSource dataSourceMysql = applicationContext.getBean(DataSource.class);
        //生成数据库文档配置

        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir("E:\\e-commerce-springcloud")
                .openOutputDir(false)
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker)
                .build();

        //生成文档的配置，包含自定义版本号，描述信息等
        // 数据库名_description.html
        Configuration config = Configuration.builder()
                .version("1.0.0")
                .description("e-commerce-springcloud")
                .dataSource(dataSourceMysql)
                .engineConfig(engineConfig)
                .produceConfig(getProduceConfig())
                .build();

        //执行config
        new DocumentationExecute(config).execute();
    }

    /**
     * 配置想要生成的数据表和想要忽略的数据表
     * @return
     */
    private ProcessConfig getProduceConfig(){
        //想要忽略的数据表
        List<String> ignoreTableName = Collections.singletonList("undo_log");

        //忽略以a , b开头的数据表
        List<String> ignorePrefix = Arrays.asList("a","b");
        //忽略以这些为后缀的表
        List<String> ignoreSuffix = Arrays.asList("_test","_Test");

        return ProcessConfig.builder()
                //根据表名生成
                .designatedTableName(Collections.emptyList())
                //根据前缀生成
                .designatedTablePrefix(Collections.emptyList())
                //根据后缀生成
                .designatedTableSuffix(Collections.emptyList())
                .ignoreTableName(ignoreTableName)
                .ignoreTablePrefix(ignorePrefix)
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }

}

package com.xr.getway.configration.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = com.xr.getway.configration.database.XrDataSourceConfig.PACKAGE, sqlSessionFactoryRef = com.xr.getway.configration.database.DbFinal.XR_SQL_SESSION_FACTORY)
public class XrDataSourceConfig {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.wh.wxfirst.mybatis.entity";
    static final String MAPPER_LOCATION = "classpath*:mapper/*.xml";

    /**
     * &serverTimezone=GMT%2B8  表示定义为中国标准时区
     * &allowMultiQueries=true  支持批量修改操作
     *
     */
    @Bean(name = com.xr.getway.configration.database.DbFinal.XR_DB_SOURCE)
    @Primary
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1/user?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=GMT%2B8&allowMultiQueries=true");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean(name = com.xr.getway.configration.database.DbFinal.XR_SQL_SESSION_FACTORY)
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier(com.xr.getway.configration.database.DbFinal.XR_DB_SOURCE) DataSource lwkDataSource) throws Exception {
        //创建一个session工厂
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //注入该数据源
        bean.setDataSource(lwkDataSource);
        //以下两行代码是适用于xml文件配置 （如果是用spring bean加载机制 则两行代码可以省略  ）
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION);
        bean.setMapperLocations(resources);
        return bean.getObject();
    }


    @Bean(name = com.xr.getway.configration.database.DbFinal.XR_SQL_SESSION_TEMPLATE)
    @Primary
    public SqlSessionTemplate lwkSqlSessionTemplate(@Qualifier(com.xr.getway.configration.database.DbFinal.XR_SQL_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
        System.out.println("{连接xr数据源成功}");
        //将工厂放入到模板中 连接数据源
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = com.xr.getway.configration.database.DbFinal.XR_TRANSACTION_MANAGER)
    @Primary
    public DataSourceTransactionManager lwkTransactionManager(@Qualifier(com.xr.getway.configration.database.DbFinal.XR_DB_SOURCE) DataSource masterDataSource) {
        //将此数据源加上事务
        return new DataSourceTransactionManager(masterDataSource);
    }

    @Bean(name = com.xr.getway.configration.database.DbFinal.XR_JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier(com.xr.getway.configration.database.DbFinal.XR_DB_SOURCE) DataSource masterDataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(masterDataSource);
        return jdbcTemplate;
    }
}

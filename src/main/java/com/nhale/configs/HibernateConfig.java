package com.nhale.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;

/**
 * cấu hình tạo bean SessionFactory cho hibernate
 */
@Configuration
// khai báo vị trí file lưu các đoạn văn bản dùng chung tạo sẵn tính theo classpath đọc từ
// file classes trong target khi build ra
@PropertySource("classpath:database.properties")
public class HibernateConfig {
    // tự động map với bean đã tạo sẵn của hệ thống để lấy thông tin của các văn bản tạo sẵn
    @Autowired
    private Environment env;

    /**
     * bean SessionFactory để truy vấn dữ liệu
     */
    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        // nơi chứa các persistence class map với các bảng csdl
        factory.setPackagesToScan("com.nhale.pojos");
        // lấy dataSource
        factory.setDataSource(dataSource());
        // lấy các Properties
        factory.setHibernateProperties(hibernateProperties());

        return factory;
    }

    /**
     * bean DataSource
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // khai báo các cấu hình cho DataSource
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("hibernate.connection.driverClass")));
        dataSource.setUrl(env.getProperty("hibernate.connection.url"));
        dataSource.setUsername(env.getProperty("hibernate.connection.username"));
        dataSource.setPassword(env.getProperty("hibernate.connection.password"));

        return dataSource;
    }

    /**
     * @return Properties cho SessionFactory
     */
    public Properties hibernateProperties() {
        Properties pros = new Properties();
        // có hiển thị câu truy vấn gốc của loại csdl đang sử dụng không
        pros.setProperty(SHOW_SQL, env.getProperty("hibernate.showSql"));
        // loại dialect tương ứng với loại csdl để hibernate biết cách dịch sang ngôn ngữ gốc của csdl đó
        pros.setProperty(DIALECT, env.getProperty("hibernate.dialect"));

        return pros;
    }

    /**
     * bean cho phép tự động tạo transaction khi truy vấn dữ liệu, không cần gọi bean tường minh
     */
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager h = new HibernateTransactionManager();

        // lấy SessionFactory từ bean tạo bằng hàm getSessionFactory
        h.setSessionFactory(getSessionFactory().getObject());

        return h;
    }
}

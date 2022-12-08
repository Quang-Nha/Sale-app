package com.nhale.configs;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * config DispatcherServlet
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * khai báo các class cấu hình không implements WebMvcConfigurer
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                HibernateConfig.class,
                TilesConfig.class,
                SpringSecurityConfig.class
        };
    }

    /**
     * khai báo class cấu hình Context là WebApplicationContextConfig(tạo các beans)
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebApplicationContextConfig.class
        };
    }

    /**
     * đường dẫn Dispatcher xác định các controller ServletMappings bắt đầu từ "/" sau /Context root/
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}

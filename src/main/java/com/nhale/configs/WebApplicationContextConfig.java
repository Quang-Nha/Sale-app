package com.nhale.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhale.formatter.CategoryFomatter;
import com.nhale.handlers.LoginSuccessHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@EnableTransactionManagement// cho phép tự động tạo transaction khi truy vấn dữ liệu
@ComponentScan(basePackages = {
        "com.nhale.controllers",
        "com.nhale.repository",
        "com.nhale.service",
        "com.nhale.validator",
})// khai báo địa chỉ tìm các controller
public class WebApplicationContextConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * tạo bean InternalResourceViewResolver cấu hình nơi tìm các tệp view là /WEB-INF/jsp/
     * và đuôi của view là .jsp
     */
    @Bean
    public InternalResourceViewResolver geInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

    /**
     * khai báo MessageSource sử dụng file properties tên messages để làm thư viện dùng chung
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");

        return source;
    }

    /**
     * map đường dẫn chứa các file tĩnh như css, images tính từ thư mục webapp
     * map đường dẫn /resources/css/ thành /css/..
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // map đường dẫn /resources/css/ thành /css/..
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/resources/css/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("/resources/images/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("/resources/js/");
    }

    /**
     * bean resolver cho phép xử lý file
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF_8");

        return resolver;
    }



    /**
     * xác nhận sử dụng validator/xác thực tự tạo
     */
    @Override
    public Validator getValidator() {
        return validator();
    }

    /**
     * bean cấu hình sử dụng xác thực dữ liệu
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean v = new LocalValidatorFactoryBean();
        // xác thực địa chỉ file lưu các tin nhắn
        v.setValidationMessageSource(messageSource());

        return v;
    }

    /**
     * thêm các đối tượng Formatter đã được cấu hình các chuyển đổi dữ liệu cho 1 class vào hệ thống
     * mỗi khi có dữ liệu khác kiểu gán cho đối tượng của class trên thì nó sẽ được chuyển sang kiểu của class
     * theo cách mà Formatter khai báo
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // thêm Formatter cho class Category
        registry.addFormatter(new CategoryFomatter());
    }

//    /**
//     * bean cấu hình cho cloud Cloudinary để upload file lên đó
//     */
//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "lenha",
//                "api_key", "539966557274424",
//                "api_secret", "crcW4uR8nuXjlY6smNQ4flYOT2E",
//                "secure", "true"));
//    }

}

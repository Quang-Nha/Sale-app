/*
 *
 */
package com.nhale.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

/**
 * cấu hình cho title phân chia bố cục các phần của trang
 * @author LeNha
 */
@Configuration
public class TilesConfig {

    @Bean
    public UrlBasedViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();

        resolver.setViewClass(TilesView.class);
        resolver.setOrder(-2);

        return resolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer configurer = new TilesConfigurer();

        // địa chỉ file xml cấu hình cấu trúc các phần trang
        configurer.setDefinitions("/WEB-INF/tiles.xml");
        configurer.setCheckRefresh(true);

        return configurer;
    }
}

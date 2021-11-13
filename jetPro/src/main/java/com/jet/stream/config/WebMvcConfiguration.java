package com.jet.stream.config;

import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MultipartProperties properties;


    public WebMvcConfiguration(MultipartProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize(DataSize.ofMegabytes(properties.getMaxFileSize().toMegabytes()));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(properties.getMaxRequestSize().toMegabytes()));
        return factory.createMultipartConfig();
    }

}

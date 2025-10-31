package com.itheima.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.interceptor.JwtTokenAdminInterceptor;
import com.itheima.interceptor.JwtTokenUserInterceptor;
import com.itheima.json.JacksonObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@OpenAPIDefinition( info = @Info( title = "苍穹外卖项目接口文档", version = "2.0", description = "签穹外卖项目接口文档" ) )

public class WebMvcConfiguration implements WebMvcConfigurer {
        @Autowired
        private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

        @Autowired
        private JwtTokenUserInterceptor jwtTokenUserInterceptor;

        @Bean
        public ObjectMapper jacksonObjectMapper() {
                return new JacksonObjectMapper();
        }

        // 🔹 Admin 接口分组
        @Bean
        public GroupedOpenApi adminApi() {
                return GroupedOpenApi.builder()
                        .group("管理端接口")
                        .pathsToMatch("/admin/**")
                        .build();
        }

        // 🔹 User 接口分组
        @Bean
        public GroupedOpenApi userApi() {
                return GroupedOpenApi.builder()
                        .group("用户端接口")
                        .pathsToMatch("/user/**")
                        .build();
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // admin 用
                registry.addInterceptor(jwtTokenAdminInterceptor)
                        .addPathPatterns("/admin/**")
                        .excludePathPatterns("/admin/employee/login");

                // user 用
                registry.addInterceptor(jwtTokenUserInterceptor)
                        .addPathPatterns("/user/**")
                        .excludePathPatterns("/user/user/login");
        }

        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                log.info("扩展消息转换器...");

                MappingJackson2HttpMessageConverter converter =
                        new MappingJackson2HttpMessageConverter(jacksonObjectMapper());

                // ⚠ 不放在 index 0，避免覆盖掉 springdoc/knife4j 需要的默认转换器
                converters.add(converter);
        }

}

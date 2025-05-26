package com.nhnacademy.meetingroomservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignUserHeaderConfig {

    @Bean
    public RequestInterceptor userHeaderInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xUser = request.getHeader("X-USER");
                if (xUser != null) {
                    requestTemplate.header("X-USER", xUser);
                }
            }
        };
    }
}

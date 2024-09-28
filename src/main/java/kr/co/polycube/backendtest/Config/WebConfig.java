package kr.co.polycube.backendtest.Config;

import kr.co.polycube.backendtest.Filter.SpecialCharacterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<SpecialCharacterFilter> specialCharacterFilter(){
        FilterRegistrationBean<SpecialCharacterFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new SpecialCharacterFilter());
        registrationBean.addUrlPatterns("/*");
        //필터 우선순위 가장 높게
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        //h2-console은 필터링 제외 (excludepatterns에 추가)
        registrationBean.addInitParameter("excludePatterns", "/h2-console");

        return registrationBean;
    }
}
package com.example.demo;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.example.*")
public class HelloSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringBootApplication.class, args);
		ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
		map.put("", "");
		
		HashMap<String,Object> maps = new HashMap();
		maps.put("","");
	}
	
	/**
     * 配置跨域访问的过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean registerFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.addUrlPatterns("/*");
        bean.setFilter(new CrosFilter());
        return bean;
    }
}

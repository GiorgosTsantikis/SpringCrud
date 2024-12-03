package com.example.demo;

import com.example.demo.config.AppConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AppConfiguration.class))
@ActiveProfiles("test")
class DemoApplicationTests {



	@Test
	void contextLoads() {
	}

}

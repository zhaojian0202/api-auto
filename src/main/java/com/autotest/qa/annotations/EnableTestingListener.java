package com.autotest.qa.annotations;


import com.autotest.qa.dataSource.DynamicDataSourceAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启整个测试框架的监听
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({DynamicDataSourceAspect.class})
public @interface EnableTestingListener {

    String value() default "";

}

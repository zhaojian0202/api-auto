package com.autotest.qa.annotations;

import org.testng.annotations.DataProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataDriver {
    String filePath() default "";

    String sheetName() default "";

}

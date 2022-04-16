package com.autotest.qa.common;

import com.autotest.qa.Application;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;


@SpringBootTest(classes = Application.class)
public class BaseTest extends AbstractTestNGSpringContextTests {

}

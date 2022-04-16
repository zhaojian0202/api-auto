# api-auto
接口自动化

**注解说明**

@DataDriver 作用于测试方法上，用于数据驱动，静态或动态读取测试用例

**RPC接口测试支持（Feign）**

对于已经声明API的Jar包可以采用类似Feign的形式实现本地化的RPC调用，API规范默认使用SpringMvcContract
自定义实现的RestTemplate可以实现接口请求前后的自定义拦截，如：参数打印，通用响应码判断，动态请求头等
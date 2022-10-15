# encrypt-spring-boot-starter
### maven坐标
   ```xml
   <dependency>
      <groupId>cloud.longfa</groupId>
      <artifactId>encrypt-spring-boot-starter</artifactId>
      <version>1.0.2-RELEASE</version>
   </dependency>
   ```
#### 介绍
## 数据加密框架
### 支持的场景 
##### 一 、数据传输加密解密
##### 二 、数据存储 加密解密

### 实现方案:
动态代理、递归获取每一个节点 通过反射操作字节码文件

### 目录结构：
```
│  HELP.md
│  pom.xml
│  README.en.md
│  README.md
│  tree.md
│  
├─src
│  └─main
│      ├─java
│      │  └─cloud
│      │      └─longfa
│      │          └─encrypt
│      │              ├─anotation
│      │              │      Decrypt.java
│      │              │      EnableEncrypt.java
│      │              │      Encrypt.java
│      │              │      
│      │              ├─aspectj
│      │              │      EncryptHandler.java
│      │              │      
│      │              ├─badger
│      │              │      HoneyBadgerEncrypt.java
│      │              │      
│      │              ├─config
│      │              │      AESConfiguration.java
│      │              │      EncryptAutoConfiguration.java
│      │              │      EncryptConfiguration.java
│      │              │      EncryptImportSelector.java
│      │              │      EncryptProvider.java
│      │              │      RSAConfiguration.java
│      │              │      
│      │              ├─cron
│      │              │      CronServer.java
│      │              │      
│      │              ├─enums
│      │              │      CipherMode.java
│      │              │      Scenario.java
│      │              │      
│      │              ├─generator
│      │              │      GeneratorSecretKey.java
│      │              │      
│      │              ├─handler
│      │              │      ExecutorPostProcessor.java
│      │              │      ScenarioEncryptSchedule.java
│      │              │      ScenarioHandler.java
│      │              │      ScenarioHolder.java
│      │              │      ScenarioPostProcessor.java
│      │              │      ScenarioSchedule.java
│      │              │      StorageScenario.java
│      │              │      TransmitScenario.java
│      │              │      
│      │              ├─register
│      │              │      RegisterBeanDefinition.java
│      │              │      
│      │              ├─spel
│      │              │      SpELExpressionHandler.java
│      │              │      SpELParserContext.java
│      │              │      
│      │              └─util
│      │                      EncryptUtils.java
│      │                      
│      └─resources
│          ├─config
│          │      cron.setting
│          │      
│          └─META-INF
│                  spring.factories
│                  
└─target
    ├─classes
    │  ├─cloud
    │  │  └─longfa
    │  │      └─encrypt
    │  │          ├─anotation
    │  │          │      Decrypt.class
    │  │          │      EnableEncrypt.class
    │  │          │      Encrypt.class
    │  │          │      
    │  │          ├─aspectj
    │  │          │      EncryptHandler.class
    │  │          │      
    │  │          ├─badger
    │  │          │      HoneyBadgerEncrypt.class
    │  │          │      
    │  │          ├─config
    │  │          │      AESConfiguration.class
    │  │          │      EncryptAutoConfiguration.class
    │  │          │      EncryptConfiguration.class
    │  │          │      EncryptImportSelector.class
    │  │          │      EncryptProvider.class
    │  │          │      RSAConfiguration.class
    │  │          │      
    │  │          ├─cron
    │  │          │      CronServer.class
    │  │          │      
    │  │          ├─enums
    │  │          │      CipherMode.class
    │  │          │      Scenario.class
    │  │          │      
    │  │          ├─generator
    │  │          │      GeneratorSecretKey.class
    │  │          │      
    │  │          ├─handler
    │  │          │      ExecutorPostProcessor.class
    │  │          │      ScenarioEncryptSchedule.class
    │  │          │      ScenarioHandler$1.class
    │  │          │      ScenarioHandler.class
    │  │          │      ScenarioHolder.class
    │  │          │      ScenarioPostProcessor.class
    │  │          │      ScenarioSchedule$1.class
    │  │          │      ScenarioSchedule.class
    │  │          │      StorageScenario.class
    │  │          │      TransmitScenario.class
    │  │          │      
    │  │          ├─register
    │  │          │      RegisterBeanDefinition$1.class
    │  │          │      RegisterBeanDefinition$GeneratorSecretKeyFactory.class
    │  │          │      RegisterBeanDefinition.class
    │  │          │      
    │  │          ├─spel
    │  │          │      SpELExpressionHandler.class
    │  │          │      SpELParserContext$1.class
    │  │          │      SpELParserContext.class
    │  │          │      
    │  │          └─util
    │  │                  EncryptUtils.class
    │  │                  
    │  ├─config
    │  │      cron.setting
    │  │      
    │  └─META-INF
    │          spring-configuration-metadata.json
    │          spring.factories
    │          
    └─generated-sources
        └─annotations

```

### 软件架构
软件架构说明

争对数据加密 网络传输、存储 数据保密性 

适用人群：企业、高校、政府等  对数据保性要求严苛的（第一个版本用的递归算法实现 需要优化的可以联系我）

基于springboot 开发的加解密框架  jdk支持：11以上 不高于17

支持场景有：网络传输、存储 两大场景

支持的加密的算法 RSA、AES、SM4....

设计模式: 策略模式

### UML 时序图

*加密的整个调用过程*

![](C:\Users\Administrator\Pictures\Saved Pictures\java\EncryptHandler_encrypt.svg)

*解密处理过程*

![](C:\Users\Administrator\Pictures\Saved Pictures\java\EncryptHandler_decrypt.svg)

#### ![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20221015234958851.png)





### 安装教程

1. 导入maven坐标

   ```xml
   <dependency>
      <groupId>cloud.longfa</groupId>
      <artifactId>encrypt-spring-boot-starter</artifactId>
      <version>1.0.2-RELEASE</version>
   </dependency>
   ```

### 使用说明

1. 使用说明

   在启动类上添加 @EnableEncrypt 注解  表示 启用加密模块

   加密注解：@Encrypt 

   解密注解：@Decypt

   配置密钥以及 AES加密算法配置 key iv   RSA加密算法配置 private_key public_key

2. 参数说明

   加解密参数一样：1 scenario值为枚举类型  2 cipher值为枚举类型 3  caseSensitive其值未做处理 所以不用设置

   ​								4 fields 值为数组类型 加密的字段名（多个） **5** **value** 重点支持spel 表达式 已经经过加工 简单应用

   ​						

3.  ```java
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Encrypt {
        /**
         * 应用场景 网络传输、或者 持久化 默认用于加密存储
         *
         * @return {@link  Scenario}
         */
        Scenario scenario() default Scenario.storage;
    
        /**
         * 默认加密方式 AES算法加密
         *
         * @return {@link  CipherMode}
         */
        CipherMode cipher() default CipherMode.AES;
    
        /**
         * 区分字段大小写 默认不区分
         *
         * @return false boolean
         */
        boolean caseSensitive() default false;
    
        /**
         * 加密的字段名方法加密需要指定字段名称 默认是对字段解密
         * 默认加密data中的数据 不区分大小写
         *
         * @return the string [ ]
         */
        String[] fields() default {"data"};
    
        /**
         * SpEL表达式  对SpEL表达式的支持
         * * @beanName.method  or @beanName.field  the field not be -> private decorated
         * * @ss.abc()  @ss.name
         *
         * @return the string
         */
        String value() default "";
    }
    ```

### 测试案例：



### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

### 特技

取决于加解密耗时时长 全注解形式配置使用 简单除暴 一共就三个注解 加上注解的一些参数

我用的垃圾电脑 测不出啥效果  感兴趣的你们自己去测试一下

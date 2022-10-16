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
## 数据加密 场景启动器
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
│      │              │      Decrypt.java                   加密注解
│      │              │      EnableEncrypt.java             导入模块注解
│      │              │      Encrypt.java                   解密注解
│      │              │      
│      │              ├─aspectj
│      │              │      EncryptHandler.java            注解处理器 aop
│      │              │      
│      │              ├─badger
│      │              │      HoneyBadgerEncrypt.java        加密实现类
│      │              │      
│      │              ├─config
│      │              │      AESConfiguration.java          AES配置
│      │              │      EncryptAutoConfiguration.java  
│      │              │      EncryptConfiguration.java      
│      │              │      EncryptImportSelector.java     
│      │              │      EncryptProvider.java           
│      │              │      RSAConfiguration.java          RSA配置
│      │              │      
│      │              ├─cron
│      │              │      CronServer.java
│      │              │      
│      │              ├─enums
│      │              │      CipherMode.java               算法枚举
│      │              │      Scenario.java                 场景枚举
│      │              │      
│      │              ├─generator
│      │              │      GeneratorSecretKey.java       密钥生成代理接口
│      │              │      
│      │              ├─handler
│      │              │      ExecutorPostProcessor.java    
│      │              │      ScenarioEncryptSchedule.java 场景调度
│      │              │      ScenarioHandler.java         场景处理
│      │              │      ScenarioHolder.java          核心容器
│      │              │      ScenarioPostProcessor.java   
│      │              │      ScenarioSchedule.java        场景调度实现类
│      │              │      StorageScenario.java         存储场景
│      │              │      TransmitScenario.java        传输场景
│      │              │      
│      │              ├─register
│      │              │      RegisterBeanDefinition.java  注册密钥生成代理类
│      │              │       
│      │              ├─spel
│      │              │      SpELExpressionHandler.java   spel表达式处理类
│      │              │      SpELParserContext.java       
│      │              │      
│      │              └─util
│      │                      EncryptUtils.java          工具类

```

### 软件架构
软件架构说明

争对数据加密 网络传输、存储 数据保密性 

适用人群：企业、高校、政府等  对数据保性要求严苛的（第一个版本用的递归算法实现 需要优化的可以联系我）

基于springboot 开发的加解密框架  jdk支持：11以上 不高于17

支持场景有：网络传输、存储 两大场景

支持的加密的算法 RSA、AES、SM4....

设计模式: 策略模式


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

```java
@EnableEncrypt  //开启加密 让加解密模块生效
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

```java
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
```java
   @Target(ElementType.METHOD)
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   public @interface Decrypt {
       /**
        * 应用场景 网络传输、或者 持久化
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
        * 区分字段大小写 默认是不区分
        *
        * @return false boolean
        */
       boolean caseSensitive() default false;
   
       /**
        * 加密的字段名方法加密需要指定字段名称 默认是对data字段解密
        *
        * @return the string [ ]
        */
       String[] fields() default {"data"};
   
       /**
        * SpEL表达式   对SpEL表达式的支持
        * * @beanName.method  or @beanName.field  the field not be -> private decorated
        * * @ss.abc()  @ss.name
        *
        * @return the string
        */
       String value() default "";
   }

```

### 配置
    
   ```yaml
   badger:
     encrypt:
       #AES加密配置
       aes-iv: xxxxxxx #16位 偏移量
       aes-key: xxxxxx
       #RSA加密配置
       public-key-base64: xxxxxx    #公钥
       private-key-base64: xxxxxxx  #私钥

```
### 使用案例：
写的是水了点 将就看 
   ```java
    //控制器
       //传输加密 模拟从service获取数据 @ENC.SYSTEM_USER 同等 Fields.SYS_USER 同等 @ENC.custom()
       // 更推荐 "@ENC.SYSTEM_USER"这种写法
      @GetMapping("/aaa")
      @Encrypt(value ="@ENC.SYSTEM_USER",cipher = CipherMode.RSA,scenario = Scenario.transmit)
      public Map<String,List<Test>> getTest1(){
            return queryData();
      }

       //传输解密
       @PostMapping("/bbb")
       @Decrypt(value = SPEL.SYS_USER,cipher = CipherMode.RSA,scenario = Scenario.transmit)
       public Map<String,List<Test>> test1(@RequestBody Map<String,List<Test>> map){
           return map;
       }


       //加密 解密 模拟存储场景 模拟多个参数 测试它会不会对request response有操作
       @PostMapping("/ccc")
       public void getTest2(@RequestBody Map<String,List<Test>> map, HttpServletRequest request,
            HttpServletResponse response){
            List<Test> testList = testService.testList(map.get("data"),request,response);
            System.out.println(testList+"\n解密了");
      }
      
      
      //配置需要解密的参数参数
      @Component("ENC")
      public class SPEL {
         //1 系统用户
         public static final String SYS_USER = "@ENC.SYSTEM_USER";
         // 待加密字段 地址、用户名、邮箱、密码
         public static final String[] SYSTEM_USER = {"address","username","email","password"};


         //1
         public String[] custom(){
            return new String[]{"address","username","email"};
         }


         //2
         public static final String ADM_USER= "@ENC.ADMIN_USER";
         //2
         public static final String[] ADMIN_USER = {"username","password","email","card","phone"};

      }
```

```java
   //测试类 service层 模拟存储至数据库 从数据库取出数据 的加解密过程
   @Service("testService")
    public class TestService {

   //or value = "@ENCRYPT.ADMIN_USER", 同等 Fields.ADM_USER
   @Encrypt(value = SPEL.ADM_USER,cipher = CipherMode.AES,scenario = Scenario.storage)
   @Decrypt(value = "@ENC.ADMIN_USER",cipher = CipherMode.AES,scenario = Scenario.storage)
   public List<Test> testList(List<Test> testList, HttpServletRequest request, HttpServletResponse response){
      System.out.println(testList+"\n加密了");
      return testList;
   }
}
```
#### 当然 你也可以这样写
```java
    @RequestMapping("/efj")
    @Decrypt(fields = {"password","card","address"},cipher = CipherMode.RSA,scenario = Scenario.transmit)
    public Map<String,List<Test>> test1(@RequestBody Map<String,List<Test>> map){
        return map;
    }

```

### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

### 特技

取决于加解密耗时时长 全注解形式配置使用 简单除暴 一共就三个注解 加上注解的一些参数

我用的垃圾电脑 测不出啥效果  感兴趣的你们自己去测试一下

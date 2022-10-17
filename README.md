# **encrypt-spring-boot-starter**

版权所有 请勿抄袭！！！

作者：longfa    email:longfa0130@163.com || longfa0130@gmail.com

因为最近在接触存储加密 所以写了这个starter 喜欢的给个支持小星星 么么哒

## maven坐标

   ```xml
<dependency>
   <groupId>cloud.longfa</groupId>
   <artifactId>encrypt-spring-boot-starter</artifactId>
   <version>1.0.3-RELEASE</version>
</dependency>
   ```

## 优势

1. **全注解、支持spel表达式(改造过、用法更简单)、支持任意字段、任意参数、任意请求方式 加解密**
2. **多种场景加减密(传输场景、存储场景)**
3. **支持扩展其他场景(已经实现网络传输、存储加解密)**
4. **支持扩展其他的加密算法**
5. **支持的加密模式：单类算法加密（多参数一种算法）、多类算法混合(单个参数|字段单个算法)、混合模式(单个参数 多种算法)、混合模式之随机密钥**
6. **支持任意请求方式 、任意多个参数、body 支持java集合、实体类等等**
7. **支持RPC框架  远程调用**
8. **支持任意多个字段|参数 任意多种加密算法同时用  前提是前端不会骂人！！！**

## 数据加密 场景启动器

### 支持的场景

#### 一 、数据传输加密解密

网络传输后端加减密 前端加解密没空写

支持RPC框架 远程调用 多种算法混合使用  能控制到每一个字段 都使用独立的算法 不必担心解不了密！！！ 怎么加怎么解

#### 二 、数据存储 加密解密

支持存储加解密 多种算法混合使用

## 支持的请求方式

任意请求方式 不仅仅只是争对body  也支持请求参数(任意个)

## 层级要求

无限层 支持无限套娃！！！

比如map里面套map 在嵌套list

只要标识了该字段名 就是天涯海角 都能处理

## 支持的加密模式

支持的加密算法 AES、RSA、SM4 混合加密 动态密钥混合加密 同时支持扩展其他算法

1. 单模式加密：

   一个接口只使用一种加密算法

2. 多种算法单模式

   一个接口下每一个参数使用单独的加密算法（不推荐这种 前端容易崩溃）

3. 混合加密：

   两种以上的加密算法融合使用 密钥使用自己配置的

4. 动态混合加密：

   密钥是随机生成  每一次请求密钥都是不一样 提供了外部获取密钥的方式

## 实现方案

动态代理、递归算法 策略模式 工厂模式

## 目录结构

老版本：

```
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
│      │              │      RegisterBeanDefinition.java  注册密钥生成代理类 工厂模式
│      │              │       
│      │              ├─spel
│      │              │      SpELExpressionHandler.java   spel表达式处理类
│      │              │      SpELParserContext.java       
│      │              │      
│      │              └─util
│      │                      EncryptUtils.java          工具类

```


## 使用说明

1. 导入maven坐标

   ```xml
   <dependency>
      <groupId>cloud.longfa</groupId>
      <artifactId>encrypt-spring-boot-starter</artifactId>
      <version>1.0.2-RELEASE</version>
   </dependency>
   ```



## 配置说明

```yaml
badger:
  encrypt:
    #16字节 1byte = 8bit
    aes-iv: 2404308462934f9f
    #128/192/256 bits 16/24/32/ 字节
    aes-key: c4a98b23a8d94035bc9e0896b620b6a7
    public-key-base64: xxx   #rsa加密算法公钥
    private-key-base64: xxx  #rsa加密算法私钥
    sm4-key: xxxxxx  #长度为16个字节
    sm4-iv: xxxxxxx #长度为16个字节
```

## 注解参数说明

- 在启动类上添加 **@EnableEncrypt** 注解  表示 启用加密模块
- 加密注解：**@Encrypt**
- 解密注解：**@Decypt**
- 字段注解：**@Badger**

**加解密参数一样**

1、scenario

scenario值为枚举类型

```java
/**
 * The enum Scenario.
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 应用场景 网络接传输 、存储
 * @since : 1.0.0
 */
public enum Scenario{
    /**
     * Transmit scenario.
     */
    transmit,  //传输
    /**
     * Storage scenario.
     */
    storage,   //存储
}
```

2 cipher

cipher值为枚举类型

```java
/**
 * The enum Cipher mode.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 加密模式
 * @since : 1.0.0
 */
public enum CipherMode {
    /**
     * Aes cipher mode.
     */
    AES,
    /**
     * Rsa cipher mode.
     */
    RSA,
    /**
     * Sm 4 cipher mode.
     */
    SM4,
    /**
     * 混合加密 AES_RSA
     */
    AES_RSA,
    /**
     *混合加密 SM4_RSA
     */
    SM4_RSA,
    /**
     * 该值不能用于 @Encrypt @Decrypt注解 这用于 @Badger
     * 如果为DEFAULT 则会使用@Encrypt 或者@Decrypt的加密模式 @Badger注解的默认值为DEFAULT
     * 你可以切换成其他支持的属性 比如AES RSA SM4 或者 混合加密方式
     */
    DEFAULT,

}
```

3  caseSensitive其值未做处理 所以不用设置 默认不区分大小写

4 fields

fields 值为数组类型 加密的字段名（多个）

```java
  /**
     * 加密的字段名方法加密需要指定字段名称 默认是对data字段解密 
     *
     * @return the string [ ]
     */
    String[] fields() default {"data"};


	@RequestMapping("/efj")
    @Decrypt(fields = {"password","card","address"},cipher = CipherMode.RSA,scenario = Scenario.transmit)
    public Map<String,List<Test>> test1(@RequestBody Map<String,List<Test>> map){
        return map;
    }

   Test类
   public class Test implements Cloneable{
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String card;
```

5 value spel 表达式 已经经过加工 简单应用 @[beanName].[方法方法/常量名]

```java
/**
 * SpEL表达式   对SpEL表达式的支持
 * * @beanName.method  or @beanName.field  the field not be -> private decorated
 * * @ss.abc()  @ss.name
 *
 * @return the string
 */
String value() default "";
```

6 dynamic 动态属性

使用规则 结合混合加密算法使用 否则不生效

```java
/**
 * 动态密钥 支持混合算法 sm4-rsa aes-rsa {@CipherMode}
 * @return the boolean
 */
boolean dynamic() default false;
```





## 使用教程

启动类上标注 @EnableEncrypt

```java
@EnableEncrypt  //开启加密 让加解密模块生效 ！！！不标注模块就不生效
@SpringBootApplication
public class Application {
   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }
}
```

### （一） 、传输场景案例

*非混合加密*

#### 1.1   **简单除暴法**

*@Encrypt 安装字段进行加解密 属性： fields = {"username","password"}  不光是对字段名生效 参数也生效*  AES加密 那么就用AES解密

实体类

```java
public class Test implements Cloneable{
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
```

控制器

 ```java
xxxController //控制器
       //传输加密  @ENC.SYSTEM_USER 同等 Fields.SYS_USER 同等 @ENC.custom()
    @GetMapping("/aaa")
    @Encrypt( fields = {"username","password"},cipher = CipherMode.AES,scenario = Scenario.transmit)
    public Map<String,List<Test>> getTest1(){
    return queryData(); //模拟100条数据
}

  private Map<String,List<Test>> queryData(){
        Map<String,List<Test>> stringListHashMap = new HashMap<>();
        Test testF = new Test();
        List<Test> testList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Test test = testF.clone();
            test.setUsername("少林寺张三丰"+new Random().nextFloat());
            test.setPassword("密码8个8 你来破解啊"+ UUID.randomUUID());
            test.setAddress("xx省xx市xxx");
            test.setEmail("longfaxxxx@163.com");
            test.setPhone("18886137xxx");
            test.setCard("xxx6xx19xx01306xxx");
            testList.add(test);
        }
        stringListHashMap.put("data",testList);
      return stringListHashMap;
    }
 ```

**如果解密呢？？？**

xxx控制器

```java
//传输解密
@PostMapping("/bbb")
@Decrypt(fields = {"username","password"},cipher = CipherMode.AES,scenario = Scenario.transmit)
public Map<String,List<Test>> test1(@RequestBody Map<String,List<Test>> map){
    return map;
}
```

#### 1.2  spel表达式

@Encrypt  注解属性：value = "@类名.方法"  跟fields属性一样 方法返回的必须是数组 也是返回待加密的字段 {"username","password"}

```java
	xxxController //控制器
        //传输加密  @ENC.SYSTEM_USER 同等 Fields.SYS_USER 同等 @ENC.custom() 
        //ta们最终返回的是数组 {"username","password"} 不用好奇 用了模板解析
        @GetMapping("/aaa")
        @Encrypt(value ="@ENC.SYSTEM_USER",cipher = CipherMode.RSA,scenario = Scenario.transmit)
        public Map<String,List<Test>> getTest1(){
        return queryData();  //模拟的数据
    }
```

#### 1.3  @Badger

@Badger  标注在需要加密的字段上

属性只有一个：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Badger {
    /**
     * 默认加密方式 AES算法加密 DEFAULT则会根据@Encrypt 或者@Decrypt cipher 的值
     *
     * @return {@link  CipherMode}
     */
    CipherMode cipher() default CipherMode.DEFAULT;
}
```

​	跟@Encrypt @Decrypt 上的一个属性一模一样

如果你修改了默认值CipherMode.DEFAULT 则会用@Badger注解上的为准

**每一个字段每一种加密算法 会出现上面后果呢？？？**

不用担心  我做过处理  不会导致多层加密  它会根据你选择的加密算法来加密该字段

后果就是我能加密  前端可能会崩溃

实体类

```java
@Badger(cipher = CipherMode.SM4)
private String phone;
@Badger(cipher = CipherMode.RSA)
private String address;
```

控制器

```java
@GetMapping("/aaa")
@Encrypt(cipher = CipherMode.SM4,scenario = Scenario.transmit)
public Map<String,List<Test>> getTest1(){
    return queryData();
}
```

太简单了 你可能不喜欢 请看下一种

#### 1.4  每个字段每一种加密算法

支持这种模式  个人不推荐 容易引起纠纷 前端跟后端的纠纷

```java
@Badger(cipher = CipherMode.AES)
private String email;
@Badger(cipher = CipherMode.SM4)
private String phone;
@Badger(cipher = CipherMode.RSA)
private String address;
```

#### 1.5  无敌模式

```java
//xxx控制器
@GetMapping("/aaa")
@Encrypt(value =SPEL.SYS_USER,fileds={"username","password"},cipher = CipherMode.RSA,scenario = Scenario.transmit)
public Map<String,List<Test>> getTest1(){
    return queryData();
}
```

```java
//xxx 实体类
private String username;
private String password;
@Badger(cipher = CipherMode.AES)
private String email;
@Badger(cipher = CipherMode.SM4)
private String phone;
@Badger(cipher = CipherMode.RSA)
private String address;
```

放心 这种方式也是支持的！！！！

#### 1.6 如何解密

简单得很 你怎么加密  你就这么解密

```java
//xxx控制器
//传输解密
@PostMapping("/bbb")
@Decrypt(value = SPEL.SYS_USER,cipher = CipherMode.SM4,scenario = Scenario.transmit)
public Map<String,List<Test>> test1(@RequestBody Map<String,List<Test>> map){
    return map;
}
// value 属性   就是类名.变量  前提是这个变量 类能访问得到
@Component("ENC")
public class SPEL {
    //1 系统用户
    public static final String SYS_USER = "@ENC.SYSTEM_USER";
    public static final String[] SYSTEM_USER = {"email","password"};
    
//xxx实体类
private String username;
@Badger  //默认 跟随上级 @Decrypt所指得加密算法
private String password;
@Badger(cipher = CipherMode.AES)   //这个字段用AES加密算法
private String email;
@Badger(cipher = CipherMode.SM4)   //这个字段用SM4加密算法
private String phone;
@Badger(cipher = CipherMode.RSA)   //这个字段用RSA加密算法
private String address;
private String card;
```

### （二）、混合加密

#### 2.1  传输场景-混合加密

```java
//传输加密 模拟从service获取数据 @ENC.SYSTEM_USER 同等 Fields.SYS_USER 同等 @ENC.custom() 等同于使用@Badger该注解进行标识
// 更推荐 "@ENC.SYSTEM_USER" @Badger 这种写法 支持任意的请求方式 支持多属性混合使用 value fields 注解同时使用 不仅仅是支持web容器
//支持rpc远程调用加解密 @Badger 的值默认就是跟随@Encrypt的加密模式  你可以指定某一个字段用何种加密模式 可以混合使用
//dynamic = true 则你的cipher参数必须也是支持动态密钥的 仅支持SM4-RSA AES-RSA 两种混合模式
//dynamic = true 每一次的密钥都是变化的 你可以通过 HoneyBadgerEncrypt 实例获取变化后的密钥 推荐使用过滤器
@GetMapping("/aaa")
@Encrypt(value =SPEL.SYS_USER,cipher = CipherMode.SM4_RSA,scenario = Scenario.transmit)
public Map<String,List<Test>> getTest1(){
    return queryData();
}

//实体类
@Badger
private String username;
@Badger
private String password;
@Badger
private String email;
```

*仅支持SM4-RSA AES-RSA 两种混合模式*

RSA加密AES 或者是SM4的密钥

获取RSA加密后的AES 、SM4密钥

```java
/**
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 案例
 * @since : 1.0.0
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {
    public static String  AESKEY = "AES-RSA";
    public static String  SM4KEY = "SM4-RSA";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 拿到密钥 必设置到响应头 前端获取 通过RSA解密获取AES密钥 再通过AES解密器对密文解密
        response.getHeaders().set(AESKEY, HoneyBadgerEncrypt.getAesKeyRSACiphertext());  
        //拿到密钥 必设置到响应头 前端获取 通过RSA解密获取SM4密钥 再通过SM4解密器对密文解密
        response.getHeaders().set(SM4KEY,HoneyBadgerEncrypt.getSm4KeyRSACiphertext());
        return body;
    }
}

```

#### 2.2 前端传过来的密钥怎么接收？？？

*不是随机密钥 自然不用去配置！！！！*

做个判断即可

```java
/**
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 案例
 * @since : 1.0.0
 */
@Component
public class xxxxxxxxx extends OncePerRequestFilter {
    public static String  AESKEY = "AES-RSA";
    public static String  SM4KEY = "SM4-RSA";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String aesKey = request.getHeader(AESKEY);
        String sm4Key = request.getHeader(SM4KEY);
        if (StringUtils.hasText(aesKey)){    //判断一  四行代码 花不了多少时间 复制粘贴就行了 异常处理还没有做！！！
            HoneyBadgerEncrypt.setRSACiphertextForAESKey(aesKey);
        }
        if (StringUtils.hasText(sm4Key)){    //判断二
            HoneyBadgerEncrypt.setRSACiphertextForSM4Key(sm4Key);
        }
        filterChain.doFilter(request,response);
    }
}
```

*基本上复制粘贴就完工了！！！*

#### 2.2  混合解密之随机密钥

跟上面一样 区别在于 密钥会动态生成 dynamic 属性设置为true就行了

```java
@GetMapping("/aaa")
@Encrypt(value =SPEL.SYS_USER,cipher = CipherMode.SM4_RSA,scenario = Scenario.transmit,dynamic = true)
public Map<String,List<Test>> getTest1(){
    return queryData();
}
```



### 2.3  存储场景案例

传输场景你会了  那么存储场景就是so easy!!!   设置 scenario = Scenario.storage

   ```java
      
        xxxController //控制器
            //传输加密  @ENC.SYSTEM_USER 同等 Fields.SYS_USER 同等 @ENC.custom() 你用注解标注字段可以 怎么方便怎么用
        @GetMapping("/aaa")
        @Encrypt(value ="@ENC.SYSTEM_USER",cipher = CipherMode.RSA,scenario = Scenario.storage)
        public Map<String,List<Test>> getTest1(){
            return queryData();  //模拟的数据
        }
       //传输解密
       @PostMapping("/bbb")
       @Decrypt(value = SPEL.SYS_USER,cipher = CipherMode.RSA,scenario = Scenario.transmit)
       public Map<String,List<Test>> test1(@RequestBody Map<String,List<Test>> map){
           return map;
       }
      
      
      //类名ENC 配合spel表达式使用 更方便
      // 比如 value= "@ENC.SYSTEM_USER" 经过加密框架解析后的值: {"address","username","email","password"}
	 // 同上来讲就是方法调用 你可以写成 类名.常量 类名.方法
      @Component("ENC") 
      public class SPEL {
         //1 系统用户
         public static final String SYS_USER = "@ENC.SYSTEM_USER";
         //1 待加密字段 地址、用户名、邮箱、密码
         public static final String[] SYSTEM_USER = {"address","username","email","password"};
          
         //1
         public String[] custom(){
            return new String[]{"address","username","email"};
         }

      }

       //测试类 service层 模拟存储至数据库 从数据库取出数据 的加解密过程
       @Service("testService")
        public class TestService {

           //or value = "@ENCRYPT.ADMIN_USER", 同等 Fields.ADM_USER
           @Encrypt(value = SPEL.ADM_USER,cipher = CipherMode.AES,scenario = Scenario.storage)
           @Decrypt(value = "@ENC.ADMIN_USER",cipher = CipherMode.AES,scenario = Scenario.storage)
           public List<Test> testList(List<Test> testList, HttpServletRequest request, HttpServletResponse 					response){
          System.out.println(testList+"\n加密了");
          return testList;
       }
    }
   ```



## 参与贡献

写的也不好 您有好的想法 欢迎提出来 一起完善！！！

不懂的联系我 远程为你协助

## 性能

自己去测试  取决于你的设备
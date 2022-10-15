package cloud.longfa.encrypt.handler;

import cloud.longfa.encrypt.anotation.Decrypt;
import cloud.longfa.encrypt.anotation.Encrypt;
import cloud.longfa.encrypt.badger.HoneyBadgerEncrypt;
import cloud.longfa.encrypt.enums.CipherMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * The type Scenario handler.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description :
 * @since : 1.0.0
 */
public abstract class ScenarioHandler  implements ScenarioHolder {
    private static final Log echo = LogFactory.getLog(ScenarioHandler.class);
    /**
     * The constant honeyBadgerEncrypt.
     */
    public static HoneyBadgerEncrypt honeyBadgerEncrypt;

    /**
     * The constant executor.
     */
//线程池
    public static Executor executor=null;

    /**
     * The constant exclude.
     */
//排除含这些名字的类型
    protected static final String[] exclude ={"http","request","response","session","cookie","servlet"};

    /**
     * 加密
     *
     * @param target     源数据 待加密
     * @param cipherMode 算法
     * @return the string
     */
    protected String encryptionProcessor(Object target, @NonNull CipherMode cipherMode){
        switch (cipherMode){
            case AES:return honeyBadgerEncrypt.aesEncrypt(target.toString());
            case RSA:return honeyBadgerEncrypt.rsaEncrypt(target.toString());
            case SM4:return honeyBadgerEncrypt.sm4Encrypt(target.toString());
            default: return "No such algorithm Contact about:email---> longfa0130@gmail.com";
        }
    }

    /**
     * 解密 {@link CipherMode}
     *
     * @param target     密文
     * @param cipherMode 算法
     * @return the string
     */
    protected String decryptionProcessor(Object target, @NonNull CipherMode cipherMode){
        switch (cipherMode){
            case AES: return honeyBadgerEncrypt.aesDecrypt(target.toString());
            case RSA: return honeyBadgerEncrypt.rsaDecrypt(target.toString());
            case SM4: return honeyBadgerEncrypt.sm4Decrypt(target.toString());
            default: return "No such algorithm Contact about:email---> longfa0130@gmail.com";
        }
    }


    /**
     * 存储加密
     * @param args 参数
     * @param signature 方法源信息
     * @param encrypt 加密实例 {@link Encrypt}
     */
    public void storageEncryptProcessor(Object[] args, MethodSignature signature, Encrypt encrypt) throws Throwable {
        CipherMode cipherMode = encrypt.cipher(); //加密模式
        //获取要加密的字段名 默认为data
        String[] fields = encrypt.fields();
        List<String> fieldNames = Arrays.stream(fields).map(String::toLowerCase).collect(Collectors.toList());
        //获取方法上的参数
        String[] parameterNames = signature.getParameterNames();
        // 如方法参数需要加密
        Object[] clone = args.clone();
        //只处理 参数类型为list set 实体类 map 也就是只处理集合跟entity 其余不处理
        for (String fieldName : fieldNames) {
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equalsIgnoreCase(fieldName) && Objects.nonNull(args[i]) && StringUtils.hasText(args[i].toString())) {  //不考虑大小写
                    args[i] = encryptionProcessor(args[i],cipherMode);
                }
            }
        }
        //过滤部分参数
        if (!Arrays.equals(clone, args)){  //实例被操作 直接返回 比较内容
            return;
        }
        for (Object arg : args) {
            for (String ex : exclude)
            {
                if (arg.getClass().getSimpleName().toLowerCase().contains(ex)){
                    break;
                }
            }
            Field[] declaredFields = arg.getClass().getDeclaredFields(); //所有字段
            handlerNestedEncrypt(arg,declaredFields,fieldNames,cipherMode);
        }
    }

    /**
     * 参数内嵌 递归查询 找到必修改其属性
     *
     * @param arg            参数 只处理
     * @param declaredFields 所有字段
     * @param fieldNames     待加密字段
     * @param cipherMode     加密模式
     * @throws Throwable the throwable
     */
    @SuppressWarnings("all")
    protected void handlerNestedEncrypt(Object arg,Field[] declaredFields,List<String> fieldNames,CipherMode cipherMode) throws Throwable {
        if (!StringUtils.hasText(arg.toString())){  //结束条件
            return;
        }
        //首要判断 是否是list 或者map
        for (Field declaredField : declaredFields) {  //当前字段可能是基本数据类型 也有可能是实体类
            declaredField.setAccessible(true); //暴力访问
            for (String fieldName : fieldNames)
            {
                if (declaredField.getName().equalsIgnoreCase(fieldName))
                {
                    Object encryptDate = declaredField.get(arg);
                    if (Objects.isNull(encryptDate))
                    {
                        continue;
                    }
                    declaredField.set(arg, encryptionProcessor(encryptDate,cipherMode));
                }
            }
        }
        //断定第一层可能是list 或者map set 其余类型不处理
        if (arg instanceof List) {
            List objects = (List) arg;
            for (Object object : objects) {
                Field[] declaredFields1 = object.getClass().getDeclaredFields();
                handlerNestedEncrypt(object, declaredFields1, fieldNames,cipherMode);
            }
        }else if (arg instanceof Map){
            Map map = (Map)arg;
            Set set = map.keySet();  //获取所有的key
            for (Object o : set) {
                Object obj = map.get(o);
                Field[] mapFields = obj.getClass().getDeclaredFields();
                handlerNestedEncrypt(obj,mapFields,fieldNames,cipherMode);
            }
        }else if (arg instanceof Set){
            Set set = (Set)arg;
            for (Object obj : set) {
                Field[] setFields = obj.getClass().getDeclaredFields();
                handlerNestedEncrypt(obj,setFields,fieldNames,cipherMode);
            }
        }
    }

    /**
     * 解密链路 存储
     * @param process 目标方法执行结果
     * @param signature 方法源信息
     * @param decrypt 解密注解源信息
     * @throws Throwable 异常
     */
    public void storageDecryptProcessor(Object process, MethodSignature signature, Decrypt decrypt) throws Throwable{
        Class<?> clazz = process.getClass();
        //判断有没有 该字段 未嵌套的情况
        Field[] declaredFields = clazz.getDeclaredFields(); //获取所有的字段 任何访问修饰符
        CipherMode cipherMode = decrypt.cipher();  //加密模式
        List<String> fieldNames = Arrays.stream(decrypt.fields()).map(String::toLowerCase).collect(Collectors.toList());
        handlerNestedDecrypt(process,declaredFields,fieldNames,cipherMode);
    }

    /**
     * 使用递归算法 反向获取属性 只争对 entity Collection 不满足业务需求 请继承AbstractProcessorHandler类
     *
     * @param process        实例
     * @param declaredFields 字段
     * @param fieldNames     the field names
     * @param cipherMode     the cipher mode
     * @throws IllegalAccessException 反射异常
     */
    @SuppressWarnings("all")
    protected void handlerNestedDecrypt(Object process, Field[] declaredFields,List<String> fieldNames,CipherMode cipherMode) throws IllegalAccessException {
        if (Objects.isNull(process) || !StringUtils.hasText(process.toString())){ //如果该实例未null 直接返回
            return;
        }
        //首要判断 是否是list 或者map
        for (Field declaredField : declaredFields) {  //当前字段可能是基本数据类型 也有可能是实体类
            declaredField.setAccessible(true); //暴力访问
            for (String fieldName : fieldNames) {
                if (declaredField.getName().equalsIgnoreCase(fieldName))
                {
                    Object encryptDate = declaredField.get(process);
                    if (Objects.isNull(encryptDate))
                    {
                        continue;
                    }
                    declaredField.set(process, decryptionProcessor(encryptDate.toString(),cipherMode));
                }
            }
        }
        //断定第一层可能是list 或者map
        if (process instanceof List) {
            List objects = (List) process;
            for (Object object : objects) {
                Field[] declaredFields1 = object.getClass().getDeclaredFields();
                handlerNestedDecrypt(object, declaredFields1,fieldNames,cipherMode);
            }
        }else if (process instanceof Map){
            Map map = (Map)process;
            Set set = map.keySet();  //获取所有的key
            for (Object o : set) {
                Object obj = map.get(o);
                Field[] mapFields = obj.getClass().getDeclaredFields();
                handlerNestedDecrypt(obj,mapFields,fieldNames,cipherMode);
            }
        }else if (process instanceof Set){
            Set set = (Set)process;
            for (Object obj : set) {
                Field[] setFields = obj.getClass().getDeclaredFields();
                handlerNestedDecrypt(obj,setFields,fieldNames,cipherMode);
            }
        }
    }


    /**
     * 传输加密
     * @param process  参数
     * @param signature 方法源信息
     * @param encrypt   加密实例
     * @throws Throwable 异常
     */
    public void transmitEncryptProcessor(Object process, MethodSignature signature, Encrypt encrypt) throws Throwable{
        if (Objects.isNull(process) ){
            return;
        }
        CipherMode cipherMode = encrypt.cipher();
        List<String> fieldNames = Arrays.stream(encrypt.fields()).map(String::toLowerCase).collect(Collectors.toList());
        Field[] declaredFields = process.getClass().getDeclaredFields();
        tHandlerNestedEncrypt(process,declaredFields,fieldNames,cipherMode);
    }


    /**
     * @param process 结果
     * @param declaredFields 字段[]
     * @param cipherMode 加密模式
     */
    @SuppressWarnings({"all"})
    private void tHandlerNestedEncrypt(Object process, Field[] declaredFields,List<String> fieldNames,CipherMode cipherMode) throws Throwable {
        if (Objects.isNull(process) || !StringUtils.hasText(process.toString())){
            return;
        }
        for (Field declaredField : declaredFields) {  //当前字段可能是基本数据类型 也有可能是实体类
            declaredField.setAccessible(true); //暴力访问
            for (String fieldName : fieldNames) {
                if (declaredField.getName().equalsIgnoreCase(fieldName))
                {
                    Object encryptDate = declaredField.get(process);
                    if (Objects.isNull(encryptDate))
                    {
                        continue;
                    }
                    declaredField.set(process,encryptionProcessor(encryptDate.toString(), cipherMode) );
                }
            }
        }
        //断定第一层可能是list 或者map 如果是嵌套 判断是否是map还是list 需要在service层解密
        if (process instanceof List) {
            List objects = (List) process;
            for (Object object : objects) {
                Field[] listFields = object.getClass().getDeclaredFields();
                tHandlerNestedEncrypt(object, listFields,fieldNames, cipherMode);
            }
        }else if (process instanceof Map){
            Map map = (Map)process;
            Set keys = map.keySet();  //获取所有的key
            for (Object key : keys) {
                Object obj = map.get(key);
                if(fieldNames.contains(key.toString())){
                    String enc = encryptionProcessor(obj,cipherMode);//加密
                    map.put(key,enc);
                }else {
                    Field[] mapFields = obj.getClass().getDeclaredFields();
                    tHandlerNestedEncrypt(obj, mapFields ,fieldNames, cipherMode);
                }
            }
        }else if (process instanceof Set){
            Set set = (Set)process;
            for (Object obj : set) {
                Field[] setFields = obj.getClass().getDeclaredFields();
                tHandlerNestedEncrypt(obj,setFields,fieldNames, cipherMode);
            }
        }
    }


    /**
     * 传输解密
     * @param args   参数
     * @param signature 方法源信息
     * @param decrypt   解密注解源信息
     * @throws Throwable
     */
    @SuppressWarnings({"all"})
    public void transmitDecryptProcessor(Object[] args, MethodSignature signature, Decrypt decrypt) throws Throwable{
        //获取方法上的参数
        String[] parameterNames = signature.getParameterNames();
        //只处理 参数类型为list set 实体类 map 也就是只处理集合跟entity 其余不处理
        // 处理param
        for (String fieldName : decrypt.fields()) {
            for (int i = 0; i < args.length; i++) {
                if (parameterNames[i].equalsIgnoreCase(fieldName) && Objects.nonNull(args[i]) && StringUtils.hasText(args[i].toString())) {  //不考虑大小写
                    args[i] = decryptionProcessor(args[i], decrypt.cipher());
                }
            }
        }
        //处理注解
        String[] fields = decrypt.fields(); //加密的字段
        CipherMode cipher = decrypt.cipher();//加密算法
        List<String> fieldList = Arrays.stream(fields).map(String::toLowerCase).collect(Collectors.toList()); //字段名小写
        //处理body
        for (Object arg : args){
            for (String ex : exclude){
                if (arg.getClass().getName().toLowerCase().contains(ex))
                {
                    break;
                }
            }
            Field[] declaredFields = arg.getClass().getDeclaredFields(); //所有字段
            tHandlerNestedDecrypt(arg,declaredFields,fieldList,cipher);
        }
    }


    @SuppressWarnings({"all"})
    private void tHandlerNestedDecrypt(Object arg, Field[] declaredFields, List<String> fieldList,CipherMode cipherMode) throws IllegalAccessException, UnsupportedEncodingException {
        if (Objects.isNull(arg) || !StringUtils.hasText(arg.toString())){
            return;
        }
        //首要判断 是否是list 或者map
        for (Field declaredField : declaredFields)
        {  //当前字段可能是基本数据类型 也有可能是实体类
            declaredField.setAccessible(true); //暴力访问
            for (String fieldName : fieldList)
            {
                if (declaredField.getName().equalsIgnoreCase(fieldName))
                {
                    Object decryptDate = declaredField.get(arg);
                    if (Objects.isNull(decryptDate))
                    {
                        continue;
                    }
                    declaredField.set(arg,decryptionProcessor(decryptDate, cipherMode));
                }
            }
        }
        //断定第一层可能是list 或者map set 其余类型不处理
        if (arg instanceof List) {
            List objects = (List)arg;
            for (Object object : objects)
            {  //object
                Field[] listFields = object.getClass().getDeclaredFields();
                tHandlerNestedDecrypt(object, listFields, fieldList,cipherMode);
            }
        }else if (arg instanceof Map){
            Map map = (Map)arg;
            Set keySet = map.keySet();  //获取所有的key
            for (Object key : keySet)
            {
                Object obj = map.get(key);
                Field[] mapFields = obj.getClass().getDeclaredFields();
                tHandlerNestedDecrypt(obj, mapFields,  fieldList,cipherMode);
            }
        }else if (arg instanceof Set)
        {
            Set set = (Set)arg;
            for (Object obj : set) {
                Field[] setFields = obj.getClass().getDeclaredFields();
                tHandlerNestedDecrypt(obj,setFields, fieldList,cipherMode);
            }
        }
    }
}

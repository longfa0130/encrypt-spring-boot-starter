前端加解密 js文件链接：https://static.longfa.cloud/files/HoneyBadgerEncrypt.js 后期完善再上传到npm
支持前端RSA AES SM4 加解密



_前端加解密-未上传到npm_

基本配置  *** 注意看注释  需要引入其他的加解密包

config.js

```javascript

import {ENCContext} from "@/handler/EncryptHandler";

const AESKEY = "c4a98b23a8d94035bc9e0896b620b6a7"; //16位 32 64
const AESIV = "2404308462934f9f"; //16


const PUBLIC_KEY = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9OODN4s0RF' +
    'U8j9lU08aeEwEML/r8hGZxjIZb00epd97FpGOMXBsYgYP61NbXaSH7F3eit2SHJM' +
    '7KeCLzH6hqP5bXAQ5sMFwdgDA4FyQne4QGNn7FgE4GnJ/xPQzjv/EX7IPUTzVwxQ' +
    'QhLoZOmwxx19nog9A5SnYWgc9MSceoXSwIDAQAB'


const PRIVATE_KEY = 'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL044M3izREVTyP2\n' +
    'VTTxp4TAQwv+vyEZnGMhlvTR6l33sWkY4xcGxiBg/rU1tdpIfsXd6K3ZIckzsp4I\n' +
    'vMfqGo/ltcBDmwwXB2AMDgXJCd7hAY2fsWATgacn/E9DOO/8Rfsg9RPNXDFBCEuh\n' +
    'k6bDHHX2eiD0DlKdhaBz0xJx6hdLAgMBAAECgYBwYLlSTWJQxQENcJzGJsbAlpa7\n' +
    'Jv5IIDe6wBYxLAIu8I4Sxs8Hu+QrEq/y2wU3sFpqFRK9PPlNz0BDjxje+e/vgr8w\n' +
    '3C2OHBbmL0xaa4sjyeOFshOUj+W0utQPqPtj4HOo8xxcI8uC5RSdSc9bm1mDhM5X\n' +
    'wjm4LhkPauJHnK2C4QJBAPhJZfQ3TBjS7dBtXwKi7lSQZARhIQUeWQsuDnj4uI8N\n' +
    'yizCnuC0O/zaTZNQRPdE3u20fbio6JLfL6g5mwnS6EMCQQDDGb65ASet4HPttba0\n' +
    'r5VQtkC1arIipOOwYYce0HaYfNyaIfdFgQQj43g7Z76zsj0U42OxJUiGbQz3UF0Q\n' +
    'ochZAkAApdhhQb+uFObP82kbDafHirToGgTHCaQ71jvtbuC0ZtnPMhbOCUpax4sK\n' +
    'RlxXtNNMFXt6ft5Ue8aZDsVryiQXAkBfKUmLt3KBLv+crItnalUsPxkk49vrZf3g\n' +
    '5FEhyJBwJEb9rx+K+TjpwGO+pgWwZHnCnH7dws+zMbGnV71tBVxRAkEA70HB3Vhp\n' +
    'V1xXQaICPCB+IFCsYm1NgcKpTEJN/lS3BHEq8p5KR5c8NtnX+Gl14h5Y9qbfHRBe\n' +
    'oTHTW4kBD9DJLw=='

const SM4KEY = 'c4a98b23a8d94035'
const SM4IV = '2404308462934f9f'

let context = new ENCContext()
context.initAes(AESKEY,AESIV)
context.initSm4(SM4KEY,SM4IV)
context.initRSA(PUBLIC_KEY,PRIVATE_KEY)

export default context

```

### 加解密处理
```javascript
/**
 * map key:fieldName value:cipherMode
 * @param data
 * @param fieldAndCipherMode
 */
import {RsaHandler,AesHandler,SM4Handler} from '@/handler/HoneyBadgerEncrypt'
import {uuid} from "vue-uuid";

let rsaHandler
let aesHandler
let sm4Handler

class ENC {
    static AESKEY
    static AESIV
    static SM4Key
    static SM4IV
    static PUBLICKEY
    static PRIVATEKEY
}

/**
 * 常量 加密模式
 */
export class CipherMode {
    static RSA = 'RSA'
    static AES = 'AES'
    static SM4 = 'SM4'
    static AES_RSA = 'AES_RSA'
    static SM4_RSA = 'SM4_RSA'
}
/**
 * 初始化加密上下文环境
 */
export class ENCContext{

    /**
     * 初始化aes加密
     * @param aesKey {String}
     * @param aesIv {String}
     */
    initAes = (aesKey,aesIv) => {
        ENC.AESKEY = aesKey
        ENC.AESIV = aesIv
        aesHandler = new AesHandler(aesKey,aesIv)
    }
    /**
     * 初始化sm4加密
     * @param sm4Key {String}
     * @param sm4Iv {String}
     */
    initSm4 = (sm4Key,sm4Iv) => {
        ENC.SM4Key = sm4Key
        ENC.SM4IV = sm4Iv
        sm4Handler = new SM4Handler(sm4Key,sm4Iv)
    }
    /**
     * 初始化RSA加密
     * @param publicKey {String}
     * @param privateKey {String}
     */
    initRSA = (publicKey,privateKey) => {
        ENC.PUBLICKEY = publicKey
        ENC.PRIVATEKEY = privateKey
        rsaHandler = new RsaHandler(publicKey,privateKey)
    }

}
/**
 * 解密配置
 */
export class KeyConfig{
    aesKey  // 经过rsa加密的aes密钥
    sm4Key

    getAesKey = () => {
        return this.aesKey;
    }

    initAesKey = () => {
        return uuid.v5.URL.replaceAll('-','')
    }

    setAesKeyForRSAText = (key) => {
        this.aesKey = key
    }

    getSm4Key = () => {
        return this.sm4Key;
    }
    //初始化
    initSm4Key = () => {
        return uuid.v5.URL.replaceAll('-','').substring(0,16)
    }

    setSm4KeyForRSAText = (key) => {
        this.sm4Key =key
    }

}

/**
 * 获取待加密字段 以及对应的加密模式
 * @param data
 * @param fieldAndCipherMode {string}
 * @param config {KeyConfig}
 */
export function decryptHandler(data,fieldAndCipherMode,config){
    if (fieldAndCipherMode === undefined){
        console.error('没有获取字段对应加密模式，请配置')
        return
    }
    let split = fieldAndCipherMode.split('-'); //数组 fieldName:cipherMode
    const fieldMap = new Map()
    for (let i = 0; i < split.length; i++) {
       let keyValue = split[i].split(':')
       let fieldName = keyValue[0]
       let cipherMode = keyValue[1]
       fieldMap.set(fieldName,cipherMode)
    }
    handlerDecrypt(data,fieldMap,config)
}

/**
 * 处理结果
 * @param data 目标数据
 * @param fieldMap {Map}
 * @param config {KeyConfig}
 */
function handlerDecrypt(data,fieldMap,config) {
    if (data === null || data === undefined){
        return
    }
    let iterableIterator = fieldMap.keys();
    for (let i = 0; i < fieldMap.size; i++) {
        let fieldName = iterableIterator.next().value;
        if (Reflect.has(data, fieldName)) { //有该属性 则解密
            let decryptContent = Reflect.get(data,fieldName);
            decryptSelector(data,fieldName,decryptContent,fieldMap.get(fieldName),config)
        }
    }
    if(data instanceof Array){
        for (let i = 0; i < data.length; i++) {
           handlerDecrypt( data[i],fieldMap,config)
        }
    }else if (data instanceof Map){
        for (let i = 0; i < data.length; i++) {
            let valueMap = data.values().next().value;
            handlerDecrypt(valueMap,fieldMap,config)
        }
    }else if (data instanceof Set){
        for (let datum of data) {
            let valueSet = data.values().next().value;
            handlerDecrypt(valueSet,fieldMap,config)
        }
    }
}

/**
 * 处理参数
 * @param data 目标数据
 * @param fieldMap {Map} key是字段名 value是算法
 * @param config {KeyConfig} 配置密钥 有两个属性 aesKey sm4Key
 */
export function encryptHandler(data,fieldMap,config) {
    if (data === undefined || data ===null){
        return
    }
    if (!fieldMap instanceof Map){
        console.error('fieldMap 必须是Map类型')
        return;
    }
    //断言 这是一个实体类
    if (fieldMap instanceof Map){
        for (let i = 0; i < fieldMap.length; i++) {
            let fieldName = fieldMap.keys().next().value //字段名
            if (data[fieldName] !==null || data[fieldName]!== undefined){
                let newVar = Reflect.get(data,fieldName); //获取未加密的值
                encryptSelector(data,fieldName,newVar,fieldMap.get(fieldName),config); //加密后的数据
            }
        }//那么可能是嵌套
        handlerEncrypt(data,fieldMap,config)
    }
}


function handlerEncrypt(data,fieldMap,config){
    if (data === null || data === undefined){
        return
    }
    let iterableIterator = fieldMap.keys();
    for (let i = 0; i < fieldMap.size; i++) {
        let fieldName = iterableIterator.next().value;
        let value = data[fieldName];  //如果有值 则进行加密处理
        if (value !== null){
            let cipher = fieldMap.get(fieldName);
            encryptSelector(data,fieldName,value,cipher,config)
        }
    }
    if(data instanceof Array){
        for (let i = 0; i < data.length; i++) {
            handlerEncrypt( data[i],fieldMap,config)
        }
    }else if (data instanceof Map){
        for (let i = 0; i < data.length; i++) {
            let valueMap = data.values().next().value;
            handlerEncrypt(valueMap,fieldMap,config)
        }
    }else if (data instanceof Set){
        for (let datum of data) {
            let valueSet = data.values().next().value;
            handlerEncrypt(valueSet,fieldMap,config)
        }
    }
}

/**
 *
 * @param data
 * @param fieldName
 * @param text
 * @param cipher
 * @param config
 */
const encMap = new Map()
function encryptSelector(data,fieldName,text,cipher,config) {
    switch (cipher) {
        case CipherMode.AES:
            let aesDecryptContent = aesHandler.encrypt(text);
            Reflect.set(data,fieldName,aesDecryptContent)
            break
        case CipherMode.SM4:
            let sm4DecryptContent = sm4Handler.encrypt(text);
            Reflect.set(data,fieldName,sm4DecryptContent)
            break
        case CipherMode.RSA:
            let rsaDecryptContent = rsaHandler.encrypt(text);
            Reflect.set(data,fieldName,rsaDecryptContent)
            break
        case CipherMode.AES_RSA:
            if (!config instanceof KeyConfig){
                console.error('config 必须是 KeyConfig 类型')
                return
            }
            let aesHandlerTem
            if (encMap.get(CipherMode.AES_RSA) === undefined || encMap.get(CipherMode.AES_RSA) === null){
                const initAesKey = uuid.v5.URL.replaceAll('-','')
                let encrypt = rsaHandler.encrypt(initAesKey);
                Reflect.set(config,'aesKey',encrypt) //加密自定义密钥 加密后的密钥
                aesHandlerTem = new AesHandler(initAesKey,ENC.AESIV)  //获取最新的密钥 后进行初始化aes加解密实例
                encMap.set(CipherMode.AES_RSA,aesHandlerTem)
            }else {
                aesHandlerTem = encMap.get(CipherMode.AES_RSA)
            }
            let aesRsaDecryptContent = aesHandlerTem.encrypt(text);  //返回解密后的内容
            Reflect.set(data,fieldName,aesRsaDecryptContent)
            break
        case CipherMode.SM4_RSA:
            if (!config instanceof KeyConfig){
                console.error('config 必须是 KeyConfig 类型')
                return
            }
            let sm4HandlerTem
            if (encMap.get(CipherMode.SM4_RSA) === undefined || encMap.get(CipherMode.SM4_RSA) === null){
                const initSm4Key = uuid.v5.URL.replaceAll('-','').substring(0,16)
                Reflect.set(config,'sm4Key',rsaHandler.encrypt(initSm4Key))
                sm4HandlerTem = new SM4Handler(initSm4Key,ENC.SM4IV)
                encMap.set(CipherMode.SM4_RSA,sm4HandlerTem)
            }else {
                sm4HandlerTem = encMap.get(CipherMode.SM4_RSA)
            }
            let sm4RsaDecryptContent = sm4HandlerTem.encrypt(text)
            Reflect.set(data,fieldName,sm4RsaDecryptContent)
            break
        default:
            console.error('不支持该算法')
    }
}

/**
 * @param data
 * @param fieldName
 * @param text 密文
 * @param cipher 加密模式
 * @param config 配置
 * @returns {*}
 */
function decryptSelector(data,fieldName,text,cipher,config) {
    switch (cipher) {
        case CipherMode.AES:
            let aesDecryptContent = aesHandler.decrypt(text);
            Reflect.set(data,fieldName,aesDecryptContent)
            break
        case CipherMode.SM4:
            let sm4DecryptContent = sm4Handler.decrypt(text);
            Reflect.set(data,fieldName,sm4DecryptContent)
            break
        case CipherMode.RSA:
            let rsaDecryptContent = rsaHandler.decrypt(text);
            Reflect.set(data,fieldName,rsaDecryptContent)
            break
        case CipherMode.AES_RSA:
            if (config.aesKey === null || config.aesKey=== ''){
                console.error('请检查 未获取密钥')
                return
            }
            let aesKey = rsaHandler.decrypt(config.aesKey) //解密
            const aesHandlerTem = new AesHandler(aesKey,ENC.AESIV)  //获取最新的密钥 后进行初始化aes加解密实例
            let aesRsaDecryptContent = aesHandlerTem.decrypt(text);  //返回解密后的内容
            Reflect.set(data,fieldName,aesRsaDecryptContent)
            break
        case CipherMode.SM4_RSA:
            if (config.aesKey === null || config.aesKey=== ''){
                console.error('请检查 未获取密钥')
                return
            }
            let sm4Key = rsaHandler.decrypt(config.sm4Key) //解密
            const sm4HandlerTem = new SM4Handler(sm4Key,ENC.SM4IV)
            let sm4RsaDecryptContent = sm4HandlerTem.decrypt(text)
            Reflect.set(data,fieldName,sm4RsaDecryptContent)
            break
        default:
            console.error('不支持该算法')
    }
}


```

### 加解密实现

```javascript
import JSEncrypt from 'jsencrypt/bin/jsencrypt'
import {b64tohex, hex2b64} from "jsencrypt/lib/lib/jsbn/base64";

//以下代码测试过 结合后端 实现加解密 没什么问题 引入外部三个包
//安装gm-crypt 出现问题的时候 如果是webpack xxx 控制台执行下面这行
//npm install node-polyfill-webpack-plugin

/**
 * @email longfa0130@163.com
 * @description aes 加解密器
 */
//引入 crypto-js 该包 解决aes加解密
//npm install crypto-js
//https://github.com/brix/crypto-js
import CryptoJS from "crypto-js";
//npm install jsencrypt
//https://github.com/travist/jsencrypt
const jsEncrypt = new JSEncrypt()

/**
 * @description sm4 加解密器
 * @type {SM4}
 */
//npm install gm-crypt
//https://github.com/pecliu/gm-crypt
const SM4 = require("gm-crypt").sm4;

/**
 * @email longfa0130@163.com
 * @description rsa 加解密器
 */
export class RsaHandler {
    publicKey
    privateKey
    /**
     * 初始化 rsa处理实例
     * @param publicKey
     * @param privateKey
     */

    constructor(publicKey,privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    setPublicKey = publicKey => {
        this.publicKey = publicKey
    }

    setPrivateKey  = privateKey => {
        this.privateKey = privateKey;
    }
    /**
     * @description rsa加密
     * @author longfa
     * @param content
     * @returns {string}
     */
     encrypt = content => {
        if (content === null || content === ''){
            console.error('待加密数据是空的')
            return content
        }
         jsEncrypt.setPublicKey(this.publicKey)
        return b64tohex(jsEncrypt.encrypt(content))
    }

    /**
     * @description rsa解密
     * @author longfa
     * @param hexStr
     * @returns {*}
     */
     decrypt = hexStr => {
        if (hexStr === null || hexStr === ''){
            console.error('待解密数据是空的')
            return hexStr
        }
         jsEncrypt.setPrivateKey(this.privateKey)
        return jsEncrypt.decrypt(hex2b64(hexStr))
    }
}

export class AesHandler {
    aes_key
    aes_iv

    /**
     * 初始化 aes处理实例
     * @param aesKey
     * @param aesIv
     */
    constructor(aesKey , aesIv) {
        this.aes_key = CryptoJS.enc.Utf8.parse(aesKey)
        this.aes_iv = CryptoJS.enc.Utf8.parse(aesIv)
    }

    setKey = aesKey => {
        this.aes_key = CryptoJS.enc.Utf8.parse(aesKey)
    }
    setIv = aesIv => {
        this.aes_iv = CryptoJS.enc.Utf8.parse(aesIv)
    }
    /**
     * aes加密
     * @param content
     * @returns {WordArray|*}
     */
    encrypt = content => {
        if (content === null || content === ''){
            console.error('待加密数据是空的');
        }
        let srcs = CryptoJS.enc.Utf8.parse(content);
        let encrypted = CryptoJS.AES.encrypt(srcs, this.aes_key, {
                iv: this.aes_iv,
                mode: CryptoJS.mode.CBC,
                padding: CryptoJS.pad.Pkcs7
        });
        // CryptoJS.enc.Utf16
        let base64Str = CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
        return b64tohex(base64Str);
    }
    /**
     *
     * @param hexStr
     * @returns {*}
     */
    decrypt = hexStr => {
        if (hexStr === null || hexStr === ''){
            console.error('待解密数据是空的');
            return hexStr
        }
        let encryptedHexStr = CryptoJS.enc.Hex.parse(hexStr)
        let base64Data = CryptoJS.enc.Base64.stringify(encryptedHexStr)
        let decrypt = CryptoJS.AES.decrypt(base64Data, this.aes_key, {
            iv: this.aes_iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        })
        return decrypt.toString(CryptoJS.enc.Utf8)
    }
}

/**
 * SM4过密加密
 * @type {SM4}
 */

export class SM4Handler {

    SM4Instance

    /**
     * 初始化 不提供set key iv方法
     * @param sm4Key
     * @param sm4Iv
     */
    constructor(sm4Key, sm4Iv) {
        //初始化sm4 加密器
        this.SM4Instance = new SM4({
            key: sm4Key,
            iv: sm4Iv,
            mode: 'cbc',
            cipherType: 'base64'
        })
    }

    /**
     * 后端返回的是 16进制数据
     * @description  加密function
     * @param content 待加密内容
     */
    encrypt = content => {
        if (content === null || content === ''){
            console.error('待加密数据是空的');
            return content
        }
        let encryptBase64Str = this.SM4Instance.encrypt(content);
        return b64tohex(encryptBase64Str)
    }

    decrypt = hexStr => {
        if (hexStr === null || hexStr === ''){
            console.error('待解密数据是空的');
            return hexStr
        }
        return this.SM4Instance.decrypt(hex2b64(hexStr))
    }
}

```
package cloud.longfa.encrypt.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * The type Encrypt utils.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 工具类
 * @since : 1.0.0
 */
public class EncryptUtils {
    /**
     * The constant SERVER_KEY.
     */
    public static final byte[] SERVER_KEY = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
    /**
     * The constant SERVER_IV.
     */
    public static final byte[] SERVER_IV = Arrays.copyOfRange(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8),0,16);
    /**
     * The constant sm4.
     */
    public static final SymmetricCrypto sm4 = new SymmetricCrypto("SM4");
    /**
     * The constant rsa.
     */
    public static  RSA rsa = new RSA(AsymmetricAlgorithm.RSA.toString());
    /**
     * The constant PRIVATE_KEY.
     */
    public static String PRIVATE_KEY; //私钥
    /**
     * The constant PUBLIC_KEY.
     */
    public static String PUBLIC_KEY;  //公钥

    /**
     * The constant digester.
     */
    public static final Digester digester = DigestUtil.digester("SM3");
    static {
        PRIVATE_KEY = rsa.getPrivateKeyBase64();
        PUBLIC_KEY = rsa.getPublicKeyBase64();
        rsa = new RSA(AsymmetricAlgorithm.RSA.toString(),PRIVATE_KEY,PUBLIC_KEY);
        System.out.println(("私钥:"+PRIVATE_KEY+"\n"+"公钥:"+PUBLIC_KEY));
    }

    /**
     * aes加密
     *
     * @param content 文本内容跟
     * @return 加密字符串 16进制
     */
    public static String aesEncrypt(String content){
        //构建
        AES aes = new AES(Mode.CTS, Padding.PKCS5Padding,SERVER_KEY,SERVER_IV);
        //
        if (content.isEmpty()){
            throw new RuntimeException("加密内容不能为空");
        }
        byte[] encrypt = aes.encrypt(content);
        //解密
        return aes.encryptHex(encrypt);
    }

    /**
     * aes解密
     *
     * @param encrypt 密文
     * @param key     密钥
     * @param iv      偏移量
     * @return 明文 string
     */
    public static String aesDecrypt(String encrypt,byte[] key,byte[] iv){
        //构建
        if (key.length == 0){
            throw new RuntimeException("没有密钥");
        }
        if (iv.length == 0){
            throw new RuntimeException("没有偏移量");
        }
        AES aes = new AES(Mode.CTS, Padding.PKCS5Padding,key,iv);
        //解密
        byte[] decrypt = aes.decrypt(encrypt);
        return aes.decryptStr(decrypt);
    }

    /**
     * SM4 国密
     *
     * @param context 明文
     * @return 密文 string
     */
    public static String sm4Encrypt(String context){
        if (context.isEmpty()){
            throw new RuntimeException("文本内容不能为空");
        }
        return sm4.encryptHex(context);
    }

    /**
     * 过密算法 解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public static String sm4Decrypt(String encrypt){
        if (encrypt.isEmpty()){
            throw new RuntimeException("密文不能为空");
        }
        return sm4.decryptStr(encrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * RSA非对称加密算法
     *
     * @param content 明文
     * @return 密文 string
     */
    public static String rsaEncrypt(String content){
        return rsa.encryptHex(content,CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
    }

    /**
     * RSA非对称加密解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public static String rsaDecrypt(String encrypt){
        return rsa.decryptStr(encrypt,KeyType.PrivateKey,CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * Sm 3 digester object string.
     *
     * @param content the content
     * @return the string
     */
// 数据完整性验证
    public static String sm3DigesterObject(String content){
        return digester.digestHex(content);
    }

    /**
     * Sm 3 digester file string.
     *
     * @param file the file
     * @return the string
     */
//校验文件完整性
    public static String sm3DigesterFile(File file){
        return digester.digestHex(file);
    }
}

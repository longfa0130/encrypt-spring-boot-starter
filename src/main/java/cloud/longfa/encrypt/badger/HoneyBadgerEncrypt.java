package cloud.longfa.encrypt.badger;

import cloud.longfa.encrypt.config.EncryptProvider;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * The type Honey badger encrypt.
 *
 * @author : longfa 蜜獾 还可以用来当作吉祥物品
 * @email : longfa0130@gmail.com
 * @description : 加解密工具类
 * @since : 1.0.0
 */
public class HoneyBadgerEncrypt implements InitializingBean {
    private static final Log echo = LogFactory.getLog(HoneyBadgerEncrypt.class);
    /**
     * The Aes key.
     */
    public static  byte[] AES_KEY; //aes密钥
    /**
     * The Aes iv.
     */
    public static  byte[] AES_IV; //偏移量 长度16
    /**
     * The constant sm4.
     */
    public static final SymmetricCrypto sm4 = new SymmetricCrypto("SM4");
    /**
     * The constant rsa.
     */
    public static RSA rsa;

    /**
     * The constant aes.
     */
    public static AES aes;
    /**
     * The constant PRIVATE_KEY.
     */
//    public Supplier<RSA> rsaSupplier = ()-> new RSA(AsymmetricAlgorithm.RSA.toString());
    public static String PRIVATE_KEY; //私钥
    /**
     * The constant PUBLIC_KEY.
     */
    public static String PUBLIC_KEY;  //公钥
    /**
     * The constant digester.
     */
    public static final Digester digester = DigestUtil.digester("sm3");


    @Override
    public void afterPropertiesSet() {

        PRIVATE_KEY = EncryptProvider.privateKeyBase64();
        PUBLIC_KEY = EncryptProvider.publicKeyBase64();

        byte[] bytes = EncryptProvider.aesKey().getBytes(StandardCharsets.UTF_8);
        AES_KEY = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), bytes).getEncoded();

        String aesIv = EncryptProvider.aesIv().replace("-", "");
        AES_IV = Arrays.copyOfRange(aesIv.getBytes(StandardCharsets.UTF_8), 0, 16);

        aes = new AES(Mode.CTS, Padding.PKCS5Padding,AES_KEY,AES_IV);
        rsa = new RSA(AsymmetricAlgorithm.RSA.toString(),PRIVATE_KEY,PUBLIC_KEY);

        echo.info("私钥:\n"+PRIVATE_KEY);
        echo.info("公钥:\n"+PUBLIC_KEY);
        echo.info("aesKey:\n"+new String(AES_KEY,StandardCharsets.UTF_8));
        echo.info("aesIv:\n"+new String(AES_IV,StandardCharsets.UTF_8));
    }


    /**
     * aes加密
     *
     * @param content 文本内容跟
     * @return 加密字符串 16进制
     */
    public  String aesEncrypt(String content){
        byte[] encrypt = aes.encrypt(content);
        return aes.encryptHex(encrypt);
    }

    /**
     * aes解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public  String aesDecrypt(String encrypt){
        byte[] decrypt = aes.decrypt(encrypt);
        return aes.decryptStr(decrypt);
    }

    /**
     * SM4 国密
     *
     * @param context 明文
     * @return 密文 string
     */
    public  String sm4Encrypt(String context){
        return sm4.encryptHex(context);
    }

    /**
     * 过密算法 解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public  String sm4Decrypt(String encrypt){
        return sm4.decryptStr(encrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * RSA非对称加密算法
     *
     * @param content 明文
     * @return 密文 string
     */
    public String rsaEncrypt(String content){
       return rsa.encryptHex(content,CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
    }

    /**
     * RSA非对称加密解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public String rsaDecrypt(String encrypt){
        return rsa.decryptStr(encrypt,KeyType.PrivateKey,CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * Sm 3 digester object string.
     *
     * @param content the content
     * @return the string
     */
// 数据完整性验证
    public  String sm3DigesterObject(String content){
        return digester.digestHex(content);
    }

    /**
     * Sm 3 digester file string.
     *
     * @param file the file
     * @return the string
     */
//校验文件完整性
    public  String sm3DigesterFile(File file){
        return digester.digestHex(file);
    }


}

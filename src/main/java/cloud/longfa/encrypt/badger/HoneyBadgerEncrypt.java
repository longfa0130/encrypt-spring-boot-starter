package cloud.longfa.encrypt.badger;

import cloud.longfa.encrypt.config.EncryptProvider;
import cloud.longfa.encrypt.enums.CipherMode;
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
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    public static SM4 sm4;
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

    public static byte[] SM4_KEY;
    public static byte[] SM4_IV;



    @Override
    public void afterPropertiesSet() {

        PRIVATE_KEY = EncryptProvider.privateKeyBase64();
        PUBLIC_KEY = EncryptProvider.publicKeyBase64();

        byte[] bytes = EncryptProvider.aesKey().getBytes(StandardCharsets.UTF_8);
        AES_KEY = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), bytes).getEncoded();

        String aesIv = EncryptProvider.aesIv();
        AES_IV = aesIv.getBytes(StandardCharsets.UTF_8);

        aes = new AES(Mode.CTS, Padding.PKCS5Padding,AES_KEY,AES_IV);
        rsa = new RSA(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(),PRIVATE_KEY,PUBLIC_KEY);

        byte[] sm4KeyBytes = EncryptProvider.sm4Key().getBytes(StandardCharsets.UTF_8);
        SM4_IV = EncryptProvider.sm4Iv().getBytes(StandardCharsets.UTF_8);
        SM4_KEY = SecureUtil.generateKey(SM4.ALGORITHM_NAME, sm4KeyBytes).getEncoded();
        sm4 = new SM4(Mode.CTS,Padding.PKCS5Padding,SM4_KEY,SM4_IV);

        echo.info("RSA私钥:\n"+PRIVATE_KEY);
        echo.info("RSA公钥:\n"+PUBLIC_KEY);
        echo.info("aesKey:\n"+new String(AES_KEY,StandardCharsets.UTF_8));
        echo.info("aesIv:\n"+new String(AES_IV,StandardCharsets.UTF_8));
        echo.info("SM4密钥\n"+new String(SM4_KEY,StandardCharsets.UTF_8));
        echo.info("SM4密钥\n"+new String(SM4_IV,StandardCharsets.UTF_8));
    }


    /**
     * aes加密
     *
     * @param content 文本内容跟
     * @return 加密字符串 16进制
     */
    public  String aesEncrypt(String content){
        return aes.encryptHex(content, StandardCharsets.UTF_8);
    }

    /**
     * aes解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public  String aesDecrypt(String encrypt){
        return aes.decryptStr(encrypt,StandardCharsets.UTF_8);
    }

    /**
     * SM4 国密
     *
     * @param context 明文
     * @return 密文 string
     */
    public  String sm4Encrypt(String context){
        return sm4.encryptHex(context,StandardCharsets.UTF_8);
    }

    /**
     * 过密算法 解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public  String sm4Decrypt(String encrypt){
        return sm4.decryptStr(encrypt, StandardCharsets.UTF_8);
    }

    /**
     * RSA非对称加密算法
     *
     * @param content 明文
     * @return 密文 string
     */
    public String rsaEncrypt(String content){
       return rsa.encryptHex(content,StandardCharsets.UTF_8,KeyType.PublicKey);
    }

    /**
     * RSA非对称加密解密
     *
     * @param encrypt 密文
     * @return 明文 string
     */
    public String rsaDecrypt(String encrypt){
        return rsa.decryptStr(encrypt, KeyType.PrivateKey, CharsetUtil.CHARSET_UTF_8);
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



    //高级模式 之混合加密
    private static final ConcurrentHashMap<CipherMode,String> rsaCiphertexts= new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<CipherMode,SymmetricCrypto> symmetricCryptos = new ConcurrentHashMap<>();

    /**
     * 初始化混合加密实例 动态生成密钥
     * 仅支持SM4-RSA AES-RSA 两种混合模式
     */
    public void initHybridEncryption(CipherMode cipherMode,boolean dynamic){
        switch (cipherMode){
            case SM4_RSA:
                if (!dynamic){
                    symmetricCryptos.put(CipherMode.SM4_RSA,sm4);
                    break;
                }
                String sm4Key = UUID.randomUUID().toString().replace("-", "").substring(0,16); //sm4的密钥 可变值
                echo.info("偏移量不变 临时sm4密钥---->"+sm4Key);
                rsaCiphertexts.put(CipherMode.SM4_RSA,rsa.encryptHex(sm4Key,StandardCharsets.UTF_8, KeyType.PublicKey));  //加密sm4密钥 将密钥放入容器
                byte[] sm4KeyBytes = sm4Key.getBytes(StandardCharsets.UTF_8);
                byte[] sm4_Key = SecureUtil.generateKey(SM4.ALGORITHM_NAME, sm4KeyBytes).getEncoded();//重新获取
                SM4 sm4Temp = new SM4(Mode.CTS, Padding.PKCS5Padding, sm4_Key, SM4_IV);
                symmetricCryptos.put(CipherMode.SM4_RSA,sm4Temp);
                break;
            case AES_RSA:
                if (!dynamic){
                    symmetricCryptos.put(CipherMode.AES_RSA,aes);
                    break;
                }
                String aesKey = UUID.randomUUID().toString().replace("-", ""); //aes的密钥
                echo.info("偏移量不变 临时aes密钥---->"+aesKey);
                rsaCiphertexts.put(CipherMode.AES_RSA , rsa.encryptHex(aesKey,StandardCharsets.UTF_8, KeyType.PublicKey));//加密aes密钥
                byte[] bytes = aesKey.getBytes(StandardCharsets.UTF_8);
                byte[] aes_key  = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), bytes).getEncoded();
                AES aesTemp = new AES(Mode.CTS, Padding.PKCS5Padding, aes_key, AES_IV);
                symmetricCryptos.put(CipherMode.AES_RSA,aesTemp);
                break;
            default: throw new RuntimeException(cipherMode+"不存在该混合模式！！！");
        }
    }

    /**
     * 初始化解密实例
     * @param cipherMode 加密模式
     * @param dynamic 动态
     */
    public void initHybridDecryption(CipherMode cipherMode,boolean dynamic){
        switch (cipherMode){
            case SM4_RSA:
                if (!dynamic){
                   symmetricCryptos.put(CipherMode.SM4_RSA,sm4);
                   break;
                }
                String sm4Key = rsaCiphertexts.get(CipherMode.SM4_RSA);
                if (Objects.nonNull(sm4Key)){
                    byte[] sm4KeyBytes = sm4Key.getBytes(StandardCharsets.UTF_8);
                    byte[] sm4_Key = SecureUtil.generateKey(SM4.ALGORITHM_NAME, sm4KeyBytes).getEncoded();//重新获取
                    SM4 sm4Temp = new SM4(Mode.CTS, Padding.PKCS5Padding, sm4_Key, SM4_IV);
                    symmetricCryptos.put(CipherMode.SM4_RSA,sm4Temp);
                }
                break;
            case AES_RSA:
                if (!dynamic){
                    symmetricCryptos.put(CipherMode.AES_RSA,aes);
                    break;
                }
                String aesKey = rsaCiphertexts.get(CipherMode.AES_RSA);
                if (Objects.nonNull(aesKey)){
                    byte[] bytes = aesKey.getBytes(StandardCharsets.UTF_8);
                    byte[] aes_key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), bytes).getEncoded();//重新获取
                    AES aesTemp = new AES(Mode.CTS, Padding.PKCS5Padding, aes_key, AES_IV);
                    symmetricCryptos.put(CipherMode.AES_RSA,aesTemp);
                }
                break;
            default: throw new RuntimeException(cipherMode+"不存在该混合模式！！！");
        }
    }

    //向外部提供获取密钥的方法

    /**
     * @return rsa加密后的sm4密钥
     */
    public static String getSm4KeyRSACiphertext(){
        return rsaCiphertexts.get(CipherMode.SM4_RSA);
    }

    /**
     * @return rsa加密后的aes密钥
     */
    public static String getAesKeyRSACiphertext(){
        return rsaCiphertexts.get(CipherMode.AES_RSA);
    }

    /**
     * 创建SM4实例
     * 请配合拦截器获取过滤器使用
     * 私钥解密 公钥加密 支持动态密钥 也就是每一次加 密钥都不一样 这个模式 仅支持传输加密 最好不要用来做存储加密方式
     * 否则 可能导致数据无法复原
     * @param sm4RSACiphertext sm4key
     */
    public static void setRSACiphertextForSM4Key(String sm4RSACiphertext){
        if (!StringUtils.hasText(sm4RSACiphertext)){
            throw new RuntimeException("没有获取到密钥"+HoneyBadgerEncrypt.class.getSimpleName());
        }
        String sm4Key = rsa.decryptStr(sm4RSACiphertext,KeyType.PrivateKey,StandardCharsets.UTF_8);
        rsaCiphertexts.put(CipherMode.SM4_RSA,sm4Key);
    }


    /**
     * 初始化aes加密  创建AES实例 解密
     * @param aesKeyRSACiphertext aesKey
     */
    public static void setRSACiphertextForAESKey(String aesKeyRSACiphertext){
        if (!StringUtils.hasText(aesKeyRSACiphertext)){
            throw new RuntimeException("没有获取到密钥"+HoneyBadgerEncrypt.class.getSimpleName());
        }
        String aesKey = rsa.decryptStr(aesKeyRSACiphertext,KeyType.PrivateKey,StandardCharsets.UTF_8);  //解密
        rsaCiphertexts.put(CipherMode.AES_RSA,aesKey);
    }

    /**
     * sm4 rsa 混合加密模式 解密
     * @param encrypt 密文
     * @return String
     */
    public String sm4RsaDecrypt(String encrypt){
        SM4 sm4HybridEncryption = (SM4)symmetricCryptos.get(CipherMode.SM4_RSA);
        Assert.notNull(sm4HybridEncryption,"你还没有配置密钥 或许你的拦截器|过滤器没有生效"+"setRSACiphertextForSM4Key(String sm4RSACiphertext)");
        return sm4HybridEncryption.decryptStr(encrypt,StandardCharsets.UTF_8);
    }

    /**
     * sm4Rsa混合加密
     * @param content 明文
     * @return 密文
     */
    public String sm4RsaEncrypt(String content){
        SM4 sm4HybridEncryption = (SM4)symmetricCryptos.get(CipherMode.SM4_RSA);
        return sm4HybridEncryption.encryptHex(content);
    }

    /**
     * aesRsa混合加密
     * @param content 明文
     * @return 密文
     */
    public String aesRsaEncrypt(String content){
        AES aesHybridEncryption = (AES)symmetricCryptos.get(CipherMode.AES_RSA);
        return aesHybridEncryption.encryptHex(content);
    }

    /**
     * aes rsa 混合加密模式 解密
     * @param encrypt 密文
     * @return String
     */
    public String aesRsaDecrypt(String encrypt){
        AES aesHybridEncryption = (AES)symmetricCryptos.get(CipherMode.AES_RSA);
        Assert.notNull(aesHybridEncryption,"你还没有配置密钥 或许你的拦截器|过滤器没有生效"+"setRSACiphertextForAESKey(String aesKeyRSACiphertext)");
        return aesHybridEncryption.decryptStr(encrypt,StandardCharsets.UTF_8);
    }
}

package cloud.longfa.encrypt.config;

import cloud.longfa.encrypt.enums.CipherMode;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Encrypt provider.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 加密提供者
 * @since : 1.0.0
 */
public abstract class EncryptProvider {
    /**
     * The constant BADGER.
     */
    protected  static  final String BADGER = "badger.encrypt";
    /**
     * The constant encryptFactory.
     */
    public static final ConcurrentHashMap<CipherMode, EncryptProvider> encryptFactory = new ConcurrentHashMap<>();


    /**
     * Aes key string.
     *
     * @return the string
     */
    public static String aesKey(){
       AESConfiguration aesConfiguration = (AESConfiguration)encryptFactory.get(CipherMode.AES);
        return aesConfiguration.getAesKey();
    }


    /**
     * Aes iv string.
     *
     * @return the string
     */
    public static String aesIv(){
       AESConfiguration aesConfiguration = (AESConfiguration)encryptFactory.get(CipherMode.AES);
        return aesConfiguration.getAesIv();
    }


    /**
     * Private key base 64 string.
     *
     * @return the string
     */
    public static String privateKeyBase64(){
        RSAConfiguration rsaConfiguration = (RSAConfiguration) encryptFactory.get(CipherMode.RSA);
        return rsaConfiguration.getPrivateKeyBase64();
    }


    /**
     * Public key base 64 string.
     *
     * @return the string
     */
    public static String publicKeyBase64(){
        RSAConfiguration rsaConfiguration = (RSAConfiguration) encryptFactory.get(CipherMode.RSA);
        return rsaConfiguration.getPublicKeyBase64();
    }


}

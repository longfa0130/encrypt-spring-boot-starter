package cloud.longfa.encrypt.enums;

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

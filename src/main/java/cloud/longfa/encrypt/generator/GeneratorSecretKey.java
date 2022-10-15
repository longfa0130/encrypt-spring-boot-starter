package cloud.longfa.encrypt.generator;

import cloud.longfa.encrypt.enums.CipherMode;

/**
 * The interface Generator secret key.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 密钥生成统一接口
 * @since : 1.0.0
 */
public interface GeneratorSecretKey {
    /**
     * RSA生成base64编码字符串
     * AES生成密钥 key  偏移量iv 长度16
     *
     * @param cipherMode the cipher mode
     * @return 生产密钥的方法 {@link CipherMode} 代理类接口
     */
    Object generatorKey(CipherMode cipherMode);
}

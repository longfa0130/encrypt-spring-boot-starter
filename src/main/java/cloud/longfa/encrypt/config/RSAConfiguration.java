package cloud.longfa.encrypt.config;

import cloud.longfa.encrypt.enums.CipherMode;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * The type Rsa configuration.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : RSA配置文件 私钥 公钥
 * @since : 1.0.0
 */
@SuppressWarnings({"all"})
@ConfigurationProperties(prefix = EncryptProvider.BADGER)
public class RSAConfiguration extends EncryptProvider implements InitializingBean {
    private static final RSA rsa = new RSA(AsymmetricAlgorithm.RSA.toString());
    private static final  String PRIVATE_KEY64 = rsa.getPrivateKeyBase64();
    private static final  String   PUBLIC_KEY64= rsa.getPublicKeyBase64();
    // RSA私钥
    private String privateKeyBase64;
    //RSA公钥
    private String publicKeyBase64;

    /**
     * Sets private key base 64.
     *
     * @param privateKeyBase64 the private key base 64
     */
    public void setPrivateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
    }

    /**
     * Sets public key base 64.
     *
     * @param publicKeyBase64 the public key base 64
     */
    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    /**
     * Gets private key base 64.
     *
     * @return the private key base 64
     */
    public String getPrivateKeyBase64() {
        if (StringUtils.hasText(privateKeyBase64)) {
            return privateKeyBase64;
        }
        return PRIVATE_KEY64;
    }


    /**
     * Gets public key base 64.
     *
     * @return the public key base 64
     */
    public String getPublicKeyBase64() {
        if (StringUtils.hasText(publicKeyBase64)) {
            return publicKeyBase64;
        }
        return PUBLIC_KEY64;
    }

    @Override
    public void afterPropertiesSet() {
        EncryptProvider.encryptFactory.put(CipherMode.RSA,this);

    }
}

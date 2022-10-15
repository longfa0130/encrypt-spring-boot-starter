package cloud.longfa.encrypt.config;

import cloud.longfa.encrypt.enums.CipherMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Encrypt configuration.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description :
 * @since : 1.0.0
 */
@Configuration
public class EncryptConfiguration {
    /**
     * Aes configuration aes configuration.
     *
     * @return the aes configuration
     */
//配置文件
    @Bean
    @ConditionalOnMissingBean(AESConfiguration.class)
    public AESConfiguration aesConfiguration(){
        AESConfiguration aesConfiguration = new AESConfiguration();
        EncryptProvider.encryptFactory.put(CipherMode.AES,aesConfiguration);
        return aesConfiguration;
    }

    /**
     * Rsa configuration rsa configuration.
     *
     * @return the rsa configuration
     */
//RSA配置文件
    @Bean
    @ConditionalOnMissingBean(RSAConfiguration.class)
    public RSAConfiguration rsaConfiguration(){
        RSAConfiguration rsaConfiguration = new RSAConfiguration();
        EncryptProvider.encryptFactory.put(CipherMode.RSA,rsaConfiguration);
        return rsaConfiguration;
    }

}

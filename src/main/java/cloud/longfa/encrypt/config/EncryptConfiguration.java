package cloud.longfa.encrypt.config;

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
        return new AESConfiguration();
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
        return new RSAConfiguration();
    }

    /**
     * SM4配置文件加载至加密容器
     * @return the sm4 configuration
     */
    @Bean
    @ConditionalOnMissingBean(SM4Configuration.class)
    public SM4Configuration sm4Configuration(){
        return new SM4Configuration();
    }

}

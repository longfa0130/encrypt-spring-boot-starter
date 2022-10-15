package cloud.longfa.encrypt.anotation;

import cloud.longfa.encrypt.config.EncryptImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The interface Enable encrypt.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 开启加密 默认值true 为false 则关闭
 * @since : 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EncryptImportSelector.class)
public @interface EnableEncrypt {}

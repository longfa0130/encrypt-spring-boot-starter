package cloud.longfa.encrypt.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * The type Encrypt import selector.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description :
 * @since : 1.0.0
 */
public class EncryptImportSelector implements ImportSelector{

    @NonNull
    @Override
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{EncryptConfiguration.class.getName()};
    }
}

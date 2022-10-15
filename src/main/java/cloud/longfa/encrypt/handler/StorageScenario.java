package cloud.longfa.encrypt.handler;

import cloud.longfa.encrypt.anotation.Decrypt;
import cloud.longfa.encrypt.anotation.Encrypt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * The type Storage scenario.
 *
 * @author : longfa {@link <a href="http://www.longfa.cloud">...</a>}
 * @email : longfa0130@gmail.com
 * @description : StorageScenario 使用场景加密存储
 * @since : 1.0.0
 */
public class StorageScenario extends ScenarioHandler {
    private static final Log echo = LogFactory.getLog(StorageScenario.class);


    @Override
    public void storageEncryptProcessor(Object[] args, MethodSignature signature, Encrypt encrypt) throws Throwable {
        super.storageEncryptProcessor(args, signature, encrypt);
    }

    @Override
    public void storageDecryptProcessor(Object process, MethodSignature signature, Decrypt decrypt) throws Throwable {
         super.storageDecryptProcessor(process, signature, decrypt);
    }
}

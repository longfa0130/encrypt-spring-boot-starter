package cloud.longfa.encrypt.handler;

import cloud.longfa.encrypt.anotation.Decrypt;
import cloud.longfa.encrypt.anotation.Encrypt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * The type Transmit scenario.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : TransmitScenario 适用场景网络传输
 * @since : 1.0.0
 */
public class TransmitScenario extends ScenarioHandler {
    private static final Log echo = LogFactory.getLog(TransmitScenario.class);

    @Override
    public void transmitEncryptProcessor(Object process, MethodSignature signature, Encrypt encrypt) throws Throwable {
        super.transmitEncryptProcessor(process, signature, encrypt);

    }

    @Override
    public void transmitDecryptProcessor(Object[] args, MethodSignature signature, Decrypt decrypt) throws Throwable {
        super.transmitDecryptProcessor(args, signature, decrypt);
    }
}

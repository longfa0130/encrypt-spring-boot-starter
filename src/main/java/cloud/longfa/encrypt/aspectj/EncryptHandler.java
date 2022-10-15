package cloud.longfa.encrypt.aspectj;

import cloud.longfa.encrypt.anotation.Decrypt;
import cloud.longfa.encrypt.handler.ScenarioHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
/**
 * The type Encrypt handler.
 *
 * @author : created by LongFa
 * @version : 1.0.0-SNAPSHOT
 * @description : AES加密处理器
 * @date : Created in 2022-07-23 17:13
 */
@Order(1)
@Aspect
public class EncryptHandler{
    /**
     * The constant echo.
     */
    public static final Log echo = LogFactory.getLog(EncryptHandler.class);

    /**
     * 根据不同场景 选择性调用 传输加密:对执行结果 存储加密:对参数
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("@annotation(cloud.longfa.encrypt.anotation.Encrypt)")
    public Object encrypt(ProceedingJoinPoint joinPoint) throws Throwable {
        return ScenarioHolder.scenarioSchedule(joinPoint);
    }

    /**
     * 传输解密:对参数  存储解密:对执行结果
     *
     * @param joinPoint the join point
     * @param decrypt   the decrypt
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("@annotation(decrypt)")
    public Object decrypt(ProceedingJoinPoint joinPoint, Decrypt decrypt) throws Throwable {
        return ScenarioHolder.scenarioSchedule(joinPoint);
    }
}

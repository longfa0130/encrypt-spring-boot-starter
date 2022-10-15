package cloud.longfa.encrypt.handler;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * The type Scenario encrypt schedule.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description :  可以在此处重写你的调度策略
 * @since : 1.0.0
 */
public class ScenarioEncryptSchedule extends ScenarioSchedule {
    @Override
    public Object scenarioSchedule(ProceedingJoinPoint joinPoint) throws Throwable {
       return super.scenarioSchedule(joinPoint);
    }
}

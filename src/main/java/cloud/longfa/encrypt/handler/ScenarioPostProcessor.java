package cloud.longfa.encrypt.handler;

import cloud.longfa.encrypt.enums.Scenario;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The type Scenario post processor.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 场景调度器 后置处理器
 * @since : 1.0.0
 */
public class ScenarioPostProcessor implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StorageScenario storageScenario = applicationContext.getBean(StorageScenario.class);
        TransmitScenario transmitScenario = applicationContext.getBean(TransmitScenario.class);
        ScenarioHolder.abstractScenarios.put(Scenario.storage,storageScenario);
        ScenarioHolder.abstractScenarios.put(Scenario.transmit,transmitScenario);
    }
}

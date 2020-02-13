package activiti.activiti.config;

import activiti.activiti.listener.ActivitiEventListenerImpl;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujia on 2020/2/7
 */
@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private ActivitiEventListenerImpl activitiEventListener;

    @Override
    public void configure(SpringProcessEngineConfiguration configuration) {
//        Map<String, List<ActivitiEventListener>> activitiEventListenerMap = new HashMap<>();
//        // 设置全局监听器
//        activitiEventListenerMap.put(ActivitiEventType.ACTIVITY_STARTED.name(), Lists.newArrayList(activitiEventListener));
//        activitiEventListenerMap.put(ActivitiEventType.ACTIVITY_COMPLETED.name(), Lists.newArrayList(activitiEventListener));
//        activitiEventListenerMap.put(ActivitiEventType.TASK_CREATED.name(), Lists.newArrayList(activitiEventListener));
//        activitiEventListenerMap.put(ActivitiEventType.TASK_COMPLETED.name(), Lists.newArrayList(activitiEventListener));
//        configuration.setTypedEventListeners(activitiEventListenerMap);
    }
}

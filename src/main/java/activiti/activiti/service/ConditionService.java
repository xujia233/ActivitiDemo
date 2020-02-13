package activiti.activiti.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.springframework.stereotype.Service;

/**
 * Created by xujia on 2020/2/5
 */
@Service
@Slf4j
public class ConditionService {

    public boolean checkCondition(Execution execution) {
        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        // 此处可以做业务判断
        log.info("tacheId:{}", executionEntity.getActivityId());
        log.info("tacheName:{}", executionEntity.getCurrentFlowElement().getName());
        return true;
    }
}

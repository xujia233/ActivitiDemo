package activiti.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 执行监听器实现类
 * Created by xujia on 2020/2/6
 */
public class ExecutionListenerImpl implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String event = delegateExecution.getEventName();
        switch (event) {
            case "start" :
                System.out.println("start event");
                break;
            case "end" :
                System.out.println("end event");
                break;
            case "take" :
                System.out.println("take event");
                break;
        }
    }
}

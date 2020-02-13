package activiti.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器实现类
 * Created by xujia on 2020/2/6
 */
public class AssigneeListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String event = delegateTask.getEventName();
        switch (event) {
            case "create" :
                System.out.println("create event");
                break;
            case "assignment" :
                System.out.println("assignment event");
                break;
            case "complete" :
                System.out.println("complete event");
                break;
            case "delete" :
                System.out.println("delete event");
                break;
        }
    }
}

package activiti.activiti.demo;

import activiti.activiti.command.DeleteTaskCmd;
import activiti.activiti.command.SetFLowNodeAndGoCmd;
import activiti.activiti.entity.ChartElement;
import activiti.activiti.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个请假小流程
 * Created by xujia on 2019/8/26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class VocationDemo {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ManagementService managementService;

    @Test
    public void demoTest() {
        // 1、发布流程
        Deployment deployment = repositoryService.createDeployment().name("请假小流程").addClasspathResource("processes/vocationDemo.bpmn20.xml").deploy();

        // 2、启动一个流程实例，由于两个环节审批的人都是写死的test，所以这边在启动流程的时候未透传下一环节处理人
        Map<String, Object> variableMap = new HashMap<>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vocation", variableMap);

        // 3、查询所有任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();

        // 4、提交到总经理审批即完成任务，同样不需要传递变量
        Task task = tasks.get(0);
        taskService.complete(task.getId());
    }

    /**
     * 测试activiti的流程变量
     */
    @Test
    public void variableDemoTest() {
        // 发布流程
        Deployment deployment = repositoryService.createDeployment().name("请假小流程").addClasspathResource("processes/vocationDemo.bpmn20.xml").deploy();

        // 启动一个流程实例，同时设置流程变量，可以通过在启动或完成任务的时候传递变量，当然也可以单独设置变量
        Map<String, Object> variableMap = new HashMap<>();
        // 基本类型的变量在activiti中可直接存储
        variableMap.put("userId", "123");
        variableMap.put("leaveDays", 3);
        variableMap.put("leaveTime", new Date());
        // 对象以流的方式存储，包括javabean类型(必须实现序列化接口)
        variableMap.put("users", Lists.newArrayList("zhangsan"));
        variableMap.put("user", new User("李四"));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vocation", variableMap);

        // 查询所有任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();

        System.out.println("Task size:" + tasks.size());

        Task task = tasks.get(0);

        // 也可以为某个任务单独设置变量，单独设置变量在实际中其实不建议存储到activiti中，由业务方自行存储比较好
        taskService.setVariable(task.getId(), "当前任务处理人", task.getAssignee());

        // 获取变量
        System.out.println("直属经理任务处理人" + taskService.getVariable(task.getId(), "当前任务处理人"));
    }

    /**
     * 多出线测试
     */
    @Test
    public void multiBranchDemoTest() {
        // 1、发布流程
        Deployment deployment = repositoryService.createDeployment().name("多分支请假流程").addClasspathResource("processes/vocationDemo2.bpmn20.xml").deploy();

        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", "xujia");
        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vocation", variables);

        // 3、查询所有任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
//
//        Task task = tasks.get(0);
//        // 4、完成直属经理审批环节任务的同时设置流程变量，来告诉Activiti该走哪条线
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("nextBranch", "以内");
//        taskService.complete(task.getId(), variables);
    }

    /**
     * 排他网关测试
     */
    @Test
    public void exclusiveGatewayTest() {
        // 1、发布流程
        Deployment deployment = repositoryService.createDeployment().name("排他网关流程").addClasspathResource("processes/exclusiveGateway.bpmn20.xml").deploy();

        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusive");

        // 3、查询所有任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();

        Task task = tasks.get(0);
        // 4、完成费用报销审批环节任务的同时设置流程变量，来告诉Activiti该走哪条线
        Map<String, Object> variables = new HashMap<>();
        variables.put("money", 400);
        taskService.complete(task.getId(), variables);
    }

    /**
     * 排他网关测试2，流程线条件以service来替代
     */
    @Test
    public void exclusiveGateway2Test() {
        // 1、发布流程
        Deployment deployment = repositoryService.createDeployment().name("排他网关流程").addClasspathResource("processes/exclusiveGateway2.bpmn20.xml").deploy();

        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusive");

        // 3、查询所有任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();

        Task task = tasks.get(0);
        // 4、完成费用报销审批环节任务的同时设置流程变量，来告诉Activiti该走哪条线

        taskService.complete(task.getId());
    }

    /**
     * 并行网关测试
     */
    @Test
    public void parallelGateway2Test() {
        // 1、发布流程，当流程的key相同时则以版本来控制，每次启动流程实例时默认取最新版本
        Deployment deployment = repositoryService.createDeployment().name("并行网关流程").addClasspathResource("processes/parallelGateway.bpmn20.xml").deploy();

        System.out.println("部署id:" + deployment.getId());

        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("parallelGateway");
        System.out.println("实例id:" + processInstance.getProcessInstanceId());
        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());

        // 3、查询所有并行任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();

        Assert.assertEquals(3, tasks.size());
        System.out.println("任务数量：" + tasks.size());
    }

    /**
     * 监听器测试
     */
    @Test
    public void listenerTest() {
        // 1、发布流程，当流程的key相同时则以版本来控制，每次启动流程实例时默认取最新版本
        Deployment deployment = repositoryService.createDeployment().name("监听器测试流程").addClasspathResource("processes/listener.bpmn20.xml").deploy();

        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", "李四");
        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("listenerTest", variables);

    }

    /**
     * 将json数据转换成BpmnModel对象，然后部署
     * @throws Exception
     */
    @Test
    public void transformChartTest() throws Exception {
        // 假设这是你已经转换好后的整个流程图对象
        ChartElement chartElement = new ChartElement();
        // 这里借助jackson库来转换
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(chartElement));
        // 然后借助工具将jsonNod转换成BpmnModel
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        // 最后直接部署即可，注意流程id不能以数字开头
        repositoryService.createDeployment().addBpmnModel("sid-123.bpmn20.xml", bpmnModel).deploy();
    }

    @Test
    public void reassignTest() {
        // 1、发布流程，当流程的key相同时则以版本来控制，每次启动流程实例时默认取最新版本
        Deployment deployment = repositoryService.createDeployment().name("简单流程").addClasspathResource("processes/simpleModel.bpmn20.xml").deploy();

        System.out.println("部署id:" + deployment.getId());

        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleModel");
        System.out.println("实例id:" + processInstance.getProcessInstanceId());
        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());

        // 3、查询当前任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        Task task = tasks.get(0);

        System.out.println("当前任务处理人：" + task.getAssignee());
    }

    /**
     * 多实例会签测试，如果是依次会签，那么只会依次创建任务
     */
    @Test
    public void multiInstanceTest() {
        // 1、发布流程，当流程的key相同时则以版本来控制，每次启动流程实例时默认取最新版本
        Deployment deployment = repositoryService.createDeployment().name("多实例会签流程").addClasspathResource("processes/multiInstance.bpmn20.xml").deploy();

        Map<String, Object> variable = new HashMap<>();
        variable.put("userList", Lists.newArrayList("张三", "李四", "王五", "赵六"));
        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("multiInstance", variable);

        // 3、查询当前任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        System.out.println("当前任务数量：" + tasks.size());

        for (Task task : tasks) {
            System.out.println("任务名:" + task.getName() + ",任务处理人：" + task.getAssignee());
        }

        // 下面演示先完成一个任务，看流程是否会流转
//        taskService.complete(tasks.get(0).getId());
//
//        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
//        System.out.println("完成一个任务后，当前任务数量：" + tasks.size());
//        for (Task task1 : tasks) {
//            System.out.println("任务名:" + task1.getName() + ",任务处理人：" + task1.getAssignee());
//        }
    }

    /**
     * 自由流转测试
     */
    @Test
    public void rollbackTest() {
        // 获取当前任务
        Task currentTask = taskService.createTaskQuery().taskId("132502").singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
        // 获取流程定义
        Process process = bpmnModel.getMainProcess();
        // 获取目标节点定义
        FlowNode targetNode = (FlowNode) process.getFlowElement("sid-C24BA4F5-F744-4DD7-8D51-03C3698044D2");

        // 删除当前运行任务，同时返回执行id，该id在并发情况下也是唯一的
        String executionEntityId = managementService.executeCommand(new DeleteTaskCmd(currentTask.getId()));
        // 流程执行到来源节点
        managementService.executeCommand(new SetFLowNodeAndGoCmd(targetNode, executionEntityId));
    }

    /**
     * 嵌入式子流程测试，即子流程专属于当前主流程，其他流程不能引用该子流程
     */
    @Test
    public void embeddedSubProcess() {
        // 1、发布流程，当流程的key相同时则以版本来控制，每次启动流程实例时默认取最新版本
        Deployment deployment = repositoryService.createDeployment().name("嵌入式子流程").addClasspathResource("processes/embeddedSubProcess.bpmn20.xml").deploy();

        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("mainProcess");
        System.out.println("实例id:" + processInstance.getProcessInstanceId());
        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());

        // 3、查询当前任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        Task task = tasks.get(0);

        System.out.println("当前任务节点：" + task.getName() + ",当前任务处理人：" + task.getAssignee());
        taskService.complete(task.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        System.out.println("此时为子流程任务，节点为：" + tasks.get(0).getName());
    }

    /**
     * 调用子流程测试，由主流程引用外部已实现的流程
     */
    @Test
    public void callActivity() {
        // 1、发布流程，当流程的key相同时则以版本来控制，每次启动流程实例时默认取最新版本
        Deployment deployment = repositoryService.createDeployment().name("调用子流程").addClasspathResource("processes/callActivity.bpmn20.xml").deploy();

        // 2、启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("callActivity");
        System.out.println("实例id:" + processInstance.getProcessInstanceId());
        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());

        // 3、查询当前任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        Task task = tasks.get(0);

        System.out.println("当前任务节点：" + task.getName() + ",当前任务处理人：" + task.getAssignee());
        taskService.complete(task.getId());
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        System.out.println("此时为子流程任务，节点为：" + tasks.get(0).getName());
    }

    /**
     * 用于发布流程
     */
    @Test
    public void deployProcess() {
        repositoryService.createDeployment().name("子流程").addClasspathResource("processes/subProcess.bpmn20.xml").deploy();
    }

    @Test
    public void reassignTask() {
        //taskService.setAssignee("132502", "李四");
        // 如果采用claim方式需先解除处理人，然后重新设置处理人
        taskService.unclaim("127509");
        taskService.claim("127509", "王五");
    }

    @Test
    public void completeTask() {
        String taskId = "140002";
        taskService.complete(taskId);
    }

    @Test
    public void queryExecution() {

        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId("147516").singleResult();
        System.out.println("nrOfInstances:" + taskService.getVariable("150033", "nrOfInstances"));
        System.out.println("nrOfActiveInstances:" + taskService.getVariable("150033", "nrOfActiveInstances"));
        System.out.println("nrOfCompletedInstances:" + taskService.getVariable("150033", "nrOfCompletedInstances"));
        System.out.println("loopCounter:" + taskService.getVariable("150033", "loopCounter"));

    }


}

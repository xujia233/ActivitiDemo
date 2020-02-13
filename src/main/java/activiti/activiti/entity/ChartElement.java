package activiti.activiti.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程图实体类
 * Created by xujia on 2020/2/7
 */
@Data
public class ChartElement {

    /**
     * 资源ID
     */
    private String resourceId = "";
    /**
     * 属性，这是最关键的，每一个元素都拥有不同的属性项，如线对象、用户任务对象、网关对象等等，以继承方式实现即可
     */
    private BaseProperty properties;
    /**
     * 流程环节的activiti类型
     * key为id
     */
    private Map<String, String> stencil = new HashMap<>();
    /**
     * 子组件
     */
    private List<ChartElement> childShapes = new ArrayList<>();
    /**
     * 组件外连的其他组件Id
     */
    private List<Map<String, String>> outgoing = new ArrayList<>();
    /**
     * 组件所在位置。
     */
    private Map<String, Coordinate> bounds = new HashMap<>();
    /**
     * 连接线上的拐点坐标
     */
    private List<Coordinate> dockers = new ArrayList<>();
    /**
     * 连接线的目标组件ID
     */
    private Map<String, String> target = new HashMap<>();

}

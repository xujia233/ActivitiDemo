package activiti.activiti.entity;

import lombok.Data;

/**
 * 流程图元素基本能属性类
 * Created by xujia on 2020/2/7
 */
@Data
public class BaseProperty {

    /**
     * 名称
     */
    private String name = "";
    /**
     * 说明
     */
    private String documentation = "";
}

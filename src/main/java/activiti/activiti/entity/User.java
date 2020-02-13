package activiti.activiti.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xujia on 2019/8/6
 */
@Data
@Builder
@AllArgsConstructor
public class User implements Serializable {

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 地址
     */
    private String address;
    /**
     * 用户邮箱，测试驼峰映射用
     */
    private String userEmail;

    public User() {}

    public User(String name) {
        this.name = name;
    }
}

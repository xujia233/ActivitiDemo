package activiti.activiti.entity;

import lombok.Data;

/**
 * 坐标实体类
 * Created by xujia on 2020/2/7
 */
@Data
public class Coordinate {

    /**
     * x轴坐标或者宽度
     */
    private double x;
    /**
     * y轴坐标或者高度
     */
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {}
}

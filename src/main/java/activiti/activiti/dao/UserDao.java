package activiti.activiti.dao;

import activiti.activiti.entity.User;

import java.util.List;

/**
 * Created by xujia on 2019/8/6
 */
public interface UserDao {

    List<User> queryAllUser();
}

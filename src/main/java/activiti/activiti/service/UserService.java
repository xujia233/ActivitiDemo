package activiti.activiti.service;

import activiti.activiti.dao.UserDao;
import activiti.activiti.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xujia on 2019/8/6
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public List<User> queryAllUser() {
        return userDao.queryAllUser();
    }
}

package activiti.activiti.controller;

import activiti.activiti.service.UserService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by xujia on 2019/8/6
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RuntimeService runtimeService;

    @RequestMapping("user/query")
    public Object queryAllUser() {
        return userService.queryAllUser();
    }
}

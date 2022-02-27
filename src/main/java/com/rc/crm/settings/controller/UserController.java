package com.rc.crm.settings.controller;

import com.rc.crm.settings.domain.User;
import com.rc.crm.exception.InvalidLoginException;
import com.rc.crm.settings.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rc
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(HttpServletRequest request, String username, String password) {
        String ip = request.getRemoteAddr();
        Map<String, Object> result = new HashMap<>(2);
        try {
            User user = userService.login(username, password, ip);
            request.getSession().setAttribute("user", user);
            result.put("success", true);
        } catch (InvalidLoginException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}

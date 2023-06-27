package win.zhangzhixing.pig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/base")
public class BaseController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "/world")
    public String world() {
        return "world";
    }

    @GetMapping(value = "/u")
    public String u() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @GetMapping(value = "/p")
    public String p() {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    }
}

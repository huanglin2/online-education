package com.ithl.eduservice.controller;

import com.ithl.commonutils.R;
import org.springframework.web.bind.annotation.*;

/**
 * @author hl
 */
@RestController
@RequestMapping("/eduservice/user")
// 解决跨域问题
@CrossOrigin
public class EduLoginController {

    // login
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    // info
    @GetMapping("/info")
    public R info(){
        return R.ok().data("name","admin").data("roles","[admin]").data("avatar","https://tse1-mm.cn.bing.net/th?id=OIP.M9HlelXIpjuMDDL8d3jqQQHaNK&pid=Api&rs=1");
    }
}

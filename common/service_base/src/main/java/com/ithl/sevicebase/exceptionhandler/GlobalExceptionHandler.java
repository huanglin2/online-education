package com.ithl.sevicebase.exceptionhandler;

import com.ithl.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hl
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
/*
    // 指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    // 可以返回数据
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    // 指定出现什么异常执行这个方法
    @ExceptionHandler(ArithmeticException.class)
    // 可以返回数据
    @ResponseBody
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理");
    }*/

    // 自定义异常
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public R error(MyException e) {
        e.printStackTrace();
        log.error(e.getMsg());
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}

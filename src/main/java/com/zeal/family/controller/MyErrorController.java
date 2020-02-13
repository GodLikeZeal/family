package com.zeal.family.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author zeal
 * @version 1.0.0
 * @description 处理自定义Error的Controller
 * @date 2018-05-09 20:38
 */
@Slf4j
@Controller
public class MyErrorController implements ErrorController {

  private static final String ERROR_PATH = "/error";

  @RequestMapping(value = ERROR_PATH)
  public String handleError(HttpServletRequest request) {
    //获取statusCode:401,404,500
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    switch (statusCode) {
      case 401:
        return "deny.html";
      case 403:
        return "deny.html";
      default:
        return "error.html";
    }

  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }
}

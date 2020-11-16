package com.zeal.family.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
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

  private ErrorAttributes errorAttributes;

  @Autowired
  public MyErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  @RequestMapping(value = ERROR_PATH)
  public String handleError(HttpServletRequest request, Model model) {
    //获取statusCode:401,404,500
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

    Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

    if (exception == null) {
      exception = (Exception) request.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
    }

    String msg = null;

    if (exception instanceof BindException) {
      StringBuffer sb = new StringBuffer();
      ((BindException) exception).getAllErrors().forEach( e -> sb.append(e.getDefaultMessage() + "\t"));
      msg = sb.toString();
    } else {
      msg = exception.getCause().getMessage();
    }
    model.addAttribute("msg", msg);

    switch (statusCode) {
      case 401:
        return "deny.html";
      case 403:
        return "deny.html";
      case 404:
        return "error.html";
      default:
        return "fail.html";
    }

  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }
}

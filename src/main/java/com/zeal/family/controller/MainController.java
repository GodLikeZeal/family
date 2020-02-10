package com.zeal.family.controller;

import com.zeal.family.bo.Mypage;
import com.zeal.family.entiy.Group;
import com.zeal.family.entiy.User;
import com.zeal.family.service.GroupService;
import com.zeal.family.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class MainController {

  @Autowired
  UserService userService;
  @Autowired
  GroupService groupService;

  @GetMapping("/")
  public String defaultPage() {
    return "index.html";
  }

  @GetMapping("login.html")
  public String login() {
    return "login.html";
  }

  @GetMapping("index.html")
  public String index() {
    return "index.html";
  }

  @GetMapping("add.html")
  public ModelAndView add() {
    ModelAndView mv = new ModelAndView();
    List<Group> list = groupService.findList();
    List<User> users = userService.findList();
    mv.addObject("groups",list);
    mv.addObject("users",users);
    mv.setViewName("add.html");
    return mv;
  }

  @GetMapping("list.html")
  public ModelAndView add(@RequestParam(defaultValue = "1") Integer pageNumber,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam String groupId,
                          @RequestParam String name) {
    ModelAndView mv = new ModelAndView();
    Mypage mypage = userService.findPage(pageNumber,pageSize,groupId,name);
    mv.addObject("page",mypage);
    mv.setViewName("list.html");
    return mv;
  }

  @GetMapping("group/add.html")
  public String groupAdd() {
    return "group_add.html";
  }

  @GetMapping("group/list.html")
  public ModelAndView groupList(@RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
    ModelAndView mv = new ModelAndView();
    Mypage page = groupService.findPage(pageNumber,pageSize);
    mv.addObject("page",page);
    mv.setViewName("group_list.html");
    return mv;
  }

  @PostMapping("group/add")
  public String groupAdd(Group group) {
    groupService.save(group);
    return "group_add_success.html";
  }

  @PostMapping("user/add")
  public String userAdd(User user) {
    userService.save(user);
    return "user_add_success.html";
  }

  @PutMapping("user/update")
  @ResponseBody
  public Boolean updateAdd(User user) {
    userService.update(user);
    return true;
  }

  @DeleteMapping("user/delete/{id}")
  @ResponseBody
  public Boolean updateAdd(@PathVariable("id") String id) {
    userService.remove(id);
    return true;
  }

}

package com.zeal.family.controller;

import com.zeal.family.bo.Mypage;
import com.zeal.family.bo.UserBo;
import com.zeal.family.entiy.Group;
import com.zeal.family.entiy.User;
import com.zeal.family.service.GroupService;
import com.zeal.family.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class MainController {

  @Autowired
  UserService userService;
  @Autowired
  GroupService groupService;

  @GetMapping("/")
  public ModelAndView defaultPage() {
    ModelAndView mv = new ModelAndView();
    UserBo u = userService.getTree();
    mv.addObject("data",u);
    mv.setViewName("index.html");
    return mv;
  }

  @GetMapping("login.html")
  public String login() {
    return "login.html";
  }

  @GetMapping("index.html")
  public ModelAndView index() {
    ModelAndView mv = new ModelAndView();
    UserBo u = userService.getTree();
    mv.addObject("data",u);
    mv.setViewName("index.html");
    return mv;
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

  @GetMapping("edit.html")
  public ModelAndView edit(@RequestParam String id) {
    ModelAndView mv = new ModelAndView();
    List<Group> list = groupService.findList();
    List<User> users = userService.findList();
    User user = userService.findById(id);
    mv.addObject("groups",list);
    mv.addObject("users",users);
    mv.addObject("user",user);
    mv.setViewName("edit.html");
    return mv;
  }

  @PostMapping("user/update")
  public ModelAndView updateUser(User user) {
    ModelAndView mv = new ModelAndView();
    userService.update(user);
    Mypage mypage = userService.findPage(1,10,null,null);
    List<Group> list = groupService.findList();
    List<User> users = userService.findList();
    mv.addObject("groups",list);
    mv.addObject("page",mypage);
    mv.addObject("users",users);
    mv.setViewName("list.html");
    return mv;
  }

  @GetMapping("list.html")
  public ModelAndView add(@RequestParam(defaultValue = "1") Integer pageNumber,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String groupId,
                          @RequestParam(required = false) String name) {
    ModelAndView mv = new ModelAndView();
    Mypage mypage = userService.findPage(pageNumber,pageSize,groupId,name);
    List<Group> list = groupService.findList();
    List<User> users = userService.findList();
    mv.addObject("groups",list);
    mv.addObject("page",mypage);
    mv.addObject("users",users);
    mv.setViewName("list.html");
    return mv;
  }

  @GetMapping("group_add.html")
  public String groupAdd() {
    return "group_add.html";
  }

  @GetMapping("group_edit.html")
  public ModelAndView editGroup(@RequestParam String id) {
    ModelAndView mv = new ModelAndView();
    Group group = groupService.findById(id);
    mv.addObject("group",group);
    mv.setViewName("group_edit.html");
    return mv;
  }

  @PostMapping("group/update")
  public ModelAndView updateGroup(Group group) {
    ModelAndView mv = new ModelAndView();
    groupService.update(group);
    Mypage page = groupService.findPage(1,10);
    mv.addObject("page",page);
    mv.setViewName("group_list.html");
    mv.setViewName("group_list.html");
    return mv;
  }

  @GetMapping("group_list.html")
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

  @GetMapping("user/delete/{id}")
  @ResponseBody
  public Boolean deleteUser(@PathVariable("id") String id) {
    userService.remove(id);
    return true;
  }

  @GetMapping("group/delete/{id}")
  @ResponseBody
  public Boolean deleteGroup(@PathVariable("id") String id) {
    groupService.remove(id);
    return true;
  }
}

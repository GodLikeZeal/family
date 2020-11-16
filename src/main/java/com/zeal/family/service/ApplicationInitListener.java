package com.zeal.family.service;

import com.zeal.family.entiy.Group;
import com.zeal.family.entiy.User;
import com.zeal.family.enums.Gender;
import com.zeal.family.enums.Role;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 初始化后，设置默认账号和密码.
 *
 * @author zhanglei
 * @date 2020/6/3  11:20 上午
 */
@Slf4j
@Component
public class ApplicationInitListener implements ApplicationRunner {

  @Value("${app.username}")
  private String username;
  @Value("${app.password}")
  private String password;
  @Value("${app.default-group}")
  private String groupName;

  @Autowired
  UserService userService;
  @Autowired
  GroupService groupService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("开始执行初始化操作...");
    log.info("查询默认分组:{}", username);
    List<Group> groups = groupService.findListByName(groupName);
    log.info("默认分组结果为{}", groups);
    if (CollectionUtils.isEmpty(groups)) {
      Group group = Group.builder()
          .name(groupName)
          .createDate(LocalDate.now())
          .introduction("默认分组")
          .build();
      groupService.save(group);

      log.info("查询默认用户名:{}", username);
      List<User> users = userService.findByUsername(username);
      log.info("默认用户结果为{}", users);
      if (CollectionUtils.isEmpty(users)) {
        userService.save(User.builder()
            .name(username)
            .groupId(group.getId())
            .groupName(group.getName())
            .role(Role.ADMIN)
            .password(new BCryptPasswordEncoder().encode(password))
            .gender(Gender.MALE)
            .build());
      }
    }


  }
}
